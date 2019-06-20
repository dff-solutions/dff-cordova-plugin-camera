package com.dff.cordova.plugin.camera.dagger.modules;

import android.content.Context;
import android.hardware.camera2.CameraManager;
import android.os.Handler;

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
    Handler provideHandler(CameraHandlerThread cameraHandlerThread) {
        cameraHandlerThread.start();
        return new Handler(cameraHandlerThread.getLooper());
    }
}
