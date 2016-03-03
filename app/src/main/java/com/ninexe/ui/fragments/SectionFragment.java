/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninexe.ui.R;
import com.ninexe.ui.adapters.SubSectionPagerAdapter;
import com.ninexe.ui.models.SubMenu;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by nagesh on 27/10/15.
 */
public class SectionFragment extends BaseFragment {

    public final static String SECTION_FRAGMENT = "section_fragment";
    private static final String ARG_SUB_MENU_LIST = "arg_sub_menu_list";
    private static final String ARG_SUB_MENU_POSITION = "arg_sub_menu_position";
    private static final String ARG_MENU_TITLE = "arg_menu_title";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.pager)
    ViewPager mPager;

    private SubSectionPagerAdapter mPagerAdapter;
    private ArrayList<SubMenu> mSubMenuArrayListParam;
    private int mSubMenuPositionParam;
    private String mMenuTitleParam;

    public static SectionFragment newInstance(ArrayList<SubMenu> subMenuArrayList,
                                              int subMenuPosition, String menuTitle) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_SUB_MENU_LIST, subMenuArrayList);
        args.putInt(ARG_SUB_MENU_POSITION, subMenuPosition);
        args.putString(ARG_MENU_TITLE, menuTitle);
        SectionFragment fragment = new SectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mSubMenuArrayListParam = getArguments().getParcelableArrayList(ARG_SUB_MENU_LIST);
            mSubMenuPositionParam = getArguments().getInt(ARG_SUB_MENU_POSITION);
            mMenuTitleParam = getArguments().getString(ARG_MENU_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initPagerAdapter();
        setPagerAdapter();
        addTabLayoutPagerInteraction();
    }

    private void initToolbar() {
        setToolbar(mToolbar);
        setToolbarTitle(mMenuTitleParam);
        enableBackButton();
        onSearchClick();
    }

    private void initPagerAdapter() {
        mPagerAdapter = new SubSectionPagerAdapter(getFragmentManager(), mSubMenuArrayListParam);
    }

    private void setPagerAdapter() {
        mPager.setAdapter(mPagerAdapter);
        mTabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        mPager.setCurrentItem(mSubMenuPositionParam);
        mPager.setOffscreenPageLimit(mSubMenuArrayListParam.size());
    }

    private void addTabLayoutPagerInteraction() {
        mTabLayout.setupWithViewPager(mPager);
        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }
}
