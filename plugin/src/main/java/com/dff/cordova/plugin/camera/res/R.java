package com.dff.cordova.plugin.camera.res;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import com.dff.cordova.plugin.camera.dagger.annotations.ApplicationContext;
import com.dff.cordova.plugin.camera.log.Log;

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
    private static final String TAG = "R";
    private static final String DEF_TYPE_LAYOUT = "layout";
    private static final String DEF_TYPE_ID = "id";
    private static final String DEF_TYPE_DRAWABLE = "drawable";

    private String packageName;
    private Resources resources;
    private Log log;

    @Inject
    public R(@ApplicationContext Context context, Log log) {
        packageName = context.getPackageName();
        resources = context.getResources();
        this.log = log;
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

    public static final int IMAGE_PREVIEW_REQUEST = 1;
    
    public static final int RESULT_OK = 1;
    public static final int RESULT_REPEAT = 2;
    public static final int RESULT_CANCELED = 3;

    public Bitmap sBitmap;
    public String sBase64Image;
    private ArrayList<CallbackContext> callbackContextList = new ArrayList<>();
    
    public ArrayList<CallbackContext> getCallBackContexts() {
        return callbackContextList;
    }
    
    public void addCallBackContext(CallbackContext callBackContext) {
        callbackContextList.add(callBackContext);
    }
}
