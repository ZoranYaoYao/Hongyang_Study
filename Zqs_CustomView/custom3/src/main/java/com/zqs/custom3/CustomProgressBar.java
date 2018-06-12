package com.zqs.custom3;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zqs on 2018-6-12.
 */

public class CustomProgressBar extends View{
    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private int mSpeed;

    private Paint mPaint;
    private int mProgress;
    private boolean isNext = false;


    public CustomProgressBar(Context context) {
        this(context,null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i< n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgressBar_firstColor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomProgressBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomProgressBar_speed:
                    mSpeed = a.getInt(attr,20);
                    break;
                case R.styleable.CustomProgressBar_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
            }
        }

        a.recycle();
        mPaint = new Paint();
        new Thread() {
            @Override
            public void run() {
              while (true) {
                  mProgress++;
                  if (mProgress == 360) {
                      mProgress =0;
                      if (!isNext) {
                          isNext = true;
                      } else {
                          isNext = false;
                      }
                  }
                  postInvalidate();
                  try {
                      Thread.sleep(mSpeed);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
              }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centre = getWidth() /2 ;
        int radius = centre - mCircleWidth /2; //内圆半径
        mPaint.setStrokeWidth(mCircleWidth); //core 设置圆环宽度
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        RectF oval = new RectF(centre - radius,centre-radius,centre+radius,centre+radius); //core 用于定义圆弧的形状和大小的界限
        if (!isNext) {
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(centre,centre,radius,mPaint); //core 画出圆环,半径:60 -15 = 45,笔宽度:30,向2边扩展15DP ,所以看着像一半
            mPaint.setColor(mSecondColor);
            canvas.drawArc(oval,-90,mProgress,false,mPaint); //core 根据进度画圆弧
        } else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(centre,centre,radius,mPaint);
            mPaint.setColor(mFirstColor);
            canvas.drawArc(oval,-90,mProgress,false,mPaint); //core
        }
    }

























}
