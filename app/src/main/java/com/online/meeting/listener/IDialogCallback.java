package com.online.meeting.listener;

/**
 * 通用对话框回调
 * @author ding
 * Created by ding on 1/12/17.
 */
public interface IDialogCallback {

    /**
     * 确认
     */
    void onConfirm();

    /**
     * 取消
     */
    void onCancel();

}