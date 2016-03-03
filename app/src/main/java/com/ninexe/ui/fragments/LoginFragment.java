/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.ninexe.ui.R;
import com.ninexe.ui.activities.FacebookActivity;
import com.ninexe.ui.activities.GooglePlusActivity;
import com.ninexe.ui.activities.TwitterActivity;
import com.ninexe.ui.analytics.AnalyticsConstants;
import com.ninexe.ui.analytics.NixonTracker;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.AccessTokenModel;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.LoginDataModel;
import com.ninexe.ui.models.LoginUserModel;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.ViewUtils;
import com.ninexe.ui.utils.dataproviders.ReactionDataHandler;
import com.ninexe.ui.utils.dataproviders.UserDataProvider;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {

    private static final int FACEBOOK_LOGIN = 1;
    private static final int GPLUS_LOGIN = 2;
    private static final int TWITTER_LOGIN = 3;
    private static final int GPLUS_ACCOUNT_PERMISSION = 4;
    private static boolean isLoginFailed = false;
    private OnLoginFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.btn_skip)
    TextView mSkip;

    @Bind(R.id.btnBack)
    ImageView mBack;

    @Bind(R.id.ninexe_logo)
    ImageView mNinexeLogo;

    @Bind(R.id.emailField)
    EditText mEmail;

    @Bind(R.id.passwordField)
    EditText mPassword;

    @OnClick(R.id.facebook)
    void facebookLogin() {
        ViewUtils.hideSoftKeyboard(getActivity());
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_SOCIAL_LOGIN_FACEBOOK, null);
            Intent intent = new Intent(getActivity(), FacebookActivity.class);
            startActivityForResult(intent, FACEBOOK_LOGIN);
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    @OnClick(R.id.google_plus)
    void gPlusLoginClick() {
        if (PackageManager.PERMISSION_DENIED != ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.GET_ACCOUNTS)) {
            gplusLogin();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    GPLUS_ACCOUNT_PERMISSION);
        }
    }

    public void gplusLogin() {
        ViewUtils.hideSoftKeyboard(getActivity());
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode == ConnectionResult.SUCCESS) {
            if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
                NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_SOCIAL_LOGIN_GPLUS, null);
                Intent intent = new Intent(getActivity(), GooglePlusActivity.class);
                startActivityForResult(intent, GPLUS_LOGIN);
            } else {
                DialogUtils.showNoNetworkDialog(getActivity());
            }
        } else {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                LogUtils.LOGD("Google Plus Login", "Play services not available");
                apiAvailability.getErrorDialog(getActivity(), resultCode, 9000).show();
            }
        }
    }

    @OnClick(R.id.twitter)
    void onTwitterLogin() {
        ViewUtils.hideSoftKeyboard(getActivity());
        if (NetworkCheckUtility.isNetworkAvailable(getActivity())) {
            NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_SOCIAL_LOGIN_TWITTER, null);
            Intent intent = new Intent(getActivity(), TwitterActivity.class);
            startActivityForResult(intent, TWITTER_LOGIN);
        } else {
            DialogUtils.showNoNetworkDialog(getActivity());
        }
    }

    @OnClick(R.id.btn_sign_in)
    void signIn() {
        if (validateForm()) {
            ViewUtils.hideSoftKeyboard(getActivity());
            if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
                NixonTracker.get().logScreenVisits(AnalyticsConstants.EVENT_TAP_APP_LOGIN, null);
                DialogUtils.showCustomProgressDialog(getActivity(), true);
                NetworkAdapter.get(getContext()).login(getUserLoginModel(), new ResponseCallback<GenericResponse<LoginDataModel>>() {
                    @Override
                    public void success(GenericResponse<LoginDataModel> loginDataModel) {
                        DialogUtils.cancelProgressDialog();
                        UserDataProvider.getInstance().storeUserDetails(loginDataModel.getData(), getActivity());
                        mListener.onLogin();
                    }

                    @Override
                    public void failure(RestError error) {
                        isLoginFailed = true;
                        if (null != error) {
                            DialogUtils.cancelProgressDialog();
                            if (null == error.getServerErrorMessage()) {
                                DialogUtils.showSingleButtonAlertDialog(getActivity(), null,
                                        getString(R.string.please_try_later), getString(R.string.ok));
                            } else {
                                DialogUtils.showSingleButtonAlertDialog(getActivity(), null,
                                        error.getServerErrorMessage(), getString(R.string.ok));
                            }
                        } else {
                            DialogUtils.showSingleButtonAlertDialog(getActivity(), null,
                                    getString(R.string.please_try_later), getString(R.string.ok));
                        }
                    }
                });
            } else {
                DialogUtils.showNoNetworkDialog(getActivity());
            }
        }
    }

    @OnClick(R.id.btn_register)
    void onRegister() {
        if (isLoginFailed) {
            clearTextFields();
        }
        ViewUtils.hideSoftKeyboard(getActivity());
        disableError();
        mListener.onRegisterSelected();
    }

    @OnClick(R.id.btn_skip)
    void onSkip() {
        mListener.onSkip();
    }

    @OnClick(R.id.btnBack)
    void onBack() {
        mListener.onBack();
    }

    @OnClick(R.id.forgotPassword)
    void onForgotPassword() {
        ViewUtils.hideSoftKeyboard(getActivity());
        mListener.onForgotPassword();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoginFailed = false;
    }

    private LoginUserModel getUserLoginModel() {
        LoginUserModel loginUserModel = new LoginUserModel();
        loginUserModel.setEmail(mEmail.getText().toString());
        loginUserModel.setPassword(mPassword.getText().toString());
        return loginUserModel;
    }

    private void clearTextFields() {
        mEmail.setText("");
        mPassword.setText("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnLoginFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);

        if (PreferenceManager.getBoolean(getActivity(), Constants.SP_LOGIN_SKIPPED, false)) {
            mSkip.setVisibility(View.GONE);
            mBack.setVisibility(View.VISIBLE);
        }

        if (getResources().getBoolean(R.bool.isTablet)) {
            mNinexeLogo.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.welcome_screen_logo));
        }

        return view;
    }

    public interface OnLoginFragmentInteractionListener {
        void onRegisterSelected();

        void onSkip();

        void onBack();

        void onForgotPassword();

        void onLogin();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AccessTokenModel accessTokenModel = new AccessTokenModel();
        switch (requestCode) {
            case FACEBOOK_LOGIN:
                if (null != data) {
                    accessTokenModel.setAccess_token(data.getStringExtra(Constants.ACCESS_TOKEN));
                    sendAccessToken(accessTokenModel, Constants.FACEBOOK);
                }
                break;
            case GPLUS_LOGIN:
                if (null != data) {
                    accessTokenModel.setAccess_token(data.getStringExtra(Constants.ACCESS_TOKEN));
                    sendAccessToken(accessTokenModel, Constants.GPLUS);
                }
                break;
            case TWITTER_LOGIN:
                if (null != data) {
                    accessTokenModel.setAccess_token(data.getStringExtra(Constants.ACCESS_TOKEN));
                    accessTokenModel.setOauth_token_secret(data.getStringExtra(Constants.TWITTER_SECRET_TOKEN));
                    accessTokenModel.setUser_id(data.getStringExtra(Constants.TWITTER_USER_ID));
                    sendAccessToken(accessTokenModel, Constants.TWITTER);
                }
                break;
        }

    }


    private void sendAccessToken(AccessTokenModel accessTokenModel, String socialPlatform) {
        DialogUtils.showCustomProgressDialog(getActivity(), true);
        NetworkAdapter.get(getContext()).socialLogin(socialPlatform, accessTokenModel,
                new ResponseCallback<GenericResponse<LoginDataModel>>() {
                    @Override
                    public void success(GenericResponse<LoginDataModel> loginDataModel) {
                        DialogUtils.cancelProgressDialog();
                        UserDataProvider.getInstance().storeUserDetails(loginDataModel.getData(), getActivity());
                        mListener.onLogin();
                    }

                    @Override
                    public void failure(RestError error) {
                        DialogUtils.cancelProgressDialog();
                    }
                });
    }

    private boolean validateForm() {
        boolean isValid = true;
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            mPassword.setError(getResources().getString(R.string.enter_password));
            isValid = false;
        } else if (mPassword.getText().toString().length() < 5) {
            mPassword.setError(getResources().getString(R.string.enter_valid_password));
            isValid = false;
        }
        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError(getResources().getString(R.string.enter_email));
            isValid = false;
        } else if (!ViewUtils.isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getString(R.string.enter_valid_email));
            isValid = false;
        }
        return isValid;
    }


    private void disableError() {
        mEmail.setError(null);
        mPassword.setError(null);
    }
}
