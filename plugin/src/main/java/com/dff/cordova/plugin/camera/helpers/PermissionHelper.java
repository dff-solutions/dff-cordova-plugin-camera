package com.dff.cordova.plugin.camera.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.log.Log;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Helps handling plugin permissions.
 */
@Singleton
public class PermissionHelper {
    private static final String TAG = "PermissionHelper";
    private Log log;
    private Context context;

    @Inject
    public PermissionHelper(
        Log log,
        @ApplicationContext Context context
    ) {
        this.log = log;
        this.context = context;
    }

    /**
     * Checks if all required permissions are granted.
     *
     * @return True if all permissions are granted
     */
    public boolean hasAllPermissions(@NonNull String[] permissions) {
        for (String permission : permissions) {
            boolean granted = PERMISSION_GRANTED == ContextCompat
                .checkSelfPermission(
                    context,
                    permission
                );

            log.i(TAG, permission + " granted: " + granted);

            if (!granted) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if user has not selected the Don't ask again option in the permission request dialog
     * for at least one required permission.
     *
     * @return True if request for permissions should be shown for at least one permission
     */
    public boolean shouldShowRequestPermissionRationale(
        @NonNull Activity activity,
        @NonNull String[] permissions
    ) {
        boolean result = false;

        for (String permission : permissions) {
            boolean should = ActivityCompat
                .shouldShowRequestPermissionRationale(
                    activity,
                    permission
                );

            log.d(TAG, String.format(
                "shouldShowRequestPermissionRationale for %s: %b",
                permission,
                should
            ));

            if (should) {
                result = true;
            }
        }

        log.d(TAG, String.format(
            "shouldShowRequestPermissionRationale for %s: %b",
            Arrays.toString(permissions),
            result
        ));

        return result;
    }
}
