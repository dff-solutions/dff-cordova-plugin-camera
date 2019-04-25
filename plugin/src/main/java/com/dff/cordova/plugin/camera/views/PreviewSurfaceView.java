package com.dff.cordova.plugin.camera.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import com.dff.cordova.plugin.camera.events.OnAutoFocus;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Class that implements a custom surfaceview
 *
 * @author Anthony Nahas
 * @version 3.0.3
 * @since 2.2.2017
 */
public class PreviewSurfaceView extends SurfaceView {

    private CameraPreview mCamPreview;
    private DrawingView mDrawingView;
    private PicIndicatorView mPicIndicatorView;
    private EventBus mEventBus;
    private boolean isListening = false;
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

    /**
     * Set default event bus in order to subscribe
     *
     * @param mEventBus
     */
    public void setEventBus(EventBus mEventBus) {
        this.mEventBus = mEventBus;
        this.mEventBus.register(this);
    }

    /**
     * Pic indicator view that will be displayed over the surface view
     *
     * @param mPicIndicatorView
     */
    public void setPicIndicatorView(PicIndicatorView mPicIndicatorView) {
        this.mPicIndicatorView = mPicIndicatorView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoFocus(final OnAutoFocus event) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (event.isSuccess()) {
                    mDrawingView.getPaint().setColor(Color.GREEN);
                } else {
                    mDrawingView.getPaint().setColor(Color.RED);
                }
                mDrawingView.invalidate();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawingView.getPaint().setColor(Color.YELLOW);
                        mDrawingView.setHaveTouch(false, null);
                        mDrawingView.invalidate();
                    }
                }, 500);
            }
        }, 1000);
    }
}