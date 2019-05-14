package com.dff.cordova.plugin.camera.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageButton;

import com.dff.cordova.plugin.camera.dagger.DaggerManager;
import com.dff.cordova.plugin.camera.res.R;
import com.dff.cordova.plugin.camera.helpers.PermissionHelper;
import com.dff.cordova.plugin.camera.listeners.SurfaceListener;
import com.dff.cordova.plugin.camera.listeners.callback.CameraStateCallback;
import com.dff.cordova.plugin.camera.log.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class Camera2Activity extends Activity {
    public static final String TAG = "Camera2Activity";
    
    public static final String CAMERA_ACTIVITY_LAYOUT = "activity_camera2";
    public static final String TEXTURE_VIEW_ID = "texture";
    public static final String CAPTURE_BUTTON = "capture_button";
    public static final String FLASH_BUTTON = "flash_button";
    public static final String FLIP_BUTTON = "flip_button";
    
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
    
    public CameraDevice cameraDevice;
    private TextureView textureView;
    private Size previweSize;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequest;
    private ImageButton captureButton;
    private ImageButton flashButton;
    private ImageButton flipButton;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        DaggerManager
            .getInstance()
            .inject(this);
        
        surfaceListener.camera2Activity = this;
        cameraStateCallback.camera2Activity = this;
        
        setContentView(r.getLayoutIdentifier(CAMERA_ACTIVITY_LAYOUT));
    
        captureButton = findViewById(r.getIdIdentifier(CAPTURE_BUTTON));
        flashButton = findViewById(r.getIdIdentifier(FLASH_BUTTON));
        flipButton =  findViewById(r.getIdIdentifier(FLIP_BUTTON));
        
        textureView = findViewById(r.getIdIdentifier(TEXTURE_VIEW_ID));
        textureView.setSurfaceTextureListener(surfaceListener);
        
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        log.d(TAG, "onResume");
        startBackgroundThread();
        
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(surfaceListener);
        }
    }
    
    @Override
    protected void onPause() {
        log.d(TAG, "onPause");
        stopBackgroundThread();
        closeCamera();
        super.onPause();
    }
    
    @SuppressLint("MissingPermission")
    public void openCamera() {
        log.d(TAG, "opening camera");
    
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String cameraId = null;
        
        try {
            cameraId = cameraManager.getCameraIdList()[0];
    
            CameraCharacteristics cameraCharacteristics =
                cameraManager.getCameraCharacteristics(cameraId);
            
            StreamConfigurationMap streamConfigurationMap =
                cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            
            previweSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
            
            cameraManager.openCamera(cameraId, cameraStateCallback, null);
            
        } catch (CameraAccessException e) {
            log.e(TAG, "error while getting cameraId");
        }
        
    }
    
    public void startCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(previweSize.getWidth(), previweSize.getHeight());
            captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface surface = new Surface(texture);
            captureRequest.addTarget(surface);
    
            log.d(TAG, "create CaptureSession");
            cameraDevice.createCaptureSession(Arrays.asList(surface),
                                              new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession captureSession) {
                    if (null == cameraDevice) {
                        return;
                    }
                    
                    cameraCaptureSession = captureSession;
                    updatePreview();
                }
                
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    log.e(TAG, "error while configurating CaptureSession");
                }
            }, null);
        } catch (CameraAccessException e) {
            log.e(TAG, "error while creating caputureRequest");
        }
    }
    
    public void closeCamera() {
        log.d(TAG, "closing camera");
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }
    
    private void updatePreview() {
        if (cameraDevice == null) {
            log.e(TAG, "no CameraDevice in updatePreview()");
        }
        captureRequest.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        try {
            cameraCaptureSession.setRepeatingRequest(captureRequest.build(), null,
                                                     null);
        } catch (CameraAccessException e) {
            log.e(TAG, "error while creating ");
        }
    }
    
    private void takePicture() {
        if (cameraDevice == null) {
            log.d(TAG, "no cameraDevice");
            return;
        }
        log.d(TAG, "take picture");
        
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics =
                manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes = null;
            if (characteristics != null) {
               jpegSizes =
                   characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                                  .getOutputSizes(ImageFormat.JPEG);
            }
            int width = 640;
            int height = 480;
            
            if (jpegSizes != null && 0 < jpegSizes.length) {
                width = jpegSizes[0].getWidth();
                height = jpegSizes[0].getHeight();
            }
    
            ImageReader reader = ImageReader.newInstance(width, height, ImageFormat.JPEG,
                                                         1);
            List<Surface> outputSurfaces = new ArrayList<Surface>(2);
            outputSurfaces.add(reader.getSurface());
            outputSurfaces.add(new Surface(textureView.getSurfaceTexture()));
            
            final CaptureRequest.Builder captureBuilder =
                cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation));
            
            final File file = new File(Environment.getExternalStorageDirectory() +
                                           "/pic" + new Date().getTime() + ".jpg");
            ImageReader.OnImageAvailableListener readerListener =
                new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    Image image = null;
                    try {
                        image = reader.acquireNextImage();
                        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                        byte[] bytes = new byte[buffer.capacity()];
                        buffer.get(bytes);
                        save(bytes);
                    } catch (FileNotFoundException e) {
                        log.e(TAG, "file not found", e);
                    } catch (IOException e) {
                        log.e(TAG, "IO exception", e);
                    } finally {
                        if (image != null) {
                            image.close();
                        }
                    }
                }
                
                private void save(byte[] bytes) throws IOException {
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(file);
                        output.write(bytes);
                    } finally {
                        if (null != output) {
                            output.close();
                        }
                    }
                }
            };
            log.d(TAG, "set OnImageAvailableListener");
            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            
            final CameraCaptureSession.CaptureCallback captureListener =
                new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(@NonNull CameraCaptureSession session,
                                             @NonNull CaptureRequest request,
                                             long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);
                    startCameraPreview();
                }
            };
            cameraDevice.createCaptureSession(outputSurfaces,
                                              new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.capture(captureBuilder.build(), captureListener,
                                        mBackgroundHandler);
                    } catch (CameraAccessException e) {
                        log.e(TAG, "error while accessing camera", e);
                    }
                }
    
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    log.e(TAG, "configuration falled @ createCaptureSession");
                }
            }, mBackgroundHandler);
            
        } catch (CameraAccessException e) {
            log.e(TAG, "error while accessing camera", e);
        }
    }
    
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
    
    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            log.e(TAG, "unablte to stop background thread", e);
        }
    }
}
