package com.online.meeting.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.online.meeting.R;
import com.online.meeting.listener.IDialogCallback;
import com.online.meeting.widget.TSCommonDialog;
import com.online.meeting.widget.TSProgressDialog;


/**
 * 定义通用对话框
 * Created by Ding on 11/7/15.
 */
public class DialogUtil {


    /**
     * 显示进度对话框
     *
     * @param context 上下文
     * @return 对话框
     */
    public static TSProgressDialog createProgressDialog(Context context) {

        TSProgressDialog progDialog = new TSProgressDialog(context);

        return progDialog;
    }

    /**
     * 显示已完成Toast
     *
     * @param context        上下文
     * @param toast          父类的toast对象
     * @param content        内容
     * @param isLongDuration 是否是长时间显示
     */
    public static void showWarningFinished(Context context, Toast toast, CharSequence content, boolean isLongDuration) {
        showCustomToast(context, toast, content, false, isLongDuration);
    }

    /**
     * 显示错误Toast
     *
     * @param context        上下文
     * @param toast          父类的toast对象
     * @param content        内容
     * @param isLongDuration 是否是长时间显示
     */
    public static void showWarningError(Context context, Toast toast, CharSequence content, boolean isLongDuration) {
        showCustomToast(context, toast, content, true, isLongDuration);
    }

    /**
     * 自定义Toast
     *
     * @param context        上下文
     * @param toast          父类的toast对象
     * @param content        内容
     * @param isError        是否是错误提示
     * @param isLongDuration 是否是长时间显示
     */
    private static void showCustomToast(Context context, Toast toast, CharSequence content, boolean isError, boolean isLongDuration) {

        View view = toast.getView();

        if (view == null) {
            view = LayoutInflater.from(context).inflate(
                    R.layout.bg_custom_toast, null);
        }

        ImageView ivError = (ImageView) view.findViewById(R.id.iv_remind_error);
        ImageView ivFinished = (ImageView) view
                .findViewById(R.id.iv_remind_finished);
        TextView tv = (TextView) view.findViewById(R.id.tv_remind);

        if (isError) {
            ivError.setVisibility(View.VISIBLE);
            ivFinished.setVisibility(View.GONE);
        } else {
            ivError.setVisibility(View.GONE);
            ivFinished.setVisibility(View.VISIBLE);
        }

        tv.setText(content);
        tv.setWidth(context.getResources().getDimensionPixelSize(R.dimen.custom_dialog_width));
        toast.setGravity(Gravity.CENTER, 0, 0);

        if (isLongDuration) {
            toast.setDuration(Toast.LENGTH_LONG);
        } else {
            toast.setDuration(Toast.LENGTH_SHORT);
        }

        toast.show();

    }

    /**
     * 使用默认字体样式的通用对话框
     *
     * @param context   上下文
     * @param title     标题
     * @param content   内容
     * @param okStr     确认
     * @param cancelStr 取消
     * @param listener  回调
     * @return 对话框
     */
    public static TSCommonDialog createCommonDialog(Context context, String title, String content, String okStr, String cancelStr, IDialogCallback listener) {
        TSCommonDialog commonDialog = new TSCommonDialog(context, listener, title, content, okStr, cancelStr);
        commonDialog.setCancelable(true);
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.show();
        return commonDialog;
    }

    /**
     * 使用默认字体样式的通用对话框
     *
     * @param context   上下文
     * @param title     标题
     * @param content   内容
     * @param okStr     确认
     * @param cancelStr 取消
     * @param listener  回调
     * @return 对话框
     */
    public static TSCommonDialog createCommonDialogNoCancel(Context context, String title, String content, String okStr, String cancelStr, IDialogCallback listener) {
        TSCommonDialog commonDialog = new TSCommonDialog(context, listener, title, content, okStr, cancelStr);
        commonDialog.setCancelable(false);
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.show();
        return commonDialog;
    }

    /**
     * 使用默认字体样式的通用对话框
     *
     * @param context    上下文
     * @param cancelable 是否可取消
     * @param title      标题
     * @param content    内容
     * @param okStr      确认
     * @param cancelStr  取消
     * @param listener   回调
     * @return 对话框
     */
    public static TSCommonDialog createVersionUpdateDialog(Context context, boolean cancelable, String title, String content, String okStr, String cancelStr, IDialogCallback listener) {
        TSCommonDialog commonDialog = new TSCommonDialog(context, listener, title, content, okStr, cancelStr);
        commonDialog.show();
        commonDialog.setCancelable(false);
        commonDialog.setCanceledOnTouchOutside(false);
        commonDialog.setVersionDes();

        if (cancelable) {
            commonDialog.setCancelBtnVisible(View.VISIBLE);
        } else {
            commonDialog.setCancelBtnVisible(View.GONE);
        }
        return commonDialog;
    }

}
