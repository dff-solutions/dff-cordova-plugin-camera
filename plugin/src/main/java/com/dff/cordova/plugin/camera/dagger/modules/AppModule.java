package com.dff.cordova.plugin.camera.dagger.modules;

import android.content.Context;
import android.content.Intent;

import com.dff.cordova.plugin.camera.dagger.annotations.ActionHandlerServiceIntent;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.dagger.components.ActionHandlerServiceComponent;
import com.dff.cordova.plugin.camera.dagger.components.PluginComponent;
import com.dff.cordova.plugin.camera.services.ActionHandlerService;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(subcomponents = {
    ActionHandlerServiceComponent.class,
    PluginComponent.class
})
public class AppModule {
    private Context applicationContext;

    public AppModule(Context context) {
        applicationContext = context;
    }

    @Provides
    @ApplicationContext
    Context provideApplicationContext() {
        return applicationContext;
    }

    @Provides
    @Singleton
    @ActionHandlerServiceIntent
    Intent provideActionHandlerServiceIntent(@ApplicationContext Context context) {
        return new Intent(context, ActionHandlerService.class);
    }

    @Provides
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }
}
