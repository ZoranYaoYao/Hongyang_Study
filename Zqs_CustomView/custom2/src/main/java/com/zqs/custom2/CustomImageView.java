package com.zqs.custom2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zqs on 2018-6-11.
 */

public class CustomImageView extends View {

    private String mTitle;
    private Rect mTextBound;
    private Paint mPaint;
    private Rect rect;
    private Bitmap mImage;
    private int mImageScale;
    private int mTextColor; // 资源索引
    private int mTextSize;

    private int mWidth;
    private int mHeight;
    private final int IMAGE_SCALE_FITXY = 0;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CutomImageView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CutomImageView_image:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
                case R.styleable.CutomImageView_imageScaleType:
                    mImageScale = a.getInt(attr, 0);
                    break;
                case R.styleable.CutomImageView_titleText:
                    mTitle = a.getString(attr);
                    break;
                case R.styleable.CutomImageView_titleTextColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CutomImageView_titleTextSize:
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }

        a.recycle(); //core
        rect = new Rect();
        mPaint = new Paint();
        mTextBound = new Rect();
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound); //设置 mTextBound的值
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else {
            int desireByIm = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            int desireByTitle = getPaddingLeft() +getPaddingRight() + mTextBound.width();
            if (specMode == MeasureSpec.AT_MOST) {
                int desire = Math.max(desireByIm,desireByTitle);
                mWidth = Math.min(desire, specSize); // core
            }
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() +mImage.getHeight() + mTextBound.height();
            if (specMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(desire, specSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight); //core
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN); //谈蓝色
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        if (mTextBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            //zqs avail 有范围
            String msg = TextUtils.ellipsize(mTitle, paint, mWidth - getPaddingLeft() - getPaddingRight(), TextUtils.TruncateAt.END).toString(); //core 生成末尾省略号的字符串
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);

        } else {
            canvas.drawText(mTitle, mWidth /2 - mTextBound.width()*1.0f/2, mHeight - getPaddingBottom(),mPaint); //baseLine 依据
        }

        rect.bottom -= mTextBound.height();

        if (mImageScale == IMAGE_SCALE_FITXY) {
            canvas.drawBitmap(mImage, null, rect, mPaint);
        } else {
            //计算居中的矩形范围
            rect.left = mWidth/2 - mImage.getWidth()/2;
            rect.right = mWidth/2 + mImage.getWidth()/2;
            rect.top = (mHeight - mTextBound.height()) /2 - mImage.getHeight() /2;
            rect.bottom = (mHeight - mTextBound.height()) /2 + mImage.getHeight() /2;
            canvas.drawBitmap(mImage, null, rect, mPaint);
        }
    }
}





























