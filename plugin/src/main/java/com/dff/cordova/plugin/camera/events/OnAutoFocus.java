package com.dff.cordova.plugin.camera.events;

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
