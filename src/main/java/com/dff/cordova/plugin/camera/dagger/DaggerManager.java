package com.dff.cordova.plugin.camera.dagger;

import android.app.Application;
import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.dagger.components.CameraPluginComponent;
import com.dff.cordova.plugin.camera.dagger.components.DaggerCameraPluginComponent;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;

public class DaggerManager {

    private static DaggerManager mDaggerManager;

    private CameraPluginComponent mCameraPluginComponent;

    private AppModule mAppModule;
    private Application mApplication;

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

    public void inject(CameraPlugin cameraPlugin){
        if(mCameraPluginComponent == null){
            mCameraPluginComponent = DaggerCameraPluginComponent
                .builder()
                .appModule(mAppModule)
                .build();
        }
        mCameraPluginComponent.inject(cameraPlugin);
    }
}
