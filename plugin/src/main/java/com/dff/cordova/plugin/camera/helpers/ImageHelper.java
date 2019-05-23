package com.dff.cordova.plugin.camera.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.util.Base64;

import com.dff.cordova.plugin.camera.res.R;
import com.dff.cordova.plugin.camera.log.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    
    public void storeImage(Image image) {
        log.d(TAG, "storeImage");
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        sBitmap = BitmapFactory
            .decodeByteArray(bytes, 0, bytes.length);
        
        rotateBitMap();
        r.sBase64Image = Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    
    public void saveImage() throws IOException {
        log.d(TAG, "saveImage");
        /*
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
        */
    }
    
    public void rotateBitMap() {
        log.d(TAG, "rotateBitMap");
        // rotate Image
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(sBitmap, 0,
                                                   0, sBitmap.getWidth(), sBitmap.getHeight(),
                                                   rotateMatrix, false);
        sBitmap = rotatedBitmap;
        
    }
}
