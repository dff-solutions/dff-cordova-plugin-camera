package com.dff.cordova.plugin.camera.helpers;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

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
    
    private static final String IC_CAMERA_FRONT = "ic_switch_cam_front";
    private static final String IC_CAMERA_BACK = "ic_switch_cam_back";
    private static final String IC_FLASH_AUTO = "ic_flash_auto_white_24px";
    private static final String IC_FLASH_OFF = "ic_flash_off_white_24px";
    private static final String IC_FLASH_ON = "ic_flash_on_white_24px";
    
    private R r;
    
    @Inject
    public ButtonHelper(R r) {
        this.r = r;
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
    
    public void changeFlashButton(ImageButton button, int flashMode) {
        switch (flashMode) {
            case 0:
                button.setImageResource(r.getDrawableIdentifier(IC_FLASH_AUTO));
                break;
            case 1:
                button.setImageResource(r.getDrawableIdentifier(IC_FLASH_OFF));
                break;
            case 2:
                button.setImageResource(r.getDrawableIdentifier(IC_FLASH_ON));
                break;
            default:
                break;
        }
    }
    
    public void changeFlipButton(ImageButton button, int flipMode) {
        if (flipMode == 0) {
            button.setImageResource(r.getDrawableIdentifier(IC_CAMERA_BACK));
        } else {
            button.setImageResource(r.getDrawableIdentifier(IC_CAMERA_FRONT));
        }
    }
}
