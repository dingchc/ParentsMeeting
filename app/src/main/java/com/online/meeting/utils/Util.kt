package com.online.meeting.utils

import android.content.Context
import android.os.Environment
import android.text.TextUtils
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
    }


}