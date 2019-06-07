package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * A callback object for receiving updates about the state of a camera capture session.
 * Updates the preview.
 *
 * @see <a href="https://developer.android.com/reference/android/hardware/camera2/CameraCaptureSession.StateCallback.html"
 *     >https://developer.android.com/reference/android/hardware/camera2/CameraCaptureSession.StateCallback.html</a>
 */
public class CameraPreviewStateCallback extends CameraCaptureSession.StateCallback {
    private static final String TAG = "CameraPreviewStateCallback";
    
    private CameraActivity cameraActivity;
    private Log log;
    
    @Inject
    public CameraPreviewStateCallback(Log log) {
        this.log = log;
    }
    
    @Override
    public void onConfigured(@NonNull CameraCaptureSession captureSession) {
        log.d(TAG, "onConfigured");
        if (null == cameraActivity.cameraDevice) {
            log.d(TAG, "no cameraDevice");
            return;
        }
    
        cameraActivity.cameraCaptureSession = captureSession;
        cameraActivity.updatePreview();
    }
    
    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        log.e(TAG, "error while configurating CaptureSession");
    }
    
    public void setCameraActivity(CameraActivity cameraActivity) {
        this.cameraActivity = cameraActivity;
    }
}
