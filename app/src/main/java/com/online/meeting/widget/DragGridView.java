package com.online.meeting.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

/**
 * @author someone
 */
public class DragGridView extends GridView {

    private static final int DRAG_IMG_SHOW = 1;
    private static final int DRAG_IMG_NOT_SHOW = 0;

    private View mStartDragItemView;
    private ImageView dragImageView;
    private WindowManager.LayoutParams dragImageViewParams;
    private WindowManager windowManager;
    private DragGridBaseAdapter mDragAdapter;

    private boolean isViewOnDrag = false;
    private boolean isEdit = false;

    /**
     * previous dragged over position
     */
    private int startPosition = AdapterView.INVALID_POSITION;
    /**
     * 拖到的中间位置
     */
    private int tempStartPosition = AdapterView.INVALID_POSITION;
    private int endPosition = AdapterView.INVALID_POSITION;
    private int hasChange = 0;
    private int downRawX;
    private int downRawY;
    private int downX;
    private int downY;
    private int moveX;
    private int moveY;
    /**
     * 按下的点到所在item的上边缘的距离
     */
    private int mPoint2ItemTop = 0;

    /**
     * 按下的点到所在item的左边缘的距离
     */
    private int mPoint2ItemLeft = 0;

    /**
     * DragGridView距离屏幕顶部的偏移量
     */
    private int mOffset2Top = 0;

    /**
     * DragGridView距离屏幕左边的偏移量
     */
    private int mOffset2Left = 0;

    /**
     * 状态栏的高度
     */
    private int mStatusHeight;
    /**
     * DragGridView自动向下滚动的边界值
     */
    private int mDownScrollBorder;

    /**
     * DragGridView自动向上滚动的边界值
     */
    private int mUpScrollBorder;

    /**
     * DragGridView自动滚动的速度
     */
    private static final int speed = 10;

    /**
     * 点击编辑按钮后，无需长按也可拖动
     *
     * @param isDrag
     */
    public void setIsDrag(boolean isDrag) {
        this.isViewOnDrag = isDrag;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    /**
     * 震动器
     */
    private Vibrator mVibrator;
    /**
     * 用于计算手指滑动的速度。
     */
    private VelocityTracker mVelocityTracker;
    /**
     * 手指拖动后，在item上停留的时间
     */
    private long downMs = 0;

    private Handler mHandler = new Handler();
    /**
     * 用来计算手指停留的时间
     */
    private Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            downMs++;
            mHandler.postDelayed(timerRunnable, 1000);
        }
    };
    /**
     * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动
     * 当moveY的值小于向下滚动的边界值，触发GridView自动向下滚动
     * 否则不进行滚动
     */
    private Runnable mScrollRunnable = new Runnable() {

        @Override
        public void run() {
            int scrollY;
            if (getFirstVisiblePosition() == 0 || getLastVisiblePosition() == getCount() - 1) {
                mHandler.removeCallbacks(mScrollRunnable);
            }

            if (moveY > mUpScrollBorder) {
                scrollY = speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            } else if (moveY < mDownScrollBorder) {
                scrollY = -speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            } else {
                scrollY = 0;
                mHandler.removeCallbacks(mScrollRunnable);
            }

            smoothScrollBy(scrollY, 10);
        }
    };


    private OnItemLongClickListener onLongClickListener = new OnItemLongClickListener() {

        @Override
        // 长按item开始拖动
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {

            // mVibrator.vibrate(50);
            mDragAdapter.showX(true);

            // createDragBitmap(view,position);
            // isViewOnDrag = true;
            isEdit = true;
            return true;
        }
    };

    private void createDragBitmap(View view, int position) {
        // 记录长按item位置
        // startPosition = position;

        // 获取被长按item的drawing cache
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        // 通过被长按item，获取拖动item的bitmap
        Bitmap dragBitmap = Bitmap.createBitmap(view.getDrawingCache());

        // 设置拖动item的参数
        dragImageViewParams.gravity = Gravity.TOP | Gravity.LEFT;
        // 设置拖动item为原item 1.2倍
        dragImageViewParams.width = (int) (dragBitmap.getWidth());
        dragImageViewParams.height = (int) (dragBitmap.getHeight());

        // 设置触摸点为绘制拖动item的中心
        // dragImageViewParams.x = (downRawX - dragImageViewParams.width/2);
        // dragImageViewParams.y = (downRawY - dragImageViewParams.height/2);
        dragImageViewParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        // view.getLeft() + downRawX - downX;
        dragImageViewParams.y = downY - mPoint2ItemTop + mOffset2Top
                - mStatusHeight;
        // view.getTop() + downRawY - downY - mStatusHeight;
        dragImageViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        // | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        // | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        dragImageViewParams.format = PixelFormat.TRANSLUCENT;
        dragImageViewParams.windowAnimations = 0;

        // dragImageView为被拖动item的容器，清空上一次的显示
        if (Integer.parseInt(String.valueOf(dragImageView.getTag())) == DRAG_IMG_SHOW) {
            windowManager.removeView(dragImageView);
            dragImageView.setTag(DRAG_IMG_NOT_SHOW);
        }

        // 设置本次被长按的item
        dragImageView.setImageBitmap(dragBitmap);

        // 添加拖动item到屏幕
        windowManager.addView(dragImageView, dragImageViewParams);
        dragImageView.setTag(DRAG_IMG_SHOW);

        // 设置被长按item不显示
        mDragAdapter.hideView(position);
    }

    public DragGridView(Context context) {
        super(context);
        initView(context);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof DragGridBaseAdapter) {
            mDragAdapter = (DragGridBaseAdapter) adapter;
        } else {
            throw new IllegalStateException(
                    "the adapter must be implements DragGridAdapter");
        }
    }

    public void initView(Context context) {
        mStatusHeight = getStatusHeight(context);
        mVibrator = (Vibrator) context
                .getSystemService(Context.VIBRATOR_SERVICE);

        setOnItemLongClickListener(onLongClickListener);

        // 初始化显示被拖动item的image view
        dragImageView = new ImageView(getContext());
        dragImageView.setTag(DRAG_IMG_NOT_SHOW);
        // 初始化用于设置dragImageView的参数对象
        dragImageViewParams = new WindowManager.LayoutParams();

        // 获取窗口管理对象，用于后面向窗口中添加dragImageView
        windowManager = (WindowManager) getContext().getSystemService(
                Context.WINDOW_SERVICE);
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 获取触摸点相对于屏幕的坐标
                downRawX = (int) ev.getRawX();
                downRawY = (int) ev.getRawY();
                downX = (int) ev.getX();
                downY = (int) ev.getY();
                startPosition = pointToPosition(downX, downY);
                endPosition = pointToPosition(downX, downY);
                mStartDragItemView = getChildAt(startPosition
                        - getFirstVisiblePosition());
                //获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
                mDownScrollBorder = getHeight() / 5;
                //获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
                mUpScrollBorder = getHeight() * 4 / 5;

                if (mStartDragItemView != null) {
                    if (mDragAdapter.isTouchX(startPosition, downRawX, downRawY)) {
                        return super.dispatchTouchEvent(ev);
                    }
                    mPoint2ItemTop = downY - mStartDragItemView.getTop();
                    mPoint2ItemLeft = downX - mStartDragItemView.getLeft();
                    mOffset2Top = (int) (downRawY - downY);
                    mOffset2Left = (int) (downRawX - downX);

                    if (isEdit) {
                        isViewOnDrag = true;
                        createDragBitmap(mStartDragItemView, startPosition);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeCallbacks(mScrollRunnable);

                break;
            default:
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        createVelocityTracker(ev);

        // 被按下时记录按下的坐标
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

        }
        // dragImageView处于被拖动时，更新dragImageView位置
        else if ((ev.getAction() == MotionEvent.ACTION_MOVE)
                && (isViewOnDrag == true)) {
            // 设置触摸点为dragImageView中心
            // dragImageViewParams.x = (int)(ev.getRawX() -
            // dragImageView.getWidth()/2);
            // dragImageViewParams.y = (int)(ev.getRawY() -
            // dragImageView.getHeight()/2);
            moveX = (int) ev.getX();
            moveY = (int) ev.getY();
            if (mStartDragItemView != null) {
                mICancelEvent.cancel(ev);
                dragImageViewParams.x = (int) (ev.getX() - mPoint2ItemLeft + mOffset2Left);
                dragImageViewParams.y = (int) (ev.getY() - mPoint2ItemTop
                        + mOffset2Top - mStatusHeight);
                try {
                    // 更新窗口显示
                    windowManager.updateViewLayout(dragImageView,
                            dragImageViewParams);
                    onSwapItem(moveX, moveY);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            //GridView自动滚动
            mHandler.post(mScrollRunnable);

        }
        // 释放dragImageView
        else if ((ev.getAction() == MotionEvent.ACTION_UP)
                && (isViewOnDrag == true)) {
            mDragAdapter.showHideView();
            if (Integer.parseInt(String.valueOf(dragImageView.getTag())) == DRAG_IMG_SHOW) {
                windowManager.removeView(dragImageView);
                dragImageView.setTag(DRAG_IMG_NOT_SHOW);
            }
            // startPosition = AdapterView.INVALID_POSITION;
            // endPosition = AdapterView.INVALID_POSITION;
            hasChange = 0;
            // isViewOnDrag = false;

        }

        return super.onTouchEvent(ev);
    }

    private void onSwapItem(int moveX, int moveY) {
        final int tempPosition = pointToPosition(moveX, moveY);
        if (tempPosition != INVALID_POSITION) {
            endPosition = tempPosition;
        }
        // 超出边界处理
        if (moveY < getChildAt(0).getTop()) {
            // 超出上边界
            endPosition = 0;
        } else if (moveY > getChildAt(getChildCount() - 1).getBottom()
                || (moveY > getChildAt(getChildCount() - 1).getTop() && moveX > getChildAt(
                getChildCount() - 1).getRight())) {
            // 超出下边界
            endPosition = getAdapter().getCount() - 1;
        }

        if (endPosition != startPosition
                && endPosition != AdapterView.INVALID_POSITION
                && getScrollVelocity() == 0) {
            timerRunnable.run();
        }
        if (endPosition != startPosition
                && endPosition != AdapterView.INVALID_POSITION
                && startPosition != AdapterView.INVALID_POSITION) {

            if ((getScrollVelocity() > 0 && getScrollVelocity() < 300)
                    || (getScrollVelocity() == 0 && downMs >= 1)) {
                if (hasChange > 0) {
                    mDragAdapter.swapView(tempStartPosition, startPosition);
                    mDragAdapter.swapView(tempStartPosition, endPosition);
                    // ((GridViewAdapter)getAdapter()).hideView(endPosition);

                } else {
                    mDragAdapter.swapView(startPosition, endPosition);
                    tempStartPosition = startPosition;

                }

                mDragAdapter.hideView(endPosition);

                startPosition = endPosition;

                hasChange++;
                mHandler.removeCallbacks(timerRunnable);
                downMs = 0;
            }
        }
    }

    /**
     * 创建VelocityTracker对象，并将触摸事件加入到VelocityTracker当中。
     *
     * @param event 右侧布局监听控件的滑动事件
     */
    private void createVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 获取手指在绑定布局上的滑动速度。
     *
     * @return 滑动速度，以每秒钟移动了多少像素值为单位。
     */
    private int getScrollVelocity() {
        mVelocityTracker.computeCurrentVelocity(1000);
        int velocity = (int) mVelocityTracker.getXVelocity();
        return Math.abs(velocity);
    }

    public interface DragGridBaseAdapter {
        /**
         * 重新排列数据
         *
         * @param oldPosition
         * @param newPosition
         */
        public void swapView(int oldPosition, int newPosition);

        /**
         * 设置某个item隐藏
         *
         * @param hidePosition
         */
        public void hideView(int hidePosition);

        public void showHideView();

        public void showX(boolean isChecked);

        public boolean isTouchX(int position, int x, int y);
    }

    private ICancelEvent mICancelEvent;

    public void setICancelEvent(ICancelEvent cancelEvent) {
        mICancelEvent = cancelEvent;
    }

    //处理与右滑关闭的冲突
    public interface ICancelEvent {
        MotionEvent cancel(MotionEvent ev);
    }

}