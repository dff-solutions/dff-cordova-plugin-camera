package com.dff.cordova.plugin.camera.listeners.callback;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CaptureRequest;
import android.os.Handler;

import com.dff.cordova.plugin.camera.log.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CameraCaptureStateCallbackUnitTest {
    
    @Mock
    Log log;
    
    @Mock
    CameraCaptureCallback cameraCaptureCallback;
    
    @Mock
    CameraCaptureSession session;
    
    @Mock
    Handler handler;
    
    @Mock
    CaptureRequest.Builder builder;
    
    @InjectMocks
    CameraCaptureStateCallback cameraCaptureStateCallback;
    
    @BeforeEach
    public void setup() {
        cameraCaptureStateCallback.mBackgroundHandler = handler;
        cameraCaptureStateCallback.captureBuilder = builder;
    }
    
    @Test
    public void onConfiguredTest() throws CameraAccessException {
        cameraCaptureStateCallback.onConfigured(session);
        verify(session).capture(builder.build(), cameraCaptureCallback, handler);
    }
}
