package com.dff.cordova.plugin.camera.actions;

import com.dff.cordova.plugin.camera.helpers.ImageHelper;

import org.json.JSONException;

import javax.inject.Inject;

/**
 * Action to set the path where the images should be saved.
 */
public class SetImagePath extends PluginAction {
    public static final String ACTION = "setImagePath";

    public static final String JSON_ARG_IMAGE_PATH = "imagePath";
    private static final String[] REQUIRED_ARGS = new String[] {
        JSON_ARG_IMAGE_PATH
    };

    private ImageHelper imageHelper;

    @Inject
    public SetImagePath(
        ImageHelper imageHelper
    ) {
        this.imageHelper = imageHelper;

        needsArgs = true;
    }

    @Override
    protected void execute() throws JSONException {
        super.checkJsonArgs(REQUIRED_ARGS);

        String imagePath = jsonArgs.getString(JSON_ARG_IMAGE_PATH);
        imageHelper.setImagePath(imagePath);
        callbackContext.success();
    }

    @Override
    public String getActionName() {
        return ACTION;
    }
}
