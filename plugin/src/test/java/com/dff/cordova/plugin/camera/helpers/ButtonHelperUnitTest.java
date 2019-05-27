package com.dff.cordova.plugin.camera.helpers;

import android.hardware.camera2.CaptureRequest;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.res.R;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ButtonHelperUnitTest {
    private static final String IC_CAMERA_FRONT = "ic_switch_cam_front";
    private static final String IC_CAMERA_BACK = "ic_switch_cam_back";
    private static final String IC_FLASH_AUTO = "ic_flash_auto_white_24px";
    private static final String IC_FLASH_OFF = "ic_flash_off_white_24px";
    private static final String IC_FLASH_ON = "ic_flash_on_white_24px";
    private final int FLASH_ON = 0;
    private final int FLASH_OFF = 1;
    private final int FLASH_AUTO = 2;
    private final int CAMERA_BACK = 0;
    private final int CAMERA_FRONT = 1;
    
    @Mock
    ImageButton button;
    
    @Mock
    CaptureRequest.Builder captureRequest;
    
    @Mock
    Log log;
    
    @Mock
    R r;
    
    @InjectMocks
    ButtonHelper buttonHelper;
    
    @Test
    public void shouldRotateButton(){
        doReturn(View.VISIBLE).when(button).getVisibility();
        
        buttonHelper.addImageButton(button);
        
        buttonHelper.rotate(0.0f, 2.0f);
        
        verify(button).getVisibility();
        verify(button).startAnimation(any());
    }
    
    @Test
    public void shouldChangeFlashMode() {
        doReturn(FLASH_AUTO).when(r).getDrawableIdentifier(IC_FLASH_AUTO);
        doReturn(FLASH_OFF).when(r).getDrawableIdentifier(IC_FLASH_OFF);
        doReturn(FLASH_ON).when(r).getDrawableIdentifier(IC_FLASH_ON);
        
        buttonHelper.changeFlashButton(captureRequest, button);
        
        verify(button).setImageResource(FLASH_AUTO);
        verify(captureRequest).set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        verify(captureRequest).set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        
        buttonHelper.changeFlashButton(captureRequest, button);
        
        verify(button).setImageResource(FLASH_ON);
        verify(captureRequest).set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
        verify(captureRequest).set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
        
        buttonHelper.changeFlashButton(captureRequest, button);
    
        verify(button).setImageResource(FLASH_OFF);
        verify(captureRequest, times(2)).set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON);
        verify(captureRequest, times(2)).set(CaptureRequest.FLASH_MODE,
                                           CaptureRequest.FLASH_MODE_OFF);
    }
    
    @Test
    public void shouldChangeCamera() {
        doReturn(CAMERA_BACK).when(r).getDrawableIdentifier(IC_CAMERA_BACK);
        doReturn(CAMERA_FRONT).when(r).getDrawableIdentifier(IC_CAMERA_FRONT);
        
        buttonHelper.changeFlipButton(button, CAMERA_BACK);
        
        verify(button).setImageResource(CAMERA_BACK);
        
        buttonHelper.changeFlipButton(button, CAMERA_FRONT);
    
        verify(button).setImageResource(CAMERA_FRONT);
    }
    
    @Test
    public void shouldDisableButtons() {
        buttonHelper.addImageButton(button);
        
        buttonHelper.enableAllButtons(false);
        assertFalse(button.isEnabled());
        
        doReturn(true).when(button).isEnabled();
        
        buttonHelper.enableAllButtons(true);
        assertTrue(button.isEnabled());
    }
}
