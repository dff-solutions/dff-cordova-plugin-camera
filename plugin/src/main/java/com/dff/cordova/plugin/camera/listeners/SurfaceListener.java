package com.dff.cordova.plugin.camera.listeners;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

public class SurfaceListener implements TextureView.SurfaceTextureListener {
    private final String TAG = "SurfaceListener";
    
    private Camera2Activity camera2Activity;
    private Log log;
    
    @Inject
    public SurfaceListener(
        Log log
    ) {
        this.log = log;
    }
    
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        log.d(TAG, "surface texture available");
        camera2Activity.openCamera();
    }
    
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    
    }
    
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        log.d(TAG, "surface texture destroyed");
        camera2Activity.closeCamera();
        return false;
    }
    
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    
    }
    
    public void setCamera2Activity(Camera2Activity camera2Activity) {
        this.camera2Activity = camera2Activity;
    }
}
