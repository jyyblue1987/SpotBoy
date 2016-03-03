/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.QuizResponse;
import com.ninexe.ui.utils.ShareUtils;
import com.ninexe.ui.utils.ViewUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizResultFragment extends BaseFragment {

    private static final String QUIZ_RESPONSE = "quiz_response";
    @Bind(R.id.thumbnail)
    ImageView mThumbnailView;

    @Bind(R.id.points)
    TextView mPointsView;

    @Bind(R.id.quizTitle)
    TextView mQuizTitle;

    @Bind(R.id.description)
    TextView mDescription;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.shareIcn)
    ImageView mShareIcn;

    @OnClick(R.id.shareIcn)
    void onShare() {
        ShareUtils.share(mShareURL, getActivity());
    }

    private String mShareURL;

    public QuizResultFragment() {
        // Required empty public constructor
    }

    public static QuizResultFragment newInstance(QuizResponse quizResponse) {
        Bundle args = new Bundle();
        args.putParcelable(QUIZ_RESPONSE, quizResponse);
        QuizResultFragment fragment = new QuizResultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        ButterKnife.bind(this, view);

        QuizResponse quizResponse = getArguments().getParcelable(QUIZ_RESPONSE);
        if (null != quizResponse) {
            loadImage(quizResponse.getThumbnail());
            initToolbar(quizResponse.getType());
            mShareURL = quizResponse.getShareURL();
        }
        if (TextUtils.equals(quizResponse.getType(), Constants.PERSONALITY_TEST)) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mDescription
                    .getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            initToolbar(getString(R.string.personality_test));
            mQuizTitle.setVisibility(View.VISIBLE);
            mQuizTitle.setText(quizResponse.getQuestion());
            mDescription.setText(quizResponse.getPersonality().getOptionDescription());
            mPointsView.setText(quizResponse.getPersonality().getOptionTitle());
            loadImage(quizResponse.getPersonality().getOptionImage());
        } else {
            mPointsView.setText(quizResponse.getTitle());
            mDescription.setText(quizResponse.getText());
        }
        return view;
    }

    private void loadImage(String url) {
        Glide.with(getActivity())
                .load(ViewUtils.getThumbnail(url, Constants.IMAGE_THUMBNAIL))
                .placeholder(R.drawable.placeholder_featured_images)
                .centerCrop()
                .crossFade()
                .into(mThumbnailView);
    }

    private void initToolbar(String title) {
        setToolbar(mToolbar);
        setToolbarTitle(title);
        enableBackButton();
        displaySearch(false);
    }

}
