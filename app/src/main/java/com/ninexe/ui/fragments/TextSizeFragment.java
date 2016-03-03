/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ninexe.ui.R;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class TextSizeFragment extends BaseFragment {


    public static final String TEXT_SIZE_FRAGMENT = "text_size_fragment";

    @Bind(R.id.text_size_very_small)
    Button mTextSizeVerySmall;


    @Bind(R.id.text_size_small)
    Button mTextSizeSmall;


    @Bind(R.id.text_size_medium)
    Button mTextSizeMedium;


    @Bind(R.id.text_size_large)
    Button mTextSizeLarge;


    @Bind(R.id.text_size_very_large)
    Button mTextSizeVeryLarge;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    public static TextSizeFragment newInstance() {
        TextSizeFragment textSizeFragment =
                new TextSizeFragment();
        return textSizeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_size, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        setTextSize();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewUtils.setThemeBackground(mToolbar);
    }

    private void setTextSize() {
        if (-1 != PreferenceManager.getWebViewTextSize(getContext())) {
            if (PreferenceManager.getWebViewTextSize(getContext()) ==
                    getResources().getInteger(R.integer.web_view_text_size_80)) {
                setSelectedState(mTextSizeVerySmall);
            } else if (PreferenceManager.getWebViewTextSize(getContext()) ==
                    getResources().getInteger(R.integer.web_view_text_size_90)) {
                setSelectedState(mTextSizeSmall);
            } else if (PreferenceManager.getWebViewTextSize(getContext()) ==
                    getResources().getInteger(R.integer.web_view_text_size_100)) {
                setSelectedState(mTextSizeMedium);
            } else if (PreferenceManager.getWebViewTextSize(getContext()) ==
                    getResources().getInteger(R.integer.web_view_text_size_110)) {
                setSelectedState(mTextSizeLarge);
            } else if (PreferenceManager.getWebViewTextSize(getContext()) ==
                    getResources().getInteger(R.integer.web_view_text_size_120)) {
                setSelectedState(mTextSizeVeryLarge);
            }
          /*  switch (PreferenceManager.getWebViewTextSize(getContext())) {
                case 80:
                    setSelectedState(mTextSizeVerySmall);
                    break;
                case 90:
                    setSelectedState(mTextSizeSmall);
                    break;
                case 100:
                    setSelectedState(mTextSizeMedium);
                    break;
                case 110:
                    setSelectedState(mTextSizeLarge);
                    break;
                case 120:
                    setSelectedState(mTextSizeVeryLarge);
                    break;
            }*/
        }
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(getString(R.string.title_text_size));
        displaySearch(false);
        enableBackButton();
    }

    @OnClick(R.id.text_size_very_small)
    void onClickTextSizeVerySmall() {
        PreferenceManager.storeWebViewTextSize(getContext(),
                getResources().getInteger(R.integer.web_view_text_size_80));
        setSelectedState(mTextSizeVerySmall);
    }

    @OnClick(R.id.text_size_small)
    void onClickTextSizeSmall() {
        PreferenceManager.storeWebViewTextSize(getContext(),
                getResources().getInteger(R.integer.web_view_text_size_90));
        setSelectedState(mTextSizeSmall);
    }

    @OnClick(R.id.text_size_medium)
    void onClickTextSizeMedium() {
        PreferenceManager.storeWebViewTextSize(getContext(),
                getResources().getInteger(R.integer.web_view_text_size_100));
        setSelectedState(mTextSizeMedium);
    }

    @OnClick(R.id.text_size_large)
    void onClickTextSizeLarge() {
        PreferenceManager.storeWebViewTextSize(getContext(),
                getResources().getInteger(R.integer.web_view_text_size_110));
        setSelectedState(mTextSizeLarge);
    }

    @OnClick(R.id.text_size_very_large)
    void onClickTextSizeVeryLarge() {
        PreferenceManager.storeWebViewTextSize(getContext(),
                getResources().getInteger(R.integer.web_view_text_size_120));
        setSelectedState(mTextSizeVeryLarge);
    }

    private void setSelectedState(Button button) {

        if (button == mTextSizeVerySmall) {
            mTextSizeVerySmall.setSelected(true);
        } else {
            mTextSizeVerySmall.setSelected(false);
        }

        if (button == mTextSizeSmall) {
            mTextSizeSmall.setSelected(true);
        } else {
            mTextSizeSmall.setSelected(false);
        }

        if (button == mTextSizeMedium) {
            mTextSizeMedium.setSelected(true);
        } else {
            mTextSizeMedium.setSelected(false);
        }

        if (button == mTextSizeLarge) {
            mTextSizeLarge.setSelected(true);
        } else {
            mTextSizeLarge.setSelected(false);
        }

        if (button == mTextSizeVeryLarge) {
            mTextSizeVeryLarge.setSelected(true);
        } else {
            mTextSizeVeryLarge.setSelected(false);
        }

    }
}
