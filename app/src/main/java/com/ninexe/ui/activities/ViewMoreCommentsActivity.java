/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.os.Bundle;

import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.ViewMoreCommentsFragment;

import butterknife.Bind;

/**
 * Created by nagesh on 20/10/15.
 */
public class ViewMoreCommentsActivity extends BaseActivity {

    @Bind(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more_comments);
        loadViewMoreCommentsFragment();
        loadAd(mAdView);
    }

    private void loadViewMoreCommentsFragment() {
        ViewMoreCommentsFragment viewMoreCommentsFragment = ViewMoreCommentsFragment.newInstance(getArticleId());
        loadFragment(R.id.fragment_container, viewMoreCommentsFragment,
                ViewMoreCommentsFragment.VIEW_MORE_COMMENTS_FRAGMENT, 0, 0, BaseFragment.FragmentTransactionType.REPLACE);
    }

    private String getArticleId() {
        String articleId = null;
        if (null != getIntent().getExtras()) {
            articleId = getIntent().getExtras().getString(Constants.EXTRA_ARTICLE_ID);
        }
        return articleId;
    }
}
