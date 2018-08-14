package com.online.meeting.activity

import android.os.Bundle
import android.support.v4.widget.SlidingPaneLayout
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.online.meeting.R
import com.online.meeting.utils.AppLogger
import com.online.meeting.widget.PageSlidingPaneLayout
import com.online.meeting.widget.TSToolbar
import com.online.meeting.widget.TSToolbar.ToolbarClickListener

/**
 * Activity父类
 *
 * @author ding
 * Created by Ding on 2018/8/13.
 */
abstract class BaseActivity : AppCompatActivity(), SlidingPaneLayout.PanelSlideListener, ToolbarClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {

        initSwipeBack()

        super.onCreate(savedInstanceState)
    }

    // ######################################## callback fun #######################################

    override fun onPanelOpened(panel: View) {
        AppLogger.i("onPanelOpened ")
    }

    override fun onPanelClosed(panel: View) {

        AppLogger.i("onPanelClosed")
//        finish()
//        overridePendingTransition(0, R.anim.scale_out)
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {
        AppLogger.i("onPanelSlide " + slideOffset)
    }

    /**
     * Toolbar点击
     * @param type 类型
     */
    override fun onToolbarClick(type: Int) {

    }

    // ######################################## private fun ########################################

    /**
     * 是否支持滑动返回
     * @return true 支持滑动返回、false 不支持
     */
    private fun isSupportSwipeBack(): Boolean {
        return false
    }

    /**
     * 初始化滑动返回
     */
    private fun initSwipeBack() {

        if (isSupportSwipeBack()) {

            val slidingPaneLayout = PageSlidingPaneLayout(this)

            try {
                //属性
                val overHang = SlidingPaneLayout::class.java.getDeclaredField("mOverhangSize")
                overHang.isAccessible = true
                overHang.set(slidingPaneLayout, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            slidingPaneLayout.setPanelSlideListener(this@BaseActivity)
            slidingPaneLayout.sliderFadeColor = resources.getColor(android.R.color.transparent)
            slidingPaneLayout.setShadowDrawableLeft(resources.getDrawable(R.drawable.sliding_left_shadow))

            val leftView = View(this)
            leftView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            slidingPaneLayout.addView(leftView, 0)

            val decor = window.decorView as ViewGroup
            val decorChild = decor.getChildAt(0) as ViewGroup
            decorChild.setBackgroundColor(resources.getColor(android.R.color.white))
            decor.removeView(decorChild)
            decor.addView(slidingPaneLayout)
            slidingPaneLayout.addView(decorChild, 1)
        }
    }
}