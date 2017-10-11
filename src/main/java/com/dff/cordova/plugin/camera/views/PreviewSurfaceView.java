package com.dff.cordova.plugin.camera.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;

/**
 * Class that implements a custom surfaceview
 *
 * @author Anthony Nahas
 * @version 3.0.2
 * @since 2.2.2017
 */
public class PreviewSurfaceView extends SurfaceView {

    private CameraPreview mCamPreview;
    private boolean isListening = false;
    private DrawingView mDrawingView;
    private boolean isInDrawingViewSet = false;

    /**
     * Custom custructor
     *
     * @param context - the used context
     * @param attrs   -  the used attrubutset
     */
    public PreviewSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec),
            MeasureSpec.getSize(heightMeasureSpec));
    }

    /**
     * Handle the touch event to draw a rect focus.
     *
     * @param event - the target event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isListening) {
            return false;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();

            Rect touchRect = new Rect(
                (int) (x - 100),
                (int) (y - 100),
                (int) (x + 100),
                (int) (y + 100));

            final Rect targetFocusRect = new Rect(
                touchRect.left * 2000 / this.getWidth() - 1000,
                touchRect.top * 2000 / this.getHeight() - 1000,
                touchRect.right * 2000 / this.getWidth() - 1000,
                touchRect.bottom * 2000 / this.getHeight() - 1000);

            mCamPreview.doTouchFocus(targetFocusRect);
            if (isInDrawingViewSet) {
                mDrawingView.setHaveTouch(true, touchRect);
                mDrawingView.invalidate();

                // Remove the square after some time
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mDrawingView.getPaint().setColor(Color.GREEN);
                        mDrawingView.invalidate();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mDrawingView.getPaint().setColor(Color.YELLOW);
                                mDrawingView.setHaveTouch(false, null);
                                mDrawingView.invalidate();
                            }
                        }, 1000);
                    }
                }, 1000);
            }
        }
        return false;
    }

    /**
     * set CameraPreview instance for touch focus.
     *
     * @param camPreview - CameraPreview
     */
    public void setListener(CameraPreview camPreview) {
        this.mCamPreview = camPreview;
        isListening = true;
    }

    /**
     * set DrawingView instance for touch focus indication.
     *
     * @param drawingView
     */
    public void setDrawingView(DrawingView drawingView) {
        mDrawingView = drawingView;
        isInDrawingViewSet = true;
    }


}