package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

public class CameraCaptureCallback extends CameraCaptureSession.CaptureCallback {
    private static final String TAG = "CameraCaptureCallback";
    private Log log;
    private Camera2Activity camera2Activity;
    
    @Inject
    public CameraCaptureCallback(Log log) {
        this.log = log;
    }
    
    @Override
    public void onCaptureStarted(@NonNull CameraCaptureSession session,
                                 @NonNull CaptureRequest request,
                                 long timestamp, long frameNumber) {
        log.d(TAG, "onCaptureStarted");
        super.onCaptureStarted(session, request, timestamp, frameNumber);
        camera2Activity.startCameraPreview();
    }
    
    public void setCamera2Activity(Camera2Activity camera2Activity) {
        this.camera2Activity = camera2Activity;
    }
}
