package com.dff.cordova.plugin.camera.configurations;

import android.os.Handler;
import android.os.Looper;

/**
 * This class extends Handler. Prevents giving the handler a different looper.
 */
public class ActionHandler extends Handler {
    public ActionHandler(Looper looper) {
        super(looper);
    }
}
