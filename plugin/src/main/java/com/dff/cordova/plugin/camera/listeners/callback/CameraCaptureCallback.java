package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 *  A callback object for tracking the progress of a CaptureRequest submitted to the camera device.
 *
 *   This callback is invoked when a request triggers a capture to start, and
 *   when the capture is complete. In case on an error capturing an image, the error method is
 *   triggered instead of the completion method.
 *
 * @see <a href="https://developer.android.com/reference/android/hardware/camera2/CameraCaptureSession.CaptureCallback.html"
 *     >https://developer.android.com/reference/android/hardware/camera2/CameraCaptureSession.CaptureCallback.html</a>
 */
public class CameraCaptureCallback extends CameraCaptureSession.CaptureCallback {
    private static final String TAG = "CameraCaptureCallback";
    private Log log;
    private CameraActivity cameraActivity;
    
    @Inject
    public CameraCaptureCallback(Log log, CameraActivity cameraActivity) {
        this.log = log;
        this.cameraActivity = cameraActivity;
    }
    
    @Override
    public void onCaptureStarted(@NonNull CameraCaptureSession session,
                                 @NonNull CaptureRequest request,
                                 long timestamp, long frameNumber) {
        log.d(TAG, "onCaptureStarted");
        super.onCaptureStarted(session, request, timestamp, frameNumber);
        cameraActivity.startCameraPreview();
    }
}
