package com.dff.cordova.plugin.camera.dagger.modules;

import com.dff.cordova.plugin.camera.configurations.ActionHandler;
import com.dff.cordova.plugin.camera.configurations.ActionHandlerThread;
import com.dff.cordova.plugin.camera.dagger.annotations.ActionHandlerScope;

import dagger.Module;
import dagger.Provides;

@Module
public class ActionHandlerServiceModule {
    @Provides
    @ActionHandlerScope
    ActionHandler provideActionHandler(ActionHandlerThread actionHandlerThread) {
        actionHandlerThread.start();
        return new ActionHandler(actionHandlerThread.getLooper());
    }
}
