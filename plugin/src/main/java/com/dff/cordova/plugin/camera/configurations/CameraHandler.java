package com.dff.cordova.plugin.camera.configurations;

import android.os.Handler;
import android.os.Looper;

/**
 * This class extends Handler to specify where the handler is injected.
 */
public class CameraHandler extends Handler {
    public CameraHandler(Looper looper) {
        super(looper);
    }
}
