package com.dff.cordova.plugin.camera.dagger;

import android.app.Application;

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.dagger.components.ActionHandlerServiceComponent;
import com.dff.cordova.plugin.camera.dagger.components.AppComponent;
import com.dff.cordova.plugin.camera.dagger.components.ActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.CameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerAppComponent;
import com.dff.cordova.plugin.camera.dagger.components.PluginComponent;
import com.dff.cordova.plugin.camera.dagger.modules.ActionHandlerServiceModule;
import com.dff.cordova.plugin.camera.dagger.modules.ActivityModule;
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
    private ActivityComponent activityComponent;
    private CameraActivityComponent cameraActivityComponent;

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
    
    /**
     * Provides objects to the PluginModule.
     *
     * @param cordovaInterface cordovaInterface
     * @param pluginPermissions needed permissions
     * @param cameraPlugin core class
     * @return Daggermanager
     */
    public DaggerManager in(
        CordovaInterface cordovaInterface,
        String[] pluginPermissions,
        CameraPlugin cameraPlugin
    ) {
        if (pluginModule == null) {
            pluginModule = new PluginModule(
                cordovaInterface,
                pluginPermissions,
                cameraPlugin
            );
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
    
    /**
     * Injects the previewActivty.
     *
     * @param previewActivity the activity
     */
    public void inject(PreviewActivity previewActivity) {
        getActivityComponent().inject(previewActivity);
    }
    
    /**
     * Injects the cameraActivty.
     *
     * @param cameraActivity the activity
     */
    public void inject(CameraActivity cameraActivity) {
        getCameraActivityComponent().inject(cameraActivity);
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

    private ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            activityComponent = getAppComponent()
                .activityComponentBuilder()
                .activityModule(new ActivityModule())
                .build();

        }
        return activityComponent;
    }
    
    private CameraActivityComponent getCameraActivityComponent() {
        if (cameraActivityComponent == null) {
            cameraActivityComponent = getActivityComponent()
                .cameraActivityComponentBuilder()
                .build();
        }
        return  cameraActivityComponent;
    }
}
