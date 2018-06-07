package com.zqs.metroui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Android 仿Win8的metro的UI界面（上）
 * https://blog.csdn.net/lmj623565791/article/details/23441455
 *
 * 一.实现思路:
 *  1. 自定义ImageView 继承 ImageView
 *  2. 通过onTouchEvent() 的按下,抬起操作 做对应的处理
 *  3. 通过 Matrix类 对ImageView的图形进行缩放操作
 *
 *  防止View 卡顿 利用了handler发送消息机制
 */
public class MainActivity extends AppCompatActivity {
    MyImageView joke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        joke = findViewById(R.id.c_joke);
        joke.setmOnViewClickListener(new MyImageView.onViewClickListener() {
            @Override
            public void onViewClick(MyImageView view) {
                Toast.makeText(MainActivity.this, "Joke", 1000).show();
            }
        });
    }
}
