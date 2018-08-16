package com.online.meeting.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 权限检查
 * @author ding
 * Created by ding on 1/12/17.
 */

public class MPermissionUtil {

    /**
     * 权限及请求Code对应表
     */
    public enum PermissionRequest {

        // 相机
        CAMERA(1001, Manifest.permission.CAMERA),
        // 录音
        AUDIO_RECORD(1002, Manifest.permission.RECORD_AUDIO),
        // 打电话及获取电话状态
        PHONE(1003, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE),
        // 读取设备编号
        READ_PHONE_STATE(1004, Manifest.permission.READ_PHONE_STATE),
        // 读写存储卡
        READ_WRITE_STORAGE(1005, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
        // 读短信
        READ_SMS(1006, Manifest.permission.READ_SMS),
        // 视频
        VIDEO(1007, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA),
        // 位置
        LOCATION(1008, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
        // 保存图片
        SAVE_IMAGE(1009, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        private int requestCode;
        private String[] permissions;

        PermissionRequest(int requestCode, String... permissions) {

            this.permissions = permissions;
            this.requestCode = requestCode;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public String[] getPermissions() {
            return permissions;
        }
    }

    /**
     * 判断是否权限已授权
     *
     * @param permissionRequest 权限
     * @param context          活动
     * @return true 已授权、 false 未授权
     */
    public static boolean checkPermission(Context context, PermissionRequest permissionRequest) {

        boolean ret = true;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT || permissionRequest == null || context == null) {

            AppLogger.i(AppLogger.TAG, "permission don't check");
            return true;
        }

        // 4.4以上， 5.1以下的系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {

            // 检查OP权限
            if (!CheckOpsUtil.checkOrNoteOpPermission(context, CheckOpsUtil.OP_TYPE_NOTE, permissionRequest.getPermissions())) {
                ret = false;
            }
        }
        // 6.0 以上的权限
        else {
            // 判断是否已经获取全部授权
            if (!isAllPermissionsGranted(context, permissionRequest.getPermissions())) {

                requestPermission(context, permissionRequest);

                ret = false;
            } else {
                AppLogger.i(AppLogger.TAG, "permission granted");
                ret = true;
            }
        }

        return ret;

    }

    /**
     * 判断是否已经获得权限的授权
     *
     * @param context    活动
     * @param permissions 权限
     * @return true 已授权、 false 未授权
     */
    private static boolean isAllPermissionsGranted(Context context, String[] permissions) {

        if (permissions == null || permissions.length <= 0) {
            return true;
        }

        for (String permission : permissions) {

            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

                return false;
            }
        }

        return true;

    }

    /**
     * 申请权限授权
     *
     * @param context          上下文（这里特指Activity）
     * @param permissionRequest 权限请求
     */
    protected static void requestPermission(Context context, PermissionRequest permissionRequest) {

        AppLogger.i(AppLogger.TAG, "requestPermission");

        if (context instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) context, permissionRequest.getPermissions(), permissionRequest.getRequestCode());
        }

    }

    /**
     * 判断是否全部权限已获取
     *
     * @param grantResults 权限结果
     * @return true 已授权、false 未授权
     */
    public static boolean hasAllPermissionsGranted(int[] grantResults) {

        if (grantResults != null && grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * 跳转到系统应用详情页面，方便用户设定权限
     *
     * @param context 上下文
     */
    public static void goSettingAppDetail(Context context) {

        if (context != null) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + Util.Companion.getPackageName(context)));
            context.startActivity(intent);
        }
    }
}
