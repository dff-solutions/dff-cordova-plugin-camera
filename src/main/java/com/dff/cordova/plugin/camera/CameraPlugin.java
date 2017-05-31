package com.dff.cordova.plugin.camera;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.dff.cordova.plugin.camera.R.R;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.common.CommonPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Plugin that handle the process to take a photo: opening and releasing a camera instance.
 *
 * @author Anthony Nahas
 * @version 3.0.0
 * @since 05.01.2017
 */
public class CameraPlugin extends CommonPlugin {

    private static final String TAG = "CameraPlugin";
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private Context mContext;

    /**
     * Initializing the plugin by setting and allocating important information and objects.
     */
    @Override
    public void pluginInitialize() {
        requestCameraPermission();
        super.pluginInitialize();
        mContext = cordova.getActivity().getApplicationContext();
        cordova.setActivityResultCallback(this);
        R.PACKAGE_NAME = mContext.getPackageName();
        R.RESOURCES = mContext.getResources();
    }

    private void requestCameraPermission() {
        CommonPlugin.addPermission(CAMERA_PERMISSION);
    }

    /**
     * Executes an action called by JavaScript
     *
     * @param action          The action to execute.
     * @param args            The exec() arguments.
     * @param callbackContext The callback context used when calling back into JavaScript.
     * @return
     * @throws JSONException
     */
    @Override
    public boolean execute(final String action, final JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action != null) {
            R.sCallBackContext = callbackContext;
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Action = " + action);
                    if (action.equals(R.ACTION_TAKE_PHOTO)) {
                        boolean withPreview = args.optBoolean(0, false);
                        Intent intent = new Intent(mContext, CameraActivity.class);
                        intent.putExtra(R.WITH_PREVIEW_KEY, withPreview);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cordova.getActivity().startActivity(intent);
                    } else {
                        Log.d(TAG, "Action not found");
                    }
                }
            });
            return true;
        }
        return false;
    }
}
