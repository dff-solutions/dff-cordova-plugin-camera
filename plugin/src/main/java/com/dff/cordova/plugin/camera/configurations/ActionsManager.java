package com.dff.cordova.plugin.camera.configurations;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.dff.cordova.plugin.camera.actions.PluginAction;
import com.dff.cordova.plugin.camera.dagger.annotations.ActionHandlerServiceIntent;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.dagger.annotations.PluginComponentScope;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.services.ActionHandlerService;
import com.dff.cordova.plugin.camera.services.ActionHandlerServiceBinder;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

@PluginComponentScope
public class ActionsManager implements ServiceConnection {
    private static final String TAG = "ActionsManager";

    private Log log;
    private ActionHandlerService actionHandlerService;
    private Map<String, Provider<? extends PluginAction>> actionProviders;
    private Context context;
    private boolean bound = false;

    @Inject
    public ActionsManager(
        @ApplicationContext Context context,
        @ActionHandlerServiceIntent Intent serviceIntent,
        Log log,
        Map<String, Provider<? extends PluginAction>> actionProviders
    ) {
        this.context = context;
        this.log = log;
        this.actionProviders = actionProviders;

        this.context.startService(serviceIntent);
        this.context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }

    public PluginAction createAction(
        final String action,
        final JSONArray args,
        final CallbackContext callbackContext
    ) {
        PluginAction actionInstance = null;
        Provider<? extends PluginAction> actionProvider = actionProviders.get(action);

        if (actionProvider != null) {
            actionInstance = actionProvider.get();
        }

        if (actionInstance != null) {
            actionInstance
                .setAction(action)
                .setArgs(args)
                .setCallbackContext(callbackContext);
        }

        return actionInstance;
    }

    public boolean runAction(PluginAction action) {
        if (bound && actionHandlerService != null) {
            return actionHandlerService.execute(action);
        }

        throw new IllegalStateException(String.format(
            "%s service %s bound %b",
            TAG,
            actionHandlerService,
            bound
        ));
    }

    public void onDestroy() {
        log.d(TAG, "onDestroy");
        context.unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        log.d(TAG, "bind service " + name);
        actionHandlerService = ((ActionHandlerServiceBinder) service).getService();
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        log.d(TAG, "unbind service " + name);
        actionHandlerService = null;
        bound = false;
    }

    public boolean isBound() {
        return bound;
    }
}
