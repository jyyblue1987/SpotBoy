/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninexe.ui.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 8/10/15.
 */
public class RetryFragment extends BaseFragment {

    public final static String RETRY_FRAGMENT = "retry_fragment";
    private OnRetryInteractionListener mListener;

    private static final String ARG_MESSAGE = "arg_message";

    @Bind(R.id.message)
    TextView mMessage;

    private String mArgParam;

    public static RetryFragment newInstance(String message) {
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        RetryFragment fragment = new RetryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnRetryInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mArgParam = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_retry, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMessage.setText(mArgParam);
    }

    @OnClick(R.id.btn_retry)
    void onRetryButtonClick() {
        if (mListener != null) {
            mListener.retry();
        }
    }

    public interface OnRetryInteractionListener {
        void retry();
    }
}
