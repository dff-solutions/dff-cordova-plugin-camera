package com.dff.cordova.plugin.camera.listeners;

import android.media.Image;
import android.media.ImageReader;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * Callback interface for being notified that a new image is available.
 * When a new image is available, it will be stored in the ImageHelper and the
 * startPrewviewActivity method from CameraActivity is called.
 *
 * @see <a href="https://developer.android.com/reference/android/view/OrientationEventListener"
 *     >https://developer.android.com/reference/android/view/OrientationEventListener</a>
 */
public class AvailableImageListener implements ImageReader.OnImageAvailableListener {
    private final String TAG = "AvailableImageListener";
    private Log log;
    private CameraActivity cameraActivity;
    private ImageHelper imageHelper;
    
    @Inject
    public AvailableImageListener(
        Log log,
        ImageHelper imageHelper,
        CameraActivity cameraActivity
    ) {
        this.log = log;
        this.imageHelper = imageHelper;
        this.cameraActivity = cameraActivity;
    }
    
    @Override
    public void onImageAvailable(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        
        imageHelper.storeImage(image);
        if (image != null) {
            log.d(TAG, "close Image.");
            image.close();
        }
        log.d(TAG, "startImagePreview");
        cameraActivity.startPreviewActivity();
    }
}
