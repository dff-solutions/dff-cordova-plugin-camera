package com.dff.cordova.plugin.camera.dagger.modules;

import android.app.Application;
import android.content.Context;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

@Module
public class AppModule {

    private Application mApp;

    public AppModule(Application app) {
        this.mApp = app;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApp;
    }

    @Provides
    Application provideApplication() {
        return mApp;
    }

    @Provides
    @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }
}
