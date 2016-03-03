/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.ninexe.ui.custom.CustomViewPager;
import com.ninexe.ui.fragments.PhotoGalleryFullScreenContentFragment;
import com.ninexe.ui.models.ArticleMedia;

import java.util.ArrayList;

/**
 * Created by nagesh on 14/11/15.
 */
public class PhotoGalleryFullScreenPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<ArticleMedia> mMediaArrayList;
    private CustomViewPager mViewPager;

    public PhotoGalleryFullScreenPagerAdapter(FragmentManager fm, ArrayList<ArticleMedia> mediaArrayList, CustomViewPager viewPager) {
        super(fm);
        mMediaArrayList = mediaArrayList;
        mViewPager = viewPager;
    }

    @Override
    public Fragment getItem(int position) {
        PhotoGalleryFullScreenContentFragment fragment;
        fragment = (PhotoGalleryFullScreenContentFragment) PhotoGalleryFullScreenContentFragment.newInstance(mMediaArrayList.get(position).getFile());
        fragment.setImageZoomListener(new PhotoGalleryFullScreenContentFragment.OnZoomListener() {
            @Override
            public void isImageZoomed(boolean zoomed) {
                mViewPager.setPagingEnabled(!zoomed);
            }
        });
        return fragment;
    }

    @Override
    public int getCount() {
        return mMediaArrayList.size();
    }


}
