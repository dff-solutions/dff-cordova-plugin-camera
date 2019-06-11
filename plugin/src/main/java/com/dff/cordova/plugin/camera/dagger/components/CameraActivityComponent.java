package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.annotations.CameraActivityScope;

import dagger.Subcomponent;

/**
 * Component for dependency injection.
 */
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