package com.zqs.custom5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Android 完美实现图片圆角和圆形（对实现进行分析）
 * https://blog.csdn.net/lmj623565791/article/details/24555655
 *
 * 设置2幅图的迭代效果:
 * paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
