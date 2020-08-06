package com.dff.cordova.plugin.camera.actions;

import android.content.Context;
import android.content.Intent;

import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.dagger.annotations.CameraActivityIntent;
import com.dff.cordova.plugin.camera.helpers.CallbackContextHelper;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;

import org.json.JSONException;

import javax.inject.Inject;

/**
 * Class that provides the action 'takePhoto'.
 * The action opens a camera activity to allow the user to take a picture.
 * With a flag can be decided if you want to see a preview of the taken picture.
 */
public class TakePhoto extends PluginAction {
    public static final String ACTION = "takePhoto";

    public static final String JSON_ARG_WITH_PREVIEW = "withPreview";
    public static final String JSON_ARG_BASE_64 = "asBase64";
    private static final String[] REQUIRED_ARGS = new String[] {
        JSON_ARG_WITH_PREVIEW,
        JSON_ARG_BASE_64
    };

    private Context context;
    private CallbackContextHelper contextHelper;
    private Intent cameraActivityIntent;
    private ImageHelper imageHelper;

    @Inject
    public TakePhoto(
        @ApplicationContext Context context,
        CallbackContextHelper contextHelper,
        @CameraActivityIntent Intent intent,
        ImageHelper imageHelper) {
        this.context = context;
        this.contextHelper = contextHelper;
        this.cameraActivityIntent = intent;
        this.imageHelper = imageHelper;
        needsArgs = true;
        requiresPermissions = true;
    }

    @Override
    protected void execute() throws JSONException {
        super.checkJsonArgs(REQUIRED_ARGS);

        imageHelper.setAsBase64(jsonArgs.getBoolean(JSON_ARG_BASE_64));

        boolean withPreview = jsonArgs.getBoolean(JSON_ARG_WITH_PREVIEW);

        cameraActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        cameraActivityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        cameraActivityIntent.putExtra(JSON_ARG_WITH_PREVIEW, withPreview);

        contextHelper.addCallBackContext(callbackContext);

        context.startActivity(cameraActivityIntent);
    }

    @Override
    public String getActionName() {
        return ACTION;
    }
}
