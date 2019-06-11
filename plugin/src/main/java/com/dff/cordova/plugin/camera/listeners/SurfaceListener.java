package com.dff.cordova.plugin.camera.listeners;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.dagger.annotations.CameraActivityScope;
import com.dff.cordova.plugin.camera.log.Log;

import javax.inject.Inject;

/**
 * Listener that opens the camera when a textureView is available or closes the camera
 * when the textureView is destroyed.
 *
 * @see <a href="https://developer.android.com/reference/android/view/TextureView.SurfaceTextureListener"
 *     >https://developer.android.com/reference/android/view/TextureView.SurfaceTextureListener</a>
 */
@CameraActivityScope
public class SurfaceListener implements TextureView.SurfaceTextureListener {
    private final String TAG = "SurfaceListener";
    
    private CameraActivity cameraActivity;
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
        cameraActivity.openCamera();
    }
    
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    
    }
    
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        log.d(TAG, "surface texture destroyed");
        cameraActivity.closeCamera();
        return false;
    }
    
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    
    }
    
    public void setCameraActivity(CameraActivity cameraActivity) {
        this.cameraActivity = cameraActivity;
    }
    
}
