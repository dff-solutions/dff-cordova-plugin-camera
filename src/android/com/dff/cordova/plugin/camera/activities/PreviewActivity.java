package com.dff.cordova.plugin.camera.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.dff.cordova.plugin.camera.R.R;


/**
 * activity that enable to preview the taken image before to be saved,
 * forwarded...
 * <p>
 * This Feature is optional.
 *
 * @author Anthony Nahas
 * @version 2.2.2
 * @since 22.2.2017
 */

public class PreviewActivity extends Activity {

    private static final String TAG = PreviewActivity.class.getSimpleName();

    private ImageView mImageView;

    private ImageButton mCancelButton;
    private ImageButton mRepeatButton;
    private ImageButton mOkButton;

    /**
     * on creating the activity, initialize all components needed to preview the taken image.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate() - PreviewActivity");
        setContentView(R.RESOURCES.getIdentifier(R.PREVIEW_ACTIVITY_LAYOUT, R.LAYOUT, R.PACKAGE_NAME));
        mImageView = (ImageView) findViewById(R.RESOURCES.getIdentifier(R.IMAGE_VIEW_PREVIEW_ID, R.ID, R.PACKAGE_NAME));

        if (R.sBitmap != null) {
            mImageView.setImageBitmap(R.sBitmap);
        } else {
            //Fehlerbehandlung!
        }

        mCancelButton = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_CANCEL, R.ID, R.PACKAGE_NAME));
        mRepeatButton = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_REPEAT, R.ID, R.PACKAGE_NAME));
        mOkButton = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_OK, R.ID, R.PACKAGE_NAME));

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, new Intent());
                finish();
            }
        });
        mRepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(R.RESULT_REPEAT);
                finish();
            }
        });
        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
