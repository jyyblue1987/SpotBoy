/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.TabSectionPagerAdapter;
import com.ninexe.ui.models.HomeTopBarTab;
import com.ninexe.ui.utils.dataproviders.HomeDataProvider;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 7/10/15.
 */
public class HomeFragment extends BaseFragment {

    public final static String HOME_FRAGMENT = "home_fragment";
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.pager)
    ViewPager mPager;

    private TabSectionPagerAdapter mPagerAdapter;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initPagerAdapter();
        setPagerAdapter();
        addTabLayoutPagerInteraction();
    }

    private void initPagerAdapter() {
        final ArrayList<HomeTopBarTab> homeTopBarTabArrayList = HomeDataProvider.getInstance().getTopBarSections();
        if (null != homeTopBarTabArrayList && !homeTopBarTabArrayList.isEmpty()) {
            mPagerAdapter = new TabSectionPagerAdapter(getChildFragmentManager(),
                    HomeDataProvider.getInstance().getTopBarSections());
        }
    }

    private void setPagerAdapter() {
        if (null != mPagerAdapter) {
            mPager.setAdapter(mPagerAdapter);
            mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);
            mPager.setOffscreenPageLimit(HomeDataProvider.getInstance().getTopBarSections().size());
        }
    }

    private void addTabLayoutPagerInteraction() {
        if (null != mPager.getAdapter()) {
            mTabLayout.setupWithViewPager(mPager);
            mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        }
    }
}
