package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraDevice;
import android.support.annotation.NonNull;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;

import javax.inject.Inject;

public class CameraStateCallback extends CameraDevice.StateCallback {
    @Inject
    Camera2Activity camera2Activity;
    
    CameraDevice cameraDevice;
    
    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        cameraDevice = camera;
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
