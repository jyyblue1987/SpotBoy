/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import android.content.Context;

import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.Menu;
import com.ninexe.ui.models.Section;
import com.ninexe.ui.models.SubMenu;

import java.util.ArrayList;

/**
 * Created by nagesh on 7/10/15.
 */
public class NavigationDrawerDataProvider {

    private final static NavigationDrawerDataProvider NAVIGATION_DRAWER_DATA_PROVIDER =
            new NavigationDrawerDataProvider();

    private NavigationDrawerDataProvider() {

    }

    public static NavigationDrawerDataProvider getInstance() {
        return NAVIGATION_DRAWER_DATA_PROVIDER;
    }

    public ArrayList<Menu> getMenuList(Context context) {
        ArrayList<Menu> menuArrayList = new ArrayList<>();

        Menu home = new Menu();
        home.setTitle(Constants.MENU_HOME);
        menuArrayList.add(home);

        if (null != HomeDataProvider.getInstance().getHomeData() && null != HomeDataProvider.getInstance().getHomeData().getSections()) {
            for (Section section : HomeDataProvider.getInstance().getHomeData().getSections()) {
                if (section.isDisplayInSidebar()) {
                    menuArrayList.add(getMenu(section));
                }
            }
        }

        Menu helloEditor = new Menu();
        helloEditor.setTitle(Constants.MENU_HELLO_EDITOR);
        menuArrayList.add(helloEditor);

        Menu notificationHub = new Menu();
        notificationHub.setTitle(Constants.MENU_NOTIFICATION_HUB);
        menuArrayList.add(notificationHub);

        Menu newsLetter = new Menu();
        newsLetter.setTitle(Constants.MENU_NEWS_LETTER);
        menuArrayList.add(newsLetter);

        Menu shareTheApp = new Menu();
        shareTheApp.setTitle(Constants.MENU_SHARE_THE_APP);
        menuArrayList.add(shareTheApp);

        Menu settings = new Menu();
        settings.setTitle(Constants.MENU_SETTINGS);
        menuArrayList.add(settings);

        return menuArrayList;
    }

    private Menu getMenu(Section section) {
        Menu menu = null;
        if (null != section) {
            menu = new Menu();
            ArrayList<SubMenu> subMenuArrayList = new ArrayList<>();
            menu.setId(section.getId());
            menu.setTitle(section.getTitle());
            menu.setIcon(section.getIcon());
            for (SubMenu subMenu : section.getSubMenu()) {
                if (subMenu.isDisplayInSidebar()) {
                    subMenuArrayList.add(subMenu);
                }
            }
            menu.setSubMenu(subMenuArrayList);
        }
        return menu;
    }


}
