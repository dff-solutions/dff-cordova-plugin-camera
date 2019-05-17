package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

public class CameraCaptureStateCallback extends CameraCaptureSession.StateCallback {
    private static final String TAG = "CameraCaptureStateCallback";
    
    public Camera2Activity camera2Activity;
    private CameraDevice cameraDevice;
    private Log log;
    
    @Inject
    public CameraCaptureStateCallback(Log log) {
        this.log = log;
    }
    
    @Override
    public void onConfigured(@NonNull CameraCaptureSession captureSession) {
        if (null == cameraDevice) {
            return;
        }
    
        camera2Activity.cameraCaptureSession = captureSession;
        camera2Activity.updatePreview();
    }
    
    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        log.e(TAG, "error while configurating CaptureSession");
    }
}
