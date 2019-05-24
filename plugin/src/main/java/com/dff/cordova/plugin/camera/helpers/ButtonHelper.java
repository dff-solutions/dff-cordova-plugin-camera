package com.dff.cordova.plugin.camera.helpers;

import android.hardware.camera2.CaptureRequest;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Helper classes to rotate a image button with a specific target degree.
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 8.2.2017
 */
@Singleton
public class ButtonHelper {
    private static final String TAG = "ButtonHelper";
    private static final String IC_CAMERA_FRONT = "ic_switch_cam_front";
    private static final String IC_CAMERA_BACK = "ic_switch_cam_back";
    private static final String IC_FLASH_AUTO = "ic_flash_auto_white_24px";
    private static final String IC_FLASH_OFF = "ic_flash_off_white_24px";
    private static final String IC_FLASH_ON = "ic_flash_on_white_24px";
    
    private R r;
    private Log log;
    private int flashMode = 2;
    
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
     * @param list        - the list that stored the imagebuttons to be rotated
     */
    public void rotate(float fromDegrees, float toDegree, List<ImageButton> list) {
        final RotateAnimation rotateAnim = new RotateAnimation(fromDegrees, toDegree,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f,
            RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(250);
        rotateAnim.setFillAfter(true);

        for (ImageButton imageButton : list) {
            if (imageButton.getVisibility() != View.GONE) {
                imageButton.startAnimation(rotateAnim);
            }
        }
    }
    
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
    
    public String changeFlipButton(ImageButton button, int flipMode) {
        log.d(TAG, "changeCamera");
        
        String cameraId = "1";
        if (flipMode == 0) {
            button.setImageResource(r.getDrawableIdentifier(IC_CAMERA_BACK));
        } else {
            button.setImageResource(r.getDrawableIdentifier(IC_CAMERA_FRONT));
        }
        
        return cameraId;
    }
}
