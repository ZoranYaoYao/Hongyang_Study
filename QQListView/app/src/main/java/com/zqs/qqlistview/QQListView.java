package com.zqs.qqlistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

/**
 * Created by zqs on 2018-6-7.
 */

public class QQListView extends ListView {
    public static final String TAG = "QQListView";

    private int touchSlop; //slop 溢出

    private boolean isSliding;

    private int xDown;
    private int yDown;
    private int xMove;
    private int yMove;

    private LayoutInflater mInflater;

    private PopupWindow mPopWindow;
    private int popWindowHeight;
    private int popWindowWidth;

    private Button mDelbtn;

    private DelButtonClickeListener mListener;

    private View mCurrentView;

    private int mCurrentViewPos;

    public QQListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mInflater = LayoutInflater.from(context);
        touchSlop = 60;
        View view = mInflater.inflate(R.layout.delete_btn, null);
        mDelbtn = view.findViewById(R.id.id_item_btn);
        mPopWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mPopWindow.getContentView().measure(0, 0);
        popWindowHeight = mPopWindow.getContentView().getMeasuredHeight();
        popWindowWidth = mPopWindow.getContentView().getWidth();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xDown = x;
                yDown = y;
                if (mPopWindow.isShowing()) {
                    dismissPopWindow();
                    return false;
                }
                mCurrentViewPos = pointToPosition(xDown, yDown); // core api : pointToPosition
                View view = getChildAt(mCurrentViewPos - getFirstVisiblePosition());
                mCurrentView = view;
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = x;
                yMove = y;
                int dx = xMove - xDown;
                int dy = yMove - yDown;
                if (xMove < xDown && Math.abs(dx) > touchSlop && Math.abs(dy) < touchSlop) {
                    isSliding = true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (isSliding) {
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    int[] location = new int[2];
                    mCurrentView.getLocationOnScreen(location);
                    mPopWindow.setAnimationStyle(android.R.style.Widget_Holo_PopupWindow);
                    mPopWindow.update();
                    mPopWindow.showAtLocation(mCurrentView, Gravity.LEFT | Gravity.TOP,
                            location[0] + mCurrentView.getWidth(), location[1] + mCurrentView.getHeight() / 2 - popWindowHeight / 2);
                    mDelbtn.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                mListener.clickHappend(mCurrentViewPos);
                                mPopWindow.dismiss();
                            }
                        }
                    });
                    break;
                case MotionEvent.ACTION_UP:
                    isSliding = false;
                    break;
            }

            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void dismissPopWindow() {
        if (mPopWindow != null && mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
    }

    public void setDelButtonClickListener(DelButtonClickeListener listener) {
        mListener = listener;
    }

    interface DelButtonClickeListener {
        public void clickHappend(int position);
    }
}
