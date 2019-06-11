package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.dagger.annotations.ActivityScope;
import com.dff.cordova.plugin.camera.dagger.modules.ActivityModule;

import dagger.Subcomponent;

/**
 * Component for dependency injection.
 */
@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    
    /**
     * Builder interface.
     */
    @Subcomponent.Builder
    interface Builder {
        Builder activityModule(ActivityModule module);
        
        ActivityComponent build();
    }
    
    CameraActivityComponent.Builder cameraActivityComponentBuilder();
    
    PreviewActivityComponent.Builder previewActivityComponentBuilder();
}
