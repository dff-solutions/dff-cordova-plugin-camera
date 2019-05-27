package com.dff.cordova.plugin.camera.dagger.components;

import com.dff.cordova.plugin.camera.dagger.annotations.ActionHandlerScope;
import com.dff.cordova.plugin.camera.dagger.modules.ActionHandlerServiceModule;
import com.dff.cordova.plugin.camera.services.ActionHandlerService;

import dagger.Subcomponent;

/**
 * Component for dependency injection.
 */
@ActionHandlerScope
@Subcomponent(modules = {
    ActionHandlerServiceModule.class
})
public interface ActionHandlerServiceComponent {
    
    /**
     * Builder interface.
     */
    @Subcomponent.Builder
    interface Builder {
        Builder actionHandlerModule(ActionHandlerServiceModule module);

        ActionHandlerServiceComponent build();
    }

    void inject(ActionHandlerService service);
}
