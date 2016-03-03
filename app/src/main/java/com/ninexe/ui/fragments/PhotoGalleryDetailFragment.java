/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.PhotoGalleryFullScreenPagerAdapter;
import com.ninexe.ui.custom.CustomViewPager;
import com.ninexe.ui.utils.PreferenceManager;
import com.ninexe.ui.utils.dataproviders.PhotoGalleryDataProvider;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nagesh on 26/10/15.
 */
public class PhotoGalleryDetailFragment extends BaseFragment {

    public static final String PHOTO_GALLERY_DETAIL_FRAGMENT = "photo_gallery_detail_fragment";
    private static String ARG_VIEW_PAGER_CURRENT_POSITION = "arg_view_pager_current_position";

    @Bind(R.id.view_pager)
    CustomViewPager mPhotoGalleryDetailViewPager;

    @Bind(R.id.toolbar_top)
    Toolbar mTopToolbar;

    int mViewPagerCurrentPosition;
    private PhotoGalleryFullScreenPagerAdapter mPhotoGalleryFullScreenPagerAdapter;

    public static PhotoGalleryDetailFragment newInstance(int viewPagerCurrentPosition) {
        Bundle args = new Bundle();
        args.putInt(ARG_VIEW_PAGER_CURRENT_POSITION, viewPagerCurrentPosition);
        PhotoGalleryDetailFragment fragment = new PhotoGalleryDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mViewPagerCurrentPosition = getArguments().getInt(ARG_VIEW_PAGER_CURRENT_POSITION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopToolbar();
        initPhotoGalleryDetailViewPager();
    }

    private void initTopToolbar() {
        mTopToolbar.setNavigationIcon(R.drawable.back_icn);
        mTopToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void initPhotoGalleryDetailViewPager() {
/*        PhotoGalleryPagerAdapter photoGalleryPagerAdapter
                = new PhotoGalleryPagerAdapter(getContext(),
                PhotoGalleryDataProvider.getInstance().getMediaList(), null,
                PhotoGalleryPagerAdapter.PhotoGalleryPagerAdapterContainerType.TYPE_PHOTO_GALLERY_DETAIL);*/

        mPhotoGalleryFullScreenPagerAdapter =
                new PhotoGalleryFullScreenPagerAdapter(getFragmentManager(),
                        PhotoGalleryDataProvider.getInstance().getMediaList(), mPhotoGalleryDetailViewPager);
        mPhotoGalleryDetailViewPager.setAdapter(mPhotoGalleryFullScreenPagerAdapter);
        mPhotoGalleryDetailViewPager.setOffscreenPageLimit(mPhotoGalleryFullScreenPagerAdapter.getCount());
        if (-1 != mViewPagerCurrentPosition) {
            mPhotoGalleryDetailViewPager.setCurrentItem(mViewPagerCurrentPosition);
        }

        mPhotoGalleryDetailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                PreferenceManager.
                        storePhotoGalleryDetailCurrentPosition(mPhotoGalleryDetailViewPager.getCurrentItem(),
                                getActivity());
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mPhotoGalleryDetailViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    @OnClick(R.id.btn_cancel)
    public void onCancelButtonClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.btn_previous)
    public void onPreviousButtonClick() {
        mPhotoGalleryDetailViewPager.setCurrentItem(mPhotoGalleryDetailViewPager.getCurrentItem() - 1, true);
    }

    @OnClick(R.id.btn_next)
    public void onNextButtonClick() {
        mPhotoGalleryDetailViewPager.setCurrentItem(mPhotoGalleryDetailViewPager.getCurrentItem() + 1, true);
    }

}
