/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ninexe.ui.fragments.TabSectionFragment;
import com.ninexe.ui.models.HomeTopBarTab;
import com.ninexe.ui.models.Menu;
import com.ninexe.ui.models.Section;

import java.util.ArrayList;

/**
 * Created by nagesh on 7/10/15.
 */
public class TabSectionPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<HomeTopBarTab> homeTopBarTabArrayList;

    public TabSectionPagerAdapter(FragmentManager fm, ArrayList<HomeTopBarTab> topBarArrayList) {
        super(fm);
        homeTopBarTabArrayList = topBarArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        fragment = TabSectionFragment.newInstance(homeTopBarTabArrayList.get(position));
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return homeTopBarTabArrayList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return homeTopBarTabArrayList.size();
    }
}
