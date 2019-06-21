package com.dff.cordova.plugin.camera.dagger.modules;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.hardware.camera2.CameraManager;

import com.dff.cordova.plugin.camera.configurations.CameraHandler;
import com.dff.cordova.plugin.camera.configurations.CameraHandlerThread;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Module for dependency injection.
 */
@Module
public class CameraActivityModule {
    
    @Provides
    CameraManager provideCameraManager(@ApplicationContext Context context) {
        return (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
    }
    
    @Provides
    DevicePolicyManager provideDevicePolicyManager(@ApplicationContext Context context) {
        return (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
    }
    
    @Provides
    CameraHandler provideHandler(CameraHandlerThread cameraHandlerThread) {
        cameraHandlerThread.start();
        return new CameraHandler(cameraHandlerThread.getLooper());
    }
}
