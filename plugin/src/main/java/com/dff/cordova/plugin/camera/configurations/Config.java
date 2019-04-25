package com.dff.cordova.plugin.camera.configurations;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Config {

    private boolean mWithPreview;
    private boolean mWithPicIndicator;

    @Inject
    public Config() {

    }

    public boolean isWithPreview() {
        return mWithPreview;
    }

    public void setWithPreview(boolean mWithPreview) {
        this.mWithPreview = mWithPreview;
    }

    public boolean isWithPicIndicator() {
        return mWithPicIndicator;
    }

    public void setWithPicIndicator(boolean mWithPicIndicator) {
        this.mWithPicIndicator = mWithPicIndicator;
    }
}
