package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.dagger.annotations.PluginComponentScope;
import com.dff.cordova.plugin.camera.dagger.modules.PluginModule;

import dagger.Subcomponent;

@PluginComponentScope
@Subcomponent(modules = { PluginModule.class })
public interface PluginComponent {

    @Subcomponent.Builder
    interface Builder {
        Builder pluginModule(PluginModule module);

        PluginComponent build();
    }

    void inject(CameraPlugin plugin);
}
