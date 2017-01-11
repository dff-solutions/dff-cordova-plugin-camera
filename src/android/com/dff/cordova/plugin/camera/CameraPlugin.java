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
 * Created by anahas on 05.01.2017.
 *
 * @author Anthony Nahas
 * @version 0.5
 * @since 05.01.2017
 */
public class CameraPlugin extends CordovaPlugin {

    private static final String TAG = "CameraPlugin";
    private Context mContext;
    //public static String sPACKAGE_NAME;
    //public static Resources sRESOURCES;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        mContext = cordova.getActivity().getApplicationContext();
        R.PACKAGE_NAME = mContext.getPackageName();
        R.RESOURCES = mContext.getResources();
    }

    @Override
    public boolean execute(final String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action != null) {
            cordova.getThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "Action = " + action);
                    if (action.equals(R.ACTION_TAKE_PHOTO)) {
                        Intent intent = new Intent(mContext, CameraActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cordova.getActivity().startActivityForResult(intent, R.RESULT_CODE);
                    } else {
                        Log.d(TAG, "Action not found 404");
                    }
                }
            });
        }

        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d(TAG, "the request code = " + requestCode);
    }
}
