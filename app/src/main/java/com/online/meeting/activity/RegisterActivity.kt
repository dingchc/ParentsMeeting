package com.online.meeting.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.online.meeting.R
import com.online.meeting.databinding.ActivityRegisterBinding
import com.online.meeting.utils.AppLogger
import com.online.meeting.viewmodel.RegisterViewModel

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


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this@RegisterActivity, R.layout.activity_register)

        initToolbar()

        setToolbarTitle(R.string.register)

        mViewMode = ViewModelProviders.of(this@RegisterActivity).get(RegisterViewModel::class.java)

        mDataBinding?.setLifecycleOwner(this@RegisterActivity)

        mDataBinding?.viewMode = mViewMode



    }

    /**
     * 初始化组件
     */
    override fun initViews() {
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
    }

}