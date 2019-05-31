package com.dff.cordova.plugin.camera.res;

import android.content.Context;
import android.content.res.Resources;

import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;

import org.apache.cordova.CallbackContext;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class to hold and allocate needed resources.
 *
 * @author Anthony Nahas
 * @version 3.0.1
 * @since 05.01.2017
 */
@Singleton
public class R {
    private static final String DEF_TYPE_LAYOUT = "layout";
    private static final String DEF_TYPE_ID = "id";
    private static final String DEF_TYPE_DRAWABLE = "drawable";

    private String packageName;
    private Resources resources;

    @Inject
    public R(@ApplicationContext Context context) {
        packageName = context.getPackageName();
        resources = context.getResources();
    }

    public int getLayoutIdentifier(String name) {
        return resources.getIdentifier(name, DEF_TYPE_LAYOUT, packageName);
    }

    public int getIdIdentifier(String name) {
        return resources.getIdentifier(name, DEF_TYPE_ID, packageName);
    }

    public int getDrawableIdentifier(String name) {
        return resources.getIdentifier(name, DEF_TYPE_DRAWABLE, packageName);
    }
    
    public static final int RESULT_OK = 1;
    public static final int RESULT_REPEAT = 2;
    public static final int RESULT_CANCELED = 3;
    
    public String sBase64Image;
    private ArrayList<CallbackContext> callbackContextList = new ArrayList<>();
    
    public ArrayList<CallbackContext> getCallBackContexts() {
        return callbackContextList;
    }
    
    public void addCallBackContext(CallbackContext callBackContext) {
        callbackContextList.add(callBackContext);
    }
}
