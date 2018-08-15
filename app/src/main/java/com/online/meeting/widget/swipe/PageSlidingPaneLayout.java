package com.online.meeting.widget.swipe;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;


/**
 * 滑动返回
 *
 * @author someone
 */
public class PageSlidingPaneLayout extends SlidingPaneLayout implements DragGridView.ICancelEvent,
        SlowScrollView.IScrollCancelEvent {

    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mEdgeSlop;
    private DragGridView mDragGridView;
    private SlowScrollView mScrollView;

    public PageSlidingPaneLayout(Context context) {
        this(context, null);
    }


    public PageSlidingPaneLayout(Context context, AttributeSet attrs, DragGridView gridView) {
        this(context, null);
        mDragGridView = gridView;
        mDragGridView.setICancelEvent(this);
    }

    public PageSlidingPaneLayout(Context context, AttributeSet attrs, SlowScrollView scrollView) {
        this(context, null);
        mScrollView = scrollView;
        mScrollView.setICancelEvent(this);
    }

    public PageSlidingPaneLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageSlidingPaneLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        ViewConfiguration config = ViewConfiguration.get(context);
        mEdgeSlop = config.getScaledEdgeSlop();
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (MotionEventCompat.getActionMasked(ev)) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = ev.getX();
                mInitialMotionY = ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                // The user should always be able to "close" the pane, so we only check
                // for child scrollability if the pane is currently closed.
                if (mInitialMotionX > mEdgeSlop && !isOpen() && canScroll(this, false,
                        Math.round(x - mInitialMotionX), Math.round(x), Math.round(y))) {

                    // How do we set super.mIsUnableToDrag = true?

                    // send the parent a cancel event
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    return super.onInterceptTouchEvent(cancelEvent);
                } else if (mDragGridView != null) {
                    return super.onInterceptTouchEvent(cancel(ev));
                } else if (mScrollView != null) {
                    return super.onInterceptTouchEvent(scrollCancel(ev));
                }

            }
            default:

        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public MotionEvent cancel(MotionEvent ev) {
        MotionEvent cancelEvent = MotionEvent.obtain(ev);
        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);

        return cancelEvent;
    }

    @Override
    public MotionEvent scrollCancel(MotionEvent ev) {
        MotionEvent cancelEvent = MotionEvent.obtain(ev);
        cancelEvent.setAction(MotionEvent.ACTION_CANCEL);

        return cancelEvent;
    }
}
