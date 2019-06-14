package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * A callback object for receiving updates about the state of a camera capture session.
 * Starts the capturing of the image.
 *
 * @see <a href="https://developer.android.com/reference/android/hardware/camera2/CameraCaptureSession.StateCallback.html"
 *     >https://developer.android.com/reference/android/hardware/camera2/CameraCaptureSession.StateCallback.html</a>
 */
public class CameraCaptureStateCallback extends CameraCaptureSession.StateCallback {
    private static final String TAG = "CameraCaptureStateCallback";
    private CameraCaptureCallback captureListener;
    public CaptureRequest.Builder captureBuilder;
    private Log log;
    public Handler handler;
    
    @Inject
    public CameraCaptureStateCallback(
        CameraCaptureCallback cameraCaptureCallback,
        Handler handler,
        Log log
    ) {
        this.captureListener = cameraCaptureCallback;
        this.handler = handler;
        this.log = log;
    }
    
    @Override
    public void onConfigured(@NonNull CameraCaptureSession session) {
        log.d(TAG, "onConfigured");
        try {
            session.capture(captureBuilder.build(), captureListener,
                            handler);
        } catch (CameraAccessException e) {
            log.e(TAG, "error while accessing camera", e);
        }
    }
    
    @Override
    public void onConfigureFailed(@NonNull CameraCaptureSession session) {
        log.e(TAG, "configuration falled @ createCaptureSession");
        log.e(TAG, "onDevice: " + session.getDevice());
        log.e(TAG, "session to string: " + session.toString());
    }
}
