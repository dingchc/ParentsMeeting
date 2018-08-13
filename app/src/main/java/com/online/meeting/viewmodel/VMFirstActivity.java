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
import com.online.meeting.databinding.ActivityFirstBinding;
import com.online.meeting.utils.AppLogger;


import java.util.Random;

/**
 * @author Ding
 *         Created by Ding on 2018/8/8.
 */

public class VMFirstActivity extends AppCompatActivity {

    private String mName = "";

    private MyViewModel mViewModel;

    private Button mClickBtn;

    private TextView mTipTextView;

    private View mLLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFirstBinding activityFirstBinding = DataBindingUtil.setContentView(this, R.layout.activity_first);

        mClickBtn = findViewById(R.id.btn_click);
        mTipTextView = findViewById(R.id.tv_tip);
        mLLayout = findViewById(R.id.ll_body);

        mViewModel = ViewModelProviders.of(this).get(MyViewModel.class);

        activityFirstBinding.setMyViewModel(mViewModel);
        activityFirstBinding.setLifecycleOwner(this);

        MyClassInfo myClassInfo = mViewModel.getClassInfoLiveData().getValue();

        if (myClassInfo != null) {
            mTipTextView.setText(myClassInfo.className);
        }

        mViewModel.getClassInfoLiveData().observe(this, new Observer<MyClassInfo>() {
            @Override
            public void onChanged(@Nullable MyClassInfo myClassInfo) {

                Log.i("dcc", myClassInfo.className);

                mTipTextView.setText(myClassInfo.className);
            }
        });

        mClickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Random random = new Random();
                int classId = random.nextInt(Integer.MAX_VALUE);

                mViewModel.getClassInfoLiveData().postValue(new MyClassInfo(classId, "第" + classId + "班"));
            }
        });

        activityFirstBinding.edInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                AppLogger.i("inputString = " + mViewModel.getInputString().getValue());
            }
        });

        initFragment();
    }

    private void initFragment() {

        FragmentManager manager = getSupportFragmentManager();

        Fragment fragment = manager.findFragmentById(R.id.ll_body);

        if (fragment == null) {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.add(R.id.ll_body, new MyFragment());
            fragmentTransaction.commit();
        }




    }
}