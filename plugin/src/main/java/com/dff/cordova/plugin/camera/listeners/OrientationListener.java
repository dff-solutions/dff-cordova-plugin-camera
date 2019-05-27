package com.dff.cordova.plugin.camera.listeners;

import android.content.Context;
import android.view.OrientationEventListener;

import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.helpers.ButtonHelper;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * Helper class for receiving notifications from the SensorManager when the orientation of
 * the device has changed.
 * Has a list of button to rotate them.
 *
 * @see <a href="https://developer.android.com/reference/android/view/OrientationEventListener"
 *     >https://developer.android.com/reference/android/view/OrientationEventListener</a>
 */
public class OrientationListener extends OrientationEventListener {
    private static final String TAG = "OrientationListener";
    private ButtonHelper buttonHelper;
    public int currentRotaion = 0;
    private Log log;
    
    @Inject
    public OrientationListener(
        @ApplicationContext Context context,
        Log log,
        ButtonHelper buttonHelper
    ) {
        super(context);
        this.buttonHelper = buttonHelper;
        this.log = log;
        this.log.d(TAG, "init listener");
    }
    
    @Override
    public void onOrientationChanged(int orientation) {
        /*orientation is inversed.
          if orientation = 270 we need to rotate to 90
          if orientation = 90  we need to rotate to 270
        */
        if ((orientation < 15 || orientation > 345) && currentRotaion != 0) {
            log.d(TAG, "setting rotation to 0");
            buttonHelper.rotate(currentRotaion, 0);
            currentRotaion = 0;
        } else if ((orientation >= 75 && orientation < 105) && currentRotaion != 270) {
            log.d(TAG, "setting rotation to -90");
            buttonHelper.rotate(currentRotaion, 270);
            currentRotaion = 270;
        } else if ((orientation >= 165 && orientation < 195) && currentRotaion != 180) {
            log.d(TAG, "setting rotation to 180");
            buttonHelper.rotate(currentRotaion, 180);
            currentRotaion = 180;
        } else if ((orientation >= 255 && orientation < 285) && currentRotaion != 90) {
            log.d(TAG, "setting rotation to 90");
            buttonHelper.rotate(currentRotaion, 90);
            currentRotaion = 90;
        }
    }
    
    /**
     * Returns Rotation so the image doesn't need to be rotated.
     *
     * @return rotation so the image doesn't need to be rotated.
     */
    public int getImageRotation() {
        int screenRotation;
        switch (currentRotaion) {
            case 0:
                screenRotation = 90;
                break;
            case 90:
                screenRotation = 0;
                break;
            case 180:
                screenRotation = 270;
                break;
            case 270:
                screenRotation = 180;
                break;
            default:
                screenRotation = 90;
                break;
        }
        return screenRotation;
    }
}
