package com.dff.cordova.plugin.camera.dagger.modules;

import android.app.Application;
import android.content.Context;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

/**
 * @author Anthony Nahas
 * @version 1.0
 * @since 12.10.17
 */
@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application app) {
        this.mApp = app;
    }

    @Provides
    @ApplicationContext
    public Context provideContext() {
        return mApp;
    }

    @Provides
    public Application provideApplication() {
        return mApp;
    }

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }
}
