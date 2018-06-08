package com.zqs.verticallinearlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 *Andoird 自定义ViewGroup实现竖向引导界面
 * https://blog.csdn.net/lmj623565791/article/details/23692439
 *
 * 一.view 工作流程
 *  1. 画出垂直linearLayout的大小 和 布局(包含子布局)
 *  2. 重写onTouchEvent()方法
 *       (1) DOWN时, 记录滑动起始点
 *       (2) MOVE时, 边界值判断 -> scrollBy(0, dy) 滚动相对距离
 *       (3) UP时,  mScroller.startScroll() 进行移动 ->  postInvalidate() 刷新 导致 ondraw()
 *                  -> computeScroll() -> scrollTo() 移动到对应的位置 -> isScrolling = false;
 */
public class VerticalLinearLayout extends ViewGroup {
    private int mScreenHeight;
    private int mScrollStart;
    private int mScrollEnd;

    private int mLastY;
    private Scroller mScroller; //滑动辅助类
    private boolean isScrolling;
    private VelocityTracker mVelocityTracker;

    private int currentPage;
    private OnPageChangeListener mOnPageChangeListener;


    public VerticalLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;

        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, mScreenHeight);  //计算孩子的高度
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            int childCount = getChildCount();
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            lp.height = mScreenHeight * childCount;
            setLayoutParams(lp);  //给父类设置 高度

            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child.getVisibility() != View.GONE) {
                    child.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight); //布局每个子类的位置
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isScrolling)
            return super.onTouchEvent(event);

        int action = event.getAction();
        int y = (int) event.getY();
        obtainVelocity(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollStart = getScrollY();
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                int dy = mLastY - y;
                int scrollY = getScrollY();
                /**边界值检查,防止getScrollY() 超出控件的边界值*/
                // 已经到达顶端，下拉多少，就往上滚动多少
                if (dy < 0 && scrollY + dy < 0) {
                    dy = -scrollY;
                }
                // 已经到达底部，上拉多少，就往下滚动多少
                if (dy > 0 && scrollY + dy > getHeight() - mScreenHeight) {
                    dy = getHeight() - mScreenHeight - scrollY;
                }
                Log.e("zqs", "dy = " + dy + ", scollY = " + scrollY);

                scrollBy(0, dy);  //core API : 手指按住,滑动的相对位置
                //scrollBy(0, 0);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                mScrollEnd = getScrollY();
                int dScrollY = mScrollEnd - mScrollStart;
                if (wantScrollToNext()) {
                    if (shouldScrollToNext()) {
                        mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
                    }
                }

                if (wantScrollToPre()) {
                    if (shouldScrollToPre()) {
                        mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);
                    } else {
                        mScroller.startScroll(0, getScrollY(), 0, -dScrollY); //回滚操作
                    }
                }

                isScrolling = true;
                postInvalidate();
                recycleVelocity();
                break;
        }
        return true;

    }

    /**
     * core API
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) { // 计算是否有滑动
            scrollTo(0, mScroller.getCurrY());
            Log.e("zqs","computeScroll() getCurrY= " +mScroller.getCurrY(),new Throwable());
            postInvalidate();
        } else {
            int position = getScrollY() / mScreenHeight;
            Log.e("zqs", position + "," + currentPage);
            if (position != currentPage) {
                if (mOnPageChangeListener != null) {
                    currentPage = position;
                    mOnPageChangeListener.onPageChange(currentPage);
                }
            }
            isScrolling = false;
        }
    }

    private void recycleVelocity() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    private boolean shouldScrollToPre() {
        return mScrollEnd - mScrollStart > mScreenHeight / 3 || Math.abs(getVelocity()) > 600;
    }

    private boolean wantScrollToPre() {
        return mScrollEnd < mScrollStart;
    }

    private boolean shouldScrollToNext() {
        return mScrollEnd - mScrollStart > mScreenHeight / 3 || Math.abs(getVelocity()) > 600;
    }

    private int getVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        return (int) mVelocityTracker.getYVelocity();
    }

    private boolean wantScrollToNext() {
        return mScrollEnd > mScrollStart;
    }

    private void obtainVelocity(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    public void setmOnPageChangeListener(OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    public interface OnPageChangeListener {
        void onPageChange(int currentPage);
    }
}
