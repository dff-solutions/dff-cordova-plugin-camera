package com.dff.cordova.plugin.camera.helpers;

import com.dff.cordova.plugin.camera.classes.json.JsonThrowable;
import com.dff.cordova.plugin.camera.log.Log;

import org.apache.cordova.CallbackContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper to manage communication between plugin and cordova app via callbackContext.
 */
@Singleton
public class CallbackContextHelper {
    private static final String TAG = "CallbackContextHelper";
    
    private Log log;
    private List<CallbackContext> callbackContextList = new ArrayList<CallbackContext>();
    private JsonThrowable jsonThrowable;
    
    @Inject
    public CallbackContextHelper(
        Log log,
        JsonThrowable jsonThrowable
    ) {
        this.log = log;
        this.jsonThrowable = jsonThrowable;
    }
    
    public void addCallBackContext(CallbackContext callBackContext) {
        callbackContextList.add(callBackContext);
    }
    
    /**
     * Return a string message to the cordova app with callbackContext as success.
     *
     * @param success string message
     */
    public void sendAllSuccess(String success) {
        log.d(TAG, "send result");
        for (CallbackContext callbackContext : callbackContextList) {
            callbackContext.success(success);
        }
        deleteAll();
    }
    
    /**
     * Return a string message to the cordova app with callbackContext as error.
     *
     * @param error string message
     */
    public void sendAllError(String error) {
        log.e(TAG, "send error: " + error);
        for (CallbackContext callbackContext : callbackContextList) {
            callbackContext.error(error);
        }
        deleteAll();
    }
    
    /**
     * Return a thrown exception as json to the cordova app with callbackContext as error.
     *
     * @param e thrown exception
     */
    public void sendAllException(Exception e) {
        try {
            JSONObject exception  = jsonThrowable.toJson(e);
            for (CallbackContext callbackContext : callbackContextList) {
                callbackContext.error(exception);
            }
        } catch (JSONException e1) {
            log.e(TAG, "unable to send exception as json", e1);
        }
        deleteAll();
    }
    
    private void deleteAll() {
        callbackContextList.clear();
    }
}
