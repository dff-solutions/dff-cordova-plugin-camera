package com.dff.cordova.plugin.camera.exceptions;

import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This class implements the Thread.UncaughtException Handler.
 * It catches unforeseen exceptions and logs them.
 */
@Singleton
public class UnexpectedExceptionHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "UnexpectedExceptionHandler";

    @Inject
    Log log;

    @Inject
    public UnexpectedExceptionHandler(Log log) {
        this.log = log;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        log.e(TAG, "UncaughtException in " + thread + " Exception = " + throwable, throwable);
    }
}
