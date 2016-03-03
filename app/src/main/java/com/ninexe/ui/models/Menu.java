/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.util.ArrayList;

/**
 * Created by nagesh on 7/10/15.
 */
public class Menu {
    String _id;
    String icon;
    String title;
    ArrayList<SubMenu> subMenu;

    public String getId() {
        return _id;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<SubMenu> getSubMenu() {
        return subMenu;
    }


    public void setId(String _id) {
        this._id = _id;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubMenu(ArrayList<SubMenu> subMenu) {
        this.subMenu = subMenu;
    }
}
