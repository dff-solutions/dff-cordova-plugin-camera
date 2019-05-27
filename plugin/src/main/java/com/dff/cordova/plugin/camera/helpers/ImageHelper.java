package com.dff.cordova.plugin.camera.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.util.Base64;

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
}
