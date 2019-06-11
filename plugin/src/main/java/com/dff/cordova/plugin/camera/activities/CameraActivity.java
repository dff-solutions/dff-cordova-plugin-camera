package com.dff.cordova.plugin.camera.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.actions.TakePhoto;
import com.dff.cordova.plugin.camera.classes.CameraState;
import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.helpers.ButtonHelper;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;
import com.dff.cordova.plugin.camera.helpers.PermissionHelper;
import com.dff.cordova.plugin.camera.listeners.AvailableImageListener;
import com.dff.cordova.plugin.camera.listeners.OrientationListener;
import com.dff.cordova.plugin.camera.listeners.SurfaceListener;
import com.dff.cordova.plugin.camera.listeners.callback.CameraCaptureStateCallback;
import com.dff.cordova.plugin.camera.listeners.callback.CameraPreviewStateCallback;
import com.dff.cordova.plugin.camera.listeners.callback.CameraStateCallback;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;

import org.apache.cordova.CallbackContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Activity to start the camera.
 *
 * @see <a href="https://developer.android.com/reference/android/hardware/camera2/package-summary.html"
 * >https://developer.android.com/reference/android/hardware/camera2/package-summary.html</a>
 */
public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";
    
    public static final String CAMERA_ACTIVITY_LAYOUT = "cameraplugin_activity_camera";
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
    public Handler backgroundHandler;
    private HandlerThread backgroundThread;
    private CameraManager cameraManager;
    private String cameraId = null;
    private CameraCharacteristics characteristics = null;
    private ImageReader reader;
    
    private CameraState cameraState = CameraState.BACK;
    private int supportedHardwareLevel = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean hasCamera =
            getApplicationContext()
                .getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA);
        
        if (!hasCamera) {
            setResult(R.RESULT_CANCELED);
            finish();
        }
        
        super.onCreate(savedInstanceState);
        
        //DaggerAppComponent.builder().build().activityComponentBuilder().build()
        // .cameraActivityComponentBuilder().build().inject(this);
        
        DaggerManager
            .getInstance()
            .inject(this);
        
        log.d(TAG, "onCreate");
        
        surfaceListener.setCameraActivity(this);
        cameraStateCallback.setCameraActivity(this);
        cameraPreviewStateCallback.setCameraActivity(this);
        cameraCaptureStateCallback.setCamera2Activity(this);
        availableImageListener.setCameraActivity(this);
        
        setContentView(r.getLayoutIdentifier(CAMERA_ACTIVITY_LAYOUT));
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        buttonHelper.cameraManager = cameraManager;
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                int cameraOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraOrientation == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraId = id;
                    supportedHardwareLevel =
                        characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                    break;
                }
            }
        } catch (Exception e) {
            log.e(TAG, "unable to access camera.", e);
        }
        
        if (cameraId == null) {
            log.d(TAG, "Unable to set cameraId automatically");
            log.d(TAG, "Set cameraId to first camera from cameraIdList");
            try {
                cameraId = cameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                log.e(TAG, "unable to set cameraId from cameraIdList");
            }
            supportedHardwareLevel =
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED;
        }
        log.d(TAG, "cameraId = " + cameraId);
        log.d(TAG, "supported hardware level = " + supportedHardwareLevel);
        
        captureButton = findViewById(r.getIdIdentifier(CAPTURE_BUTTON));
        flashButton = findViewById(r.getIdIdentifier(FLASH_BUTTON));
        flipButton = findViewById(r.getIdIdentifier(FLIP_BUTTON));
        
        buttonHelper.addImageButton(flashButton);
        buttonHelper.addImageButton(flipButton);
        
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
        
        buttonHelper.checkFlash(getApplicationContext(), flashButton);
        buttonHelper.checkFrontCamera(getApplicationContext(), flipButton);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startBackgroundThread();
    }
    
    @Override
    protected void onResume() {
        log.d(TAG, "onResume");
        super.onResume();
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
        buttonHelper.enableAllButtons(true);
        captureButton.setEnabled(true);
        buttonHelper.cameraId = cameraId;
        try {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            
            previewSize = new Size(size.y, size.x);
            log.d(TAG, "previewSize " + previewSize.toString());
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
            cameraCaptureSession.setRepeatingRequest(
                captureRequest.build(),
                null,
                null
            );
        } catch (CameraAccessException e) {
            log.e(TAG, "error while setting repeating request", e);
        }
    }
    
    private void takePicture() {
        if (cameraDevice == null) {
            log.e(TAG, "no cameraDevice");
            return;
        }
        log.d(TAG, "take picture");
        try {
            //avoid error due to double clicks
            buttonHelper.enableAllButtons(false);
            captureButton.setEnabled(false);
            
            Size optimalSize;
            if (supportedHardwareLevel >
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED) {
                
                characteristics = cameraManager.getCameraCharacteristics(cameraId);
                optimalSize = imageHelper.getOptimalImageSize(characteristics);
                log.d(TAG, "optimal Image Size = " + optimalSize.toString());
            } else {
                optimalSize = previewSize;
            }
            
            reader = ImageReader.newInstance(
                optimalSize.getWidth(),
                optimalSize.getHeight(),
                ImageFormat.JPEG,
                1
            );
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            
            final CaptureRequest.Builder captureBuilder =
                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            
            int screenRotation = orientationEventListener.getImageRotation();
            int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            int jpegOrientation = (screenRotation + sensorOrientation + 270) % 360;
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, jpegOrientation);
            
            reader.setOnImageAvailableListener(availableImageListener, backgroundHandler);
            
            cameraCaptureStateCallback.mBackgroundHandler = backgroundHandler;
            cameraCaptureStateCallback.captureBuilder = captureBuilder;
            
            cameraDevice.createCaptureSession(
                outputSurfaces,
                cameraCaptureStateCallback,
                backgroundHandler
            );
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
            intent.putExtra("image", imageHelper.getBytes());
            startActivityForResult(intent, R.RESULT_OK);
        } else {
            log.d(TAG, "show no PreviewActivity");
            returnImage();
            finish();
        }
    }
    
    protected void startBackgroundThread() {
        backgroundThread = new HandlerThread("Camera Background");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }
    
    protected void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            log.e(TAG, "unable to stop background thread", e);
        }
    }
    
    private void changeFlashMode() {
        buttonHelper.changeFlashButton(captureRequest, flashButton);
        updatePreview();
    }
    
    private void changeCamera() {
        if (cameraState == CameraState.FRONT) {
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
            cameraState = CameraState.BACK;
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
            cameraState = CameraState.FRONT;
        }
        buttonHelper.changeFlipButton(flipButton, cameraState);
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
                returnImage();
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
    
    private void returnImage() {
        if (imageHelper.getBase64Image() != null) {
            for (CallbackContext callbackContext : r.getCallBackContexts()) {
                callbackContext.success(imageHelper.getBase64Image());
            }
        } else {
            log.e(TAG, "sBase64Image is empty");
        }
    }
}
