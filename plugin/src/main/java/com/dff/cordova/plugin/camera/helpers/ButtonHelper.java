package com.dff.cordova.plugin.camera.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.classes.CameraState;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper classes to rotate a image button with a specific target degree.
 *
 */
@Singleton
public class ButtonHelper {
    private static final String TAG = "ButtonHelper";
    private static final String IC_CAMERA_FRONT = "camera_ic_switch_cam_front";
    private static final String IC_CAMERA_BACK = "camera_ic_switch_cam_back";
    private static final String IC_FLASH_AUTO = "camera_ic_flash_auto_white_24px";
    private static final String IC_FLASH_OFF = "camera_ic_flash_off_white_24px";
    private static final String IC_FLASH_ON = "camera_ic_flash_on_white_24px";
    
    private R r;
    private Log log;
    private int flashMode = 2;
    public String cameraId;
    public CameraManager cameraManager;
    private List<ImageButton> imageButtonList = new ArrayList<>();
    
    @Inject
    public ButtonHelper(R r, Log log) {
        this.r = r;
        this.log = log;
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

        for (ImageButton imageButton : imageButtonList) {
            if (imageButton.getVisibility() != View.GONE) {
                imageButton.startAnimation(rotateAnim);
            }
        }
    }
    
    /**
     * Changes the image of the button and set the flashMode in the captureRequest.
     *  @param captureRequest package of settings to capture a image
     * @param button button with an image
     */
    public void changeFlashButton(CaptureRequest.Builder captureRequest, ImageButton button) {
        log.d(TAG, "changeFlashMode");
        
        switch (flashMode) {
            case 0:
                log.d(TAG, "single flash / flash on");
                button.setImageResource(r.getDrawableIdentifier(IC_FLASH_ON));
                captureRequest.set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
                captureRequest.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
                flashMode = 1;
                break;
            case 1:
                log.d(TAG, "flash off");
                button.setImageResource(r.getDrawableIdentifier(IC_FLASH_OFF));
                captureRequest.set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON);
                captureRequest.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
                flashMode = 2;
                break;
            case 2:
                log.d(TAG, "auto flash");
                button.setImageResource(r.getDrawableIdentifier(IC_FLASH_AUTO));
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
     *  @param button button with image
     * @param flipMode state of the camera
     */
    public void changeFlipButton(ImageButton button, CameraState flipMode) {
        log.d(TAG, "changeCamera");
        if (flipMode == CameraState.BACK) {
            button.setImageResource(r.getDrawableIdentifier(IC_CAMERA_BACK));
        } else {
            button.setImageResource(r.getDrawableIdentifier(IC_CAMERA_FRONT));
        }
    }
    
    public void addImageButton(ImageButton button) {
        imageButtonList.add(button);
    }
    
    /**
     * Enables or disables all buttons.
     *
     * @param enable true = enable, false = disable
     */
    public void enableAllButtons(boolean enable) {
        for (ImageButton imageButton : imageButtonList) {
            if (imageButton.getVisibility() != View.GONE) {
                imageButton.setEnabled(enable);
            }
        }
    }
    
    /**
     * Hides the button when there is no flashMode.
     *
     * @param applicationContext information of the application environment
     * @param flashButton button to switch flashMode
     */
    public void checkFlash(Context applicationContext, ImageButton flashButton) {
        boolean hasFlashMode =
            applicationContext
                .getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    
        if (!hasFlashMode) {
            log.d(TAG, "disable changeFlash button");
            flashButton.setEnabled(false);
            flashButton.setVisibility(View.GONE);
        }
    }
    
    /**
     * Hides the button when the device does not have a front camera.
     *
     * @param applicationContext information of the application environment
     * @param flipButton button to switch cameras
     */
    public void checkFrontCamera(Context applicationContext, ImageButton flipButton) {
        boolean hasFrontCamera =
            applicationContext
                .getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    
        if (!hasFrontCamera) {
            log.d(TAG, "disable changeCamera button");
            flipButton.setEnabled(false);
            flipButton.setVisibility(View.GONE);
        }
    }
}
