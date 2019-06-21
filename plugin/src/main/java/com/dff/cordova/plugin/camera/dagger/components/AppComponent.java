package com.dff.cordova.plugin.camera.dagger.components;

import android.content.Context;

import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.dagger.annotations.PluginPermissions;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Component for dependency injection.
 */
@Singleton
@Component(modules = {
    AppModule.class
})
public interface AppComponent {

    ActionHandlerServiceComponent.Builder actionHandlerServiceComponentBuilder();

    PluginComponent.Builder pluginComponentBuilder();
    
    ActivityComponent.Builder activityComponentBuilder();
    
    /**
     * Builder interface.
     */
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(@ApplicationContext Context context);
    
        @BindsInstance
        Builder pluginPermissions(@PluginPermissions String[] permissions);
        
        AppComponent build();
    }
    
}
