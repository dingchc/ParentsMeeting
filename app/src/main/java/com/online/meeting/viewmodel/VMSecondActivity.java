package com.online.meeting.viewmodel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.online.meeting.R;
import com.online.meeting.activity.BaseActivity;
import com.online.meeting.databinding.ActivityFirstBinding;
import com.online.meeting.utils.AppLogger;

import java.util.Random;

/**
 * @author Ding
 *         Created by Ding on 2018/8/8.
 */

public class VMSecondActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

//        mViewMode?.operate?.observe(this@RegisterActivity, object : Observer<String> {
//
//            override fun onChanged(t: String?) {
//
//                AppLogger.i("t=" + t)
//            }
//        })
    }


}