package com.dff.cordova.plugin.camera.helpers;

import android.app.admin.DevicePolicyManager;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.annotations.CameraActivityScope;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * Helps configuring the camera.
 */
@CameraActivityScope
public class CameraHelper {
    private static final String TAG = "CameraHelper";
    private Log log;
    private CameraActivity cameraActivity;
    private CameraManager cameraManager;
    private int supportedHardwareLevel = 0;
    private CallbackContextHelper contextHelper;
    private PackageManager packageManager;
    private DevicePolicyManager devicePolicyManager;
    
    @Inject
    public CameraHelper(
        Log log,
        CameraActivity cameraActivity,
        CameraManager cameraManager,
        CallbackContextHelper callbackContextHelper,
        PackageManager packageManager,
        DevicePolicyManager devicePolicyManager
    ) {
        this.packageManager = packageManager;
        this.log = log;
        this.cameraActivity = cameraActivity;
        this.cameraManager = cameraManager;
        contextHelper = callbackContextHelper;
        this.devicePolicyManager = devicePolicyManager;
    }
    
    /**
     * Initializes the cameraId and the supportedHardwareLevel.
     * CameraId is the id of the selected camera which is normally the back facing camera.
     * Each device as a different supportedHardwareLevel since not every device supports all
     * features.
     */
    public void initCameraId() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_DEVICE_ADMIN)) {
            log.d(TAG, "device has device policy.");
            if (devicePolicyManager.getCameraDisabled(null)) {
                log.e(TAG, "camera is disabled by device policy.");
                contextHelper.sendAllError("camera is disabled by device policy.");
                cameraActivity.finish();
                return;
            }
        }
        getCameraIdFromAndroid();
        
        log.d(TAG, "cameraId = " + cameraActivity.cameraId);
        log.d(TAG, "supported hardware level = " + supportedHardwareLevel);
    }
    
    private void getCameraIdFromAndroid() {
        log.d(TAG, "receiving cameraId from android");
        try {
            log.d(TAG, cameraManager.getCameraIdList().toString());
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(id);
                int cameraOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (cameraOrientation == CameraCharacteristics.LENS_FACING_BACK) {
                    cameraActivity.cameraId = id;
                    supportedHardwareLevel =
                        characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                    break;
                }
            }
        } catch (CameraAccessException e) {
            log.e(TAG, "unable to access camera.", e);
            contextHelper.sendAllException(e);
        }  catch (NullPointerException e2) {
            log.e(TAG, "Nullpointer when accessing characteristics", e2);
            contextHelper.sendAllException(e2);
        }
    
        if (cameraActivity.cameraId == null) {
            log.e(TAG, "unable to set cameraId from cameraIdList");
            contextHelper.sendAllError("unable to setCameraId");
            cameraActivity.finish();
        }
    }
    
    public int getSupportedHardwareLevel() {
        return supportedHardwareLevel;
    }
}
