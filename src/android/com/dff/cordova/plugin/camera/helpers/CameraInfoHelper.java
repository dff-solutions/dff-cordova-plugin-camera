package com.dff.cordova.plugin.camera.helpers;

import android.hardware.Camera;

/**
 * Created by anahas on 22.03.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 22.03.2017
 */
public class CameraInfoHelper {

    public static boolean isFrontCameraOn(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

}
