package com.dff.cordova.plugin.camera.dagger.components;

/**
 * @author Anthony Nahas
 * @version 3.0.2
 * @since 11.10.17
 */

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.components.sub.CameraActivitySubcomponent;
import com.dff.cordova.plugin.camera.dagger.modules.AppModule;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

import javax.inject.Singleton;


@Module(subcomponents = CameraActivitySubcomponent.class)
abstract class ActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(CameraActivity.class)
    abstract AndroidInjector.Factory<? extends CameraActivity>
    bindYourActivityInjectorFactory(CameraActivitySubcomponent.Builder builder);
}

@Singleton
@Component(modules =
    {
        AppModule.class,
        ActivityModule.class
    })
public interface CameraPluginComponent {

    void inject(CameraPlugin cameraPlugin);
}
