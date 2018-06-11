package com.zqs.customview;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Android 自定义View (一)
 * https://blog.csdn.net/lmj623565791/article/details/24252901
 *
 * 一. 自定义view的流程
 *      1.自定义View的属性
 *      2.在View的构造方法中获取我们自定义的属性
 *      [3.重写onMesure,onlayout]
 *      4.重写onDraw
 *
 * 二. 获取自定义属性值的方法
 *       context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0);
 *
 * 三. Paint 对象相关的API:
 *       1. 设置文字大小 mPaint.setTextSize(mTitleTextSize)
 *       2. 设置文本边界 mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
 *
 * 三. 画矩形 画文字
 *      canvas.drawRect(0,0, getMeasuredWidth(),getMeasuredHeight(),mPaint); //画矩形
 *      canvas.drawText(mTitleText, getWidth()/2 - mBound.width()/2, getHeight()/2 + mBound.height()/2, mPaint); //画文字
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final CustomTitleView tv_custom = findViewById(R.id.tv_custom);

        CountDownTimer timer = new CountDownTimer(2000,2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                tv_custom.mTitleText ="7506";
                //tv_custom.postInvalidate(); //core 不调用重绘的话, 是不会重绘的
                Toast.makeText(MainActivity.this,"mTitleText = " +tv_custom.mTitleText,100).show();
            }
        };
        timer.start();

    }
}
