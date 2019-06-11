package com.dff.cordova.plugin.camera.helpers;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.os.Environment;
import android.util.Base64;
import android.util.Size;

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
    private final String imageName = "pic";
    private final int sizeLimit = 1080;
    
    private byte[] bytes;
    private Log log;
    private String base64Image;
    
    @Inject
    public ImageHelper(Log log) {
        this.log = log;
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
        
        base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    
    /**
     * Saves the image into storage.
     *
     * @throws IOException Exception for parsing JSON
     */
    public void saveImage() throws IOException {
        log.d(TAG, "saveImage");
        
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String name = imageName + new Date().getTime() + ".jpg";
        final File file = new File(path, name);
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
     * The optimal size is limited to 1080 pixel height.
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
        
        /*
        Checks if a size is modulo to the preview size and smaller then the given sizeLimit
         */
        for (Size size : jpegSizes) {
            if ((size.getHeight() % previewSize.getHeight()) == 0 && (
                size.getWidth() % previewSize.getWidth()) == 0 &&
                size.getHeight() > optimalSize.getHeight() &&
                size.getHeight() <= sizeLimit
            ) {
                optimalSize = size;
                log.d(TAG, "set size: " + optimalSize);
            }
        }
        
        return optimalSize;
    }
    
    public String getBase64Image() {
        return base64Image;
    }
    
    public byte[] getBytes() {
        byte[] copy = bytes.clone();
        return copy;
    }
}
