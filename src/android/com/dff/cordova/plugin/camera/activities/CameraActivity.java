package com.dff.cordova.plugin.camera.activities;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.ImageButton;
import android.widget.Toast;
import com.dff.cordova.plugin.camera.R.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;


/**
 * Created by anahas on 05.01.2017.
 *
 * @author Anthony Nahas
 * @version 1.0.7
 * @since 05.01.2017
 */
public class CameraActivity extends Activity implements SurfaceHolder.Callback {

    private static final String TAG = CameraActivity.class.getSimpleName();

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private ImageButton mCaptureImage;
    private ImageButton mFlashButton;
    private ImageButton mFlipCamera;
    private static int sFlashMode = 0;
    private int mCameraID;
    private int mRotation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.RESOURCES.getIdentifier(R.CAMERA_ACTIVITY_LAYOUT, R.LAYOUT, R.PACKAGE_NAME));

        //on creating the surface view
        mCameraID = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
        mSurfaceView = (SurfaceView) findViewById(R.RESOURCES.getIdentifier(R.CAMERA_SURFACE_ID, R.ID, R.PACKAGE_NAME));
        mCaptureImage = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_TAKE_IMAGE, R.ID, R.PACKAGE_NAME));
        mFlashButton = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_CHANGE_FLASH_MODE, R.ID, R.PACKAGE_NAME));
        mFlipCamera = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_FLIP_CAMERA, R.ID, R.PACKAGE_NAME));

        //mCamera = getCameraInstance();
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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

        //keep the screen on until the activity is running.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        releaseCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (!openCamera(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK)) {
            Log.d(TAG, "On surface created : camera could not be opened");
        } else {
            Log.d(TAG, " camera opened");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(surfaceHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            //stop the preview
            mCamera.stopPreview();
            //release the camera
            mCamera.release();
            //unbind the camera from this object
            mCamera = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean openCamera(int id) {
        boolean result = false;
        mCameraID = id;
        releaseCamera(); //imp
        try {
            mCamera = Camera.open(mCameraID);
        } catch (Exception e) {
            Log.e(TAG, "Error while opening the camera", e);
        }
        if (mCamera != null) {
            try {
                setUpCamera(mCamera);
                mCamera.setErrorCallback(new Camera.ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {
                        R.sCallBackContext.error("Error: cannot open camera. Please make sure that the permission " +
                            "is granted");
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

    private void releaseCamera() {
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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setUpCamera(Camera c) {
        try {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraID, info);
            mRotation = getWindowManager().getDefaultDisplay().getRotation();
            int degree = 0;
            switch (mRotation) {
                case Surface.ROTATION_0:
                    degree = 0;
                    break;
                case Surface.ROTATION_90:
                    degree = 90;
                    break;
                case Surface.ROTATION_180:
                    degree = 180;
                    break;
                case Surface.ROTATION_270:
                    degree = 270;
                    break;

                default:
                    break;
            }

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // frontFacing
                mRotation = (info.orientation + degree) % 360;
                mRotation = (360 - mRotation) % 360;
            } else {
                // Back-facing
                mRotation = (info.orientation - degree + 360) % 360;
            }
            c.setDisplayOrientation(mRotation);
            Camera.Parameters params = c.getParameters();

            //showFlashButton(params);

            List<String> focusModes = params.getSupportedFlashModes();
            if (focusModes != null) {
                if (focusModes
                    .contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                }
            }
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            params.setRotation(mRotation);
            c.setParameters(params);
            c.enableShutterSound(true);
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
    }


    private void takeImage() {
        mCamera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        }, null, new Camera.PictureCallback() {

            private File imageFile;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    if (!R.sSaveInGallery) {
                        Log.d(TAG, "data in base64");
                        String base64Image = Base64.encodeToString(data, Base64.DEFAULT);
                        Log.d(TAG, base64Image);
                        R.sCallBackContext.success(base64Image);
                        finish();
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
                            java.util.Date date = new java.util.Date();
                            imageFile = new File(folder.getAbsolutePath()
                                + File.separator
                                //+ new Timestamp(date.getTime()).toString()
                                + new Date()
                                + "Image.jpg");

                            Boolean resOfCreatingImage = imageFile.createNewFile();
                            Log.d(TAG, "Result of creating an new image = " + resOfCreatingImage);
                        } else {
                            Toast.makeText(getBaseContext(), "Image Not saved",
                                Toast.LENGTH_SHORT).show();
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

                        CameraActivity.this.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error: ", e);
                }
            }
        });
    }


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
}