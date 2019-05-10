package com.dff.cordova.plugin.camera.dagger;

import android.app.Application;
import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.dagger.components.ActionHandlerServiceComponent;
import com.dff.cordova.plugin.camera.dagger.components.AppComponent;
import com.dff.cordova.plugin.camera.dagger.components.CameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerAppComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerCameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerPreviewActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.PluginComponent;
import com.dff.cordova.plugin.camera.dagger.components.PreviewActivityComponent;
import com.dff.cordova.plugin.camera.dagger.modules.ActionHandlerServiceModule;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;
import com.dff.cordova.plugin.camera.dagger.modules.PluginModule;
import com.dff.cordova.plugin.camera.services.ActionHandlerService;

import org.apache.cordova.CordovaInterface;

/**
 * Manages Dagger injection.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 13.10.17
 */
public class DaggerManager {
    private static DaggerManager daggerManager;

    private AppComponent appComponent;
    private ActionHandlerServiceComponent actionHandlerServiceComponent;
    private PluginComponent pluginComponent;
    private CameraActivityComponent cameraActivityComponent;
    private PreviewActivityComponent previewActivityComponent;

    private AppModule appModule;
    private PluginModule pluginModule;

    private DaggerManager() {}

    public static synchronized DaggerManager getInstance() {
        if (daggerManager == null) {
            daggerManager = new DaggerManager();
        }

        return daggerManager;
    }

    public DaggerManager in(Application application) {
        if (appModule == null && application != null) {
            appModule = new AppModule(application);
        }

        return this;
    }

    public DaggerManager in(CordovaInterface cordovaInterface, String[] pluginPermissions) {
        if (pluginModule == null) {
            pluginModule = new PluginModule(cordovaInterface, pluginPermissions);
        }

        return this;
    }

    public void inject(CameraPlugin cameraPlugin) {
        getPluginComponent()
            .inject(cameraPlugin);
    }

    public void inject(ActionHandlerService actionHandlerService) {
        getActionHandlerServiceComponent()
            .inject(actionHandlerService);
    }

    public void inject(CameraActivity cameraActivity) {
        if (cameraActivityComponent == null) {
            cameraActivityComponent = DaggerCameraActivityComponent
                .builder()
                .appModule(appModule)
                .build();
        }

        cameraActivityComponent.inject(cameraActivity);
    }
    
    public void inject(Camera2Activity cameraActivity) {
        if (cameraActivityComponent == null) {
            cameraActivityComponent = DaggerCameraActivityComponent
                .builder()
                .appModule(appModule)
                .build();
        }
        
        cameraActivityComponent.inject(cameraActivity);
    }

    public void inject(PreviewActivity previewActivity) {
        if (previewActivityComponent == null) {
            previewActivityComponent = DaggerPreviewActivityComponent
                .builder()
                .appModule(appModule)
                .build();
        }

        previewActivityComponent.inject(previewActivity);
    }

    private AppComponent getAppComponent() {
        if (appComponent == null) {
            appComponent = DaggerAppComponent
                .builder()
                .appModule(appModule)
                .build();
        }

        return appComponent;
    }

    private PluginComponent getPluginComponent() {
        if (pluginComponent == null) {
            pluginComponent = getAppComponent()
                .pluginComponentBuilder()
                .pluginModule(pluginModule)
                .build();
        }

        return pluginComponent;
    }

    private ActionHandlerServiceComponent getActionHandlerServiceComponent() {
        if (actionHandlerServiceComponent == null) {
            actionHandlerServiceComponent = getAppComponent()
                .actionHandlerServiceComponentBuilder()
                .actionHandlerModule(new ActionHandlerServiceModule())
                .build();
        }

        return actionHandlerServiceComponent;
    }
}
