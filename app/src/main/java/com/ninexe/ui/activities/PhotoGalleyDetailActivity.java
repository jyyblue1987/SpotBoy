/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.PhotoGalleryDetailFragment;
import com.ninexe.ui.fragments.PhotoGalleryFragment;

/**
 * Created by nagesh on 26/10/15.
 */
public class PhotoGalleyDetailActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery_detail);
        loadPhotoGalleryDetailFragment();
    }

    private void loadPhotoGalleryDetailFragment() {
        PhotoGalleryDetailFragment photoGalleryDetailFragment = PhotoGalleryDetailFragment.newInstance(getViewPagerCurrentPosition());
        loadFragment(R.id.fragment_container, photoGalleryDetailFragment,
                PhotoGalleryDetailFragment.PHOTO_GALLERY_DETAIL_FRAGMENT, 0, 0, BaseFragment.FragmentTransactionType.REPLACE);
    }

    private int getViewPagerCurrentPosition() {
        int currentPosition = -1;
        if (null != getIntent().getExtras()) {
            currentPosition = getIntent().getExtras().getInt(Constants.EXTRA_PHOTO_GALLERY_VIEW_PAGER_CURRENT_POSITION);
        }
        return currentPosition;
    }
}
