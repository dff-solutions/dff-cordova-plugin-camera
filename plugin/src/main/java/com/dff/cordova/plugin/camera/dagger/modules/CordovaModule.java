package com.dff.cordova.plugin.camera.dagger.modules;

import android.app.Activity;
import android.content.Context;
import com.dff.cordova.plugin.camera.dagger.annotations.ActivityContext;
import dagger.Module;
import dagger.Provides;
import org.apache.cordova.CordovaInterface;

/**
 * @author Anthony Nahas
 * @version 1.0
 * @since 17.10.2017
 */
@Module
public class CordovaModule {

    private CordovaInterface mCordovaInterface;

    public CordovaModule(CordovaInterface mCordovaInterface) {
        this.mCordovaInterface = mCordovaInterface;
    }

    @Provides
    @ActivityContext
    Context provideActivityContext() {
        return mCordovaInterface.getActivity();
    }

    @Provides
    Activity provideMainActivityRef() {
        return mCordovaInterface.getActivity();
    }
}
