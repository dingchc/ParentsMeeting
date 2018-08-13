package com.online.meeting.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Random;

/**
 * 我的ViewModel
 *
 * @author Ding
 *         Created by Ding on 2018/8/8.
 */

public class MyViewModel extends AndroidViewModel {

    private MutableLiveData<MyClassInfo> classInfoMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<String> inputString = new MutableLiveData<>();

    public MyViewModel(@NonNull Application application) {
        super(application);
        inputString.setValue("hello");
    }

    public void setClassInfo(MyClassInfo classInfo) {
        classInfoMutableLiveData.setValue(classInfo);
    }

    public MutableLiveData<MyClassInfo> getClassInfoLiveData() {
        return classInfoMutableLiveData;
    }

    public MutableLiveData<String> getInputString() {
        return inputString;
    }

    public void setInputString(MutableLiveData<String> inputString) {
        this.inputString = inputString;
    }

    public void onMyClick() {
        Log.i("dcc", "onMyClick");
        inputString.setValue("onMyClick " + new Random().nextInt(100));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i("dcc", "onCleared");
    }
}
