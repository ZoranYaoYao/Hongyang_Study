package com.zqs.custom4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Android 自定义View (四) 视频音量调控
 * https://blog.csdn.net/lmj623565791/article/details/24529807
 *
 * 一. 设置连线头的方式
 *        mPaint.setStrokeCap(Paint.Cap.ROUND);   //core!! 定义线段连接形状为圆头
 * 二. 画图
 *       canvas.drawBitmap(mImage, null, mRect, mPaint);
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
