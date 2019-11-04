package com.dff.cordova.plugin.camera.configurations;

import android.os.HandlerThread;
import android.os.Process;

import com.dff.cordova.plugin.camera.exceptions.UnexpectedExceptionHandler;

import javax.inject.Inject;

/**
 * This class extends HandlerThread.
 * On construction it sets the UncaughtExceptionHandler.
 * The thread is used to process the taken picture in the camera activity.
 */
public class CameraHandlerThread extends HandlerThread {
    private static final String TAG = "CameraHandlerThread";
    
    @Inject
    public CameraHandlerThread(UnexpectedExceptionHandler unexpectedExceptionHandler) {
        super(TAG, Process.THREAD_PRIORITY_BACKGROUND);
        this.setUncaughtExceptionHandler(unexpectedExceptionHandler);
    }
}
