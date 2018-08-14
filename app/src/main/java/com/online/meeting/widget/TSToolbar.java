package com.online.meeting.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.online.meeting.R;


/**
 * 导航条
 *
 * @author ding
 *         Created by ding on 2/13/17.
 */
public class TSToolbar extends Toolbar implements View.OnClickListener {

    /**
     * 默认
     */
    public final static int MODE_DEFAULT = 0;

    /**
     * 全屏
     */
    public final static int MODE_DEVICE_FULL = 1;

    /**
     * 模式
     */
    private int mMode;
    protected View mLeftArea, mRightArea;
    protected TextView mTvTitleLeft, mTvTitleRight, mTvTitle;
    protected ImageView mIvIconLeft, mIvIconRight;
    protected ToolbarClickListener mToolbarClickListener;
    protected int mScreenWidth;


    /**
     * 上下文
     */
    private Context mContext;

    public TSToolbar(Context context) {
        this(context, null);
    }

    public TSToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TSToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    public void init(Context context) {
        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mLeftArea = findViewById(R.id.rl_left_area);
        mRightArea = findViewById(R.id.rl_right_area);

        mTvTitle = findViewById(R.id.tv_title);
        mTvTitleLeft = this.findViewById(R.id.tv_title_left);
        mTvTitleRight = this.findViewById(R.id.tv_title_right);

        mIvIconLeft = this.findViewById(R.id.iv_icon_left);
        mIvIconRight = this.findViewById(R.id.iv_icon_right);

        addClickListener();
    }

    private void addClickListener() {

        mTvTitleLeft.setOnClickListener(this);
        mTvTitleRight.setOnClickListener(this);
        mIvIconLeft.setOnClickListener(this);
        mIvIconRight.setOnClickListener(this);
    }

    /**
     * 设置toolbar点击回调
     *
     * @param clickListener 回调
     */
    public void setmToolbarClickListener(ToolbarClickListener clickListener) {
        this.mToolbarClickListener = clickListener;
    }

    @Override
    public void onClick(View view) {

        int type = 0;

        switch (view.getId()) {

            case R.id.tv_title_left:
                type = ToolbarClickEnum.LEFT_TITLE.ordinal();
                break;
            case R.id.tv_title_right:
                type = ToolbarClickEnum.RIGHT_TITLE.ordinal();
                break;
            case R.id.iv_icon_left:
                type = ToolbarClickEnum.LEFT_ICON.ordinal();
                break;
            case R.id.iv_icon_right:
                type = ToolbarClickEnum.RIGHT_ICON.ordinal();
                break;
            default:
        }

        if (mToolbarClickListener != null) {
            mToolbarClickListener.onToolbarClick(type);
        }

    }

    /**
     * 设置左侧区域是否可见
     *
     * @param visible 可见值
     */
    public void setLeftAreaVisible(int visible) {
        if (mLeftArea != null) {
            mLeftArea.setVisibility(visible);
            measureTitleView();
        }
    }

    /**
     * 设置右侧区域是否可见
     *
     * @param visible 可见值
     */
    public void setRightAreaVisible(int visible) {
        if (mRightArea != null) {
            mRightArea.setVisibility(visible);
            measureTitleView();
        }
    }

    /**
     * 设置左侧标题是否可见
     *
     * @param visible 可见值
     */
    private void setLeftTitleVisible(int visible) {
        if (mTvTitleLeft != null) {
            mTvTitleLeft.setVisibility(visible);
            measureTitleView();
        }
    }

    /**
     * 设置右侧标题是否可见
     *
     * @param visible 可见值
     */
    private void setRightTitleVisible(int visible) {
        if (mTvTitleRight != null) {
            mTvTitleRight.setVisibility(visible);
            measureTitleView();
        }
    }

    /**
     * 设置左侧图标是否可见
     *
     * @param visible 可见值
     */
    public void setLeftIconVisible(int visible) {
        if (mIvIconLeft != null) {
            mIvIconLeft.setVisibility(visible);
            measureTitleView();
        }
    }

    /**
     * 设置右侧图标是否可见
     *
     * @param visible 可见值
     */
    private void setRightIconVisible(int visible) {
        if (mIvIconRight != null) {
            mIvIconRight.setVisibility(visible);
            measureTitleView();
        }
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */

    public void setTitle(String title) {

        if (mTvTitle != null) {

            if (TextUtils.isEmpty(title)) {
                mTvTitle.setText("");
            } else {
                mTvTitle.setText(title);
            }
        }
    }

    @Override
    public void setTitle(@StringRes int resId) {

        setTitle(getContext().getResources().getString(resId));
    }

    @Override
    public void setTitle(CharSequence title) {

        if (title != null) {
            setTitle(title.toString());
        }
    }

    /**
     * 设置左侧标题
     *
     * @param title 标题
     */
    public void setLeftTitle(String title) {


        if (mTvTitleLeft != null) {

            setLeftTitleVisible(VISIBLE);
            setLeftIconVisible(GONE);

            if (TextUtils.isEmpty(title)) {
                mTvTitleLeft.setText("");
            } else {
                mTvTitleLeft.setText(title);
            }
            measureTitleView();
        }
    }

    /**
     * 设置右侧标题
     *
     * @param title 标题
     */
    public void setRightTitle(String title) {


        if (mTvTitleRight != null) {

            setRightTitleVisible(VISIBLE);
            setRightIconVisible(INVISIBLE);

            if (TextUtils.isEmpty(title)) {
                mTvTitleRight.setText("");
            } else {
                mTvTitleRight.setText(title);
            }
            measureTitleView();

        }
    }

    /**
     * 设置右侧带图片标题
     *
     * @param title 标题
     */
    public void setRightTitleWithIcon(String title, int resId) {


        if (mTvTitleRight != null) {
            setRightTitleVisible(VISIBLE);
            setRightIconVisible(INVISIBLE);
            mTvTitleRight.setText(getIconTitle(title, resId));
            measureTitleView();

        }
    }

    private CharSequence getIconTitle(String title, int resId) {
        SpannableString sb = new SpannableString("  " + title);
        try {
            sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.orange)), 2, title.length() + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            sb.setSpan(new AbsoluteSizeSpan(14, true), 2, 2 + title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Drawable image = ContextCompat.getDrawable(mContext, resId);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }


    /**
     * 设置左侧图标
     *
     * @param resId 图标id
     */
    public void setLeftIcon(int resId) {

        if (mIvIconLeft != null && resId > 0) {

            setLeftTitleVisible(GONE);
            setLeftIconVisible(VISIBLE);

            mIvIconLeft.setImageResource(resId);

            measureTitleView();

        }
    }

    /**
     * 设置右侧图标
     *
     * @param resId 图片id
     */
    public void setRightIcon(int resId) {

        if (mIvIconRight != null && resId > 0) {

            setRightTitleVisible(INVISIBLE);
            setRightIconVisible(VISIBLE);

            mIvIconRight.setImageResource(resId);

            measureTitleView();

        }

    }

    public ImageView getRightIcon() {
        return mIvIconRight;
    }

    public ImageView getLeftIcon() {
        return mIvIconLeft;
    }

    public TextView getmTvTitleRight() {
        return mTvTitleRight;
    }

    public interface ToolbarClickListener {

        /**
         * toolbar点击回调
         *
         * @param type 1-左侧标题、2-左侧图标、3-右侧标题、4-右侧图标
         */
        void onToolbarClick(int type);
    }

    public enum ToolbarClickEnum {

        /**
         * 1-左侧标题、2-左侧图标、3-右侧标题、4-右侧图标
         */
        LEFT_TITLE(1), LEFT_ICON(2), RIGHT_TITLE(3), RIGHT_ICON(4);

        private int type;

        ToolbarClickEnum(int type) {
            this.type = type;
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        measureTitleView();
    }

    /**
     * 测量标题边距
     */
    private void measureTitleView() {
        try {

            mLeftArea.measure(0, 0);
            mRightArea.measure(0, 0);

            int leftAreaMeasuredWidth = mLeftArea.getMeasuredWidth();
            int rightAreaMeasuredWidth = mRightArea.getMeasuredWidth();

            int maxLeftRight = Math.max(leftAreaMeasuredWidth, rightAreaMeasuredWidth);

            int titleLeftMargin = maxLeftRight - leftAreaMeasuredWidth;
            int titleRightMargin = maxLeftRight - rightAreaMeasuredWidth;

            int maxPixels = mScreenWidth - maxLeftRight * 2;

            MarginLayoutParams titleMarginLayoutParam = (MarginLayoutParams) mTvTitle.getLayoutParams();

            if (titleLeftMargin > 0) {
                titleMarginLayoutParam.leftMargin = titleLeftMargin;
            } else if (titleRightMargin > 0) {
                titleMarginLayoutParam.rightMargin = titleRightMargin;
            }

            mTvTitle.setMaxWidth(maxPixels);

            mTvTitle.setLayoutParams(titleMarginLayoutParam);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
