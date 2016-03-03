/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
 * A simple {@link Fragment} subclass.
 */
public class ForgotPasswordFragment extends BaseFragment {


    @Bind(R.id.emailField)
    EditText mEmail;

    @Bind(R.id.retry_frame_container)
    View mRetryFrameContainer;

    @Bind(R.id.message)
    TextView mRetryMessage;

    @Bind(R.id.ninexe_logo)
    ImageView mNinexeLogo;

    @OnClick(R.id.btn_retry)
    void onRetryButtonClick() {
        onSend();
    }

    @OnClick(R.id.btn_back1)
    void onBack() {
        ViewUtils.hideSoftKeyboard(getActivity());
        mListener.onBack();
    }

    @OnClick(R.id.btn_send)
    void onSend() {
        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.enter_email));
            return;
        }
        if (!ViewUtils.isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.enter_valid_email));
            return;
        }
        sendEmail(mEmail.getText().toString());
    }

    private void sendEmail(String email) {
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            mRetryFrameContainer.setVisibility(View.GONE);
            DialogUtils.showCustomProgressDialog(getActivity(), true);
            NetworkAdapter.get(getActivity()).forgotPassword(email, new ResponseCallback<GenericResponse<GenericMessageModel>>() {
                @Override
                public void success(GenericResponse<GenericMessageModel> forgotPasswordResponse) {
                    DialogUtils.cancelProgressDialog();
                    if (forgotPasswordResponse.getGenericServerError() == null)
                        showDialog(forgotPasswordResponse.getData().getMessage(), true);
                    else
                        showDialog(forgotPasswordResponse.getGenericServerError().getMessage(), false);
                }

                @Override
                public void failure(RestError error) {
                    DialogUtils.cancelProgressDialog();
                }
            });
        } else {
            showRetryFrame(getString(R.string.no_network));
        }
    }

    private void showDialog(String message, final boolean redirectToLogin) {
        DialogUtils.showAlertDialogWithCallBack(getActivity(), ""
                , message, getString(R.string.ok), "", false, new DialogUtils.DialogInterfaceCallBack() {
            @Override
            public void positiveButtonClick(DialogInterface dialog) {
                if (redirectToLogin)
                    mListener.onBack();
            }

            @Override
            public void negativeButtonClick(DialogInterface dialog) {

            }
        });
    }

    private OnForgotPasswordInteractionListener mListener;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnForgotPasswordInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement OnRegisterFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, view);

        if(getResources().getBoolean(R.bool.isTablet)){
            mNinexeLogo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.welcome_screen_logo));
        }
        return view;
    }

    public interface OnForgotPasswordInteractionListener {
        void onBack();
    }

    private void showRetryFrame(String message) {
        showRetryContainer();
        mRetryMessage.setText(message);
    }

    private void showRetryContainer() {
        mRetryFrameContainer.setVisibility(View.VISIBLE);
    }


}
