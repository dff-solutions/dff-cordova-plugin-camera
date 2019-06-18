package com.dff.cordova.plugin.camera.dagger.modules;

import com.dff.cordova.plugin.camera.actions.PluginAction;
import com.dff.cordova.plugin.camera.actions.TakePhoto;
import com.dff.cordova.plugin.camera.dagger.annotations.PluginComponentScope;

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

    public PluginModule() {}
    
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
