package com.dff.cordova.plugin.camera.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.exceptions.UnexpectedExceptionHandler;
import com.dff.cordova.plugin.camera.helpers.ButtonHelper;
import com.dff.cordova.plugin.camera.helpers.CameraInfoHelper;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;
import com.dff.cordova.plugin.camera.views.CameraPreview;
import com.dff.cordova.plugin.camera.views.DrawingView;
import com.dff.cordova.plugin.camera.views.PreviewSurfaceView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import static com.dff.cordova.plugin.camera.actions.TakePhoto.JSON_ARG_WITH_PREVIEW;

/**
 * Class to create a preview for the camera (including focus mechanism).
 *
 * @author Anthony Nahas
 * @version 2.2.2
 * @since 05.01.2017
 */
public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";

    public static final String CAMERA_ACTIVITY_LAYOUT = "activity_camera";
    public static final String CAMERA_SURFACE_ID = "camera_preview_surface_view";
    public static final String CAMERA_DRAWING_SURFACE_ID = "camera_drawing_surface_view";
    public static final String BUTTON_TAKE_IMAGE = "take_image";
    public static final String BUTTON_CHANGE_FLASH_MODE = "button_flash";
    public static final String BUTTON_FLIP_CAMERA = "button_flip_camera";

    @Inject
    Log log;

    @Inject
    R r;

    @Inject
    CameraInfoHelper mCameraInfoHelper;

    @Inject
    ButtonHelper mButtonHelper;

    @Inject
    CameraPreview mCameraPreview;

    @Inject
    EventBus mEventBus;

    @Inject
    UnexpectedExceptionHandler unexpectedExceptionHandler;

    private PreviewSurfaceView mSurfaceView;
    private DrawingView mDrawingView;
    private SurfaceHolder mSurfaceHolder;
    private ImageButton mCaptureImage;
    private ImageButton mFlashButton;
    private ImageButton mFlipCamera;
    private int mRotation;
    private OrientationEventListener mOrientationEventListener;

    /**
     * Initialize used components on create the activity.
     *
     * @param savedInstanceState - saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerManager
            .getInstance()
            .inject(this);

        Thread.currentThread().setUncaughtExceptionHandler(unexpectedExceptionHandler);

        setContentView(r.getLayoutIdentifier(CAMERA_ACTIVITY_LAYOUT));

        //on creating the surface view
        mSurfaceView = findViewById(r.getIdIdentifier(CAMERA_SURFACE_ID));
        mCaptureImage = findViewById(r.getIdIdentifier(BUTTON_TAKE_IMAGE));
        mFlashButton = findViewById(r.getIdIdentifier(BUTTON_CHANGE_FLASH_MODE));
        mFlipCamera = findViewById(r.getIdIdentifier(BUTTON_FLIP_CAMERA));
        mDrawingView = findViewById(r.getIdIdentifier(CAMERA_DRAWING_SURFACE_ID));

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mCameraPreview);

        mSurfaceView.setListener(mCameraPreview);
        mSurfaceView.setDrawingView(mDrawingView);
        mSurfaceView.setEventBus(mEventBus);

        mCameraPreview.setContext(this);
        mCameraPreview.setWithPreview(getIntent().getBooleanExtra(JSON_ARG_WITH_PREVIEW, false));
        mCameraPreview.setCaptureImage(mCaptureImage);
        mCameraPreview.setFlashButton(mFlashButton);
        mCameraPreview.setFlipCamera(mFlipCamera);
        
        mButtonHelper.addImageButton(mFlashButton);
        mButtonHelper.addImageButton(mFlipCamera);

        mOrientationEventListener = new OrientationEventListener(
            this,
            SensorManager.SENSOR_DELAY_NORMAL
        ) {
            @Override
            public void onOrientationChanged(int orientation) {
                log.d(TAG, "orientation changed: " + orientation);

                switch (orientation) {
                    case 0:
                        if (mRotation != 0) {
                            log.d(TAG, "Orientation = 0");
                            log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 90:
                                    mButtonHelper.rotate(-90, 0);
                                    break;
                                case 270:
                                    mButtonHelper.rotate(90, 0);
                                    break;
                                default:
                                    mButtonHelper.rotate(0, 0);
                                    break;
                            }
                            mRotation = 0;
                            mCameraPreview.onOrientationChanged(mRotation);
                        }
                        break;
                    case 90:
                        if (mRotation != 90) {
                            log.d(TAG, "Orientation = 90");
                            log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 0:
                                    // TODO check call parameters
                                    mButtonHelper.rotate(360, 270);
                                    break;
                                case 180:
                                    mButtonHelper.rotate(360, 270);
                                    break;
                                default:
                                    mButtonHelper.rotate(0, 270);
                                    break;
                            }
                            mRotation = 90;
                            mCameraPreview.onOrientationChanged(mRotation);
                        }
                        break;
                    case 180:
                        if (mRotation != 180) {
                            log.d(TAG, "Orientation 180");
                            log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 90:
                                    mButtonHelper.rotate(270, 180);
                                    break;
                                case 270:
                                    mButtonHelper.rotate(90, 180);
                                    break;
                                default:
                                    mButtonHelper.rotate(0, 180);
                                    break;
                            }
                            mRotation = 180;
                            mCameraPreview.onOrientationChanged(mRotation);
                        }
                        break;
                    case 270:
                        if (mRotation != 270) {
                            log.d(TAG, "Orientation 270");
                            log.d(TAG, "mRotation = " + mRotation);
                            switch (mRotation) {
                                case 0:
                                    mButtonHelper.rotate(0, 90);
                                    break;
                                case 180:
                                    mButtonHelper.rotate(180, 90);
                                    break;
                                default:
                                    mButtonHelper.rotate(0, 90);
                                    break;
                            }
                            mRotation = 270;
                            mCameraPreview.onOrientationChanged(mRotation);
                        }
                        break;
                    default:
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
        log.d(TAG, "onPause()");
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
        log.d(TAG, "requestCode = " + requestCode);
        log.d(TAG, "resultCode = " + resultCode);

        switch (resultCode) {
            case RESULT_OK:
                log.d(TAG, "resultCode2 = " + resultCode);
                if (r.sBase64Image != null && !r.sBase64Image.isEmpty()) {
                    log.d(TAG, r.sBase64Image);
                    r.getCallBackContext().success(r.sBase64Image);
                } else {
                    r.getCallBackContext().error("Error: the base64 image is empty or null");
                }
                finish();
                break;
            case RESULT_CANCELED:
                log.d(TAG, "resultCode2 = " + resultCode);
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
            default:
                break;
        }
    }
}
