package com.dff.cordova.plugin.camera.listeners;

import android.graphics.SurfaceTexture;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
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
    Camera2Activity camera2Activity;
    
    @Mock
    SurfaceTexture surfaceTexture;
    
    @InjectMocks
    SurfaceListener surfaceListener;
    
    @BeforeEach
    public void setup(){
        surfaceListener.setCamera2Activity(camera2Activity);
    }
    
    @Test
    public void surfaceAvailableTest() {
        surfaceListener.onSurfaceTextureAvailable(surfaceTexture, 0 , 0);
        verify(camera2Activity).openCamera();
        verify(camera2Activity, never()).closeCamera();
    }
    
    @Test void surfaceDestroyedTest() {
        boolean result = surfaceListener.onSurfaceTextureDestroyed(surfaceTexture);
        verify(camera2Activity, never()).openCamera();
        verify(camera2Activity).closeCamera();
        assertFalse(result);
    }
}
