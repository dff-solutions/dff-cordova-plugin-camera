package com.dff.cordova.plugin.camera.dagger;

import android.app.Application;
import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.components.CameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.CameraPluginComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerCameraActivityComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerCameraPluginComponent;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;
import com.dff.cordova.plugin.camera.dagger.modules.CordovaModule;
import org.apache.cordova.CordovaInterface;

/**
 * @author Anthony Nahas
 * @version 1.0
 * @since 13.10.17
 */
public class DaggerManager {

    private static DaggerManager mDaggerManager;

    private CameraPluginComponent mCameraPluginComponent;
    private CameraActivityComponent mCameraActivityComponent;

    private AppModule mAppModule;
    private CordovaModule mCordovaModule;

    private Application mApplication;
    private CordovaInterface mCordovaInterface;

    public static synchronized DaggerManager getInstance() {
        if (mDaggerManager == null) {
            mDaggerManager = new DaggerManager();
        }
        return mDaggerManager;
    }

    public DaggerManager in(Application mApplication) {
        this.mApplication = mApplication;
        if (mAppModule == null) {
            mAppModule = mApplication != null ? new AppModule(mApplication) : null;
        }
        return this;
    }

    public DaggerManager and(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
        if (mCordovaModule == null) {
            mCordovaModule = mCordovaInterface != null ? new CordovaModule(mCordovaInterface) : null;
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
}
