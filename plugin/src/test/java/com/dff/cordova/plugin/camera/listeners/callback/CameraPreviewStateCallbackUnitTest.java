package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
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
    CameraActivity cameraActivity;
    
    @Mock
    CameraDevice device;
    
    @Mock
    CameraCaptureSession cameraCaptureSession;
    
    @InjectMocks
    CameraPreviewStateCallback cameraPreviewStateCallback;
    
    @BeforeEach
    public void setup() {
        cameraPreviewStateCallback.setCameraActivity(cameraActivity);
    }
    
    @Test
    public void onCofiguredTest() {
        cameraPreviewStateCallback.onConfigured(cameraCaptureSession);
        verify(cameraActivity, never()).updatePreview();
        
        cameraActivity.cameraDevice = device;
        cameraPreviewStateCallback.onConfigured(cameraCaptureSession);
        verify(cameraActivity).updatePreview();
        assertEquals(cameraActivity.cameraCaptureSession, cameraCaptureSession);
    }
}
