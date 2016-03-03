/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ninexe.ui.R;
import com.ninexe.ui.adapters.PollResultAdapter;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.PollResults;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.ShareUtils;
import com.ninexe.ui.utils.Theme;
import com.ninexe.ui.utils.ViewUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PollsResultFragment extends BaseFragment {

    @Bind(R.id.poll_result_recycler_view)
    RecyclerView mPollResultView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.pollCoverImage)
    ImageView mThumbnailView;
    private String mShareURL;

    @OnClick(R.id.shareIcn)
    void onShare() {
        ShareUtils.share(mShareURL, getActivity());
    }

    final static String POLL_RESULT = "poll_result";

    public PollsResultFragment() {
        // Required empty public constructor
    }

    public static PollsResultFragment newInstance(PollResults pollResults) {
        Bundle args = new Bundle();
        args.putParcelable(POLL_RESULT, pollResults);
        PollsResultFragment fragment = new PollsResultFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_polls_result, container, false);
        ButterKnife.bind(this, view);
        initToolbar();
        PollResults pollResults = getArguments().getParcelable(POLL_RESULT);
        if (null != pollResults && null != pollResults.getPollResultModels()) {
            PollResultAdapter pollResultAdapter = new PollResultAdapter(pollResults.getPollResultModels());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
            mPollResultView.setLayoutManager(linearLayoutManager);
            mPollResultView.setAdapter(pollResultAdapter);
            mShareURL = pollResults.getShareURL();

            Glide.with(getActivity())
                    .load(ViewUtils.getThumbnail(pollResults.getThumbnail(), Constants.IMAGE_THUMBNAIL))
                    .placeholder(R.drawable.placeholder_featured_images)
                    .centerCrop()
                    .crossFade()
                    .into(mThumbnailView);
        }
        return view;
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(getString(R.string.polls));
        enableBackButton();
        displaySearch(false);
        // onSearchClick();
    }


}
