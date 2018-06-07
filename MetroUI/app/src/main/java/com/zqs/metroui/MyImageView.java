package com.zqs.metroui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * Created by zqs on 2018-6-7.
 */

public class MyImageView extends android.support.v7.widget.AppCompatImageView{
    private static final String TAG = "MyImageView";
    private static final int SCALE_REDUCE_INIT = 0;
    private static final int SCALING = 1;
    private static final int SCALE_ADD_INIT = 6;

    private int mWidth;
    private int mHeight;
    private int mCenterWidth;
    private int mCenterHeight;
    private float mMinScale = 0.85f;

    private boolean isFinish = true;


    public MyImageView(Context context) {
        this(context,null);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mWidth = getWidth() - getPaddingLeft() - getPaddingRight();
            mHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            mCenterWidth = mWidth/2;
            mCenterHeight = mHeight/2;

            Drawable drawable = getDrawable();
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bd.setAntiAlias(true);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScaleHander.sendEmptyMessage(SCALE_REDUCE_INIT);
                break;
            case MotionEvent.ACTION_UP:
                mScaleHander.sendEmptyMessage(SCALE_ADD_INIT);
                break;
        }
        return true;
    }

    private  MyHandler mScaleHander = new MyHandler();


    private  class MyHandler extends Handler {
        private Matrix matrix = new Matrix();
        private int count = 0;
        private float s;

        private boolean isClicked;
        @Override
        public void handleMessage(Message msg) {
            matrix.set(getImageMatrix()); //core api
            switch (msg.what) {
                case SCALE_REDUCE_INIT:
                    if (!isFinish) {
                        mScaleHander.sendEmptyMessage(SCALE_REDUCE_INIT);
                    } else {
                        isFinish = false;
                        count = 0;
                        s = (float) Math.sqrt(Math.sqrt(mMinScale));
                        beginScale(matrix, s); //核心地方 s的值 多次平方之后的值
                        mScaleHander.sendEmptyMessage(SCALING);
                    }
                    break;
                case SCALING:
                    beginScale(matrix,s);
                    if(count < 4) {
                        mScaleHander.sendEmptyMessage(SCALING);
                    } else {
                        isFinish = true; //缩小标识符 完成
                        if (MyImageView.this.mOnViewClickListener != null && !isClicked) {
                            isClicked = true;
                            MyImageView.this.mOnViewClickListener.onViewClick(MyImageView.this);
                        } else {
                            isClicked = false;
                        }
                    }
                    count++;
                    break;
                case SCALE_ADD_INIT:
                    if (!isFinish) {
                        mScaleHander.sendEmptyMessage(SCALE_ADD_INIT);
                    } else {
                        isFinish = false;
                        count = 0;
                        s = (float) Math.sqrt(Math.sqrt(1.0f/mMinScale));
                        beginScale(matrix, s); //核心地方,2次方法 (有动画效果?)
                        mScaleHander.sendEmptyMessage(SCALING);
                    }
                    break;
            }
        }
    }

    private synchronized void beginScale(Matrix matrix, float scale) {
        matrix.postScale(scale,scale,mCenterWidth,mCenterHeight); //core api: 围绕中心缩放
        setImageMatrix(matrix);
    }

    public void setmOnViewClickListener(onViewClickListener mOnViewClickListener) {
        this.mOnViewClickListener = mOnViewClickListener;
    }

    private onViewClickListener mOnViewClickListener;
    public interface onViewClickListener {
        void onViewClick(MyImageView view);
    }

}
