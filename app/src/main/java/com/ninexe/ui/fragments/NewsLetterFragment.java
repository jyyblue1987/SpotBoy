/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ninexe.ui.R;
import com.ninexe.ui.models.GenericMessageModel;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 9/10/15.
 */
public class NewsLetterFragment extends BaseFragment {

    public static final String NEWS_LETTER_FRAGMENT = "news_letter_fragment";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.emailField)
    EditText mEmail;

    @OnClick(R.id.btnSend)
    void onSend() {
        mEmail.requestFocus();
        if (validateForm()) {
            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                sendEmail(mEmail.getText().toString());
            } else {
                DialogUtils.showNoNetworkDialog(getActivity());
            }
        }
    }

    private boolean validateForm() {
        String email = mEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmail.setError(getString(R.string.enter_email));
            return false;
        } else if (!ViewUtils.isValidEmail(email)) {
            mEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }
        return true;
    }

    public static NewsLetterFragment newInstance() {
        Bundle args = new Bundle();
        NewsLetterFragment fragment = new NewsLetterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_letter, container, false);
        ButterKnife.bind(this, view);
        ViewUtils.hideSoftKeyboard(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(getString(R.string.news_letter));
        displaySearch(false);
        enableBackButton();
    }

    private void sendEmail(String email) {
        DialogUtils.showCustomProgressDialog(getActivity(), true);
        NetworkAdapter.get(getActivity()).emailSubscribe(email, new ResponseCallback<GenericResponse<GenericMessageModel>>() {
            @Override
            public void success(GenericResponse<GenericMessageModel> forgotPasswordResponse) {
                DialogUtils.cancelProgressDialog();
                if (forgotPasswordResponse.getGenericServerError() == null) {
                    DialogUtils.showSingleButtonAlertDialog(getActivity(), "",
                            forgotPasswordResponse.getData().getMessage(), getString(R.string.ok));
                    mEmail.setText("");
                } else
                    DialogUtils.showSingleButtonAlertDialog(getActivity(), "",
                            forgotPasswordResponse.getGenericServerError().getMessage(), getString(R.string.ok));
            }

            @Override
            public void failure(RestError error) {
                DialogUtils.cancelProgressDialog();
                DialogUtils.showSingleButtonAlertDialog(getActivity(), null,
                        error.getServerErrorMessage(), getString(R.string.ok));
            }
        });
    }


}
