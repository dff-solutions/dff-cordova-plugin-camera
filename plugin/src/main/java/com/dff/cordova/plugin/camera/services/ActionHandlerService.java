package com.dff.cordova.plugin.camera.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.MessageQueue;

import com.dff.cordova.plugin.camera.actions.PluginAction;
import com.dff.cordova.plugin.camera.configurations.ActionHandler;
import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.dagger.annotations.ActionHandlerScope;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * ActionHandlerService manages the call of the actions.
 * Actions are executed in order. (first in first out)
 *
 * @see <a href="https://developer.android.com/guide/components/services">https://developer.android.com/guide/components/services</a>
 */
@ActionHandlerScope
public class ActionHandlerService extends Service implements MessageQueue.IdleHandler {
    private static final String TAG = "ActionHandlerService";
    private boolean started = false;
    private boolean bound = false;

    @Inject
    ActionHandler actionHandler;

    @Inject
    ActionHandlerServiceBinder binder;

    @Inject
    Log log;

    @Inject
    @ApplicationContext
    Context context;

    @Override
    public void onCreate() {
        DaggerManager
            .getInstance()
            .in(getApplication())
            .inject(this);

        log.d(TAG, "onCreate");

        binder.setService(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            actionHandler.getLooper().getQueue().addIdleHandler(this);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        log.d(TAG, "onBind " + intent);

        if (! started) {
            throw new IllegalStateException(TAG + " has to be started before binding");
        }

        bound = true;

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        log.d(TAG, "onUnbind " + intent);
        bound = false;

        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log.d(TAG, "onStartCommand " + intent + " " + flags + " " + startId);
        started = true;

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        log.d(TAG, "onDestroy");
        started = false;
        actionHandler.getLooper().quitSafely();
        actionHandler = null;
    }

    public boolean execute(PluginAction action) {
        return actionHandler.post(action);
    }

    @Override
    public boolean queueIdle() {
        log.d(TAG, "queueIdle");

        // stop self if there are no more components bound and queue is idle
        if (! bound) {
            log.d(TAG, "no bindings and queue is idle => stopSelf()");
            stopSelf();
        }

        return true;
    }
}
