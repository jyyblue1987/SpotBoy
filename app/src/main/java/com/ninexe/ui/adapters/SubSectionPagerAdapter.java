/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ninexe.ui.fragments.SectionDetailFragment;
import com.ninexe.ui.models.SubMenu;

import java.util.ArrayList;

/**
 * Created by nagesh on 7/10/15.
 */
public class SubSectionPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<SubMenu> subMenus;

    public SubSectionPagerAdapter(FragmentManager fm, ArrayList<SubMenu> subMenuArrayList) {
        super(fm);
        subMenus = subMenuArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        fragment = SectionDetailFragment
                .newInstance(subMenus.get(position));
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return subMenus.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return subMenus.size();
    }
}
