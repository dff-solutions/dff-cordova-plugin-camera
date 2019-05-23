package com.dff.cordova.plugin.camera.actions;

import android.content.Context;
import android.content.Intent;

import com.dff.cordova.plugin.camera.CameraPlugin;
import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.res.R;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;

import org.json.JSONException;

import javax.inject.Inject;

public class TakePhoto extends PluginAction {
    public static final String ACTION = "takePhoto";

    public static final String JSON_ARG_WITH_PREVIEW = "withPreview";
    private static final String[] REQUIRED_ARGS = new String[] {
        JSON_ARG_WITH_PREVIEW
    };

    private Context context;
    private R r;
    private CameraPlugin cameraPlugin;

    @Inject
    public TakePhoto(@ApplicationContext Context context, R r, CameraPlugin cameraPlugin) {
        this.context = context;
        this.r = r;
        this.cameraPlugin = cameraPlugin;
        needsArgs = true;
        requiresPermissions = true;
    }

    @Override
    protected void execute() throws JSONException {
        super.checkJsonArgs(REQUIRED_ARGS);

        boolean withPreview = jsonArgs.getBoolean(JSON_ARG_WITH_PREVIEW);
        Intent intent = new Intent(context, Camera2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(JSON_ARG_WITH_PREVIEW, withPreview);

        cameraPlugin.cordova.startActivityForResult(
            cameraPlugin,
            intent,
            R.RESULT_OK
        );
        
        // TODO keep callback and return Photo
        r.setCallBackContext(callbackContext);
    }

    @Override
    public String getActionName() {
        return ACTION;
    }
}
