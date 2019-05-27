package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;

import com.dff.cordova.plugin.camera.activities.Camera2Activity;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CameraCaptureCallbackUnitTest {
    
    @Mock
    Log log;
    
    @Mock
    Camera2Activity camera2Activity;
    
    @Mock
    CameraCaptureSession cameraCaptureSession;
    
    @Mock
    CaptureRequest request;
    
    @InjectMocks
    CameraCaptureCallback cameraCaptureCallback;
    
    @BeforeEach
    public void setup() {
        cameraCaptureCallback.setCamera2Activity(camera2Activity);
    }
    
    @Test
    public void onCaptureStartedTest() {
        cameraCaptureCallback.onCaptureStarted(cameraCaptureSession, request, 0,0);
        verify(camera2Activity).startCameraPreview();
    }
}
