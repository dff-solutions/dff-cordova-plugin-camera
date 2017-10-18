package com.dff.cordova.plugin.camera.helpers;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Helper classes to rotate a image button with a specific target degree
 *
 * @author Anthony Nahas
 * @version 1.1
 * @since 8.2.2017
 */
@Singleton
public class RotationHelper {

    @Inject
    public RotationHelper() {
    }

    /**
     * rotate all imagebuttons stored in the given list from a specific degree value
     * to a specific dregree value.
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
}
