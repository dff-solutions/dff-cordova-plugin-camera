package com.dff.cordova.plugin.camera.listeners.callback;

import android.media.Image;
import android.media.ImageReader;

import com.dff.cordova.plugin.camera.log.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.inject.Inject;

public class AvailableImageListener implements ImageReader.OnImageAvailableListener {
    private final String TAG = "AvailableImageListener";
    private File file;
    private Log log;
    
    @Inject
    public AvailableImageListener(Log log) {
        this.log = log;
    }
    
    @Override
    public void onImageAvailable(ImageReader reader) {
        Image image = null;
        try {
            image = reader.acquireLatestImage();
            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.capacity()];
            buffer.get(bytes);
            save(bytes);
        } catch (FileNotFoundException e) {
            log.e(TAG, "file not found", e);
        } catch (IOException e) {
            log.e(TAG, "IOException", e);
        } finally {
            if (image != null) {
                image.close();
            }
        }
    }
    
    private void save(byte[] bytes) throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(getFile());
            output.write(bytes);
        } finally {
            if (null != output) {
                output.close();
            }
        }
    }
    
    public File getFile() {
        return file;
    }
    
    public void setFile(File file) {
        this.file = file;
    }
}
