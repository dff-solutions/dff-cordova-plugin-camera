package com.dff.cordova.plugin.camera.dagger.modules;

import com.dff.cordova.plugin.camera.dagger.components.ActionHandlerServiceComponent;
import com.dff.cordova.plugin.camera.dagger.components.ActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.CameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.PluginComponent;

import dagger.Module;

/**
 * Module for dependency injection.
 */
@Module(subcomponents = {
    CameraActivityComponent.class
})
public class ActivityModule {
}
