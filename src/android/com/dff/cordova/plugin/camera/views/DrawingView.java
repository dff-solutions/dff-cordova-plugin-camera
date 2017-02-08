package com.dff.cordova.plugin.camera.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


/**
 * Class to draw a custom rec that indicate the touch event on the preview surface.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 2.2.2017
 */
public class DrawingView extends View {

    private boolean mTouched = false;
    private Rect mRectArea;
    private Paint mPaint;

    /**
     * Custom custructor
     *
     * @param context - the used context
     * @param attrs   -  the used attrubutset
     */
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(0xeed7d7d7);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);
        mTouched = false;
    }

    /**
     * change the value of "haveTOuch" and store the rectangle object.
     *
     * @param val  - whether the surface is touched
     * @param rect - the rect to draw
     */
    public void setHaveTouch(boolean val, Rect rect) {
        mTouched = val;
        mRectArea = rect;
    }

    /**
     * Draw a rectangle with specific values.
     *
     * @param canvas - the canvas used to draw the rectangle
     */
    @Override
    public void onDraw(Canvas canvas) {
        if (mTouched) {
            canvas.drawRect(
                mRectArea.left, mRectArea.top, mRectArea.right, mRectArea.bottom,
                mPaint);
        }
    }

}