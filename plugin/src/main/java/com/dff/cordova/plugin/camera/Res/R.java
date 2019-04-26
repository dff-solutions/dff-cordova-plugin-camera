package com.dff.cordova.plugin.camera.Res;

import android.content.res.Resources;
import android.graphics.Bitmap;

import org.apache.cordova.CallbackContext;

/**
 * Class to hold and allocate needed resources.
 *
 * @author Anthony Nahas
 * @version 3.0.1
 * @since 05.01.2017
 */
public class R {
    public static String PACKAGE_NAME;
    public static Resources RESOURCES;

    public static final String LAYOUT = "layout";
    public static final String ID = "id";
    public static final String DRAWABLE = "drawable";

    public static final int IMAGE_PREVIEW_REQUEST = 1;
    public static final int RESULT_REPEAT = 2;
    public static final String CAMERA_ACTIVITY_LAYOUT = "activity_camera";
    public static final String PREVIEW_ACTIVITY_LAYOUT = "activity_preview";
    public static final String CAMERA_SURFACE_ID = "camera_preview_surface_view";
    public static final String CAMERA_DRAWING_SURFACE_ID = "camera_drawing_surface_view";
    public static final String CAMERA_DRAWING_PICINDICATOR_ID = "camera_drawing_picindicatorview";
    public static final String BUTTON_TAKE_IMAGE = "take_image";
    public static final String BUTTON_CHANGE_FLASH_MODE = "button_flash";
    public static final String BUTTON_FLIP_CAMERA = "button_flip_camera";
    public static final String BUTTON_CANCEL = "button_cancel";
    public static final String BUTTON_REPEAT = "button_repeat";
    public static final String BUTTON_OK = "button_ok";
    public static final String IMAGE_VIEW_PREVIEW_ID = "image_view";
    public static final String IC_FLASH_AUTO = "ic_flash_auto_white_24px";
    public static final String IC_FLASH_OFF = "ic_flash_off_white_24px";
    public static final String IC_FLASH_ON = "ic_flash_on_white_24px";
    public static final String IC_CAMERA_FRONT = "ic_switch_cam_front";
    public static final String IC_CAMERA_BACK = "ic_switch_cam_back";
    public static final String WITH_PREVIEW = "withPreview";
    public static final String WITH_PICINDICATOR = "withPicindicator";
    public static final String PICINDICATOR_MSG = "Bitte achten Sie bei einer Detail-Aufnahme" +
        " darauf, dass das Motiv ungefähr die Größe der beiden seitlichen Rahmen einnimmt.";
    public static final String WITH_PREVIEW_KEY = "withpreviewkey";

    public static final String ACTION_TAKE_PHOTO = "takephoto";

    public static Bitmap sBitmap;
    public static String sBase64Image;
    public static CallbackContext sCallBackContext;
}
