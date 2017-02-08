package com.dff.cordova.plugin.camera.R;

import android.content.res.Resources;
import org.apache.cordova.CallbackContext;

/**
 * Class to hold and allocate needed resources.
 *
 * @author Anthony Nahas
 * @version 1.5
 * @since 05.01.2017
 */
public class R {

    public static String PACKAGE_NAME;
    public static Resources RESOURCES;


    public static final String LAYOUT = "layout";
    public static final String ID = "id";
    public static final String DRAWABLE = "drawable";

    public static boolean sSaveInGallery = false;

    public static final String CAMERA_ACTIVITY_LAYOUT = "activity_camera";
    public static final String CAMERA_SURFACE_ID = "camera_preview_surface_view";
    public static final String CAMERA_DRAWING_SURFACE_ID = "camera_drawing_surface_view";
    public static final String BUTTON_TAKE_IMAGE = "take_image";
    public static final String BUTTON_CHANGE_FLASH_MODE = "button_flash";
    public static final String BUTTON_FLIP_CAMERA = "button_flip_camera";
    public static final String IC_FLASH_AUTO = "ic_flash_auto_white_24px";
    public static final String IC_FLASH_OFF = "ic_flash_off_white_24px";
    public static final String IC_FLASH_ON = "ic_flash_on_white_24px";
    public static final String IC_CAMERA_FRONT = "ic_switch_cam_front";
    public static final String IC_CAMERA_BACK = "ic_switch_cam_back";

    public static final String RESULT_KEY_BASE64_IMG = "base64img";

    public static final String CAMERA_ACTIVITY_PATH = "com.dff.cordova.plugin.camera.CameraPlugin";
    public static final String ACTION_TAKE_PHOTO = "takephoto";

    public static CallbackContext sCallBackContext;
}
