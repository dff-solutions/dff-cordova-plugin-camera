package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules =
    {
        AppModule.class
    })
public interface CameraActivityComponent {

    void inject(CameraActivity cameraActivity);

}
