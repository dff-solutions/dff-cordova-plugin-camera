package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

public class CameraPreviewStateCallback extends CameraCaptureSession.StateCallback {
    private static final String TAG = "CameraPreviewStateCallback";
    
    private Camera2Activity camera2Activity;
    private Log log;
    
    @Inject
    public CameraPreviewStateCallback(Log log) {
        this.log = log;
    }
    
    @Override
    public void onConfigured(@NonNull CameraCaptureSession captureSession) {
        log.d(TAG, "onConfigured");
        if (null == camera2Activity.cameraDevice) {
            log.d(TAG, "no cameraDevice");
            return;
        }
    
        camera2Activity.cameraCaptureSession = captureSession;
        camera2Activity.updatePreview();
    }
    
    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        log.e(TAG, "error while configurating CaptureSession");
    }
    
    public void setCamera2Activity(Camera2Activity camera2Activity) {
        this.camera2Activity = camera2Activity;
    }
}
