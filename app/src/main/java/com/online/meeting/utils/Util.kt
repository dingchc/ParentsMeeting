package com.online.meeting.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import com.online.meeting.app.PMApplication
import com.online.meeting.R
import com.online.meeting.listener.IDialogCallback
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*

/**
 * 工具类
 *
 * @author ding
 * Created by Ding on 2018/8/13.
 */
class Util {

    companion object {

        fun dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        /**
         * 是否为 MI UI
         *
         * @return true 是、 false 否
         */
        fun isMiUI(): Boolean {

            var inputStream: InputStream? = null

            try {
                val properties = Properties()
                val file = File(Environment.getRootDirectory(), "build.prop")

                inputStream = FileInputStream(file)
                properties.load(inputStream)

                val miUiVersionCode = properties.getProperty("ro.miui.ui.version.code")
                val miUiVersionName = properties.getProperty("ro.miui.ui.version.name")

                return !TextUtils.isEmpty(miUiVersionCode) && !TextUtils.isEmpty(miUiVersionName)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            return false
        }

        /**
         * 获取包名
         *
         * @param context 上下文
         * @return 包名
         */
        fun getPackageName(context: Context?): String? {

            return context?.packageName
        }

        /**
         * 当授权失败后，反馈用户，是否进入设置页面
         *
         * @param title          标题
         * @param message        消息
         * @param dialogCallback 回调
         */
        fun showPermissionFailedDialog(activity: Activity, title: String, message: String, dialogCallback: IDialogCallback?) {

            AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialog, which ->
                if (dialogCallback != null) {
                    dialogCallback.onConfirm()
                } else {
                    MPermissionUtil.goSettingAppDetail(activity)
                }
            }).setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, which ->
                if (dialogCallback != null) {
                    dialogCallback.onCancel()
                }
            }).setCancelable(false).show()
        }

        /**
         * 当授权失败后，反馈用户，是否进入设置页面
         *
         * @param title 标题
         * @param message 信息
         */
        fun showOpsPermissionDenyDialog(activity: Activity, title: String, message: String) {

            AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(R.string.confirm, DialogInterface.OnClickListener { dialog, which -> }).setCancelable(false).show()
        }

        /**
         * 检查对象是否为空
         *
         * @param obj 对象
         * @return true 不为空、 false为空
         */
        fun checkObjNotNull(obj: Any?): Boolean {

            return obj != null
        }

        /**
         * 检查list是否为空
         *
         * @param dataList 数据表
         * @return true 不为空、 false为空
         */
        fun checkListNotNull(dataList: List<*>?): Boolean {

            return dataList != null
        }

        /**
         * 检查list size是否大于0
         *
         * @param dataList 数据表
         * @return true 大于0、 false 小于等于0
         */
        fun checkListNotEmpty(dataList: List<*>): Boolean {

            return checkListNotNull(dataList) && dataList.size > 0
        }

        /**
         * 检查Map非空
         *
         * @param map 数据Map
         * @return true 非空、 false 空
         */
        fun checkMapNotNull(map: Map<*, *>?): Boolean {

            return map != null
        }

        /**
         * 检查Map size是否大于0
         *
         * @param map 数据Map
         * @return true 大于0、 false 小于等于0
         */
        fun checkMapNotEmpty(map: Map<*, *>): Boolean {

            return checkMapNotNull(map) && map.size > 0
        }

        /**
         * 检查文件是否存在
         *
         * @param filePath 文件路径
         * @return true 文件存在、 false 文件不存在
         */
        fun checkFileExist(filePath: String): Boolean {

            var ret = false
            if (!TextUtils.isEmpty(filePath)) {
                val file = File(filePath)
                ret = file.exists() && file.isFile
            }

            return ret
        }

        /**
         * 获取颜色
         *
         * @param color 颜色资源值
         * @return 颜色Int值
         */
        fun getColorCompat(@ColorRes color: Int): Int {
            return ContextCompat.getColor(PMApplication.instance!!, color)

        }
    }


}