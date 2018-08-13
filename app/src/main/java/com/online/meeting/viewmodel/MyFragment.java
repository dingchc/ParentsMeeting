package com.online.meeting.viewmodel;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.online.meeting.R;

/**
 * @author Ding
 * Created by Ding on 2018/8/12.
 */

public class MyFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_body, container, false);

        final TextView tipTextView = view.findViewById(R.id.tv_tip);

        MyViewModel viewModel = ViewModelProviders.of(getActivity()).get(MyViewModel.class);

        MyClassInfo classInfo = viewModel.getClassInfoLiveData().getValue();

        if (classInfo != null) {
            tipTextView.setText(classInfo.className);
        }

        viewModel.getInputString().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String input) {

                tipTextView.setText(input);
            }
        });

        return view;

    }
}
