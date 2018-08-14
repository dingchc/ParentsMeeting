package com.online.meeting.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import com.online.meeting.utils.Util;

/**
 * @author someone
 */
public class SlowScrollView extends HorizontalScrollView {
    private Context context;

    public SlowScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public SlowScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public SlowScrollView(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (upListener != null) {
                    upListener.onActionUp();
                }
                break;
            default:
        }
        return super.onTouchEvent(ev);
    }

    float oldx = 0;
    private boolean isScroll = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            oldx = ev.getX();
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            float newx = ev.getX();
            if (Math.abs(newx - oldx) > Util.Companion.dp2px(context, 80)) {
                isScroll = true;
                mICancelEvent.scrollCancel(ev);
                return true;
            }
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (isScroll) {
                isScroll = false;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    private OnActionUpListener upListener;

    public void setUpListener(OnActionUpListener upListener) {
        this.upListener = upListener;
    }

    public interface OnActionUpListener {
        void onActionUp();
    }

    public interface IScrollCancelEvent {
        MotionEvent scrollCancel(MotionEvent ev);
    }

    private IScrollCancelEvent mICancelEvent;

    public void setICancelEvent(IScrollCancelEvent cancelEvent) {
        mICancelEvent = cancelEvent;
    }
}
