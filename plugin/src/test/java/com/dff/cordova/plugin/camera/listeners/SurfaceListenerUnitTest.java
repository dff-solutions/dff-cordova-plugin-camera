package com.dff.cordova.plugin.camera.listeners;

import android.graphics.SurfaceTexture;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SurfaceListenerUnitTest {
    
    @Mock
    Log log;
    
    @Mock
    CameraActivity cameraActivity;
    
    @Mock
    SurfaceTexture surfaceTexture;
    
    @InjectMocks
    SurfaceListener surfaceListener;
    
    @BeforeEach
    public void setup(){
        surfaceListener.setCameraActivity(cameraActivity);
    }
    
    @Test
    public void surfaceAvailableTest() {
        surfaceListener.onSurfaceTextureAvailable(surfaceTexture, 0 , 0);
        verify(cameraActivity).openCamera();
        verify(cameraActivity, never()).closeCamera();
    }
    
    @Test void surfaceDestroyedTest() {
        boolean result = surfaceListener.onSurfaceTextureDestroyed(surfaceTexture);
        verify(cameraActivity, never()).openCamera();
        verify(cameraActivity).closeCamera();
        assertFalse(result);
    }
}
