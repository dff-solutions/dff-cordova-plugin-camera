package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

public class CameraStateCallback extends CameraDevice.StateCallback {
    private final String TAG = "CameraStateCallback";
    
    public Camera2Activity camera2Activity;
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
    
    }
    
    @Override
    public void onError(@NonNull CameraDevice camera, int error) {
    
    }
}
