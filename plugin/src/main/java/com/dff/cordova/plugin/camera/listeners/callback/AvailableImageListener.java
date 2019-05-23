package com.dff.cordova.plugin.camera.listeners.callback;

import android.media.Image;
import android.media.ImageReader;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

public class AvailableImageListener implements ImageReader.OnImageAvailableListener {
    private final String TAG = "AvailableImageListener";
    private Log log;
    private Camera2Activity camera2Activity;
    private ImageHelper imageHelper;
    
    @Inject
    public AvailableImageListener(
        Log log,
        ImageHelper imageHelper
    ) {
        this.log = log;
        this.imageHelper = imageHelper;
    }
    
    @Override
    public void onImageAvailable(ImageReader reader) {
        Image image = reader.acquireLatestImage();
        
        imageHelper.storeImage(image);
        if (image != null) {
            image.close();
        }
        startImagePreview();
    }
    
    private void startImagePreview() {
        log.d(TAG, "startImagePreview");
        camera2Activity.startPreviewActivity();
    }
    
    public void setCamera2Activity(Camera2Activity camera2Activity) {
        this.camera2Activity = camera2Activity;
    }
}
