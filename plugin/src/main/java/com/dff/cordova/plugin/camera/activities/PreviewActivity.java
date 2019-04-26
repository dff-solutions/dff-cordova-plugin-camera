package com.dff.cordova.plugin.camera.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.dff.cordova.plugin.camera.Res.R;

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
    private static final String TAG = PreviewActivity.class.getSimpleName();

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
        Log.d(TAG, "onCreate() - PreviewActivity");
        setContentView(
            R.RESOURCES.getIdentifier(R.PREVIEW_ACTIVITY_LAYOUT, R.LAYOUT, R.PACKAGE_NAME)
        );
        ImageView imageView = findViewById(
            R.RESOURCES.getIdentifier(R.IMAGE_VIEW_PREVIEW_ID, R.ID, R.PACKAGE_NAME)
        );

        if (R.sBitmap != null) {
            imageView.setImageBitmap(R.sBitmap);
        } else {
            Toast
                .makeText(this,"Error while previewing the image 5125", Toast.LENGTH_LONG)
                .show();
        }

        ImageButton cancelButton = findViewById(
            R.RESOURCES.getIdentifier(R.BUTTON_CANCEL, R.ID, R.PACKAGE_NAME)
        );
        ImageButton repeatButton = findViewById(
            R.RESOURCES.getIdentifier(R.BUTTON_REPEAT, R.ID, R.PACKAGE_NAME)
        );
        ImageButton okButton = findViewById(
            R.RESOURCES.getIdentifier(R.BUTTON_OK, R.ID, R.PACKAGE_NAME)
        );

        cancelButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED, new Intent());
            finish();
        });
        repeatButton.setOnClickListener(view -> {
            setResult(R.RESULT_REPEAT);
            finish();
        });
        okButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            setResult(RESULT_OK, intent);
            finish();
        });

    }
}
