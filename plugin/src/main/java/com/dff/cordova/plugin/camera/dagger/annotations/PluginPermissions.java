package com.dff.cordova.plugin.camera.dagger.annotations;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for the PluginPermission.
 */
@Qualifier
@Retention(RUNTIME)
public @interface PluginPermissions {
}
