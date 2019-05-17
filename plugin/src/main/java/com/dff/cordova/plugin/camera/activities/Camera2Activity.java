package com.dff.cordova.plugin.camera.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
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

import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.helpers.ButtonHelper;
import com.dff.cordova.plugin.camera.helpers.PermissionHelper;
import com.dff.cordova.plugin.camera.listeners.OrientationListener;
import com.dff.cordova.plugin.camera.listeners.SurfaceListener;
import com.dff.cordova.plugin.camera.listeners.callback.AvailableImageListener;
import com.dff.cordova.plugin.camera.listeners.callback.CameraCaptureStateCallback;
import com.dff.cordova.plugin.camera.listeners.callback.CameraPreviewStateCallback;
import com.dff.cordova.plugin.camera.listeners.callback.CameraStateCallback;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

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
    
    public CameraDevice cameraDevice;
    private TextureView textureView;
    private Size previweSize;
    public CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequest;
    private ImageButton captureButton;
    private ImageButton flashButton;
    private ImageButton flipButton;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    
    private int flashMode = 0;
    private int cameraMode = 0;
    
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
    
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
    
    @SuppressLint("MissingPermission")
    public void openCamera() {
        log.d(TAG, "opening camera");
    
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        
        try {
            cameraId = cameraManager.getCameraIdList()[0];
    
            CameraCharacteristics cameraCharacteristics =
                cameraManager.getCameraCharacteristics(cameraId);
            
            StreamConfigurationMap streamConfigurationMap =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            
            previweSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
            
            cameraManager.openCamera(cameraId, cameraStateCallback, null);
            
        } catch (CameraAccessException e) {
            log.e(TAG, "error while getting cameraId");
        }
        
    }
    
    public void startCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previweSize.getWidth(), previweSize.getHeight());
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
     * This methods closes the cameraDevice.
     */
    public void closeCamera() {
        log.d(TAG, "closing camera");
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }
    
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
            log.e(TAG, "error while creating ");
        }
    }
    
    private void takePicture() {
        if (cameraDevice == null) {
            log.d(TAG, "no cameraDevice");
            return;
        }
        log.d(TAG, "take picture");
        
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics =
                manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
               jpegSizes =
                   characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                                  .getOutputSizes(ImageFormat.JPEG);
            }
            int width = previweSize.getWidth();
            int height = previweSize.getHeight();
            
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
    
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG,
                                                         1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            
            final CaptureRequest.Builder captureBuilder =
                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, 90);
            
            log.d(TAG, "set OnImageAvailableListener");
            reader.setOnImageAvailableListener(availableImageListener, mBackgroundHandler);
            
           cameraCaptureStateCallback.mBackgroundHandler = mBackgroundHandler;
           cameraCaptureStateCallback.captureBuilder = captureBuilder;
            
            cameraDevice.createCaptureSession(
                outputSurfaces,
                cameraCaptureStateCallback,
                mBackgroundHandler
            );
            
        } catch (CameraAccessException e) {
            log.e(TAG, "error while accessing camera", e);
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
        log.d(TAG, "changeFlashMode");
        switch (flashMode) {
            case 0:
                flashMode = 1;
                break;
            case 1:
                flashMode = 2;
                break;
            case 2:
                flashMode = 0;
                break;
            default:
                break;
        }
        buttonHelper.changeFlashButton(flashButton, flashMode);
    }
    
    private void changeCamera() {
        log.d(TAG, "changeCamera");
        
        if (cameraMode == 0) {
            cameraMode = 1;
        } else {
            cameraMode = 0;
        }
        buttonHelper.changeFlipButton(flipButton, cameraMode);
    }
}
