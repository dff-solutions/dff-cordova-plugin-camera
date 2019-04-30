package com.dff.cordova.plugin.camera.services;

import android.os.Binder;

import com.dff.cordova.plugin.camera.dagger.annotations.ActionHandlerScope;

import javax.inject.Inject;

@ActionHandlerScope
public class ActionHandlerServiceBinder extends Binder {
    private ActionHandlerService actionHandlerService;

    @Inject
    public ActionHandlerServiceBinder() {}

    public ActionHandlerService getService() {
        return actionHandlerService;
    }

    void setService(ActionHandlerService actionHandlerService) {
        this.actionHandlerService = actionHandlerService;
    }
}
