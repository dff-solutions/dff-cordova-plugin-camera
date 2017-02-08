package com.dff.cordova.plugin.camera.views;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import com.dff.cordova.plugin.camera.R.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.OrientationEventListener.ORIENTATION_UNKNOWN;


/**
 * Class to preview the camera.
 *
 * @author Anthony Nahas
 * @version 1.0.0
 * @since 2.2.2017
 */
public class CameraPreview implements SurfaceHolder.Callback {

    private final static String TAG = "CameraPreview";
    private static int sFlashMode;
    private static boolean sSaveInGallery;
    private Camera mCamera = null;
    private Camera.Parameters mParams;
    private SurfaceHolder mSurfaceHolder;
    private ImageButton mFlashButton;
    private ImageButton mCaptureImage;
    private ImageButton mFlipCamera;

    private int mRotation;
    private int mCameraID;
    private Context mContext;


    /**
     * Custom contructor
     *
     * @param context
     * @param flashButton
     * @param captureImage
     * @param flipCamera
     */
    public CameraPreview(Context context,
                         ImageButton flashButton, ImageButton captureImage, ImageButton flipCamera) {

        mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        mContext = context;
        mFlashButton = flashButton;
        mCaptureImage = captureImage;
        mFlipCamera = flipCamera;

        sFlashMode = 0;
        sSaveInGallery = false;


        mCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
                //Toast.makeText(mContext, "took photo", Toast.LENGTH_LONG).show();
            }
        });
        mFlashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFlashMode();
            }
        });
        mFlipCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //releaseCamera();
                mCamera.release();
                if (mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    mFlipCamera.setImageResource(R.RESOURCES.getIdentifier(R.IC_CAMERA_FRONT, R.DRAWABLE, R.PACKAGE_NAME));
                    mFlashButton.setEnabled(false);
                    mFlashButton.setVisibility(View.GONE);
                } else {
                    mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
                    mFlipCamera.setImageResource(R.RESOURCES.getIdentifier(R.IC_CAMERA_BACK, R.DRAWABLE, R.PACKAGE_NAME));
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


    /**
     * Try to open the camera with params.
     *
     * @param id - whether front or back camera.
     * @return
     */
    private boolean openCamera(int id) {
        boolean result = false;
        mCameraID = id;
        releaseCamera(); //imp
        try {
            mCamera = Camera.open(mCameraID);
        } catch (Exception e) {
            Log.e(TAG, "Error while opeing the camera", e);
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
                e.printStackTrace();
                Log.e(TAG, "Error: ", e);
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    /**
     * Setup the caemra with the correct params and rotation.
     */
    private void setUpCamera() {
        try {

            mParams = mCamera.getParameters();

            //showFlashButton(mParams);

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
     * @param surfaceHolder
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        try {
            setCameraDisplayOrientation(((Activity) mContext), mCameraID);
            mCamera.setPreviewDisplay(surfaceHolder);
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
     * @param - Rect - new area for auto focus
     */
    public void doTouchFocus(final Rect tfocusRect) {
        Log.i(TAG, "TouchFocus");
        try {
            final List<Camera.Area> focusList = new ArrayList<Camera.Area>();
            Camera.Area focusArea = new Camera.Area(tfocusRect, 1000);
            focusList.add(focusArea);

            Camera.Parameters para = mCamera.getParameters();
            para.setFocusAreas(focusList);
            para.setMeteringAreas(focusList);
            mCamera.setParameters(para);

            mCamera.autoFocus(myAutoFocusCallback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Unable to autofocus");
        }

    }

    /**
     * AutoFocus callback
     */
    AutoFocusCallback myAutoFocusCallback = new AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
            if (arg0) {
                mCamera.cancelAutoFocus();
            }
        }
    };

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
            //parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
            parameters.setFlashMode(mode);
            mCamera.setParameters(parameters);
            Log.d(TAG, "flash mode = " + mCamera.getParameters().getFlashMode());
        }
    }


    /**
     * Change the flash mode:
     * <p>
     * 0: auto
     * 1: off
     * 2: on
     */
    private void changeFlashMode() {
        switch (sFlashMode) {
            case 0:
                mFlashButton.setImageResource(R.RESOURCES.getIdentifier(R.IC_FLASH_AUTO, R.DRAWABLE, R.PACKAGE_NAME));
                sFlashMode = 1;
                refreshParams(Camera.Parameters.FLASH_MODE_AUTO);
                break;
            case 1:
                mFlashButton.setImageResource(R.RESOURCES.getIdentifier(R.IC_FLASH_OFF, R.DRAWABLE, R.PACKAGE_NAME));
                sFlashMode = 2;
                refreshParams(Camera.Parameters.FLASH_MODE_OFF);
                break;
            case 2:
                mFlashButton.setImageResource(R.RESOURCES.getIdentifier(R.IC_FLASH_ON, R.DRAWABLE, R.PACKAGE_NAME));
                sFlashMode = 0;
                refreshParams(Camera.Parameters.FLASH_MODE_ON);
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
     * Return: Base64
     * other options: to be saved in the file system as JPG.
     */
    private void takeImage() {
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
                    if (!sSaveInGallery) {
                        Log.d(TAG, "data in base64");
                        String base64Image = Base64.encodeToString(data, Base64.DEFAULT);
                        Log.d(TAG, base64Image);
                        R.sCallBackContext.success(base64Image);
                        ((Activity) mContext).finish();
                    } else {
                        Bitmap loadedImage = BitmapFactory.decodeByteArray(data, 0,
                            data.length);

                        // rotate Image
                        Matrix rotateMatrix = new Matrix();
                        rotateMatrix.postRotate(mRotation);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(loadedImage, 0,
                            0, loadedImage.getWidth(), loadedImage.getHeight(),
                            rotateMatrix, false);
                        String state = Environment.getExternalStorageState();
                        File folder = null;
                        if (state.contains(Environment.MEDIA_MOUNTED)) {
                            folder = new File(Environment
                                .getExternalStorageDirectory() + "/Demo");
                        } else {
                            folder = new File(Environment
                                .getExternalStorageDirectory() + "/Demo");
                        }

                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdirs();
                        }
                        if (success) {
                            Date date = new Date();
                            imageFile = new File(folder.getAbsolutePath()
                                + File.separator
                                //+ new Timestamp(date.getTime()).toString()
                                + new Date()
                                + "Image.jpg");

                            Boolean resOfCreatingImage = imageFile.createNewFile();
                            Log.d(TAG, "Result of creating an new image = " + resOfCreatingImage);
                        } else {
                            return;
                        }

                        ByteArrayOutputStream ostream = new ByteArrayOutputStream();

                        // save image into gallery
                        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

                        FileOutputStream fout = new FileOutputStream(imageFile);
                        fout.write(ostream.toByteArray());
                        fout.close();
                        ContentValues values = new ContentValues();

                        values.put(MediaStore.Images.Media.DATE_TAKEN,
                            System.currentTimeMillis());
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        values.put(MediaStore.MediaColumns.DATA,
                            imageFile.getAbsolutePath());

                        mContext.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error: ", e);
                }

            }
        });
    }

    /**
     * set the camera display orientation
     *
     * @param activity - the current activity
     * @param cameraId - the id of the camera - back or front camera
     */
    private void setCameraDisplayOrientation(Activity activity, int cameraId) {
        android.hardware.Camera.CameraInfo info =
            new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
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
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mRotation = result;
        mCamera.setDisplayOrientation(result);
    }


    /**
     * Change the orientation of the picture that will be shot by the camera
     *
     * @param orientation - the current orientation
     */
    public void onOrientationChanged(int orientation) {
        if (orientation == ORIENTATION_UNKNOWN) return;
        Camera.CameraInfo info =
            new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraID, info);
        orientation = (orientation + 45) / 90 * 90;
        int rotation = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            rotation = (info.orientation - orientation + 360) % 360;
        } else {  // back-facing camera
            rotation = (info.orientation + orientation) % 360;
        }
        mParams.setRotation(rotation);
        mCamera.setParameters(mParams);
    }

    /**
     * Print all params of the camera.
     * This method is implemented for test and debug purposes
     */
    private void printParameters() {
        String space = " ";
        Log.d(TAG, "Camera params: "
            + "Antibanding: "
            + mParams.getAntibanding()
            + space
            + "ExposureCompensation "
            + mParams.getExposureCompensation()
            + space
            + "MinExposureCompensation "
            + mParams.getMinExposureCompensation()
            + space
            + "MaxExposureCompensation "
            + mParams.getMaxExposureCompensation()
            + space
            + "MaxNumFocusAreas "
            + mParams.getMaxNumFocusAreas()
            + space
            + "MaxNumMeteringAreas "
            + mParams.getMaxNumMeteringAreas()
            + space
            //+ "MeteringAreas "
            //+ mParams.getMeteringAreas()
            + "FocusAreas "
            + mParams.getFocusAreas()
            + space
            + "FocalLength "
            + mParams.getFocalLength()
            + space
            + "MaxZoom "
            + mParams.getMaxZoom()
            + space
            + "PictureFormat"
            + mParams.getPictureFormat()
            + space
            + "PreviewFormat "
            + mParams.getPreviewFormat()
            + space
            + "MaxZoom "
            + mParams.getMaxZoom()
            + space
            + "FlashMode "
            + mParams.getFlashMode()
            + space
            + "ZoomRatios "
            + mParams.getZoomRatios()
            + space
            + "WhiteBalance "
            + mParams.getWhiteBalance()
            + space
            + "SceneMode "
            + mParams.getSceneMode()
            + space
            + "AutoExposureLock "
            + mParams.getAutoExposureLock()
            + space
            + "ExposureCompensationStep "
            + mParams.getExposureCompensationStep()
            + space
            + "HorizontalViewAngle "
            + mParams.getHorizontalViewAngle()
            + space
            + "VerticalViewAngle "
            + mParams.getVerticalViewAngle()
            + space
            + "rotation "
            + mRotation);

    }

}