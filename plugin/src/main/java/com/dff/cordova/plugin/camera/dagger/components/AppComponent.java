package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.dagger.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
    AppModule.class
})
public interface AppComponent {

    ActionHandlerServiceComponent.Builder actionHandlerServiceComponentBuilder();

    PluginComponent.Builder pluginComponentBuilder();
}