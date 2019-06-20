package com.dff.cordova.plugin.camera.listeners;

import android.content.Context;
import android.view.OrientationEventListener;

import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.dagger.annotations.CameraActivityScope;
import com.dff.cordova.plugin.camera.helpers.CameraButtonHelper;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * Helper class for receiving notifications from the SensorManager when the orientation of
 * the device has changed.
 * Also has a method to return the rotation for the taken image.
 *
 * @see <a href="https://developer.android.com/reference/android/view/OrientationEventListener"
 *     >https://developer.android.com/reference/android/view/OrientationEventListener</a>
 */
@CameraActivityScope
public class OrientationListener extends OrientationEventListener {
    private static final String TAG = "OrientationListener";
    
    //should be between 0 and 45. Else it might cause errors.
    private final int orientationRange = 25;
    private CameraButtonHelper buttonHelper;
    public int currentRotation = 0;
    private Log log;
    
    @Inject
    public OrientationListener(
        @ApplicationContext Context context,
        Log log
    ) {
        super(context);
        this.log = log;
        this.log.d(TAG, "init listener");
    }
    
    @Override
    public void onOrientationChanged(int orientation) {
        /*orientation is inverse.
          if orientation = 270 we need to rotate to 90
          if orientation = 90  we need to rotate to 270
        */
        if ((
            orientation >= (360 - orientationRange) ||
            orientation <= (orientationRange)) &&
            currentRotation != 0
        ) {
            log.d(TAG, "setting rotation to 0");
            buttonHelper.rotate(currentRotation, 0);
            currentRotation = 0;
            
        } else if ((
            orientation >= 90 - orientationRange &&
            orientation <= 90 + orientationRange) &&
            currentRotation != 270
        ) {
            log.d(TAG, "setting rotation to -90");
            buttonHelper.rotate(currentRotation, 270);
            currentRotation = 270;
            
        } else if ((
            orientation >= 180 - orientationRange &&
            orientation <= 180 + orientationRange) &&
            currentRotation != 180
        ) {
            log.d(TAG, "setting rotation to 180");
            buttonHelper.rotate(currentRotation, 180);
            currentRotation = 180;
            
        } else if ((
            orientation >= 270 - orientationRange &&
            orientation <= 270 + orientationRange) &&
            currentRotation != 90
        ) {
            log.d(TAG, "setting rotation to 90");
            buttonHelper.rotate(currentRotation, 90);
            currentRotation = 90;
        }
    }
    
    /**
     * Returns Rotation so the image doesn't need to be rotated.
     *
     * @return rotation so the image doesn't need to be rotated.
     */
    public int getImageRotation() {
        int screenRotation;
        switch (currentRotation) {
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
    
    public void setButtonHelper(CameraButtonHelper buttonHelper) {
        this.buttonHelper = buttonHelper;
    }
}
