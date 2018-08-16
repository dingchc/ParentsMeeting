package com.online.meeting.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.widget.Toast;

import com.online.meeting.R;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查OPS权限
 *
 * @author ding
 *         Created by ding on 18/01/2018.
 */
public class CheckOpsUtil {

    /**
     * OPS检查类型-系统提示框
     */
    public static final int OP_TYPE_NOTE = 1;

    /**
     * OPS检查类型-只检查
     */
    public static final int OP_TYPE_CHECK = 2;

    /**
     * OPS权限 - 粗略位置
     */
    public static final int OP_COARSE_LOCATION = 0;

    /**
     * OPS权限 - 细致位置
     */
    public static final int OP_FINE_LOCATION = 1;

    /**
     * OPS权限 - 电话
     */
    private static final int OP_CALL_PHONE = 13;

    /**
     * OPS权限 - 读取短信
     */
    private static final int OP_READ_SMS = 14;

    /**
     * OPS权限 - 相机
     */
    private static final int OP_CAMERA = 26;

    /**
     * OPS权限 - 录音
     */
    private static final int OP_RECORD_AUDIO = 27;

    /**
     * OPS权限 - 读取电话
     */
    private static final int OP_READ_PHONE_STATE = 51;

    /**
     * 权限与OP对应表
     */
    private static Map<String, Integer> opMap = new HashMap<>();

    static {

        //TODO 读取及写入外部存储器，是不需要申请权限的

        opMap.put(Manifest.permission.CAMERA, OP_CAMERA);
        opMap.put(Manifest.permission.RECORD_AUDIO, OP_RECORD_AUDIO);
        opMap.put(Manifest.permission.READ_PHONE_STATE, OP_READ_PHONE_STATE);
        opMap.put(Manifest.permission.CALL_PHONE, OP_CALL_PHONE);
        opMap.put(Manifest.permission.READ_SMS, OP_READ_SMS);
        opMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, OP_COARSE_LOCATION);
        opMap.put(Manifest.permission.ACCESS_FINE_LOCATION, OP_FINE_LOCATION);

    }

    /**
     * 检查权限，并弹出
     *
     * @param context     上下文
     * @param permissions 权限
     * @return true 有权限，false 没有权限
     */
    public static boolean checkOrNoteOpPermission(Context context, int opType, String[] permissions) {

        if (context == null || permissions == null) {
            return false;
        }

        for (String permission : permissions) {

            Integer op = opMap.get(permission);

            if (op != null && !checkOrNoteOp(context, opType, op)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查OPS权限，需要SDK >= 19
     *
     * @param context 上下文
     * @return 0 - 有权限、 1 - 无权限、 3 - 询问
     */
    public static boolean checkOrNoteOp(Context context, int opType, int op) {

        if (context == null) {
            return false;
        }

        // 检查OPS权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            int opsRet = checkOpPermission(context, opType, op);

            // 对话框出现
            if (opsRet != AppOpsManager.MODE_ALLOWED && opsRet != AppOpsManager.MODE_IGNORED) {
                return false;
            } else if (opsRet == AppOpsManager.MODE_IGNORED) {
                showErrorToast(context, op);
                return false;
            }
        }

        return true;
    }

    /**
     * 提示未获取权限
     *
     * @param context 上下文
     * @param op      OP权限值
     */
    private static void showErrorToast(Context context, int op) {

        String tip = "";

        switch (op) {
            case OP_CAMERA:
                tip = "相机";
                break;
            case OP_RECORD_AUDIO:
                tip = "录音";
                break;
            case OP_READ_SMS:
                tip = "短信";
                break;
            case OP_COARSE_LOCATION:
            case OP_FINE_LOCATION:
                tip = "位置";
                break;
            case OP_READ_PHONE_STATE:
            case OP_CALL_PHONE:
                tip = "电话";
                break;
            default:
        }
        Toast.makeText(context, context.getString(R.string.no_ops_permission, tip), Toast.LENGTH_SHORT).show();
    }


    /**
     * 检查OPS权限，需要SDK >= 19
     *
     * @param context 上下文
     * @param type    检查方式 1 - check、 2 - note（出现系统提示框）
     * @param op      ops权限整数
     * @return 0 - 有权限、 1 - 无权限、 3 - 询问
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static int checkOpPermission(Context context, final int type, final int op) {

        final int version = Build.VERSION.SDK_INT;

        String operate = (type == OP_TYPE_NOTE ? "noteOp" : "checkOp");

        if (version >= Build.VERSION_CODES.KITKAT) {

            Object object = context.getSystemService(Context.APP_OPS_SERVICE);

            if (object != null) {

                Class c = object.getClass();
                try {
                    Class[] cArg = new Class[3];
                    cArg[0] = int.class;
                    cArg[1] = int.class;
                    cArg[2] = String.class;
                    Method method = c.getDeclaredMethod(operate, cArg);
                    return (Integer) method.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return AppOpsManager.MODE_IGNORED;
    }


}
