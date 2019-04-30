package com.dff.cordova.plugin.camera.configurations;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Config {
    private boolean mWithPreview = false;

    @Inject
    public Config() {}

    public boolean isWithPreview() {
        return mWithPreview;
    }

    public void setWithPreview(boolean withPreview) {
        this.mWithPreview = withPreview;
    }
}
