package com.dff.cordova.plugin.camera.listeners.callback;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.ImageReader;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

public class AvailableImageListener implements ImageReader.OnImageAvailableListener {
    private final String TAG = "AvailableImageListener";
    private Log log;
    private Context context;
    private Camera2Activity camera2Activity;
    private ImageHelper imageHelper;
    
    @Inject
    public AvailableImageListener(
        Log log,
        ImageHelper imageHelper,
        @ApplicationContext Context context
    ) {
        this.log = log;
        this.imageHelper = imageHelper;
        this.context = context;
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
        Intent intent = new Intent(context, PreviewActivity.class);
        camera2Activity.startActivityForResult(intent, PreviewActivity.RESULT_OK);
    }
    
    public void setCamera2Activity(Camera2Activity camera2Activity) {
        this.camera2Activity = camera2Activity;
    }
}
