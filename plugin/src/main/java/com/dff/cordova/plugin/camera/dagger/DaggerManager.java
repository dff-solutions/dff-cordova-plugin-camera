package com.dff.cordova.plugin.camera.dagger;

import android.Manifest;
import android.app.Application;
import android.content.Context;

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.dagger.components.ActionHandlerServiceComponent;
import com.dff.cordova.plugin.camera.dagger.components.ActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.AppComponent;
import com.dff.cordova.plugin.camera.dagger.components.CameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerAppComponent;
import com.dff.cordova.plugin.camera.dagger.components.PluginComponent;
import com.dff.cordova.plugin.camera.dagger.components.PreviewActivityComponent;
import com.dff.cordova.plugin.camera.dagger.modules.ActionHandlerServiceModule;
import com.dff.cordova.plugin.camera.dagger.modules.ActivityModule;
import com.dff.cordova.plugin.camera.dagger.modules.CameraActivityModule;
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
    
    // contains dangerous permissions
    // @see https://developer.android.com/guide/topics/permissions/overview.html#normal-dangerous
    private static final String[] PERMISSIONS = new String[] {
        Manifest.permission.CAMERA,
    };

    private AppComponent appComponent;
    private ActionHandlerServiceComponent actionHandlerServiceComponent;
    private PluginComponent pluginComponent;
    private ActivityComponent activityComponent;
    private PreviewActivityComponent previewActivityComponent;

    private Context context;
    private PluginModule pluginModule;
    private CameraActivityModule cameraActivityModule;
    
    private CordovaInterface cordovaInterface;
    private CameraActivity cameraActivity;

    private DaggerManager() {}

    public static synchronized DaggerManager getInstance() {
        if (daggerManager == null) {
            daggerManager = new DaggerManager();
        }
        return daggerManager;
    }

    public DaggerManager in(Application application) {
        this.context = application;
        return this;
    }
    
    /**
     * Creates a new cameraActivityModule if it wasn't already and stores the cameraActivity.
     *
     * @param cameraActivity CameraActivity which is store in daggerManger to be provided in the
     *                       component
     * @return DaggerManager
     */
    public DaggerManager in(CameraActivity cameraActivity) {
        this.cameraActivity = cameraActivity;
        if (cameraActivityModule == null) {
            cameraActivityModule = new CameraActivityModule();
        }
        return this;
    }
    
    /**
     * Provides objects to the PluginModule.
     *
     * @param cordovaInterface CordovaInterface
     * @return DaggerManager
     */
    public DaggerManager in(
        CordovaInterface cordovaInterface
    ) {
        if (pluginModule == null) {
            pluginModule = new PluginModule();
        }
        this.cordovaInterface = cordovaInterface;

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
     * Injects the previewActivity.
     *
     * @param previewActivity the activity
     */
    public void inject(PreviewActivity previewActivity) {
        getPreviewActivityComponent().inject(previewActivity);
    }
    
    /**
     * Injects the cameraActivity.
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
                .context(context)
                .pluginPermissions(PERMISSIONS)
                .build();
        }
        return appComponent;
    }

    private PluginComponent getPluginComponent() {
        if (pluginComponent == null) {
            pluginComponent = getAppComponent()
                .pluginComponentBuilder()
                .pluginModule(pluginModule)
                .pluginCordovaInterface(cordovaInterface)
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
        return getActivityComponent()
            .cameraActivityComponentBuilder()
            .cameraActivityModule(cameraActivityModule)
            .cameraAcitvity(cameraActivity)
            .build();
        
    }
    
    private PreviewActivityComponent getPreviewActivityComponent() {
        if (previewActivityComponent == null) {
            previewActivityComponent = getActivityComponent()
                .previewActivityComponentBuilder()
                .build();
        }
        return  previewActivityComponent;
    }
}
