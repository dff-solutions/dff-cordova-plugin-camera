package com.dff.cordova.plugin.camera.listeners;

import android.media.Image;
import android.media.ImageReader;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AvailableImageListenerUnitTest {
    
    @Mock
    Log log;
    
    @Mock
    ImageHelper imageHelper;
    
    @Mock
    CameraActivity cameraActivity;
    
    @Mock
    ImageReader reader;
    
    @Mock
    Image image;
    
    @InjectMocks
    AvailableImageListener imageListener;
    
    
    @Test
    public void onImageAvailableTest() {
        doReturn(image).when(reader).acquireLatestImage();
        
        imageListener.onImageAvailable(reader);
        
        verify(imageHelper).storeImage(image);
        verify(image).close();
        verify(cameraActivity).startPreviewActivity();
    }
}
