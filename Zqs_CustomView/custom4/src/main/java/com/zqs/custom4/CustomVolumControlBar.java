package com.zqs.custom4;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by zqs on 2018-6-12.
 */

public class CustomVolumControlBar extends View {
    private int mFirstColor;
    private int mSecondColor;
    private int mCircleWidth;
    private Paint mPaint;
    private int mCurrentCount = 3;
    private Bitmap mImage;
    private int mCount;
    private Rect mRect;
    private int mSplitSize;


    public CustomVolumControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumControlBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumControlBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumControlBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CustomVolumControlBar_firstColor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomVolumControlBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.CYAN);
                    break;
                case R.styleable.CustomVolumControlBar_bg:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CustomVolumControlBar_circleWidth:
                    mCircleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomVolumControlBar_dotCount:
                    mCount = a.getInt(attr, 20);
                    break;
                case R.styleable.CustomVolumControlBar_splitSize:
                    mSplitSize = a.getInt(attr, 20);
                    break;
            }
        }

        a.recycle();
        mPaint = new Paint();
        mRect = new Rect();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //        super.onDraw(canvas);

        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);   //core!! 定义线段连接形状为圆头
        mPaint.setStyle(Paint.Style.STROKE);  //设置空心
        int centre = getWidth() / 2;
        int radius = centre - mCircleWidth / 2;  //半径
        drawOval(canvas, centre, radius);

        int relRadius = radius - mCircleWidth / 2;  //core 获得内圆半径
        mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        mRect.bottom = (int) (mRect.left + Math.sqrt(2) * relRadius);
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);

        //如果图片比较小，那么根据图片的尺寸放置到正中心
        if (mImage.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);
            mRect.bottom = (int) (mRect.left + mImage.getWidth());
            mRect.right = (int) (mRect.top + mImage.getHeight());
        }

        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    //画小块块, oval椭圆 arc-圆弧
    private void drawOval(Canvas canvas, int centre, int radius) {
        float itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;
        RectF oval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
        mPaint.setColor(mFirstColor);
        for (int i = 0; i < mCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint); //core 画小椭圆块
        }

        mPaint.setColor(mSecondColor);
        for (int i = 0; i < mCurrentCount; i++) {
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false, mPaint);
        }
    }

    int xDowm;
    int xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDowm = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDowm) {
                    down();
                } else {
                    up();
                }
                break;
        }
        return true;
    }

    private void up() {
        if(mCurrentCount >= mCount) return; //保护

        mCurrentCount++;
        postInvalidate();
    }

    private void down() {
        if (mCurrentCount ==0) return; //保护
        mCurrentCount--;
        postInvalidate();
    }
}
