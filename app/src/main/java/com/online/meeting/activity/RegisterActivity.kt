package com.online.meeting.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import com.online.meeting.R
import com.online.meeting.databinding.ActivityRegisterBinding

/**
 * 注册页面
 * @author ding
 * Created by Ding on 2018/8/14.
 */
class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ActivityRegisterBinding = DataBindingUtil.setContentView<ActivityRegisterBinding>(this@RegisterActivity, R.layout.activity_register)

    }
}