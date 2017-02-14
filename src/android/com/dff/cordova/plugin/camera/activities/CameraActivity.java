package com.dff.cordova.plugin.camera.activities;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import android.widget.ImageButton;
import com.dff.cordova.plugin.camera.R.R;
import com.dff.cordova.plugin.camera.helpers.RotationHelper;
import com.dff.cordova.plugin.camera.views.CameraPreview;
import com.dff.cordova.plugin.camera.views.DrawingView;
import com.dff.cordova.plugin.camera.views.PreviewSurfaceView;

import java.util.ArrayList;
import java.util.List;


/**
 * Class to create a preview for the camera (including focus mechanism).
 *
 * @author Anthony Nahas
 * @version 2.0.1
 * @since 05.01.2017
 */
public class CameraActivity extends Activity {

    private static final String TAG = CameraActivity.class.getSimpleName();

    private PreviewSurfaceView mSurfaceView;
    private DrawingView mDrawingView;
    private CameraPreview mCameraPreview;
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
        setContentView(R.RESOURCES.getIdentifier(R.CAMERA_ACTIVITY_LAYOUT, R.LAYOUT, R.PACKAGE_NAME));

        //on creating the surface view
        mSurfaceView = (PreviewSurfaceView) findViewById(R.RESOURCES.getIdentifier(R.CAMERA_SURFACE_ID, R.ID, R.PACKAGE_NAME));
        mCaptureImage = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_TAKE_IMAGE, R.ID, R.PACKAGE_NAME));
        mFlashButton = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_CHANGE_FLASH_MODE, R.ID, R.PACKAGE_NAME));
        mFlipCamera = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_FLIP_CAMERA, R.ID, R.PACKAGE_NAME));
        mDrawingView = (DrawingView) findViewById(R.RESOURCES.getIdentifier(R.CAMERA_DRAWING_SURFACE_ID, R.ID, R.PACKAGE_NAME));

        mSurfaceHolder = mSurfaceView.getHolder();
        mCameraPreview = new CameraPreview(this, mFlashButton, mCaptureImage, mFlipCamera);
        mSurfaceHolder.addCallback(mCameraPreview);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mSurfaceView.setListener(mCameraPreview);
        mSurfaceView.setDrawingView(mDrawingView);

        final List<ImageButton> imageButtonList = new ArrayList<ImageButton>();
        imageButtonList.add(mFlashButton);
        imageButtonList.add(mFlipCamera);

        mOrientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
            @Override
            public void onOrientationChanged(int orientation) {
                //Log.d(TAG, "orientation changed: " + orientation);

                switch (orientation) {
                    case 0:
                        if (mRotation != 0) {
                            Log.d(TAG, "Orientation = 0");
                            Log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 90:
                                    RotationHelper.rotate(-90, 0, imageButtonList);
                                    break;
                                case 270:
                                    RotationHelper.rotate(90, 0, imageButtonList);
                                    break;
                                default:
                                    RotationHelper.rotate(0, 0, imageButtonList);
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
                                    RotationHelper.rotate(360, 270, imageButtonList);
                                case 180:
                                    RotationHelper.rotate(360, 270, imageButtonList);
                                    break;
                                default:
                                    RotationHelper.rotate(0, 270, imageButtonList);
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
                                    RotationHelper.rotate(270, 180, imageButtonList);
                                    break;
                                case 270:
                                    RotationHelper.rotate(90, 180, imageButtonList);
                                    break;
                                default:
                                    RotationHelper.rotate(0, 180, imageButtonList);
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
                                    RotationHelper.rotate(0, 90, imageButtonList);
                                    break;
                                case 180:
                                    RotationHelper.rotate(180, 90, imageButtonList);
                                    break;
                                default:
                                    RotationHelper.rotate(0, 90, imageButtonList);
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
}