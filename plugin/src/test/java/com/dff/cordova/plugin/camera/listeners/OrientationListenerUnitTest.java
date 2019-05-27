package com.dff.cordova.plugin.camera.listeners;

import android.content.Context;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.helpers.ButtonHelper;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrientationListenerUnitTest {
    
    @Mock
    Log log;
    
    @Mock
    Context context;
    
    @Mock
    ButtonHelper buttonHelper;
    
    @Mock
    ImageButton button;
    
    @InjectMocks
    OrientationListener listener;
    
    @Test
    public void shouldChangeOrientation() {
        listener.onOrientationChanged(90);
        assertEquals(listener.currentRotaion, 270);
    
        verify(buttonHelper).rotate(0, 270, new ArrayList<ImageButton>());
    
        listener.onOrientationChanged(180);
        assertEquals(listener.currentRotaion, 180);
    
        verify(buttonHelper).rotate(270, 180, new ArrayList<ImageButton>());
        
        listener.onOrientationChanged(270);
        assertEquals(listener.currentRotaion, 90);
    
        verify(buttonHelper).rotate(180, 90, new ArrayList<ImageButton>());
    
        listener.onOrientationChanged(0);
        assertEquals(listener.currentRotaion, 0);
    
        verify(buttonHelper).rotate(90, 0, new ArrayList<ImageButton>());
        
    }
    
}
