/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdView;
import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.PhotoGalleryFragment;
import com.ninexe.ui.utils.DialogUtils;
import com.ninexe.ui.utils.NavigationUtils;
import com.ninexe.ui.utils.NetworkCheckUtility;
import com.ninexe.ui.utils.RateAppUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 20/10/15.
 */
public class PhotoGalleryActivity extends BaseActivity
        implements PhotoGalleryFragment.OnPhotoGalleryFragmentInteractionListener {

    @Bind(R.id.adView)
    AdView mAdView;

    @Override
    protected void onResume() {
        super.onResume();
        RateAppUtil.showRateDialog(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        ButterKnife.bind(this);
        loadPhotoGalleryFragment();
        loadAd(mAdView);
    }

    private void loadPhotoGalleryFragment() {
        PhotoGalleryFragment photoGalleryFragment = PhotoGalleryFragment.newInstance(getArticleId());
        loadFragment(R.id.fragment_container, photoGalleryFragment,
                PhotoGalleryFragment.PHOTO_GALLERY_FRAGMENT, 0, 0, BaseFragment.FragmentTransactionType.REPLACE);
    }

    private String getArticleId() {
        String articleId = null;
        if (null != getIntent().getExtras()) {
            articleId = getIntent().getExtras().getString(Constants.EXTRA_ARTICLE_ID);
        }
        return articleId;
    }


    @Override
    public void onPhotoSelection(int position) {
        if (NetworkCheckUtility.isNetworkAvailable(this)) {
            NavigationUtils.startPhotoGalleryDetailActivity(this, position);
        } else {
            DialogUtils.showNoNetworkDialog(this);
        }
    }

}
