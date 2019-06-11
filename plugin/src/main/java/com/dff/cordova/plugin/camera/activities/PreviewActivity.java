package com.dff.cordova.plugin.camera.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.exceptions.UnexpectedExceptionHandler;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;

import org.apache.cordova.CallbackContext;

import javax.inject.Inject;


/**
 * A preview of the taken image before it is saved.
 *
 * This Feature is optional.
 *
 * @author Anthony Nahas
 * @version 3.0.2
 * @since 22.2.2017
 */
public class PreviewActivity extends Activity {
    private static final String TAG = "PreviewActivity";

    private static final String PREVIEW_ACTIVITY_LAYOUT = "cameraplugin_activity_preview";
    private static final String IMAGE_VIEW_PREVIEW_ID = "image_view";
    private static final String BUTTON_CANCEL = "button_cancel";
    private static final String BUTTON_REPEAT = "button_repeat";
    private static final String BUTTON_OK = "button_ok";

    @Inject
    Log log;

    @Inject
    R r;
    
    @Inject
    ImageHelper imageHelper;
    
    @Inject
    UnexpectedExceptionHandler unexpectedExceptionHandler;

    /**
     * On creating the activity, initialize all components needed to preview the taken image.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *      previously being shut down then this Bundle contains the data it most
     *      recently supplied in {@link #onSaveInstanceState}.
     *      <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerManager
            .getInstance()
            .inject(this);

        Thread.currentThread().setUncaughtExceptionHandler(unexpectedExceptionHandler);

        log.d(TAG, "onCreate");
        setContentView(r.getLayoutIdentifier(PREVIEW_ACTIVITY_LAYOUT));
        ImageView imageView = findViewById(r.getIdIdentifier(IMAGE_VIEW_PREVIEW_ID));
        
        if (imageHelper.sBitmap != null) {
            imageView.setImageBitmap(imageHelper.sBitmap);
        } else {
            Toast
                .makeText(this,"Error while previewing the image 5125", Toast.LENGTH_LONG)
                .show();
        }

        ImageButton cancelButton = findViewById(r.getIdIdentifier(BUTTON_CANCEL));
        ImageButton repeatButton = findViewById(r.getIdIdentifier(BUTTON_REPEAT));
        ImageButton okButton = findViewById(r.getIdIdentifier(BUTTON_OK));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(R.RESULT_CANCELED, new Intent());
                log.d(TAG, "finish Preview Activity. Result = canceled");
                finish();
            }
        });
        repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(R.RESULT_REPEAT);
                log.d(TAG, "finish Preview Activity. Result = repeat");
                finish();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnImage();
            }
        });

    }
    
    private void returnImage() {
        Intent returnIntent = new Intent();
        if (r.sBase64Image != null) {
            for (CallbackContext callbackContext : r.getCallBackContexts()) {
                callbackContext.success(r.sBase64Image);
            }
            setResult(R.RESULT_OK);
            log.d(TAG, "finish Preview Activity. Result = ok");
        } else {
            log.e(TAG, "sBase64Image is empty");
            log.e(TAG, "repeat capture");
            setResult(R.RESULT_REPEAT, returnIntent);
        }
        finish();
    }
}
