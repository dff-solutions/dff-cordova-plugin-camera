package com.dff.cordova.plugin.camera;

import android.content.Context;
import com.dff.cordova.plugin.camera.R.R;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONException;

/**
 * Created by anahas on 05.01.2017.
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
    public boolean execute(String action, String rawArgs, CallbackContext callbackContext) throws JSONException {
        return super.execute(action, rawArgs, callbackContext);
    }
}
