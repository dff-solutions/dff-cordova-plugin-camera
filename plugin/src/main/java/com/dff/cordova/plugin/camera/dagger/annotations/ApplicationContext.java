package com.dff.cordova.plugin.camera.dagger.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Anthony Nahas
 * @version 3.0.2
 * @since 12.10.17
 */
@Qualifier
@Retention(RUNTIME)
public @interface ApplicationContext {
}
