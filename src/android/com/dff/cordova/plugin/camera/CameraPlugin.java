package com.dff.cordova.plugin.camera;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.dff.cordova.plugin.camera.R.R;
import com.dff.cordova.plugin.camera.activities.CameraActivity;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Plugin that handle the process to take a photo: opening and releasing a camera instance.
 *
 * @author Anthony Nahas
 * @version 2.0.0
 * @since 05.01.2017
 */
public class CameraPlugin extends CordovaPlugin {

    private static final String TAG = "CameraPlugin";
    private Context mContext;

    /**
     * Initializing the plugin by setting and allocating important information and objects.
     */
    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        mContext = cordova.getActivity().getApplicationContext();
        cordova.setActivityResultCallback(this);
        R.PACKAGE_NAME = mContext.getPackageName();
        R.RESOURCES = mContext.getResources();
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
    public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action != null) {
            R.sCallBackContext = callbackContext;
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Action = " + action);
                    if (action.equals(R.ACTION_TAKE_PHOTO)) {
                        Intent intent = new Intent(mContext, CameraActivity.class);
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
