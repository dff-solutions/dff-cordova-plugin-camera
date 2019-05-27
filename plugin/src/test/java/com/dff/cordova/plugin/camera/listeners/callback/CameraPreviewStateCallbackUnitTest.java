package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CameraPreviewStateCallbackUnitTest {
    
    @Mock
    Log log;
    
    @Mock
    Camera2Activity camera2Activity;
    
    @Mock
    CameraDevice device;
    
    @Mock
    CameraCaptureSession cameraCaptureSession;
    
    @InjectMocks
    CameraPreviewStateCallback cameraPreviewStateCallback;
    
    @BeforeEach
    public void setup() {
        cameraPreviewStateCallback.setCamera2Activity(camera2Activity);
    }
    
    @Test
    public void onCofiguredTest() {
        cameraPreviewStateCallback.onConfigured(cameraCaptureSession);
        verify(camera2Activity, never()).updatePreview();
        
        camera2Activity.cameraDevice = device;
        cameraPreviewStateCallback.onConfigured(cameraCaptureSession);
        verify(camera2Activity).updatePreview();
        assertEquals(camera2Activity.cameraCaptureSession, cameraCaptureSession);
    }
}
