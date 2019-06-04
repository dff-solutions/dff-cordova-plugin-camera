package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * A callback object for receiving updates about the state of a camera device.
 * Starts the camera preview when the camera is opened.
 *
 * @see <a href="https://developer.android.com/reference/android/hardware/camera2/CameraDevice.StateCallback.html"
 *     >https://developer.android.com/reference/android/hardware/camera2/CameraDevice.StateCallback.html</a>
 */
public class CameraStateCallback extends CameraDevice.StateCallback {
    private final String TAG = "CameraStateCallback";
    
    private Camera2Activity camera2Activity;
    private Log log;
    
    @Inject
    public CameraStateCallback(
        Log log
    ) {
        this.log = log;
    }
    
    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        log.d(TAG, "starting camera preview");
        camera2Activity.cameraDevice = camera;
        camera2Activity.startCameraPreview();
    }
    
    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {
        log.d(TAG, "disconnecting cameraDevice");
        camera2Activity.closeCamera();
    }
    
    @Override
    public void onError(@NonNull CameraDevice camera, int error) {
        String errorText = "none";
        switch (error) {
            case ERROR_CAMERA_IN_USE:
                errorText = "ERROR_CAMERA_IN_USE";
                break;
            case ERROR_MAX_CAMERAS_IN_USE:
                errorText = "ERROR_MAX_CAMERAS_IN_USE";
                break;
            case ERROR_CAMERA_DISABLED:
                errorText = "ERROR_CAMERA_DISABLED";
                break;
            case ERROR_CAMERA_DEVICE:
                errorText = "ERROR_CAMERA_DEVICE";
                break;
            case ERROR_CAMERA_SERVICE:
                errorText = "ERROR_CAMERA_SERVICE";
                break;
            default:
                break;
        }
        log.e(TAG, errorText + ". closing camera. errorCode: " + error);
        
        camera2Activity.closeCamera();
        camera2Activity.finish();
    }
    
    public void setCamera2Activity(Camera2Activity camera2Activity) {
        this.camera2Activity = camera2Activity;
    }
}
