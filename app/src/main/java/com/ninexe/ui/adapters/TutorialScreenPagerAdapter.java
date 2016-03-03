/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ninexe.ui.fragments.TutorialScreenFragment;

import java.util.Random;

/**
 * Created by nagesh on 19/11/15.
 */
public class TutorialScreenPagerAdapter extends FragmentPagerAdapter {

    private int pagerCount = 2;

    private Random random = new Random();

    public TutorialScreenPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override public Fragment getItem(int position) {
        return TutorialScreenFragment.newInstance(position);
    }

    @Override public int getCount() {
        return pagerCount;
    }
}
