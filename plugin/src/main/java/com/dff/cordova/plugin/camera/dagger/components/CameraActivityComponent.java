package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.annotations.CameraActivityScope;
import com.dff.cordova.plugin.camera.dagger.modules.CameraActivityModule;

import dagger.BindsInstance;
import dagger.Subcomponent;

/**
 * Component for dependency injection.
 */
@CameraActivityScope
@Subcomponent(modules = {
    CameraActivityModule.class
})
public interface CameraActivityComponent {
    
    /**
     * Builder interface.
     */
    @Subcomponent.Builder
    interface Builder {
        Builder cameraActivityModule(CameraActivityModule module);
    
        @BindsInstance
        CameraActivityComponent.Builder cameraAcitvity(CameraActivity cameraActivity);
        
        CameraActivityComponent build();
    }
    
    void inject(CameraActivity cameraActivity);
 
}
