package com.dff.cordova.plugin.camera.dagger.modules;

import com.dff.cordova.plugin.camera.dagger.components.CameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.PreviewActivityComponent;

import dagger.Module;

/**
 * Module for dependency injection.
 */
@Module(subcomponents = {
    CameraActivityComponent.class,
    PreviewActivityComponent.class
})
public class ActivityModule {

}
