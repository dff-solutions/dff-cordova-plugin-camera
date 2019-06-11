package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.dagger.annotations.PreviewActivityScope;

import dagger.Subcomponent;

/**
 * Component for dependency injection.
 */
@PreviewActivityScope
@Subcomponent
public interface PreviewActivityComponent {
    
    /**
     * Builder interface.
     */
    @Subcomponent.Builder
    interface Builder {
        PreviewActivityComponent build();
    }
    
    void inject(PreviewActivity previewActivity);
    
}
