/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.PreferenceManager;

/**
 * Created by nagesh on 19/11/15.
 */
public class TutorialScreenFragment extends BaseFragment {
    private static final String ARG_SCREEN_NUM = "arg_screen_num";

    public interface WelcomeScreens {
        int WELCOME_SCREEN_0 = 0;
        int WELCOME_SCREEN_1 = 1;
    }

    private int mScreenNUm;

    public static TutorialScreenFragment newInstance(int screenNum) {
        TutorialScreenFragment fragment = new TutorialScreenFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SCREEN_NUM, screenNum);
        fragment.setArguments(args);
        return fragment;
    }

    public TutorialScreenFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mScreenNUm = getArguments().getInt(ARG_SCREEN_NUM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getWelcomeScreen(mScreenNUm, container);
        return view;
    }

    private View getWelcomeScreen(int screenNUm, ViewGroup container) {
        View view = null;
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        switch (screenNUm) {
            case WelcomeScreens.WELCOME_SCREEN_0:
                view = inflater.inflate(R.layout.welcome_screen_1, container, false);
                break;
            case WelcomeScreens.WELCOME_SCREEN_1:
                view = inflater.inflate(R.layout.welcome_screen_2, container, false);
                break;
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != getView().findViewById(R.id.lets_get_started_container)) {
            getView().findViewById(R.id.lets_get_started_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PreferenceManager.setIsTutorialScreenShown(getActivity(), true);
                    NavigationUtils.startHomeActivity(getActivity());
                    getActivity().finish();
                    LogUtils.LOGD(Constants.APP_TAG, "Clicked on Lets get started");
                }
            });
        }
    }
}
