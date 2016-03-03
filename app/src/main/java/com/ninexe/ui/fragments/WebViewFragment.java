/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ninexe.ui.R;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 26/10/15.
 */
public class WebViewFragment extends BaseFragment {

    public static final String WEB_VIEW_FRAGMENT = "web_view_fragment";

    private static final String ARG_URL = "arg_url";
    private static final String ARG_TOOLBAR_TITLE = "arg_toolbar_title";

    private String mUrl;
    private String mToolbarTitleParam;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.web_view)
    WebView mWebView;


    public static WebViewFragment newInstance(String url, String toolbarTitle) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_TOOLBAR_TITLE, toolbarTitle);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUrl = getArguments().getString(ARG_URL);
            mToolbarTitleParam = getArguments().getString(ARG_TOOLBAR_TITLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        loadWebView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewUtils.setThemeBackground(mToolbar);
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(mToolbarTitleParam);
        displaySearch(false);
        enableBackButton();
    }

    private void loadWebView() {
        if (null != mUrl) {
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    DialogUtils.cancelProgressDialog();
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        getActivity().startActivity(i);
                    } else {
                        DialogUtils.showNoNetworkDialog(getActivity());
                    }
                    return true;
                }
            });
            mWebView.loadUrl(mUrl);
            mWebView.setWebChromeClient(new WebChromeClient());
            mWebView.getSettings().setJavaScriptEnabled(true);
        }
    }
}
