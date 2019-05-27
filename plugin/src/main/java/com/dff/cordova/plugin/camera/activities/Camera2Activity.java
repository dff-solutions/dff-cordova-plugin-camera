package com.dff.cordova.plugin.camera.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.actions.TakePhoto;
import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.helpers.ButtonHelper;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;
import com.dff.cordova.plugin.camera.helpers.PermissionHelper;
import com.dff.cordova.plugin.camera.listeners.OrientationListener;
import com.dff.cordova.plugin.camera.listeners.SurfaceListener;
import com.dff.cordova.plugin.camera.listeners.AvailableImageListener;
import com.dff.cordova.plugin.camera.listeners.callback.CameraCaptureStateCallback;
import com.dff.cordova.plugin.camera.listeners.callback.CameraPreviewStateCallback;
import com.dff.cordova.plugin.camera.listeners.callback.CameraStateCallback;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Activity to start the camera.
 * @see <a href="https://developer.android.com/reference/android/hardware/camera2/package-summary.html"
 *      >https://developer.android.com/reference/android/hardware/camera2/package-summary.html</a>
 */
public class Camera2Activity extends Activity {
    private static final String TAG = "Camera2Activity";
    
    public static final String CAMERA_ACTIVITY_LAYOUT = "activity_camera2";
    public static final String TEXTURE_VIEW_ID = "texture";
    public static final String CAPTURE_BUTTON = "capture_button";
    public static final String FLASH_BUTTON = "flash_button";
    public static final String FLIP_BUTTON = "flip_button";
    
    @Inject
    SurfaceListener surfaceListener;
    
    @Inject
    CameraStateCallback cameraStateCallback;
    
    @Inject
    PermissionHelper permissionHelper;
    
    @Inject
    Log log;
    
    @Inject
    R r;
    
    @Inject
    OrientationListener orientationEventListener;
    
    @Inject
    CameraPreviewStateCallback cameraPreviewStateCallback;
    
    @Inject
    AvailableImageListener availableImageListener;
    
    @Inject
    CameraCaptureStateCallback cameraCaptureStateCallback;
    
    @Inject
    ButtonHelper buttonHelper;
    
    @Inject
    ImageHelper imageHelper;
    
    public CameraDevice cameraDevice;
    private TextureView textureView;
    private Size previewSize;
    public CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequest;
    private ImageButton captureButton;
    private ImageButton flashButton;
    private ImageButton flipButton;
    public Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private CameraManager cameraManager;
    private String cameraId;
    private CameraCharacteristics characteristics;
    private ImageReader reader;
    
    private int flipMode = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        DaggerManager
            .getInstance()
            .inject(this);
        log.d(TAG, "onCreate");
        
        surfaceListener.setCamera2Activity(this);
        cameraStateCallback.setCamera2Activity(this);
        cameraPreviewStateCallback.setCamera2Activity(this);
        cameraCaptureStateCallback.setCamera2Activity(this);
        availableImageListener.setCamera2Activity(this);
        
        setContentView(r.getLayoutIdentifier(CAMERA_ACTIVITY_LAYOUT));
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                int cameraOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraOrientation == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = id;
                    break;
                }
            }
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            log.e(TAG, "unable to access camera.", e);
        }
        captureButton = findViewById(r.getIdIdentifier(CAPTURE_BUTTON));
        flashButton = findViewById(r.getIdIdentifier(FLASH_BUTTON));
        flipButton =  findViewById(r.getIdIdentifier(FLIP_BUTTON));
        
        orientationEventListener.addImageButton(flashButton);
        orientationEventListener.addImageButton(flipButton);
        
        textureView = findViewById(r.getIdIdentifier(TEXTURE_VIEW_ID));
        textureView.setSurfaceTextureListener(surfaceListener);
        
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFlashMode();
            }
        });
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCamera();
            }
        });
    
        boolean hasFlashMode =
            getApplicationContext()
                .getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        
        if (!hasFlashMode) {
            flashButton.setEnabled(false);
            flashButton.setVisibility(View.GONE);
        }
        
        boolean hasFrontCamera =
            getApplicationContext()
                .getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        
        if (!hasFrontCamera) {
            flipButton.setEnabled(false);
            flipButton.setVisibility(View.GONE);
        }
    
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startBackgroundThread();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        log.d(TAG, "onResume");
        startBackgroundThread();
        orientationEventListener.enable();
        
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceListener);
        }
    }
    
    @Override
    protected void onPause() {
        log.d(TAG, "onPause");
        stopBackgroundThread();
        closeCamera();
        orientationEventListener.disable();
        super.onPause();
    }
    
    /**
     * This methods opens the camera on the camera Manager.
     */
    @SuppressLint("MissingPermission")
    public void openCamera() {
        log.d(TAG, "opening camera");
        
        try {
            characteristics =
                cameraManager.getCameraCharacteristics(cameraId);
            
            StreamConfigurationMap streamConfigurationMap =
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            
            previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
            
            cameraManager.openCamera(cameraId, cameraStateCallback, null);
            
        } catch (CameraAccessException e) {
            log.e(TAG, "error while getting cameraId");
        }
        
    }
    
    /**
     * This method start the camera preview.
     */
    public void startCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface surface = new Surface(texture);
            captureRequest.addTarget(surface);
            
            log.d(TAG, "create CaptureSession");
            cameraDevice.createCaptureSession(
                Arrays.asList(surface),
                cameraPreviewStateCallback,
                null
            );
    
            this.orientationEventListener.enable();
        } catch (CameraAccessException e) {
            log.e(TAG, "error while creating caputureRequest");
        }
    }
    
    /**
     * This method closes the cameraDevice.
     */
    public void closeCamera() {
        log.d(TAG, "closing camera");
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }
    
    /**
     * This method updates the Preview. Changes on the setting affect after the update.
     */
    public void updatePreview() {
        log.d(TAG, "updatePreview");
        if (cameraDevice == null) {
            log.e(TAG, "no cameraDevice");
        }
        captureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequest.build(), null,
                                                     null);
        } catch (CameraAccessException e) {
            log.e(TAG, "error while setting repeating request", e);
        }
    }
    
    private void takePicture() {
        if (cameraDevice == null) {
            log.d(TAG, "no cameraDevice");
            return;
        }
        log.d(TAG, "take picture");
        try {
            //avoid error due to double clicks
            flipButton.setEnabled(false);
            captureButton.setEnabled(false);
            flashButton.setEnabled(false);
            
            Size[] jpegSizes =
                characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    .getOutputSizes(ImageFormat.JPEG);
            
            int width = previewSize.getWidth();
            int height = previewSize.getHeight();
    
            //choose maximum size
            for (Size size : jpegSizes) {
                if ((size.getHeight() % previewSize.getHeight()) == 0 && (
                    size.getWidth() % previewSize.getWidth()) == 0 &&
                    size.getHeight() > height &&
                    size.getHeight() <= 1080
                ) {
                    width = size.getWidth();
                    height = size.getHeight();
                }
            }
            log.d(TAG, "width: " + width);
            log.d(TAG, "height: " + height);
            reader = ImageReader.newInstance(width, height, ImageFormat.JPEG,
                                                         1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            
            final CaptureRequest.Builder captureBuilder =
                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    
            int screenRotation;
            switch (orientationEventListener.currentRotaion) {
                case 0:
                    screenRotation = 90;
                    break;
                case 90:
                    screenRotation = 0;
                    break;
                case 180:
                    screenRotation = 270;
                    break;
                case 270:
                    screenRotation = 180;
                    break;
                default:
                    screenRotation = 90;
                    break;
            }
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, screenRotation);
            
            reader.setOnImageAvailableListener(availableImageListener, mBackgroundHandler);
            
           cameraCaptureStateCallback.mBackgroundHandler = mBackgroundHandler;
           cameraCaptureStateCallback.captureBuilder = captureBuilder;
            
            cameraDevice.createCaptureSession(
                outputSurfaces,
                cameraCaptureStateCallback,
                mBackgroundHandler
            );
    
            flipButton.setEnabled(true);
            captureButton.setEnabled(true);
            flashButton.setEnabled(true);
            
        } catch (CameraAccessException e) {
            log.e(TAG, "error while accessing camera", e);
        }
    }
    
    /**
     * When the picture is captured, this method starts the previewActivity.
     * If the flag withPreview isn't set, the activity will return the image without preview.
     */
    public void startPreviewActivity() {
        log.d(TAG, "close ImageReader");
        reader.close();
        
        if (this.getIntent().getBooleanExtra(TakePhoto.JSON_ARG_WITH_PREVIEW, false)) {
            Intent intent = new Intent(this, PreviewActivity.class);
            startActivityForResult(intent, R.RESULT_OK);
        } else {
            log.d(TAG, "show no PreviewActivity");
            try {
                imageHelper.saveImage();
            } catch (IOException e) {
                log.e(TAG, "unable to save image", e);
            }
            Intent returnIntent = new Intent();
            if (r.sBase64Image != null) {
                returnIntent.putExtra("result", r.sBase64Image);
                setResult(R.RESULT_OK, returnIntent);
            } else {
                log.e(TAG, "sBase64Image is empty");
                log.e(TAG, "repeat capture");
                setResult(R.RESULT_REPEAT, returnIntent);
            }
            finish();
        }
    }
    
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            log.e(TAG, "unablte to stop background thread", e);
        }
    }
    
    private void changeFlashMode() {
        buttonHelper.changeFlashButton(captureRequest, flashButton);
        updatePreview();
    }
    
    private void changeCamera() {
        if (flipMode == 0) {
            log.d(TAG, "flip to back camera");
            try {
                for (String id : cameraManager.getCameraIdList()) {
                    CameraCharacteristics characteristics =
                        cameraManager.getCameraCharacteristics(id);
                    int cameraOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                    if (cameraOrientation == CameraCharacteristics.LENS_FACING_BACK) {
                        cameraId = id;
                        log.d(TAG, "cameraId: " + id);
                    }
                }
            } catch (CameraAccessException e) {
                log.e(TAG, "unable to access camera", e);
            }
            flipMode = 1;
        } else {
            log.d(TAG, "flip to front camera");
            try {
                for (String id : cameraManager.getCameraIdList()) {
                    CameraCharacteristics characteristics =
                        cameraManager.getCameraCharacteristics(id);
                    int cameraOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                    if (cameraOrientation == CameraCharacteristics.LENS_FACING_FRONT) {
                        cameraId = id;
                        log.d(TAG, "cameraId: " + id);
                    }
                }
            } catch (CameraAccessException e) {
                log.e(TAG, "unable to access camera", e);
            }
            flipMode = 0;
        }
        buttonHelper.changeFlipButton(flipButton, flipMode);
        closeCamera();
        openCamera();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != resultCode) {
            log.d(TAG, "onActivityResult: not expected result");
        }
        switch (resultCode) {
            case R.RESULT_OK:
                log.d(TAG, "onActivityResult: set result = OK");
                setResult(R.RESULT_OK, data);
                closeCamera();
                finish();
                break;
            case R.RESULT_CANCELED:
                log.d(TAG, "onActivityResult: set result = canceled");
                setResult(R.RESULT_CANCELED);
                closeCamera();
                finish();
                break;
            case R.RESULT_REPEAT:
                log.d(TAG, "onActivityResult: set result = repeat");
                break;
            default:
                break;
        }
    }
}
