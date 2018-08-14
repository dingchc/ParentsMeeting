package com.online.meeting.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.online.meeting.R;
import com.online.meeting.listener.IDialogCallback;


/**
 * 通用对话框
 *
 * @author Ding
 */
public class CommonDialog extends AlertDialog implements View.OnClickListener {
    private Context mContext = null;
    private View mContentView = null;
    private View mCancelBtnView = null;

    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mTvUpdateVerContent;
    private TextView mButtonOk;
    private TextView mButtonCancel;

    private IDialogCallback listener;
    private String mTitle;
    private String mContent;

    /**
     * 带自定义样式的内容
     */
    private SpannableString mSSContent;

    /**
     * 确定按钮文字
     */
    private String mOK;

    /**
     * 取消按钮文字
     */
    private String mCancel;

    public CommonDialog(Context context, IDialogCallback iDialogCommonListener, String mTitle, String mContent, String mOK, String mCancel) {
        super(context);
        this.mContext = context;
        this.listener = iDialogCommonListener;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mOK = mOK;
        this.mCancel = mCancel;
        this.mContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null);
    }

    public CommonDialog(Context context, IDialogCallback iDialogCommonListener, String mTitle, SpannableString spannableString, String mOK, String mCancel) {
        super(context);
        this.mContext = context;
        this.listener = iDialogCommonListener;
        this.mTitle = mTitle;
        this.mSSContent = spannableString;
        this.mOK = mOK;
        this.mCancel = mCancel;
        this.mContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mContentView);
        DisplayMetrics dm = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) (screenWidth * 0.8f);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        initView();
        initEvent();
    }

    private void initView() {
        mTvTitle = (TextView) mContentView.findViewById(R.id.tv_title);
        mTvContent = (TextView) mContentView.findViewById(R.id.tv_content);
        mTvUpdateVerContent = (TextView) mContentView.findViewById(R.id.tv_update_version_content);
        mButtonCancel = (TextView) mContentView.findViewById(R.id.btn_cancel);
        mCancelBtnView = mContentView.findViewById(R.id.fl_cancel);
        mButtonOk = (TextView) mContentView.findViewById(R.id.btn_ok);
        if (TextUtils.isEmpty(mTitle)) {
            //如果标题为空，设置标题行和第一条分割线消失
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setText(mTitle);
        }
        mTvContent.setText(TextUtils.isEmpty(mContent) ? mSSContent : mContent);
        if (TextUtils.isEmpty(mOK)) {
            mButtonOk.setVisibility(View.GONE);
        } else {
            mButtonOk.setText(mOK);
        }
        if (TextUtils.isEmpty(mCancel)) {
            mCancelBtnView.setVisibility(View.GONE);
        } else {
            mCancelBtnView.setVisibility(View.VISIBLE);
            mButtonCancel.setText(mCancel);
        }
    }

    private void initEvent() {
        mButtonOk.setOnClickListener(this);
        mButtonCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
                break;
            case R.id.btn_ok:
                dismiss();
                if (listener != null) {
                    listener.onConfirm();
                }
                break;
            default:
        }
    }

    public void setCancelBtnVisible(int btnVisible) {
        if (mCancelBtnView != null && mButtonCancel != null) {
            mButtonCancel.setVisibility(btnVisible);
            mCancelBtnView.setVisibility(btnVisible);
        }
    }

    public void setVersionDes() {
        mTvContent.setVisibility(View.GONE);
        mTvUpdateVerContent.setVisibility(View.VISIBLE);
        mTvUpdateVerContent.setText(mContent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (listener != null) {
            listener.onCancel();
        }
    }
}

