package com.dff.cordova.plugin.camera.actions;

import android.content.Context;
import android.content.Intent;

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

    @Inject
    public TakePhoto(@ApplicationContext Context context, R r) {
        this.context = context;
        this.r = r;
        needsArgs = true;
        requiresPermissions = true;
    }

    @Override
    protected void execute() throws JSONException {
        super.checkJsonArgs(REQUIRED_ARGS);

        boolean withPreview = jsonArgs.getBoolean(JSON_ARG_WITH_PREVIEW);
        Intent intent = new Intent(context, Camera2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(JSON_ARG_WITH_PREVIEW, withPreview);

        context.startActivity(intent);
        // TODO keep callback and return Photo
        r.sCallBackContext = callbackContext;
    }

    @Override
    public String getActionName() {
        return ACTION;
    }
}
