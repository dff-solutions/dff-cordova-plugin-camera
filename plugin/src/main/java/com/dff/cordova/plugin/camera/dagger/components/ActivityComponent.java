package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;

import dagger.Component;

import javax.inject.Singleton;

/**
 * Component for dependency injection.
 */
@Singleton
@Component(modules = AppModule.class)
public interface ActivityComponent {

    void inject(CameraActivity cameraActivity);
    
    void inject(Camera2Activity cameraActivity);
    
    void inject(PreviewActivity previewActivity);

}
