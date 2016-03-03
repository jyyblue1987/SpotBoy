/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.SettingsRecyclerViewAdapter;
import com.ninexe.ui.models.Settings;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.utils.dataproviders.SettingsDataProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 9/10/15.
 */
public class SettingsFragment extends BaseFragment {

    public final static String SETTINGS_FRAGMENT = "settings_fragment";

    @Bind(R.id.settings_recycler_view)
    RecyclerView mSettingsRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private SettingsRecyclerViewAdapter mSettingsRecyclerViewAdapter;
    private SettingsInteractionListener mListener;

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (SettingsInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement SettingsInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initSettingsRecyclerView();
        setSettingsRecyclerViewData();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewUtils.setThemeBackground(mToolbar);
    }

    private void setSettingsRecyclerViewData() {
        mSettingsRecyclerView.setAdapter(getSettingsRecyclerViewAdapter());
    }

    private void initSettingsRecyclerView() {
        mSettingsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSettingsRecyclerView.setHasFixedSize(true);
    }

    private SettingsRecyclerViewAdapter getSettingsRecyclerViewAdapter() {
        if (null == mSettingsRecyclerViewAdapter) {
            ArrayList<Settings> settingsArrayList = SettingsDataProvider.getInstance().getSettingsList();
            mSettingsRecyclerViewAdapter
                    = new SettingsRecyclerViewAdapter(settingsArrayList,
                    new SettingsRecyclerViewAdapter.SettingsRecyclerViewAdapterListener() {
                        @Override
                        public void onNotificationsClick() {
                            mListener.onNotificationsClick();
                        }

                        @Override
                        public void onThemeToggleSelection(boolean isChecked) {
                            mListener.onThemeToggleSelection(isChecked);
                        }

                        @Override
                        public void onTextSizeClick() {
                            mListener.onTextSizeClick();
                        }

                        @Override
                        public void onOfflineCachingSelection(boolean isChecked) {
                            mListener.onOfflineCachingSelection(isChecked);
                        }

                        @Override
                        public void onFeedbackClick() {
                            mListener.onFeedbackClick();
                        }

                        @Override
                        public void onRateAndReviewClick() {
                            mListener.onRateAndReviewClick();
                        }

                        @Override
                        public void onTermsAndConditionsClick() {
                            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                                mListener.onTermsAndConditionsClick();
                            } else {
                                DialogUtils.showNoNetworkDialog(getActivity());
                            }
                        }

                        @Override
                        public void onPrivacyPolicyClick() {
                            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                                mListener.onPrivacyPolicyClick();
                            } else {
                                DialogUtils.showNoNetworkDialog(getActivity());
                            }
                        }

                        @Override
                        public void onAboutUsClick() {
                            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                                mListener.onAboutUsClick();
                            } else {
                                DialogUtils.showNoNetworkDialog(getActivity());
                            }
                        }

                        @Override
                        public void onContactUsClick() {
                            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                                mListener.onContactUsClick();
                            } else {
                                DialogUtils.showNoNetworkDialog(getActivity());
                            }
                        }

                        @Override
                        public void onDisclaimerClick() {
                            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                                mListener.onDisclaimerClick();
                            } else {
                                DialogUtils.showNoNetworkDialog(getActivity());
                            }
                        }
                    });
        }
        return mSettingsRecyclerViewAdapter;
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(getString(R.string.settings));
        displaySearch(false);
        enableBackButton();
    }


    public void updateTheme() {
        ViewUtils.setThemeBackground(mToolbar);
    }

    public interface SettingsInteractionListener {
        void onNotificationsClick();

        void onThemeToggleSelection(boolean isChecked);

        void onTextSizeClick();

        void onOfflineCachingSelection(boolean isChecked);

        void onFeedbackClick();

        void onRateAndReviewClick();

        void onTermsAndConditionsClick();

        void onPrivacyPolicyClick();

        void onAboutUsClick();

        void onContactUsClick();

        void onDisclaimerClick();
    }
}
