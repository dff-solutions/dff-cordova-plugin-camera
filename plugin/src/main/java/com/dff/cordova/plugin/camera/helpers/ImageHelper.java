package com.dff.cordova.plugin.camera.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.util.Base64;
import android.util.Size;

import com.dff.cordova.plugin.camera.res.R;
import com.dff.cordova.plugin.camera.log.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Provides methods to store an image.
 */
@Singleton
public class ImageHelper {
    private static final String TAG = "ImageHelper";
    
    public Bitmap sBitmap;
    private byte[] bytes;
    private Log log;
    private R r;
    
    @Inject
    public ImageHelper(Log log, R r) {
        this.log = log;
        this.r = r;
    }
    
    /**
     * stores the image as sBase64 String in r.
     *
     * @param image given image by the imageReader.
     */
    public void storeImage(Image image) {
        log.d(TAG, "storeImage");
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        sBitmap = BitmapFactory
            .decodeByteArray(bytes, 0, bytes.length);
        
        //rotateBitMap();
        r.sBase64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    
    /**
     * Saves the image into storage.
     *
     * @throws IOException Exception for parsing JSON
     */
    public void saveImage() throws IOException {
        log.d(TAG, "saveImage");
        
        final File file = new File("/storage/emulated/0/DCIM/Camera" +
                                       "/pic" + new Date().getTime() + ".jpg");
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(bytes);
        } finally {
            if (null != output) {
                log.d(TAG, "close outputStream");
                output.close();
            }
        }
    }
    
    /**
     * Returns the optimal size for the image.
     *
     * @param characteristics properties describing a CameraDevice
     * @return optimal image size
     */
    public Size getOptimalImageSize(CameraCharacteristics characteristics) {
        Size[] jpegSizes =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                .getOutputSizes(ImageFormat.JPEG);
        StreamConfigurationMap streamConfigurationMap =
            characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
    
        Size previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
        
        Size optimalSize = previewSize;
        for (Size size : jpegSizes) {
            if ((size.getHeight() % previewSize.getHeight()) == 0 && (
                size.getWidth() % previewSize.getWidth()) == 0 &&
                size.getHeight() > optimalSize.getHeight() &&
                size.getHeight() <= 1080
            ) {
                optimalSize = size;
            }
        }
        log.d(TAG, "width: " + optimalSize.getWidth());
        log.d(TAG, "height: " + optimalSize.getHeight());
        
        return optimalSize;
    }
}
