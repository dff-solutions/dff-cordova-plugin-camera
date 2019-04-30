package com.dff.cordova.plugin.camera;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dff.cordova.plugin.camera.Res.R;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.configurations.Config;
import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    // private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    @Inject
    @ApplicationContext
    Context mContext;

    @Inject
    Config mConfig;

    /**
     * Initializing the plugin by setting and allocating important information and objects.
     */
    @Override
    public void pluginInitialize() {
        super.pluginInitialize();

        DaggerManager
            .getInstance()
            .in(cordova.getActivity().getApplication())
            .and(cordova)
            .inject(this);

        mContext = cordova.getActivity().getApplicationContext();
        cordova.setActivityResultCallback(this);
    }

    @Nullable
    private JSONObject parseArgs(JSONArray args) {
        try {
            return args.getJSONObject(0);
        } catch (JSONException e) {
            Log.e(TAG, "Error: ", e);
            return null;
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
        if (action != null) {
            R.sCallBackContext = callbackContext;
            cordova.getThreadPool().execute(() -> {
                Log.d(TAG, "Action = " + action);

                if (action.equals(R.ACTION_TAKE_PHOTO)) {
                    JSONObject params = parseArgs(args);
                    Intent intent = new Intent(mContext, CameraActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    if (params != null) {
                        mConfig.setWithPreview(params.optBoolean(R.WITH_PREVIEW, false));
                        intent.putExtra(R.WITH_PREVIEW_KEY, mConfig.isWithPreview());
                    }

                    mContext.startActivity(intent);
                } else {
                    callbackContext.error("Error: action not found --> " + action);
                }
            });
            return true;
        }
        return false;
    }
}
