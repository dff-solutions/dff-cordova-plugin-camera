package com.dff.cordova.plugin.camera.helpers;

import com.dff.cordova.plugin.camera.log.Log;

import org.apache.cordova.CallbackContext;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper to manage communication between plugin and cordova app via callbackContext.
 */
@Singleton
@SuppressWarnings("PMD.LooseCoupling")
public class CallbackContextHelper {
    private final String TAG = "CallbackContextHelper";
    
    private Log log;
    private ArrayList<CallbackContext> callbackContextList = new ArrayList<CallbackContext>();
    
    @Inject
    public CallbackContextHelper(
        Log log
    ) {
        this.log = log;
    }
    
    public void addCallBackContext(CallbackContext callBackContext) {
        callbackContextList.add(callBackContext);
    }
    
    public void sendAllSuccess(String success) {
        log.d(TAG, "send result");
        for (CallbackContext callbackContext : callbackContextList) {
            callbackContext.success(success);
        }
    }
    
    public void sendAllError(String error) {
        log.e(TAG, "send error: " + error);
        for (CallbackContext callbackContext : callbackContextList) {
            callbackContext.error(error);
        }
    }
}
