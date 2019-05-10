package com.dff.cordova.plugin.camera.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.res.R;
import com.dff.cordova.plugin.camera.helpers.PermissionHelper;
import com.dff.cordova.plugin.camera.listeners.SurfaceListener;
import com.dff.cordova.plugin.camera.listeners.callback.CameraStateCallback;
import com.dff.cordova.plugin.camera.log.Log;

import java.util.Arrays;

import javax.inject.Inject;

public class Camera2Activity extends Activity {
    public static final String TAG = "Camera2Activity";
    
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
    
    public CameraDevice cameraDevice;
    private TextureView textureView;
    private Size previweSize;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequest;
    private ImageButton captureButton;
    private ImageButton flashButton;
    private ImageButton flipButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        DaggerManager
            .getInstance()
            .inject(this);
        
        surfaceListener.camera2Activity = this;
        cameraStateCallback.camera2Activity = this;
        
        setContentView(r.getLayoutIdentifier(CAMERA_ACTIVITY_LAYOUT));
    
        captureButton = findViewById(r.getIdIdentifier(CAPTURE_BUTTON));
        flashButton = findViewById(r.getIdIdentifier(FLASH_BUTTON));
        flipButton =  findViewById(r.getIdIdentifier(FLIP_BUTTON));
        
        textureView = findViewById(r.getIdIdentifier(TEXTURE_VIEW_ID));
        textureView.setSurfaceTextureListener(surfaceListener);
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
            cameraDevice.createCaptureSession(Arrays.asList(surface),
                                              new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession captureSession) {
                    if (null == cameraDevice) {
                        return;
                    }
                    
                    cameraCaptureSession = captureSession;
                    updatePreview();
                }
                
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    log.e(TAG, "error while configurating CaptureSession");
                }
            }, null);
        } catch (CameraAccessException e) {
            log.e(TAG, "error while creating caputureRequest");
        }
    }
    
    public void closeCamera() {
        log.d(TAG, "closing camera");
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }
    
    private void updatePreview() {
        if (cameraDevice == null) {
            log.e(TAG, "no CameraDevice in updatePreview()");
        }
        captureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequest.build(), null, null);
        } catch (CameraAccessException e) {
            log.e(TAG, "error while creating ");
        }
    }
}
