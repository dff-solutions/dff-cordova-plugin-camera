package com.dff.cordova.plugin.camera.helpers;

import android.hardware.Camera;
import android.util.Log;

/**
 * Created by anahas on 22.03.2017.
 *
 * @author Anthony Nahas
 * @version 1.2
 * @since 22.03.2017
 */
public class CameraInfoHelper {

    private static final String TAG = CameraInfoHelper.class.getSimpleName();

    /**
     * Method that returns whether the front or back camera is in use
     *
     * @param cameraID - the used camera id
     * @return - whether the front or back camera is in use
     */
    public static boolean isFrontCameraOn(int cameraID) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraID, info);
        return info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;
    }

    /**
     *
     * @param cameraID
     * @return
     */
    public static Camera.CameraInfo getCameraInfo(int cameraID) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraID, info);
        return info;
    }

    /**
     * Print all params of the camera.
     * This method is implemented for test and debug purposes
     */
    public static void printParameters(Camera.Parameters parameters) {
        String space = " ";
        Log.d(TAG, "Camera params: "
            + "Antibanding: "
            + parameters.getAntibanding()
            + space
            + "ExposureCompensation "
            + parameters.getExposureCompensation()
            + space
            + "MinExposureCompensation "
            + parameters.getMinExposureCompensation()
            + space
            + "MaxExposureCompensation "
            + parameters.getMaxExposureCompensation()
            + space
            + "MaxNumFocusAreas "
            + parameters.getMaxNumFocusAreas()
            + space
            + "MaxNumMeteringAreas "
            + parameters.getMaxNumMeteringAreas()
            + space
            //+ "MeteringAreas "
            //+ parameters.getMeteringAreas()
            + "FocusAreas "
            + parameters.getFocusAreas()
            + space
            + "FocalLength "
            + parameters.getFocalLength()
            + space
            + "MaxZoom "
            + parameters.getMaxZoom()
            + space
            + "PictureFormat"
            + parameters.getPictureFormat()
            + space
            + "PreviewFormat "
            + parameters.getPreviewFormat()
            + space
            + "MaxZoom "
            + parameters.getMaxZoom()
            + space
            + "FlashMode "
            + parameters.getFlashMode()
            + space
            + "ZoomRatios "
            + parameters.getZoomRatios()
            + space
            + "WhiteBalance "
            + parameters.getWhiteBalance()
            + space
            + "SceneMode "
            + parameters.getSceneMode()
            + space
            + "AutoExposureLock "
            + parameters.getAutoExposureLock()
            + space
            + "ExposureCompensationStep "
            + parameters.getExposureCompensationStep()
            + space
            + "HorizontalViewAngle "
            + parameters.getHorizontalViewAngle()
            + space
            + "VerticalViewAngle "
            + parameters.getVerticalViewAngle()
            + space
        );
    }

}
