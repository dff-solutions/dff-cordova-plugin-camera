package com.dff.cordova.plugin.camera.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;

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
    
    public Bitmap sBitmap;
    private byte[] bytes;
    private Log log;
    
    @Inject
    public ImageHelper(Log log) {
        this.log = log;
    }
    
    public void storeImage(Image image) {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        log.d(TAG, "storeImage");
        sBitmap = BitmapFactory
            .decodeByteArray(bytes, 0, bytes.length);
    }
    
    public void saveImage() throws IOException {
        log.d(TAG, "saveImage");
        final File file = new File(Environment.getExternalStorageDirectory() +
                                       "/pic" + new Date().getTime() + ".jpg");
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
}
