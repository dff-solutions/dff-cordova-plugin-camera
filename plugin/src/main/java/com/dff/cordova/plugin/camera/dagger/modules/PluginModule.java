package com.dff.cordova.plugin.camera.dagger.modules;

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.actions.PluginAction;
import com.dff.cordova.plugin.camera.actions.TakePhoto;
import com.dff.cordova.plugin.camera.dagger.annotations.PluginComponentScope;
import com.dff.cordova.plugin.camera.dagger.annotations.PluginPermissions;

import org.apache.cordova.CordovaInterface;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Provider;

import dagger.Module;
import dagger.Provides;

/**
 * Module for dependency injection.
 */
@Module
public class PluginModule {
    private CordovaInterface cordovaInterface;
    private String[] pluginPermissions;
    private CameraPlugin cameraPlugin;

    public PluginModule(
        CordovaInterface cordovaInterface,
        String[] pluginPermissions,
        CameraPlugin cameraPlugin
    ) {
        this.cordovaInterface = cordovaInterface;
        this.pluginPermissions = Arrays.copyOf(pluginPermissions, pluginPermissions.length);
        this.cameraPlugin = cameraPlugin;
    }

    @Provides
    @PluginComponentScope
    CordovaInterface provideCordovaInterface() {
        return cordovaInterface;
    }

    @Provides
    @PluginComponentScope
    @PluginPermissions
    String[] providePluginPermissions() {
        return pluginPermissions.clone();
    }
    
    @Provides
    @PluginComponentScope
    CameraPlugin provideCameraPlugin() {
        return cameraPlugin;
    }
    
    @Provides
    @PluginComponentScope
    Map<String, Provider<? extends PluginAction>> provideActionProviders(
        Provider<TakePhoto> takePhotoProvider
    ) {
        Map<String, Provider<? extends PluginAction>> actionProviders = new HashMap<>();

        actionProviders.put(TakePhoto.ACTION, takePhotoProvider);

        return actionProviders;
    }
}
