package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.helpers.CallbackContextHelper;
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
    
    private CameraActivity cameraActivity;
    private Log log;
    private CallbackContextHelper contextHelper;
    
    @Inject
    public CameraStateCallback(
        Log log,
        CameraActivity cameraActivity,
        CallbackContextHelper callbackContextHelper
    ) {
        this.log = log;
        this.cameraActivity = cameraActivity;
        contextHelper = callbackContextHelper;
    }
    
    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        log.d(TAG, "starting camera preview");
        cameraActivity.setCameraDevice(camera);
        cameraActivity.startCameraPreview();
    }
    
    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {
        log.d(TAG, "disconnecting cameraDevice");
        cameraActivity.closeCamera();
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
        log.e(TAG, errorText + ". closing camera and activity. errorCode: " + error);
        contextHelper.sendAllError(errorText +
                                       ". closing camera and activity. errorCode: " +
                                       error);
        
        cameraActivity.closeCamera();
        cameraActivity.finish();
    }
}
