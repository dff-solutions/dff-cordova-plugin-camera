package com.dff.cordova.plugin.camera.helpers;

import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.classes.CameraState;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;

import javax.inject.Inject;

/**
 * Helper class to manage button functionality.
 * The Helper can rotate the buttons and change the icon based on the chosen function.
 * Flash mode and camera mode can be checked and changed.
 * Supported Flash modes are single flash, auto flash and no flash
 * Camera modes are front or back camera
 */
public class CameraButtonHelper {
    private static final String TAG = "CameraButtonHelper";
    private static final String IC_CAMERA_FRONT = "camera_ic_switch_cam_front";
    private static final String IC_CAMERA_BACK = "camera_ic_switch_cam_back";
    private static final String IC_FLASH_AUTO = "camera_ic_flash_auto_white_24px";
    private static final String IC_FLASH_OFF = "camera_ic_flash_off_white_24px";
    private static final String IC_FLASH_ON = "camera_ic_flash_on_white_24px";
    private static final String CAPTURE_BUTTON = "capture_button";
    private static final String FLASH_BUTTON = "flash_button";
    private static final String FLIP_BUTTON = "flip_button";
    
    private R r;
    private Log log;
    private int flashMode = 2;
    private PackageManager packageManager;
    private ImageButton captureButton;
    private ImageButton flashButton;
    private ImageButton flipButton;
    private CameraActivity cameraActivity;
    private CameraState cameraState = CameraState.BACK;
    private CameraManager cameraManager;
    private CallbackContextHelper contextHelper;
    
    @Inject
    public CameraButtonHelper(
        R r,
        Log log,
        PackageManager packageManager,
        CameraActivity cameraActivity,
        CameraManager cameraManager,
        CallbackContextHelper callbackContextHelper
    ) {
        this.r = r;
        this.log = log;
        this.packageManager = packageManager;
        this.cameraActivity = cameraActivity;
        this.cameraManager = cameraManager;
        contextHelper = callbackContextHelper;
    }
    
    /**
     * Initializes the imageButtons on the screen, sets their onClick event and
     * checks if the device as a front camera and flashMode available.
     */
    public void initButtons()
    {
        captureButton = cameraActivity.findViewById(r.getIdIdentifier(CAPTURE_BUTTON));
        flashButton = cameraActivity.findViewById(r.getIdIdentifier(FLASH_BUTTON));
        flipButton = cameraActivity.findViewById(r.getIdIdentifier(FLIP_BUTTON));
    
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraActivity.takePicture();
            }
        });
        flashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraActivity.changeFlashMode();
            }
        });
        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeCamera();
            }
        });
        
        checkFlash();
        checkFrontCamera();
    }

    /**
     * Rotate all image buttons stored in the given list from a specific degree value
     * to a specific degree value.
     *
     * @param fromDegrees - the start degree value
     * @param toDegree    - the target degree value
     */
    public void rotate(float fromDegrees, float toDegree) {
        final RotateAnimation rotateAnim = new RotateAnimation(fromDegrees, toDegree,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(250);
        rotateAnim.setFillAfter(true);
        
        flashButton.startAnimation(rotateAnim);
        flipButton.startAnimation(rotateAnim);
    }
    
    /**
     * Changes the image of the button and set the flashMode in the captureRequest.
     *  @param captureRequest package of settings to capture a image
     */
    public void changeFlashButton(CaptureRequest.Builder captureRequest) {
        log.d(TAG, "changeFlashMode");
        
        switch (flashMode) {
            case 0:
                log.d(TAG, "single flash / flash on");
                flashButton.setImageResource(r.getDrawableIdentifier(IC_FLASH_ON));
                captureRequest.set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                captureRequest.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                flashMode = 1;
                break;
            case 1:
                log.d(TAG, "flash off");
                flashButton.setImageResource(r.getDrawableIdentifier(IC_FLASH_OFF));
                captureRequest.set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON);
                captureRequest.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                flashMode = 2;
                break;
            case 2:
                log.d(TAG, "auto flash");
                flashButton.setImageResource(r.getDrawableIdentifier(IC_FLASH_AUTO));
                captureRequest.set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                captureRequest.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                flashMode = 0;
                break;
            default:
                break;
        }
        
    }
    
    /**
     * Changes the icon of the button.
     * @param flipMode state of the camera
     */
    public void changeFlipButton(CameraState flipMode) {
        log.d(TAG, "changeCamera");
        if (flipMode == CameraState.BACK) {
            flipButton.setImageResource(r.getDrawableIdentifier(IC_CAMERA_BACK));
        } else {
            flipButton.setImageResource(r.getDrawableIdentifier(IC_CAMERA_FRONT));
        }
    }
    
    /**
     * Enables or disables all buttons.
     *
     * @param enable true = enable, false = disable
     */
    public void enableAllButtons(boolean enable) {
        flipButton.setEnabled(enable);
        captureButton.setEnabled(enable);
        flashButton.setEnabled(enable);
    }
    
    /**
     * Hides the button when there is no flashMode.
     */
    public void checkFlash() {
        boolean hasFlashMode = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    
        if (!hasFlashMode) {
            log.d(TAG, "disable changeFlash button");
            flashButton.setEnabled(false);
            flashButton.setVisibility(View.GONE);
        }
    }
    
    /**
     * Hides the button when the device does not have a front camera.
     */
    public void checkFrontCamera() {
        boolean hasFrontCamera = packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    
        if (!hasFrontCamera) {
            log.d(TAG, "disable changeCamera button");
            flipButton.setEnabled(false);
            flipButton.setVisibility(View.GONE);
        }
    }
    
    /**
     * The method changes between front and back camera.
     */
    public void changeCamera() {
        if (cameraState == CameraState.FRONT) {
            log.d(TAG, "flip to back camera");
            try {
                for (String id : cameraManager.getCameraIdList()) {
                    CameraCharacteristics characteristics =
                        cameraManager.getCameraCharacteristics(id);
                    int cameraOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                    if (cameraOrientation == CameraCharacteristics.LENS_FACING_BACK) {
                        cameraActivity.cameraId = id;
                        log.d(TAG, "cameraId: " + id);
                    }
                }
            } catch (CameraAccessException e) {
                log.e(TAG, "unable to access camera", e);
                contextHelper.sendAllException(e);
            }
            cameraState = CameraState.BACK;
        } else {
            log.d(TAG, "flip to front camera");
            try {
                for (String id : cameraManager.getCameraIdList()) {
                    CameraCharacteristics characteristics =
                        cameraManager.getCameraCharacteristics(id);
                    int cameraOrientation = characteristics.get(CameraCharacteristics.LENS_FACING);
                    if (cameraOrientation == CameraCharacteristics.LENS_FACING_FRONT) {
                        cameraActivity.cameraId = id;
                        log.d(TAG, "cameraId: " + id);
                    }
                }
            } catch (CameraAccessException e) {
                log.e(TAG, "unable to access camera", e);
                contextHelper.sendAllException(e);
            }
            cameraState = CameraState.FRONT;
        }
        changeFlipButton(cameraState);
        cameraActivity.closeCamera();
        cameraActivity.openCamera();
    }
}
