package com.dff.cordova.plugin.camera.dagger.annotations;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by anahas on 13.06.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 13.06.17
 */

@Qualifier
@Retention(RUNTIME)
public @interface ActivityContext {
}
