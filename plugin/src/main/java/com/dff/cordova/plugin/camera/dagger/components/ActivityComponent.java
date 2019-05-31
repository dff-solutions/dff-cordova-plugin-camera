package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.dagger.annotations.ActivityScope;
import com.dff.cordova.plugin.camera.dagger.modules.ActivityModule;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;

import dagger.Component;
import dagger.Subcomponent;

import javax.inject.Singleton;

/**
 * Component for dependency injection.
 */
@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    
    @Subcomponent.Builder
    interface Builder {
        Builder activityModule(ActivityModule module);
        
        ActivityComponent build();
    }
    
    void inject(Camera2Activity cameraActivity);
    
    void inject(PreviewActivity previewActivity);
}
