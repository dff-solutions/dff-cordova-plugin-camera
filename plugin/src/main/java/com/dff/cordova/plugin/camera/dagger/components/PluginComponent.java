package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.dagger.annotations.PluginComponentScope;
import com.dff.cordova.plugin.camera.dagger.modules.PluginModule;

import org.apache.cordova.CordovaInterface;

import dagger.BindsInstance;
import dagger.Subcomponent;

/**
 * Component for dependency injection.
 */
@PluginComponentScope
@Subcomponent(modules = { PluginModule.class })
public interface PluginComponent {
    
    /**
     * Builder interface.
     */
    @Subcomponent.Builder
    interface Builder {
        Builder pluginModule(PluginModule module);
        
        @BindsInstance
        Builder pluginCordovaInterface(CordovaInterface cordovaInterface);
        
        PluginComponent build();
    }

    void inject(CameraPlugin plugin);
}
