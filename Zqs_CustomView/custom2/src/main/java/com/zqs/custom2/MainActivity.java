package com.zqs.custom2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Android 自定义View (二) 进阶
 * https://blog.csdn.net/lmj623565791/article/details/24300125
 *
 * 一. 引用数据类型 和 枚举类型的自定义属性
 *
 * 二. onMeasure() 确定自定义View的大小
 *
 * 三. onDraw() 画出每个控件
 *      1.画边框 canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
 *      2.画文字 canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
 *      3.画图片 canvas.drawBitmap(mImage, null, rect, mPaint);
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
