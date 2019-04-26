package com.dff.cordova.plugin.camera.configurations;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Config {
    private boolean mWithPreview = false;
    private boolean mWithPicIndicator = false;

    @Inject
    public Config() {

    }

    public boolean isWithPreview() {
        return mWithPreview;
    }

    public void setWithPreview(boolean withPreview) {
        this.mWithPreview = withPreview;
    }

    public boolean isWithPicIndicator() {
        return mWithPicIndicator;
    }

    public void setWithPicIndicator(boolean withPicIndicator) {
        this.mWithPicIndicator = withPicIndicator;
    }
}
