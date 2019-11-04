package com.dff.cordova.plugin.camera.dagger.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for the PluginComponent.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface PluginComponentScope {
}
