package com.dff.cordova.plugin.camera.dagger.components.sub;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent(modules = {AppModule.class})
public interface CameraActivitySubcomponent  extends AndroidInjector<CameraActivity> {

    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<CameraActivity>{}

}
