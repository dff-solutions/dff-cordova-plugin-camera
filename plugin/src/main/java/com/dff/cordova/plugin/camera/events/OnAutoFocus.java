package com.dff.cordova.plugin.camera.events;

/**
 * Class with a boolean "isSuccess".
 */
public class OnAutoFocus {

    private boolean isSuccess;

    public OnAutoFocus(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
