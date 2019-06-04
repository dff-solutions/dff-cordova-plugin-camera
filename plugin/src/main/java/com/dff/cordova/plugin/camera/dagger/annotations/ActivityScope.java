package com.dff.cordova.plugin.camera.dagger.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for Activity.
 */
@Qualifier
@Retention(RUNTIME)
public @interface ActivityScope {
}
