package com.dff.cordova.plugin.camera.dagger.components;

/**
 * @author Anthony Nahas
 * @version 3.0.2
 * @since 11.10.17
 */

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules =
    {
        AppModule.class
    })
public interface CameraPluginComponent {

    void inject(CameraPlugin cameraPlugin);

}
