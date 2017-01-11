package com.dff.cordova.plugin.camera.activities;

/**
 * Created by anahas on 05.01.2017.
 *
 * @author Anthony Nahas
 * @version 0.4.1
 * @since 05.01.2017
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
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

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.attr.rotation;

//import android.support.v7.app.AppCompatActivity;
//import org.apache.cordova.R;

//import java.security.Timestamp;

public class CameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CameraActivity.class.getSimpleName();

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private ImageButton mCaputeImage;
    private ImageButton mFlashButton;
    private ImageButton mFlipCamera;
    private static int sFlashMode = 0;
    private int mCameraID;
    private int mRotation;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_camera);
        setContentView(R.RESOURCES.getIdentifier(R.CAMERA_ACTIVITY_LAYOUT, R.LAYOUT, R.PACKAGE_NAME));
        mContext = this;

        //on creating the surface view
        mCameraID = android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
        //mSurfaceView = (SurfaceView) findViewById(android.R.id.camera_surface_view);
        mSurfaceView = (SurfaceView) findViewById(R.RESOURCES.getIdentifier(R.CAMERA_SURFACE_ID, R.ID, R.PACKAGE_NAME));
        mCaputeImage = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_TAKE_IMAGE, R.ID, R.PACKAGE_NAME));
        mFlashButton = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_CHANGE_FLASH_MODE, R.ID, R.PACKAGE_NAME));
        mFlipCamera = (ImageButton) findViewById(R.RESOURCES.getIdentifier(R.BUTTON_FLIP_CAMERA, R.ID, R.PACKAGE_NAME));
        //mCaputeImage = (Button) findViewById(R.id.take_image);

        //mCamera = getCameraInstance();
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCaputeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
                //mCamera.takePicture(null, null, mPicture);
                Toast.makeText(mContext, "took photo", Toast.LENGTH_LONG).show();
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
                } else {
                    mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
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
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (!openCamera(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK)) {
            //alertCameraDialog ();
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
            // intentionally left blank for a test
            Log.e(TAG, "Error: ", e);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //mCamera.stopPreview();
        //mCamera.release();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean openCamera(int id) {
        boolean result = false;
        mCameraID = id;
        //releaseCamera();
        try {
            mCamera = Camera.open(mCameraID);
        } catch (Exception e) {
            Log.e(TAG, "Error while opeing the camera", e);
        }
        if (mCamera != null) {
            try {
                setUpCamera(mCamera);
                mCamera.setErrorCallback(new Camera.ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {
                        //to show the error message.
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
                mRotation = (info.orientation + degree) % 330;
                mRotation = (360 - rotation) % 360;
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
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                    //params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                }
            }
            params.setRotation(mRotation);

        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
        }
    }


    private void takeImage() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {

            private File imageFile;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    if (!R.sSaveInGallery) {
                        Log.d(TAG, "data in base64");
                        String base64Image = Base64.encodeToString(data, Base64.DEFAULT);
                        Log.d(TAG, base64Image);
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

    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "Error: ", e);
            // cannot get camera or does not exist
        }
        return camera;
    }

    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.d(TAG, "picture == null");
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                Log.d(TAG, "img written");
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Error: ", e);

            } catch (IOException e) {
                Log.e(TAG, "Error: ", e);
            }
        }
    };

    private static File getOutputMediaFile() {
        File mediaStorageDir = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            mediaStorageDir = new File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCameraApp");
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
            + "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
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
                //
                refreshParams(Camera.Parameters.FLASH_MODE_AUTO);
                sFlashMode = 1;
                break;
            case 1:
                //
                refreshParams(Camera.Parameters.FLASH_MODE_OFF);
                sFlashMode = 2;
                break;
            case 2:
                //
                refreshParams(Camera.Parameters.FLASH_MODE_ON);
                sFlashMode = 0;
                break;
        }
    }

}