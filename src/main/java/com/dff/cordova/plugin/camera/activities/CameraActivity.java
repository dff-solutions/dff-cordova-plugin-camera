package com.dff.cordova.plugin.camera.activities;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import com.dff.cordova.plugin.camera.Res.R;
import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.helpers.CameraInfoHelper;
import com.dff.cordova.plugin.camera.helpers.RotationHelper;
import com.dff.cordova.plugin.camera.views.CameraPreview;
import com.dff.cordova.plugin.camera.views.DrawingView;
import com.dff.cordova.plugin.camera.views.PicIndicatorView;
import com.dff.cordova.plugin.camera.views.PreviewSurfaceView;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


/**
 * Class to create a preview for the camera (including focus mechanism).
 *
 * @author Anthony Nahas
 * @version 2.2.2
 * @since 05.01.2017
 */
public class CameraActivity extends Activity {

    private static final String TAG = CameraActivity.class.getSimpleName();

    @Inject
    CameraInfoHelper mCameraInfoHelper;

    @Inject
    RotationHelper mRotationHelper;

    @Inject
    CameraPreview mCameraPreview;

    @Inject
    EventBus mEventBus;

    private PreviewSurfaceView mSurfaceView;
    private DrawingView mDrawingView;
    private SurfaceHolder mSurfaceHolder;
    private ImageButton mCaptureImage;
    private ImageButton mFlashButton;
    private ImageButton mFlipCamera;
    private int mRotation;
    private OrientationEventListener mOrientationEventListener;

    /**
     * Iinitialize used components on create the activity.
     *
     * @param savedInstanceState - saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        boolean withPicindicator = getIntent().getBooleanExtra(R.WITH_PICINDICATOR, false);

        DaggerManager
            .getInstance()
            .inject(this);
        setContentView(R.RESOURCES.getIdentifier(R.CAMERA_ACTIVITY_LAYOUT, R.LAYOUT, R.PACKAGE_NAME));


        //on creating the surface view
        mSurfaceView = (PreviewSurfaceView) findViewById(R.RESOURCES.getIdentifier(R.CAMERA_SURFACE_ID, R.ID, R.PACKAGE_NAME));
        mCaptureImage = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_TAKE_IMAGE, R.ID, R.PACKAGE_NAME));
        mFlashButton = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_CHANGE_FLASH_MODE, R.ID, R.PACKAGE_NAME));
        mFlipCamera = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_FLIP_CAMERA, R.ID, R.PACKAGE_NAME));
        mDrawingView = (DrawingView) findViewById(R.RESOURCES.getIdentifier(R.CAMERA_DRAWING_SURFACE_ID, R.ID, R.PACKAGE_NAME));

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mCameraPreview);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mSurfaceView.setListener(mCameraPreview);
        mSurfaceView.setDrawingView(mDrawingView);
        mSurfaceView.setEventBus(mEventBus);

        if (withPicindicator) {
            PicIndicatorView picIndicatorView = (PicIndicatorView) findViewById(R.RESOURCES.getIdentifier
                (R.CAMERA_DRAWING_PICINDICATOR_ID, R.ID, R.PACKAGE_NAME));
            picIndicatorView.setVisibility(View.VISIBLE);
            mSurfaceView.setPicIndicatorView(picIndicatorView);
            Toast.makeText(this, R.PICINDICATOR_MSG, Toast.LENGTH_LONG).show();
        }

        mCameraPreview.setContext(this);
        mCameraPreview.setWithPreview(getIntent().getExtras().getBoolean(R.WITH_PREVIEW_KEY));
        mCameraPreview.setCaptureImage(mCaptureImage);
        mCameraPreview.setFlashButton(mFlashButton);
        mCameraPreview.setFlipCamera(mFlipCamera);

        final List<ImageButton> imageButtonList = new ArrayList<>();
        imageButtonList.add(mFlashButton);
        imageButtonList.add(mFlipCamera);

        mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                Log.d(TAG, "orientation changed: " + orientation);

                switch (orientation) {
                    case 0:
                        if (mRotation != 0) {
                            Log.d(TAG, "Orientation = 0");
                            Log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 90:
                                    mRotationHelper.rotate(-90, 0, imageButtonList);
                                    break;
                                case 270:
                                    mRotationHelper.rotate(90, 0, imageButtonList);
                                    break;
                                default:
                                    mRotationHelper.rotate(0, 0, imageButtonList);
                                    break;
                            }
                            mRotation = 0;
                            mCameraPreview.onOrientationChanged(mRotation);

                        }
                        break;
                    case 90:
                        if (mRotation != 90) {
                            Log.d(TAG, "Orientation = 90");
                            Log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 0:
                                    mRotationHelper.rotate(360, 270, imageButtonList);
                                case 180:
                                    mRotationHelper.rotate(360, 270, imageButtonList);
                                    break;
                                default:
                                    mRotationHelper.rotate(0, 270, imageButtonList);
                                    break;
                            }
                            mRotation = 90;
                            mCameraPreview.onOrientationChanged(mRotation);
                        }
                        break;
                    case 180:
                        if (mRotation != 180) {
                            Log.d(TAG, "Orientation 180");
                            Log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 90:
                                    mRotationHelper.rotate(270, 180, imageButtonList);
                                    break;
                                case 270:
                                    mRotationHelper.rotate(90, 180, imageButtonList);
                                    break;
                                default:
                                    mRotationHelper.rotate(0, 180, imageButtonList);
                                    break;
                            }
                            mRotation = 180;
                            mCameraPreview.onOrientationChanged(mRotation);
                        }
                        break;
                    case 270:
                        if (mRotation != 270) {
                            Log.d(TAG, "Orientation 270");
                            Log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 0:
                                    mRotationHelper.rotate(0, 90, imageButtonList);
                                    break;
                                case 180:
                                    mRotationHelper.rotate(180, 90, imageButtonList);
                                    break;
                                default:
                                    mRotationHelper.rotate(0, 90, imageButtonList);
                                    break;
                            }
                            mRotation = 270;
                            mCameraPreview.onOrientationChanged(mRotation);
                        }
                        break;
                }
            }
        };

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //keep the screen on until the activity is running.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    /**
     * Release the camera when leaving the activity.
     * Disable the orientation event listener when leaving the activity.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        mCameraPreview.releaseCamera();
        mOrientationEventListener.disable();
    }

    /**
     * recall the the orientation event listener after resuming the activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mOrientationEventListener.enable();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(TAG, "requestCode = " + requestCode);
        Log.d(TAG, "resultCode = " + resultCode);

        switch (resultCode) {
            case RESULT_OK:
                Log.d(TAG, "resultCode2 = " + resultCode);
                if (R.sBase64Image != null && !R.sBase64Image.isEmpty()) {
                    Log.d(TAG, R.sBase64Image);
                    R.sCallBackContext.success(R.sBase64Image);
                } else {
                    R.sCallBackContext.error("Error: the base64 image is empty or null");
                }
                finish();
                break;
            case RESULT_CANCELED:
                Log.d(TAG, "resultCode2 = " + resultCode);
                setResult(RESULT_OK);
                finish();
                break;
            case R.RESULT_REPEAT:
                mCaptureImage.setVisibility(View.VISIBLE);
                mFlipCamera.setVisibility(View.VISIBLE);
                if (!mCameraInfoHelper.isFrontCameraOn(mCameraPreview.getCameraID())) {
                    mFlashButton.setVisibility(View.VISIBLE);
                }
                break;
        }

    }
}
