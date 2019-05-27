package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraDevice;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CameraStateCallbackUnitTest {
    
    @Mock
    Camera2Activity camera2Activity;
    
    @Mock
    CameraDevice device;
    
    @Mock
    Log log;
    
    @InjectMocks
    CameraStateCallback cameraStateCallback;
    
    @BeforeEach
    public void setup() {
        cameraStateCallback.setCamera2Activity(camera2Activity);
    }
    
    @Test
    public void onErrorTest() {
        cameraStateCallback.onError(device, 0);
        
        verify(camera2Activity).closeCamera();
        verify(camera2Activity).finish();
    }
    
    @Test
    public void onDisconnectedTest() {
        cameraStateCallback.onDisconnected(device);
        verify(camera2Activity).closeCamera();
    }
    
    @Test
    public void onOpenedTest() {
        cameraStateCallback.onOpened(device);
        
        assertEquals(device, camera2Activity.cameraDevice);
        verify(camera2Activity).startCameraPreview();
    }
}
