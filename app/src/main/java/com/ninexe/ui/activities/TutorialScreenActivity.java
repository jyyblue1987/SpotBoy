/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.TutorialScreenPagerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by nagesh on 19/11/15.
 */
public class TutorialScreenActivity extends BaseActivity {

    @Bind(R.id.viewpager_default)
    ViewPager mTutorialScreenViewPager;

    @Bind(R.id.indicator_default)
    CircleIndicator mCircleIndicator;

    TutorialScreenPagerAdapter mTutorialScreenPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_screen);
        ButterKnife.bind(this);

        mTutorialScreenPagerAdapter = new TutorialScreenPagerAdapter(getSupportFragmentManager());
        mTutorialScreenViewPager.setAdapter(mTutorialScreenPagerAdapter);
        mCircleIndicator.setViewPager(mTutorialScreenViewPager);
    }
}
