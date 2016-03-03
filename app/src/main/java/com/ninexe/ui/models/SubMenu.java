/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nagesh on 7/10/15.
 */
public class SubMenu implements Parcelable {
    String _id;
    String icon;
    String title;
    String type;
    String actionUrl;
    boolean displayInTopbar;
    boolean displayInSidebar;
    String queryModifier;

    public String getActionUrl() {
        return actionUrl;
    }

    public String getType() {
        return type;
    }

    protected SubMenu(Parcel in) {
        _id = in.readString();
        icon = in.readString();
        title = in.readString();
        type = in.readString();
        actionUrl = in.readString();
        queryModifier = in.readString();
        displayInTopbar = in.readByte() != 0;
        displayInSidebar = in.readByte() != 0;
    }

    public static final Creator<SubMenu> CREATOR = new Creator<SubMenu>() {
        @Override
        public SubMenu createFromParcel(Parcel in) {
            return new SubMenu(in);
        }

        @Override
        public SubMenu[] newArray(int size) {
            return new SubMenu[size];
        }
    };

    public String getId() {
        return _id;
    }

    public String getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDisplayInTopbar() {

        return displayInTopbar;
    }

    public boolean isDisplayInSidebar() {
        return displayInSidebar;
    }

    public String getQueryModifier() {
        return queryModifier;
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
        dest.writeString(type);
        dest.writeString(actionUrl);
        dest.writeString(queryModifier);
        dest.writeByte((byte) (displayInTopbar ? 1 : 0));
        dest.writeByte((byte) (displayInSidebar ? 1 : 0));
    }
}
