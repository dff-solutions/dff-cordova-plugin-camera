package com.dff.cordova.plugin.camera.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.Res.R;
import com.dff.cordova.plugin.camera.activities.PreviewActivity;
import com.dff.cordova.plugin.camera.events.OnAutoFocus;
import com.dff.cordova.plugin.camera.helpers.CameraInfoHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.view.OrientationEventListener.ORIENTATION_UNKNOWN;

/**
 * Class to preview the camera.
 *
 * @author Anthony Nahas
 * @version 2.1.1
 * @since 2.2.2017
 */
public class CameraPreview implements SurfaceHolder.Callback, AutoFocusCallback {
    private static final String TAG = "CameraPreview";
    private static int sFlashMode;

    private Context mContext;
    private EventBus mEventBus;
    private Camera mCamera = null;
    private Camera.Parameters mParams;
    private SurfaceHolder mSurfaceHolder;
    private CameraInfoHelper mCameraInfoHelper;
    private ImageButton mFlashButton;
    private ImageButton mCaptureImage;
    private ImageButton mFlipCamera;
    private Boolean mWithPreview;

    private int mRotation;
    private int mCameraID;

    /**
     * Custom constructor.
     */
    @Inject
    public CameraPreview(
        EventBus eventBus,
        CameraInfoHelper cameraInfoHelper
    ) {
        mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        this.mEventBus = eventBus;
        this.mCameraInfoHelper = cameraInfoHelper;

        sFlashMode = 0;
    }

    /**
     * Get the camera id in order to prepare additional information concerning
     * the camera using the CameraInfoHelper classes.
     *
     * @return - the id of the opened camera
     */
    public int getCameraID() {
        return mCameraID;
    }

    /**
     * Try to open the camera with params.
     *
     * @param id - whether front or back camera.
     * @return Whether camera could be opened
     */
    private boolean openCamera(int id) {
        boolean result = false;
        mCameraID = id;
        releaseCamera(); //imp
        try {
            mCamera = Camera.open(mCameraID);
        } catch (Exception e) {
            Log.e(TAG, "Error while opening the camera", e);
            return false;
        }
        if (mCamera != null) {
            try {
                mCamera.setErrorCallback(new Camera.ErrorCallback() {
                    @Override
                    public void onError(int error, Camera camera) {
                        //to show the error message.
                        //callback
                    }
                });
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                result = true;
            } catch (IOException e) {
                Log.e(TAG, "Error: ", e);
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    /**
     * Setup the camera with the correct params and rotation.
     */
    private void setUpCamera() {
        try {

            mParams = mCamera.getParameters();
            List<String> focusModes = mParams.getSupportedFlashModes();
            if (focusModes != null) {
                if (focusModes
                    .contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                    mParams.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                }
            }
            mCamera.setParameters(mParams);
            mCamera.enableShutterSound(true);
            mCamera.startPreview();
            mCamera.setParameters(mParams);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
    }

    /**
     * Try to open the camera when the surface is created.
     *
     * @param surfaceHolder - the assigned surface holder.
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;

        if (!openCamera(mCameraID)) {
            Log.d(TAG, "On surface created : camera could not be opened");
        } else {
            Log.i(TAG, " camera opened");
        }
    }

    /**
     * Start preview with new settings.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            setCameraDisplayOrientation(((Activity) mContext), mCameraID);
            mCamera.setPreviewDisplay(holder);
            setUpCamera();
            onOrientationChanged(mRotation);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
    }

    /**
     * Try to release the camera when the surface is destroyed.
     *
     * @param surfaceHolder - the assigned surfaceholder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //releaseCamera();
        if (mCamera != null) {
            //stop the preview
            mCamera.stopPreview();
            //release the camera
            mCamera.release();
            //unbind the camera from this object
            mCamera = null;
        }

    }

    /**
     * Called from PreviewSurfaceView to set touch focus.
     *
     * @param focusRect - new area for auto focus
     */
    public void doTouchFocus(final Rect focusRect) {
        Log.i(TAG, "TouchFocus");
        try {
            final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(focusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters para = mCamera.getParameters();
            para.setFocusAreas(focusList);
            para.setMeteringAreas(focusList);
            mCamera.setParameters(para);

            mCamera.autoFocus(this);
        } catch (Exception e) {
            Log.i(TAG, "Unable to autofocus", e);
        }

    }

    /**
     * Refresh params: ex: when changing the flash mode.
     *
     * @param mode - the assigned mode to be set.
     */
    private void refreshParams(String mode) {
        if (mCamera != null) {
            Log.d(TAG, "supported flash mode");
            Log.d(TAG, mCamera.getParameters().getSupportedFlashModes().toString());
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFlashMode(mode);
            mCamera.setParameters(parameters);
            Log.d(TAG, "flash mode = " + mCamera.getParameters().getFlashMode());
        }
    }

    /**
     * Change the flash mode.
     * <p>
     * 0: auto
     * 1: off
     * 2: on
     */
    private void changeFlashMode() {
        switch (sFlashMode) {
            case 0:
                mFlashButton.setImageResource(
                    R.RESOURCES.getIdentifier(R.IC_FLASH_AUTO, R.DRAWABLE, R.PACKAGE_NAME)
                );
                sFlashMode = 1;
                refreshParams(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case 1:
                mFlashButton.setImageResource(
                    R.RESOURCES.getIdentifier(R.IC_FLASH_OFF, R.DRAWABLE, R.PACKAGE_NAME)
                );
                sFlashMode = 2;
                refreshParams(Camera.Parameters.FLASH_MODE_OFF);
                break;
            case 2:
                mFlashButton.setImageResource(
                    R.RESOURCES.getIdentifier(R.IC_FLASH_ON, R.DRAWABLE, R.PACKAGE_NAME)
                );
                sFlashMode = 0;
                refreshParams(Camera.Parameters.FLASH_MODE_ON);
                break;
            default:
                break;
        }
    }

    /**
     * Release the camera if it not equal to null.
     */
    public void releaseCamera() {
        try {
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.setErrorCallback(null);
                mCamera.stopPreview();
                mCamera.release();
                mCamera = null;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
            mCamera = null;
        }
    }

    /**
     * Take a photo and return/save it.
     * <p>
     * Forwarding: image as Base64
     * other options: to be saved in the file system as JPG.
     */
    private void takeImage() {
        if (mCamera != null) {
            try {
                mCaptureImage.setVisibility(View.GONE);
                mFlashButton.setVisibility(View.GONE);
                mFlipCamera.setVisibility(View.GONE);
                mCamera.takePicture(new Camera.ShutterCallback() {
                    @Override
                    public void onShutter() {
                        // to enable shutter sound
                    }
                }, null, new Camera.PictureCallback() {

                    private File imageFile;

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                            // convert byte array into bitmap
                            R.sBase64Image = Base64.encodeToString(data, Base64.DEFAULT);
                            if (mWithPreview) {
                                R.sBitmap = BitmapFactory
                                    .decodeByteArray(data, 0, data.length);
                                Intent intent = new Intent(mContext, PreviewActivity.class);
                                ((Activity) mContext)
                                    .startActivityForResult(intent, R.IMAGE_PREVIEW_REQUEST);
                            } else {
                                if (R.sBase64Image != null && !R.sBase64Image.isEmpty()) {
                                    Log.d(TAG, R.sBase64Image);
                                    R.sCallBackContext.success(R.sBase64Image);
                                } else {
                                    R.sCallBackContext
                                        .error("Error: the base64 image is empty or null");
                                }
                                ((Activity) mContext).finish();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error: ", e);
                        }
                    }
                });
            } catch (RuntimeException e) {
                Log.e(TAG, "Error while taking picture.. ", e);
            }
        }
    }

    /**
     * Set the camera display orientation.
     *
     * @param activity - the current activity
     * @param cameraId - the id of the camera - back or front camera
     */
    private void setCameraDisplayOrientation(Activity activity, int cameraId) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
            .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                break;
        }

        int result;
        Camera.CameraInfo info = mCameraInfoHelper.getCameraInfo(cameraId);
        if (mCameraInfoHelper.isFrontCameraOn(cameraId)) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mRotation = result;
        mCamera.setDisplayOrientation(result);
    }

    /**
     * Change the orientation of the picture that will be shot by the camera.
     *
     * @param orientation - the current orientation
     */
    public void onOrientationChanged(int orientation) {
        if (orientation == ORIENTATION_UNKNOWN) {
            return;
        }
        Camera.CameraInfo info =
            new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, info);

        int newOrientation = (orientation + 45) / 90 * 90;

        int rotation;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation - newOrientation + 360) % 360;
        } else {
            rotation = (info.orientation + newOrientation) % 360;
        }
        try {
            mParams.setRotation(rotation);
            mCamera.setParameters(mParams);
        } catch (RuntimeException e) {
            Log.e(TAG, "Error while setting the rotation params for the camera: ", e);
        }
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        mEventBus.post(new OnAutoFocus(success));
        if (success) {
            camera.cancelAutoFocus();
        }
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void setWithPreview(Boolean withPreview) {
        this.mWithPreview = withPreview;
    }

    public void setFlashButton(ImageButton flashButton) {
        this.mFlashButton = flashButton;
        this.mFlashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFlashMode();
            }
        });
    }

    public void setCaptureImage(ImageButton captureImage) {
        this.mCaptureImage = captureImage;
        this.mCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
            }
        });
    }

    public void setFlipCamera(final ImageButton flipCamera) {
        this.mFlipCamera = flipCamera;
        this.mFlipCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseCamera();
                //mCamera.release();
                if (mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    flipCamera.setImageResource(
                        R.RESOURCES.getIdentifier(R.IC_CAMERA_FRONT, R.DRAWABLE, R.PACKAGE_NAME)
                    );
                    mFlashButton.clearAnimation();
                    mFlashButton.setVisibility(View.GONE);
                    mFlashButton.setEnabled(false);
                } else {
                    mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
                    flipCamera.setImageResource(
                        R.RESOURCES.getIdentifier(R.IC_CAMERA_BACK, R.DRAWABLE, R.PACKAGE_NAME)
                    );
                    mFlashButton.setEnabled(true);
                    mFlashButton.setVisibility(View.VISIBLE);
                }
                if (!openCamera(mCameraID)) {
                    //alertCameraDialog ();
                    Log.d(TAG, "On surface created : camera could not be opened");
                } else {
                    Log.d(TAG, " camera opened");
                }
            }
        });
    }
}
