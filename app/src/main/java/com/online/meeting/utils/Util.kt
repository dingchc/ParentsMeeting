package com.online.meeting.utils

import android.content.Context

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
    }
}