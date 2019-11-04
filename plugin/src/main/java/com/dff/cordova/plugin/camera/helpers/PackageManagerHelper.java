package com.dff.cordova.plugin.camera.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helps working with current package.
 */
@Singleton
public class PackageManagerHelper {
    private Context context;

    @Inject
    public PackageManagerHelper(@ApplicationContext Context context) {
        this.context = context;
    }

    /**
     * Checks if this application would like to allow debugging of its code.
     *
     * @return True if debugging is allowed
     */
    public boolean isDebuggable() {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        return (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) ==
            ApplicationInfo.FLAG_DEBUGGABLE;
    }

    /**
     * Get default directory of package to persist data.
     *
     * @return Full path to the default directory assigned to the package for its
     *     persistent data.
     */
    public String getDataDir() {
        return context.getApplicationInfo().dataDir;
    }
}
