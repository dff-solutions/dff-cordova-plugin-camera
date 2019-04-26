package com.dff.cordova.plugin.camera.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by anahas on 24.10.2017.
 *
 * @author Anthony Nahas
 * @version 1.0
 * @since 24.10.2017
 */

public class PicIndicatorView extends View {

  private static final String TAG = PicIndicatorView.class.getSimpleName();

  private Paint mPaint;

  private static final int AVG = 15;
  private static final int MARGIN = 50;

  private int mPaddingW;
  private int mPaddingH;
  private int mMaxWidth;
  private int mMaxHeight;

  public PicIndicatorView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    mPaint = new Paint();
    mPaint.setColor(Color.YELLOW);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeWidth(10);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    Log.d(TAG, widthMeasureSpec + " | " + heightMeasureSpec);
    // Try for a width based on our minimum
    int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
    int minh = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();

    mMaxWidth = resolveSizeAndState(minw, widthMeasureSpec, 1);
    mMaxHeight = resolveSizeAndState(minh, heightMeasureSpec, 1);

    Log.d(TAG, "w --> " + mMaxWidth);
    Log.d(TAG, "h --> " + mMaxHeight);

    mPaddingH = (int) ((double) AVG / 100 * mMaxHeight);
    mPaddingW = (int) ((double) AVG / 100 * mMaxWidth);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    Log.d(TAG, "padding h -->" + mPaddingH);
    canvas.drawLine(MARGIN, mPaddingH, MARGIN, mMaxHeight - mPaddingH, mPaint);
    canvas.drawLine(MARGIN * 2, mMaxHeight - mPaddingH, mMaxWidth - mPaddingW - MARGIN,
      mMaxHeight - mPaddingH, mPaint);
  }
}
