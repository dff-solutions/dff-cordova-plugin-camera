package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
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
