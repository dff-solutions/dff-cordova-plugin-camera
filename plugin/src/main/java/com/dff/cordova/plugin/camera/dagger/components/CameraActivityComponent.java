package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.annotations.CameraActivityScope;
import com.dff.cordova.plugin.camera.dagger.modules.ActivityModule;

import dagger.Component;
import dagger.Subcomponent;

@CameraActivityScope
@Subcomponent
public interface CameraActivityComponent {
    
    /**
     * Builder interface.
     */
    @Subcomponent.Builder
    interface Builder {
        CameraActivityComponent build();
    }
    
    void inject(CameraActivity cameraActivity);
}
