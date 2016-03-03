/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.SectionDetailFragment;
import com.ninexe.ui.fragments.SectionFragment;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.PollDataHandler;
import com.ninexe.ui.models.SubMenu;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 18/10/15.
 */
public class SectionDetailActivity extends BaseActivity
        implements SectionDetailFragment.OnSectionDetailFragmentInteractionListener {

    @Bind(R.id.adView)
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_detail);
        ButterKnife.bind(this);
        loadSectionFragment();
        loadAd(mAdView);
    }


    private void loadSectionFragment() {
        SectionFragment sectionFragment =
                SectionFragment.newInstance(getSubMenuList(), getSubMenuPosition(),
                        getMenuTitle());
        loadFragment(R.id.fragment_container, sectionFragment,
                SectionFragment.SECTION_FRAGMENT, 0, 0, BaseFragment.FragmentTransactionType.REPLACE);
    }

    private String getMenuTitle() {
        String title = null;
        if (null != getIntent().getExtras()) {
            title = getIntent().getExtras().getString(Constants.EXTRA_MENU_TITLE);
        }
        return title;
    }

    private int getSubMenuPosition() {
        int position = 0;
        if (null != getIntent().getExtras()) {
            position = getIntent().getExtras().getInt(Constants.EXTRA_SUB_MENU_POSITION);
        }
        return position;
    }

    private ArrayList<SubMenu> getSubMenuList() {
        ArrayList<SubMenu> subMenuArrayList = null;
        if (null != getIntent().getExtras()) {
            subMenuArrayList = getIntent().getExtras().getParcelableArrayList(Constants.EXTRA_SUB_MENU_LIST);
        }
        return subMenuArrayList;
    }


    @Override
    public void onArticleSelection(Article article, String articleTitle) {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            LogUtils.LOGD(Constants.APP_TAG, "selected article id " + article.getId());
            if (article.isPhoto()) {
                NavigationUtils.startPhotoGalleryActivity(this, article.getId());
            } else if (article.isQuiz()) {
                if (TextUtils.equals(article.getType(), Constants.POLL)) {
                    if (PollDataHandler.isPollAttempted(article.getId(), this)) {
                        DialogUtils.showSingleButtonAlertDialog(this, "", getString(R.string.poll_attemted), getString(R.string.ok));
                        return;
                    }
                }
                NavigationUtils.startQuizActivity(this, article.getId(), article.getType());
            } else {
                NavigationUtils.startArticleDetailActivity(this, article.getId(), articleTitle);
            }
        } else {
            if (OfflineArticleDataHandler.isArticleCached(article.getId(), this)) {
                NavigationUtils.startArticleDetailActivity(this, article.getId(), articleTitle);
            } else {
                DialogUtils.showNoNetworkDialog(this);
            }
        }
    }

    @Override
    public void onRetryButtonClick() {
        loadAd(mAdView);
    }
}
