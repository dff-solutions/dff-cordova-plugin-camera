package com.dff.cordova.plugin.camera.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Size;
import android.view.Display;
import android.view.Surface;
import android.view.TextureView;

import com.dff.cordova.plugin.camera.actions.TakePhoto;
import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.dagger.annotations.PreviewActivityIntent;
import com.dff.cordova.plugin.camera.helpers.CameraButtonHelper;
import com.dff.cordova.plugin.camera.helpers.CallbackContextHelper;
import com.dff.cordova.plugin.camera.helpers.CameraHelper;
import com.dff.cordova.plugin.camera.helpers.ImageHelper;
import com.dff.cordova.plugin.camera.helpers.PermissionHelper;
import com.dff.cordova.plugin.camera.listeners.AvailableImageListener;
import com.dff.cordova.plugin.camera.listeners.OrientationListener;
import com.dff.cordova.plugin.camera.listeners.SurfaceListener;
import com.dff.cordova.plugin.camera.listeners.callback.CameraCaptureStateCallback;
import com.dff.cordova.plugin.camera.listeners.callback.CameraPreviewStateCallback;
import com.dff.cordova.plugin.camera.listeners.callback.CameraStateCallback;
import com.dff.cordova.plugin.camera.log.Log;
import com.dff.cordova.plugin.camera.res.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

/**
 * Activity to start the camera.
 *
 * @see <a href="https://developer.android.com/reference/android/hardware/camera2/package-summary.html"
 * >https://developer.android.com/reference/android/hardware/camera2/package-summary.html</a>
 */
public class CameraActivity extends Activity {
    private static final String TAG = "CameraActivity";
    
    public static final String CAMERA_ACTIVITY_LAYOUT = "cameraplugin_activity_camera";
    public static final String TEXTURE_VIEW_ID = "texture";
    
    @Inject
    SurfaceListener surfaceListener;
    
    @Inject
    CameraStateCallback cameraStateCallback;
    
    @Inject
    PermissionHelper permissionHelper;
    
    @Inject
    Log log;
    
    @Inject
    R r;
    
    @Inject
    OrientationListener orientationEventListener;
    
    @Inject
    CameraPreviewStateCallback cameraPreviewStateCallback;
    
    @Inject
    AvailableImageListener availableImageListener;
    
    @Inject
    CameraCaptureStateCallback cameraCaptureStateCallback;
    
    @Inject
    CameraButtonHelper buttonHelper;
    
    @Inject
    ImageHelper imageHelper;
    
    @Inject
    CallbackContextHelper contextHelper;
    
    @Inject
    Handler backgroundHandler;
    
    @Inject
    PackageManager packageManager;
    
    @Inject
    @PreviewActivityIntent
    Intent previewActivityIntent;
    
    @Inject
    CameraManager cameraManager;
    
    @Inject
    CameraHelper cameraHelper;
    
    private CameraDevice cameraDevice;
    private final Object cameraLock = new Object();
    private TextureView textureView;
    private Size previewSize;
    public CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequest;
    private CameraCharacteristics characteristics = null;
    private ImageReader reader;
    public String cameraId = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DaggerManager
            .getInstance()
            .in(this)
            .inject(this);
    
        boolean hasCamera = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
        if (!hasCamera) {
            log.e(TAG, "device has no camera");
            contextHelper.sendAllError("device has no camera");
            setResult(R.RESULT_CANCELED);
            finish();
        }
        
        log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        
        setContentView(r.getLayoutIdentifier(CAMERA_ACTIVITY_LAYOUT));
        cameraHelper.initCameraId();
        
        textureView = findViewById(r.getIdIdentifier(TEXTURE_VIEW_ID));
        textureView.setSurfaceTextureListener(surfaceListener);
        
        buttonHelper.initButtons();
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
    @Override
    protected void onResume() {
        log.d(TAG, "onResume");
        super.onResume();
        orientationEventListener.enable();
        
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceListener);
        }
    }
    
    @Override
    protected void onPause() {
        log.d(TAG, "onPause");
        closeCamera();
        orientationEventListener.disable();
        super.onPause();
    }
    
    /**
     * This methods opens the camera on the camera Manager.
     */
    @SuppressLint("MissingPermission")
    public void openCamera() {
        log.d(TAG, "opening camera with id " + cameraId);
        buttonHelper.enableAllButtons(true);
        try {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            
            previewSize = new Size(size.y, size.x);
            log.d(TAG, "previewSize " + previewSize.toString());
            cameraManager.openCamera(cameraId, cameraStateCallback, null);
        } catch (CameraAccessException e) {
            log.e(TAG, "error while opening camera");
            contextHelper.sendAllException(e);
        }
    }
    
    /**
     * This method start the camera preview.
     */
    public void startCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            
            captureRequest = getCameraDevice()
                .createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            
            Surface surface = new Surface(texture);
            captureRequest.addTarget(surface);
            
            log.d(TAG, "create CaptureSession");
            getCameraDevice().createCaptureSession(
                Arrays.asList(surface),
                cameraPreviewStateCallback,
                null
            );
            
            this.orientationEventListener.enable();
        } catch (CameraAccessException e) {
            log.e(TAG, "error while creating caputureRequest");
            contextHelper.sendAllException(e);
        }
    }
    
    /**
     * This method closes the cameraDevice.
     */
    public void closeCamera() {
        synchronized (cameraLock) {
            if (getCameraDevice() != null) {
                log.d(TAG, "closing camera");
                getCameraDevice().close();
                setCameraDevice(null);
            }
        }
    }
    
    /**
     * This method updates the Preview. Changes on the setting affect after the update.
     */
    public void updatePreview() {
        log.d(TAG, "updatePreview");
        synchronized (cameraLock) {
            if (getCameraDevice() == null) {
                log.e(TAG, "no cameraDevice / cameraDevice already closed");
                return;
            }
            captureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            try {
                cameraCaptureSession.setRepeatingRequest(
                    captureRequest.build(),
                    null,
                    null
                );
            } catch (CameraAccessException e) {
                log.e(TAG, "error while setting repeating request", e);
                contextHelper.sendAllException(e);
            }
        }
    }
    
    /**
     * Method to take a picture with the current cameraDevice.
     * Prepares the captureSession by setting size of the image, orientation and initializing the
     * imageReader and captureRequest.
     * The image format is jpg.
     */
    public void takePicture() {
        if (getCameraDevice() == null) {
            log.e(TAG, "no cameraDevice");
            return;
        }
        log.d(TAG, "take picture");
        try {
            //avoid error due to double clicks
            buttonHelper.enableAllButtons(false);
            
            Size optimalSize;
            if (cameraHelper.getSupportedHardwareLevel() >
                CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED) {
                
                characteristics = cameraManager
                    .getCameraCharacteristics(cameraId);
                
                optimalSize = imageHelper.getOptimalImageSize(characteristics);
                log.d(TAG, "optimal Image Size = " + optimalSize.toString());
            } else {
                optimalSize = previewSize;
            }
            
            reader = ImageReader.newInstance(
                optimalSize.getWidth(),
                optimalSize.getHeight(),
                ImageFormat.JPEG,
                1
            );
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            
            final CaptureRequest.Builder captureBuilder =
                getCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            
            int screenRotation = orientationEventListener.getImageRotation();
            int sensorOrientation = 0;
            if (characteristics != null) {
                sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            }
            int jpegOrientation = (screenRotation + sensorOrientation + 270) % 360;
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, jpegOrientation);
            
            reader.setOnImageAvailableListener(availableImageListener, backgroundHandler);
            
            cameraCaptureStateCallback.handler = backgroundHandler;
            cameraCaptureStateCallback.captureBuilder = captureBuilder;
            
            getCameraDevice().createCaptureSession(
                outputSurfaces,
                cameraCaptureStateCallback,
                backgroundHandler
            );
        } catch (CameraAccessException e) {
            log.e(TAG, "error while accessing camera", e);
            contextHelper.sendAllException(e);
        }
    }
    
    /**
     * When the picture is captured, this method starts the previewActivity.
     * If the flag withPreview isn't set, the activity will return the image without preview.
     */
    public void startPreviewActivity() {
        log.d(TAG, "close ImageReader");
        reader.close();
        
        if (this.getIntent().getBooleanExtra(TakePhoto.JSON_ARG_WITH_PREVIEW, false)) {
            startActivityForResult(previewActivityIntent, R.RESULT_OK);
        } else {
            log.d(TAG, "show no PreviewActivity");
            returnImage();
            finish();
        }
    }
    
    public void changeFlashMode() {
        buttonHelper.changeFlashButton(captureRequest);
        updatePreview();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != resultCode) {
            log.d(TAG, "onActivityResult: not expected result");
        }
        switch (resultCode) {
            case R.RESULT_OK:
                log.d(TAG, "onActivityResult: set result = OK");
                setResult(R.RESULT_OK, data);
                returnImage();
                closeCamera();
                backgroundHandler.getLooper().quitSafely();
                finish();
                break;
            case R.RESULT_CANCELED:
                log.d(TAG, "onActivityResult: set result = canceled");
                setResult(R.RESULT_CANCELED);
                closeCamera();
                backgroundHandler.getLooper().quitSafely();
                finish();
                break;
            case R.RESULT_REPEAT:
                log.d(TAG, "onActivityResult: set result = repeat");
                break;
            default:
                break;
        }
    }
    
    private void returnImage() {
        if (imageHelper.getBase64Image() != null) {
            contextHelper.sendAllSuccess(imageHelper.getBase64Image());
        } else {
            log.e(TAG, "base64Image is empty");
            contextHelper.sendAllError("unable to return image");
        }
    }
    
    public CameraDevice getCameraDevice() {
            return cameraDevice;
    }
    
    public void setCameraDevice(CameraDevice cameraDevice) {
            this.cameraDevice = cameraDevice;
    }
    
    public Object getCameraLock() {
        return cameraLock;
    }
}
