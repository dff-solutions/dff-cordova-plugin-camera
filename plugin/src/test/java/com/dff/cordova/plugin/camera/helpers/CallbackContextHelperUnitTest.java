package com.dff.cordova.plugin.camera.helpers;

import com.dff.cordova.plugin.camera.classes.json.JsonThrowable;
import com.dff.cordova.plugin.camera.log.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.inject.Inject;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CallbackContextHelperUnitTest {
    private static final String successMessage = "success";
    private static final String errorMessage = "error";
    
    @Mock
    CallbackContext context1;
    
    @Mock
    CallbackContext context2;
    
    @Mock
    JSONObject jsonObject;
    
    @Mock
    Log log;
    
    @Mock
    JsonThrowable jsonThrowable;
    
    @Mock
    Exception exception;
    
    @InjectMocks
    CallbackContextHelper contextHelper;
    
    @BeforeEach
    void setup() {
        contextHelper.addCallBackContext(context1);
    }
    
    @Test
    public void shouldSendSuccess() {
        contextHelper.sendAllSuccess(successMessage);
        verify(context1).success(successMessage);
        
        contextHelper.addCallBackContext(context2);
        
        contextHelper.sendAllSuccess(successMessage);
        verify(context1, times(2)).success(successMessage);
        verify(context2).success(successMessage);
    }
    
    @Test
    public void shouldSendError() {
        contextHelper.sendAllError(errorMessage);
        verify(context1).error(errorMessage);
        
        contextHelper.addCallBackContext(context2);
        
        contextHelper.sendAllError(errorMessage);
        verify(context1, times(2)).error(errorMessage);
        verify(context2).error(errorMessage);
    }
    
    @Test
    public void shouldSendException() throws JSONException {
        doReturn(jsonObject).when(jsonThrowable).toJson(exception);
        
        contextHelper.sendAllException(exception);
        verify(context1).error(jsonObject);
    
        contextHelper.addCallBackContext(context2);
        
        contextHelper.sendAllException(exception);
        verify(context1, times(2)).error(jsonObject);
        verify(context2).error(jsonObject);
    }
}
