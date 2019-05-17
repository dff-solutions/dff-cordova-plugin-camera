package com.dff.cordova.plugin.camera;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import com.dff.cordova.plugin.camera.actions.PluginAction;
import com.dff.cordova.plugin.camera.configurations.ActionsManager;
import com.dff.cordova.plugin.camera.configurations.Config;
import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.helpers.PermissionHelper;
import com.dff.cordova.plugin.camera.log.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;

import java.util.Arrays;

import javax.inject.Inject;

/**
 * Plugin that handle the process to take a photo: opening and releasing a camera instance.
 *
 * @author Anthony Nahas
 * @version 3.0.3
 * @since 05.01.2017
 */
public class CameraPlugin extends CordovaPlugin {
    private static final String TAG = "CameraPlugin";

    private static final int PERMISSION_REQUEST_CODE = 0;
    // contains dangerous permissions
    // @see https://developer.android.com/guide/topics/permissions/overview.html#normal-dangerous
    private static final String[] PERMISSIONS = new String[] {
        Manifest.permission.CAMERA,
    };

    @Inject
    Config mConfig;

    @Inject
    Log log;

    @Inject
    ActionsManager actionsManager;

    @Inject
    PermissionHelper permissionHelper;
    
    @Inject
    @ApplicationContext
    Context applicationContext;
    
    @Inject
    public CameraPlugin(){}

    /**
     * Initializing the plugin by setting and allocating important information and objects.
     */
    @Override
    public void pluginInitialize() {
        super.pluginInitialize();

        DaggerManager
            .getInstance()
            .in(cordova.getActivity().getApplication())
            .in(cordova, PERMISSIONS, this)
            .inject(this);
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);
        requestPermissions();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log.d(TAG, "onDestroy");
        actionsManager.onDestroy();
    }

    @Override
    public void onRequestPermissionResult(
        int requestCode,
        String[] permissions,
        int[] grantResults
    ) {
        log.d(TAG, "onRequestPermissionResult - requestCode: " + requestCode);

        for (int i = 0; i < grantResults.length; i++) {
            boolean granted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            log.i(TAG, permissions[i] + " granted: " + granted);
        }
    }

    /**
     * Executes an action called by JavaScript.
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return Whether the action was valid
     */
    @Override
    public boolean execute(
        final String action,
        final JSONArray args,
        final CallbackContext callbackContext
    ) {
        PluginAction actionInstance;

        try {
            // check permissions
            if (!requestPermissions()) {
                // try action anyway since not all actions require permission.
                // actions are queued and PERMISSIONS might be granted when action is running
                log.w(TAG, String.format(
                    "required permissions %s not granted",
                    Arrays.toString(PERMISSIONS)
                ));
            }

            actionInstance = actionsManager
                .createAction(action, args, callbackContext);
            if (actionInstance != null) {
                boolean result = actionsManager.runAction(actionInstance);

                if (!result) {
                    throw new Exception("could not queue action " + actionInstance.getActionName());
                }

                return true;
            }

            return super.execute(action, args, callbackContext);
        } catch (Exception e) {
            log.e(TAG, e.getMessage(), e);
            callbackContext.error(e.getMessage());

            return false;
        }
    }

    /**
     * Requests permissions if user has not selected the Don't ask again option
     * for all permissions.
     *
     * @return True if all permissions are granted false otherwise
     */
    private boolean requestPermissions() {
        boolean allGranted = permissionHelper.hasAllPermissions(PERMISSIONS);
        boolean showPermissionRationale = permissionHelper
            .shouldShowRequestPermissionRationale(cordova.getActivity(), PERMISSIONS);

        log.d(TAG, String.format("all permissions granted: %b", allGranted));

        if (!allGranted && showPermissionRationale) {
            log.d(TAG, String.format("request permissions for %s", Arrays.toString(PERMISSIONS)));
            cordova.requestPermissions(this, PERMISSION_REQUEST_CODE, PERMISSIONS);
        }

        return allGranted;
    }
}
