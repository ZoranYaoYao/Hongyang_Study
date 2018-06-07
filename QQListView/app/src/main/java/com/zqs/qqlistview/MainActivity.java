package com.zqs.qqlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ListView滑动删除 ，仿腾讯QQ
 * https://blog.csdn.net/lmj623565791/article/details/22961279
 *
 * 1. ListView + PopWindow 实现滑动删除效果
 * 2. 主要点:
 *      (1) dispatchTouchEvent() 进行判断进入显示界面
 *      (2) onTouchEvent() 处理滑动事件的流程
 *      (3) popWindow 记录ListView中Item的位置,进行showACLocation()展示
 */
public class MainActivity extends AppCompatActivity {

    private QQListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = findViewById(R.id.id_listview);
        // 不要直接Arrays.asList
        mDatas = new ArrayList<String>(Arrays.asList("HelloWorld", "Welcome", "Java", "Android", "Servlet", "Struts",
                "Hibernate", "Spring", "HTML5", "Javascript", "Lucene"));
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mDatas);
        mListView.setAdapter(mAdapter);

        mListView.setDelButtonClickListener(new QQListView.DelButtonClickeListener() {
            @Override
            public void clickHappend(int position) {
                Toast.makeText(MainActivity.this, position + " : " + mAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                mAdapter.remove(mAdapter.getItem(position)); //core Api: remove()
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Toast.makeText(MainActivity.this, position + " : " + mAdapter.getItem(position), 1).show();
            }
        });
    }
}
