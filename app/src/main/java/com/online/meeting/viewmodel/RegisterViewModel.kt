package com.online.meeting.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.online.meeting.utils.AppLogger

/**
 * 注册的ViewModel
 *
 * @author ding
 * Created by Ding on 2018/8/15.
 */
class RegisterViewModel(app: Application) : AndroidViewModel(app) {

    /**
     * 学段
     */
    val leaningSection: MutableLiveData<String> = MutableLiveData()

    /**
     * 入学年龄
     */
    val leaningYear: MutableLiveData<String> = MutableLiveData()

    /**
     * 学生姓名
     */
    val studentName: MutableLiveData<String> = MutableLiveData()

    /**
     * 选择角色
     */
    val selectRole: MutableLiveData<String> = MutableLiveData()


}