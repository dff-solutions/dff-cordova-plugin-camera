package com.dff.cordova.plugin.camera.dagger;

import android.app.Application;
import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.dagger.components.CameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.CameraPluginComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerCameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerCameraPluginComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerPreviewActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.PreviewActivityComponent;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;
import com.dff.cordova.plugin.camera.dagger.modules.CordovaModule;
import org.apache.cordova.CordovaInterface;

/**
 * Manages Dagger injection.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 13.10.17
 */
public class DaggerManager {
    private static DaggerManager mDaggerManager;

    private CameraPluginComponent mCameraPluginComponent;
    private CameraActivityComponent mCameraActivityComponent;
    private PreviewActivityComponent previewActivityComponent;

    private AppModule mAppModule;
    private CordovaModule mCordovaModule;

    public static synchronized DaggerManager getInstance() {
        if (mDaggerManager == null) {
            mDaggerManager = new DaggerManager();
        }
        return mDaggerManager;
    }

    public DaggerManager in(Application application) {
        if (mAppModule == null && application != null) {
            mAppModule = new AppModule(application);
        }

        return this;
    }

    public DaggerManager and(CordovaInterface cordovaInterface) {

        if (mCordovaModule == null && cordovaInterface != null) {
            mCordovaModule = new CordovaModule(cordovaInterface);
        }
        return this;
    }

    public void inject(CameraPlugin cameraPlugin) {
        if (mCameraPluginComponent == null) {
            mCameraPluginComponent = DaggerCameraPluginComponent
                .builder()
                .appModule(mAppModule)
                .cordovaModule(mCordovaModule)
                .build();
        }
        mCameraPluginComponent.inject(cameraPlugin);
    }

    public void inject(CameraActivity cameraActivity) {
        if (mCameraActivityComponent == null) {
            mCameraActivityComponent = DaggerCameraActivityComponent
                .builder()
                .appModule(mAppModule)
                .cordovaModule(mCordovaModule)
                .build();
        }
        mCameraActivityComponent.inject(cameraActivity);
    }

    public void inject(PreviewActivity previewActivity) {
        if (previewActivityComponent == null) {
            previewActivityComponent = DaggerPreviewActivityComponent
                .builder()
                .appModule(mAppModule)
                .build();
        }

        previewActivityComponent.inject(previewActivity);
    }
}
