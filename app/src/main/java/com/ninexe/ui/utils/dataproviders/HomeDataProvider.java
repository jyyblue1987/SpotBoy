/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import com.ninexe.ui.models.HomeData;
import com.ninexe.ui.models.HomeTopBarTab;
import com.ninexe.ui.models.Section;
import com.ninexe.ui.models.SubMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by nagesh on 7/10/15.
 */
public class HomeDataProvider {
    private static final HomeDataProvider HOME_DATA_PROVIDER = new HomeDataProvider();
    private ArrayList<Section> sectionArrayList = null;
    private HomeData homeData;

    private HomeDataProvider() {

    }


    public static HomeDataProvider getInstance() {
        return HOME_DATA_PROVIDER;
    }


    public void setSectionArrayList(ArrayList<Section> sections) {
        sectionArrayList = sections;
        Collections.sort(sectionArrayList, new Comparator<Section>() {
            @Override
            public int compare(Section lhs, Section rhs) {
                return lhs.getDisplayOrder() < rhs.getDisplayOrder() ? -1 : (lhs.getDisplayOrder() == rhs.getDisplayOrder() ? 0 : 1);
            }
        });
    }

    public ArrayList<Section> getSectionArrayList() {
        return sectionArrayList;
    }

    public void setHomeData(HomeData homeData) {
        this.homeData = homeData;
    }

    public HomeData getHomeData() {
        return homeData;
    }

    public ArrayList<Section> getFilteredSectionList() {
        ArrayList<Section> filteredSectionArrayList = new ArrayList<>();
        for (Section section : getSectionArrayList()) {
            if (section.isDisplayInTopbar()) {
                filteredSectionArrayList.add(section);
            }
        }
        return filteredSectionArrayList;
    }

    public ArrayList<HomeTopBarTab> getTopBarSections() {
/*        ArrayList<Section> topBarSectionList = new ArrayList<>();
        for (Section section : getHomeData().getSections()) {

*//*            if (section.isDisplayInTopbar()) {
                topBarSectionList.add(section);
            }*//*

            for (SubMenu subMenu : section.getSubMenu()) {
                if (subMenu.isDisplayInTopbar()) {
                    topBarSectionList.add(getSection(subMenu));
                }
            }

        }
        return topBarSectionList;*/
        ArrayList<HomeTopBarTab> topBarList = new ArrayList<>();
        if (null != getHomeData() && null != getHomeData().getSections()) {
            for (Section section : getHomeData().getSections()) {
                for (SubMenu subMenu : section.getSubMenu()) {
                    if (subMenu.isDisplayInTopbar()) {
                        topBarList.add(geTopBarItem(subMenu));
                    }
                }

            }
        }
        return topBarList;
    }

    private HomeTopBarTab geTopBarItem(SubMenu subMenu) {
        HomeTopBarTab homeTopBarTab = new HomeTopBarTab();
        homeTopBarTab.setId(subMenu.getId());
        homeTopBarTab.setQueryModifier(subMenu.getQueryModifier());
        homeTopBarTab.setTitle(subMenu.getTitle());
        return homeTopBarTab;
    }

    public ArrayList<Section> getSideBarSections() {
        ArrayList<Section> sideBarSectionList = new ArrayList<>();
        for (Section section : getHomeData().getSections()) {
            if (section.isDisplayInSidebar()) {
                sideBarSectionList.add(section);
            }
        }
        return sideBarSectionList;
    }

    public Section getSection(SubMenu subMenu) {
        Section section = new Section();
        section.set_id(subMenu.getId());
        section.setTitle(subMenu.getTitle());
        section.setDisplayInSidebar(subMenu.isDisplayInSidebar());
        section.setDisplayInTopbar(subMenu.isDisplayInTopbar());
        return section;
    }
}
