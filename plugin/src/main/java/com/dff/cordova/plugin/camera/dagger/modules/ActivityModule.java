package com.dff.cordova.plugin.camera.dagger.modules;

import android.os.Handler;

import com.dff.cordova.plugin.camera.configurations.CameraHandlerThread;
import com.dff.cordova.plugin.camera.dagger.components.CameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.PreviewActivityComponent;

import dagger.Module;
import dagger.Provides;

/**
 * Module for dependency injection.
 */
@Module(subcomponents = {
    CameraActivityComponent.class,
    PreviewActivityComponent.class
})
public class ActivityModule {
    @Provides
    Handler provideHandler(CameraHandlerThread cameraHandlerThread) {
        cameraHandlerThread.start();
        return new Handler(cameraHandlerThread.getLooper());
    }
}
