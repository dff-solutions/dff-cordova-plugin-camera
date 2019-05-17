package com.dff.cordova.plugin.camera.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.util.Base64;

import com.dff.cordova.plugin.camera.log.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ImageHelper {
    private static final String TAG = "ImageHelper";
    
    public static Bitmap sBitmap;
    public static String sBase64Image;
    public static byte[] bytes;
    public static Image image;
    private Log log;
    
    @Inject
    public ImageHelper(Log log) {
        this.log = log;
    }
    
    public void storeImage(Image image) {
        ImageHelper.image = image;
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        log.d(TAG, "storeImage");
        ImageHelper.bytes = bytes;
        ImageHelper.sBase64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
        ImageHelper.sBitmap = BitmapFactory
            .decodeByteArray(bytes, 0, bytes.length);
    
        log.d(TAG, "ImageHelper: " + this);
        log.d(TAG, "ImageHelper.sBitmap: " + sBitmap);
    }
    
    public void saveImage() throws IOException {
        log.d(TAG, "saveImage");
        final File file = new File(Environment.getExternalStorageDirectory() +
                                       "/pic" + new Date().getTime() + ".jpg");
        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            output.write(ImageHelper.bytes);
        } finally {
            if (null != output) {
                output.close();
            }
        }
    }
}
