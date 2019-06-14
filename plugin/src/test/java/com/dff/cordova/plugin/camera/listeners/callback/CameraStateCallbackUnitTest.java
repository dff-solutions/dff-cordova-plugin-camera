package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraDevice;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.log.Log;

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
    CameraActivity cameraActivity;
    
    @Mock
    CameraDevice device;
    
    @Mock
    Log log;
    
    @InjectMocks
    CameraStateCallback cameraStateCallback;
    
    @Test
    public void onErrorTest() {
        cameraStateCallback.onError(device, 0);
        
        verify(cameraActivity).closeCamera();
        verify(cameraActivity).finish();
    }
    
    @Test
    public void onDisconnectedTest() {
        cameraStateCallback.onDisconnected(device);
        verify(cameraActivity).closeCamera();
    }
    
    @Test
    public void onOpenedTest() {
        cameraStateCallback.onOpened(device);
        
        verify(cameraActivity).startCameraPreview();
    }
}
