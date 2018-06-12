package com.zqs.custom3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Android 自定义View (三) 圆环交替 等待效果
 * https://blog.csdn.net/lmj623565791/article/details/24500107
 *
 * 一. 画圆:
 *      canvas.drawCircle(centre,centre,radius,mPaint); //core 画出圆环,半径:60 -15 = 45,笔宽度:30,向2边扩展15DP ,所以看着像一半
 *
 * 二. 画圆弧:
 *      canvas.drawArc(oval,-90,mProgress,false,mPaint); //core
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
