package com.dff.cordova.plugin.camera.helpers;

import android.content.pm.PackageManager;
import android.hardware.camera2.CaptureRequest;
import android.view.View;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.activities.CameraActivity;
import com.dff.cordova.plugin.camera.classes.CameraState;
import com.dff.cordova.plugin.camera.res.R;
import com.dff.cordova.plugin.camera.log.Log;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ButtonHelperUnitTest {
    private static final String IC_CAMERA_FRONT = "camera_ic_switch_cam_front";
    private static final String IC_CAMERA_BACK = "camera_ic_switch_cam_back";
    private static final String IC_FLASH_AUTO = "camera_ic_flash_auto_white_24px";
    private static final String IC_FLASH_OFF = "camera_ic_flash_off_white_24px";
    private static final String IC_FLASH_ON = "camera_ic_flash_on_white_24px";
    private final int FLASH_ON = 0;
    private final int FLASH_OFF = 1;
    private final int FLASH_AUTO = 2;
    private final CameraState CAMERA_BACK = CameraState.BACK;
    private final CameraState CAMERA_FRONT = CameraState.FRONT;
    private static final String CAPTURE_BUTTON = "capture_button";
    private static final String FLASH_BUTTON = "flash_button";
    private static final String FLIP_BUTTON = "flip_button";
    private static final int CAPTURE_BUTTON_ID = 0;
    private static final int FLASH_BUTTON_ID = 1;
    private static final int FLIP_BUTTON_ID = 2;
    
    @Mock
    ImageButton captureButton;
    
    @Mock
    ImageButton flipButton;
    
    @Mock
    ImageButton flashButton;
    
    @Mock
    CaptureRequest.Builder captureRequest;
    
    @Mock
    PackageManager packageManager;
    
    @Mock
    Log log;
    
    @Mock
    R r;
    
    @Mock
    CameraActivity cameraActivity;
    
    @InjectMocks
    CameraButtonHelper buttonHelper;
    
    @BeforeEach
    void setup() {
        doReturn(CAPTURE_BUTTON_ID).when(r).getIdIdentifier(CAPTURE_BUTTON);
        doReturn(FLASH_BUTTON_ID).when(r).getIdIdentifier(FLASH_BUTTON);
        doReturn(FLIP_BUTTON_ID).when(r).getIdIdentifier(FLIP_BUTTON);
        
        doReturn(captureButton).when(cameraActivity).findViewById(CAPTURE_BUTTON_ID);
        doReturn(flashButton).when(cameraActivity).findViewById(FLASH_BUTTON_ID);
        doReturn(flipButton).when(cameraActivity).findViewById(FLIP_BUTTON_ID);
        
        doReturn(true).when(packageManager).hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        doReturn(true).when(packageManager).hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        
        buttonHelper.initButtons();
    }
    
    @Test
    public void shouldRotateButton(){
        
        buttonHelper.rotate(0.0f, 2.0f);
        
        verify(flipButton).startAnimation(any());
        verify(flashButton).startAnimation(any());
    }
    
    @Test
    public void shouldChangeFlashMode() {
        doReturn(FLASH_AUTO).when(r).getDrawableIdentifier(IC_FLASH_AUTO);
        doReturn(FLASH_OFF).when(r).getDrawableIdentifier(IC_FLASH_OFF);
        doReturn(FLASH_ON).when(r).getDrawableIdentifier(IC_FLASH_ON);
        
        buttonHelper.changeFlashButton(captureRequest);
        
        verify(flashButton).setImageResource(FLASH_AUTO);
        verify(captureRequest).set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        verify(captureRequest).set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        
        buttonHelper.changeFlashButton(captureRequest);
        
        verify(flashButton).setImageResource(FLASH_ON);
        verify(captureRequest).set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON_ALWAYS_FLASH);
        verify(captureRequest).set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_SINGLE);
        
        buttonHelper.changeFlashButton(captureRequest);
    
        verify(flashButton).setImageResource(FLASH_OFF);
        verify(captureRequest, times(2)).set(CaptureRequest.CONTROL_AE_MODE,
                                   CaptureRequest.CONTROL_AE_MODE_ON);
        verify(captureRequest, times(2)).set(CaptureRequest.FLASH_MODE,
                                           CaptureRequest.FLASH_MODE_OFF);
    }
    
    @Test
    public void shouldChangeCamera() {
        doReturn(CAMERA_BACK.ordinal()).when(r).getDrawableIdentifier(IC_CAMERA_BACK);
        doReturn(CAMERA_FRONT.ordinal()).when(r).getDrawableIdentifier(IC_CAMERA_FRONT);
        
        buttonHelper.changeFlipButton(CAMERA_BACK);
        
        verify(flipButton).setImageResource(CAMERA_BACK.ordinal());
        
        buttonHelper.changeFlipButton(CAMERA_FRONT);
    
        verify(flipButton).setImageResource(CAMERA_FRONT.ordinal());
    }
    
    @Test
    public void shouldDisableButtons() {
        buttonHelper.enableAllButtons(false);
        assertFalse(captureButton.isEnabled());
        assertFalse(flashButton.isEnabled());
        assertFalse(flipButton.isEnabled());
    
        doReturn(true).when(captureButton).isEnabled();
        doReturn(true).when(flashButton).isEnabled();
        doReturn(true).when(flipButton).isEnabled();
        
        buttonHelper.enableAllButtons(true);
        assertTrue(captureButton.isEnabled());
        assertTrue(flashButton.isEnabled());
        assertTrue(flipButton.isEnabled());
    }
}
