package com.online.meeting.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.online.meeting.R
import com.online.meeting.databinding.ActivityRegisterBinding
import com.online.meeting.utils.AppLogger
import com.online.meeting.viewmodel.RegisterViewModel
import java.util.*

/**
 * 注册页面
 * @author ding
 * Created by Ding on 2018/8/14.
 */
class RegisterActivity : BaseActivity() {


    /**
     * ViewMode
     */
    var mViewMode: RegisterViewModel? = null

    /**
     * 数据绑定
     */
    var mDataBinding: ActivityRegisterBinding? = null

    /**
     * 初始化组件
     */
    override fun initViews() {

        setToolbarTitle(R.string.register)
    }

    /**
     * 初始化事件
     */
    override fun initEvent() {
    }

    /**
     * 初始化数据
     */
    override fun initData() {

        mViewMode = ViewModelProviders.of(this@RegisterActivity).get(RegisterViewModel::class.java)

        mDataBinding?.setLifecycleOwner(this@RegisterActivity)

        mDataBinding?.viewMode = mViewMode
    }


    /**
     * 左侧图标被点击
     */
    override fun onLeftIconClick() {

        AppLogger.i("* onleft click")
        finishCompat()
    }

    /**
     * 点击注册按钮
     */
    fun onRegisterBtnClick(view : View) {

        mDataBinding?.ilLearningSection?.error = "太短了"

        mViewMode?.leaningSection?.value = "" + Random().nextInt(100)

        mViewMode?.print()
    }

    /**
     * 获取设置contentView
     *
     * @return 控件
     */
    override fun getContentLayout(): View {

        mDataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_register, null, false)
        return mDataBinding!!.root
    }

}