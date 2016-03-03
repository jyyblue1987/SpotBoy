/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by nagesh on 12/10/15.
 */
public class Section implements Parcelable {

    String _id;
    String icon;
    String title;
    ArrayList<SubMenu> subMenu;
    FeaturedArticle featured;
    ArrayList<RecentArticle> recent;
    int displayOrder;
    boolean displayInTopbar;
    boolean displayInSidebar;

    protected Section(Parcel in) {
        _id = in.readString();
        icon = in.readString();
        title = in.readString();
        featured = in.readParcelable(FeaturedArticle.class.getClassLoader());
        recent = in.readArrayList(RecentArticle.class.getClassLoader());
        subMenu = in.readArrayList(SubMenu.class.getClassLoader());
        displayInTopbar = in.readByte() != 0;
        displayInSidebar = in.readByte() != 0;
    }

    public Section() {

    }

    public static final Creator<Section> CREATOR = new Creator<Section>() {
        @Override
        public Section createFromParcel(Parcel in) {
            return new Section(in);
        }

        @Override
        public Section[] newArray(int size) {
            return new Section[size];
        }
    };

    public boolean isDisplayInSidebar() {
        return displayInSidebar;
    }

    public boolean isDisplayInTopbar() {
        return displayInTopbar;
    }

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

    public FeaturedArticle getFeatured() {
        return featured;
    }

    public ArrayList<RecentArticle> getRecent() {
        return recent;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }


    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDisplayInTopbar(boolean displayInTopbar) {
        this.displayInTopbar = displayInTopbar;
    }

    public void setDisplayInSidebar(boolean displayInSidebar) {
        this.displayInSidebar = displayInSidebar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(icon);
        dest.writeString(title);
        dest.writeParcelable(featured, flags);
        dest.writeList(recent);
        dest.writeList(subMenu);
        dest.writeByte((byte) (displayInTopbar ? 1 : 0));
        dest.writeByte((byte) (displayInSidebar ? 1 : 0));
    }
}
