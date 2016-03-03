/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.LinkMovementMethod;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.StaticUrlData;
import com.ninexe.ui.models.UserRegistration;
import com.ninexe.ui.models.UserRegistrationData;
import com.ninexe.ui.network.NetworkAdapter;
import com.ninexe.ui.network.ResponseCallback;
import com.ninexe.ui.network.RestError;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.ViewUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.mime.TypedFile;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_PICKER = 102;
    private OnRegisterFragmentListener mListener;

    @Bind(R.id.btn_back)
    ImageView mBack;

    @Bind(R.id.camera)
    ImageView mCamera;

    @Bind(R.id.gallery)
    ImageView mGallery;

    @Bind(R.id.nameField)
    EditText mName;

    @Bind(R.id.emailField)
    EditText mEmail;

    @Bind(R.id.passwordField)
    EditText mPassword;

    @Bind(R.id.mobileNumField)
    EditText mMobileNumber;

    @Bind(R.id.btn_toggle)
    ToggleButton mTACBtn;

    @OnClick(R.id.tac)
    void termsAndConditions() {
        mListener.onTermsAndConditionsClick();
    }


    @OnClick(R.id.btn_register)
    void onRegister() {
        if (validateForm()) {
            ViewUtils.hideSoftKeyboard(getActivity());
            if (NetworkCheckUtility.isNetworkAvailable(getContext())) {
                DialogUtils.showCustomProgressDialog(getActivity(), true);
                ResponseCallback<GenericResponse<UserRegistrationData>> responseCallback = new ResponseCallback<GenericResponse<UserRegistrationData>>() {
                    @Override
                    public void success(GenericResponse<UserRegistrationData> userRegistrationResponse) {
                        if (getActivity() != null && isAdded()) {
                            DialogUtils.cancelProgressDialog();
                            if (null == userRegistrationResponse.getGenericServerError()) {
                                DialogUtils.showSingleButtonAlertDialog(getActivity(), null,
                                        getResources().getString(R.string.register_success),
                                        getResources().getString(R.string.ok), new DialogUtils.SingleButtonDialogListener() {
                                            @Override
                                            public void onButtonClick() {
                                                mListener.onBack();
                                            }
                                        }, false);
                            } else {
                                DialogUtils.showSingleButtonAlertDialog(getActivity(),
                                        null, userRegistrationResponse.getGenericServerError().getMessage(), getResources().getString(R.string.ok));
                            }
                        }
                    }

                    @Override
                    public void failure(RestError error) {
                        LogUtils.LOGD(Constants.APP_TAG, "failure");
                        if (getActivity() != null && isAdded()) {
                            //TODO: handle failure
                            DialogUtils.cancelProgressDialog();
                        }
                    }
                };
                NetworkAdapter.get(getContext()).registerUser(getUserRegistrationModel(), responseCallback);
            } else {
                DialogUtils.showNoNetworkDialog(getActivity());
            }
        }
    }

    private UserRegistration getUserRegistrationModel() {
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setEmail(mEmail.getText().toString());
        userRegistration.setName(mName.getText().toString());
        userRegistration.setPassword(mPassword.getText().toString());
        userRegistration.setPhone(mMobileNumber.getText().toString());
        if (null != mImageFile)
            userRegistration.setProfilePic(new TypedFile("application/octet-stream", mImageFile));
        return userRegistration;
    }


    private File mImageFile;


    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_back:
                    ViewUtils.hideSoftKeyboard(getActivity());
                    mListener.onBack();
                    break;
                case R.id.gallery:
                    ViewUtils.hideSoftKeyboard(getActivity());
                    launchImagePicker();
                    break;
                case R.id.camera:
                    ViewUtils.hideSoftKeyboard(getActivity());
                    takePhoto();
                    break;
            }
        }
    };

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnRegisterFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity()
                    + " must implement OnRegisterFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        mBack.setOnClickListener(mClickListener);
        mCamera.setOnClickListener(mClickListener);
        mGallery.setOnClickListener(mClickListener);


        return view;
    }

    public interface OnRegisterFragmentListener {
        void onBack();

        void onTermsAndConditionsClick();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap imageBitmap = null;
        if (null != data)
            switch (requestCode) {
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = data.getExtras();
                    if (null != extras) {
                        imageBitmap = (Bitmap) extras.get("data");
                        mImageFile = getFileFromBitmap(imageBitmap);
                    }

                    break;
                case REQUEST_IMAGE_PICKER:
                    Uri imageUri = data.getData();
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mImageFile = getFileFromBitmap(imageBitmap);
                    break;
            }
    }

    private void launchImagePicker() {
        Intent chooseIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(chooseIntent, REQUEST_IMAGE_PICKER);
    }

    private File getFileFromBitmap(Bitmap bitmap) {
        File file = new File(getActivity().getCacheDir(), "profilepic.jpg");
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private boolean validateForm() {
        boolean isValid = true;
        boolean isNameValid = true, isEmailValid = true, isPasswordValid = true, isPhoneValid = true;
        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError(getResources().getString(R.string.enter_email));
            isValid = false;
            isNameValid = false;
        }
        if (!ViewUtils.isValidEmail(mEmail.getText().toString())) {
            mEmail.setError(getResources().getString(R.string.enter_valid_email));
            isValid = false;
            isEmailValid = false;
        }
        if (TextUtils.isEmpty(mName.getText().toString())) {
            mName.setError(getResources().getString(R.string.enter_name));
            isValid = false;
            isNameValid = false;
        }
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            mPassword.setError(getResources().getString(R.string.enter_password));
            isValid = false;
            isPasswordValid = false;
        } else if (mPassword.getText().toString().length() < 5) {
            mPassword.setError(getResources().getString(R.string.enter_valid_password));
            isValid = false;
            isPasswordValid = false;
        }
        if (TextUtils.isEmpty(mMobileNumber.getText().toString())) {
            mMobileNumber.setError(getResources().getString(R.string.enter_mobile_number));
            isValid = false;
            isPhoneValid = false;
        } else if (mMobileNumber.getText().toString().length() != 10) {
            mMobileNumber.setError(getString(R.string.enter_valid_phone_number));
            isValid = false;
            isPhoneValid = false;
        }
        if (!mTACBtn.isChecked() && isEmailValid && isNameValid && isPasswordValid && isPhoneValid) {
            DialogUtils.showSingleButtonAlertDialog(getActivity(), null,
                    getResources().getString(R.string.agree_tac),
                    getResources().getString(R.string.ok));
            isValid = false;
        }
        // removeErrorFields();
        return isValid;
    }

    private void removeErrorFields() {
        mName.setError(null);
        mEmail.setError(null);
        mPassword.setError(null);
        mMobileNumber.setError(null);
    }

}
