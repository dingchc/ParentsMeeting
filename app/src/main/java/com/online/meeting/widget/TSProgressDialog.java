package com.online.meeting.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.online.meeting.R;
import com.online.meeting.utils.AppLogger;


/**
 * 自定义加载对话框
 * @author ding
 * Created by Ding on 11/8/15.
 */
public class TSProgressDialog extends AlertDialog {

    private Context mContext = null;
    private View mContentView = null;

    private TextView mTvMessage = null;

    private TextView mTvCancel = null;
    private CancelDialogListener cancelDialogListener;


    public TSProgressDialog(Context context) {
        super(context);
        mContext = context;
        mContentView = LayoutInflater.from(context).inflate(R.layout.bg_dialog_progress, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(mContentView);

        WindowManager.LayoutParams p = getWindow().getAttributes();

        p.width = mContext.getResources().getDimensionPixelSize(R.dimen.process_dialog_width);

        getWindow().setBackgroundDrawableResource(R.drawable.round_bg_rect_transparent);
        getWindow().setAttributes(p);

        initView();

        initEvent();
    }

    private void initView() {

        mTvMessage = (TextView) mContentView.findViewById(R.id.tv_content);
        mTvCancel = (TextView) mContentView.findViewById(R.id.tv_cancel);

    }

    private void initEvent() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialogListener.cancel();
            }
        });
    }

    /**
     * 设置内容
     * @param message 内容
     */
    public void setTipMessage(String message) {

        mTvMessage.setText(message);
    }

    public void setCancelBtnVisible(int status) {
        mTvCancel.setVisibility(status);
    }

    public void setCancelDialogListener(CancelDialogListener cancelDialogListener) {
        this.cancelDialogListener = cancelDialogListener;
    }

    public interface CancelDialogListener {
        void cancel();
    }

    /**
     * 设置当前的用户
     * @param isXxt 是否是校讯通
     * @return 0 成功、 其他
     */
    public int getCurrentUserId(boolean isXxt) {

        return 0;
    }
}
