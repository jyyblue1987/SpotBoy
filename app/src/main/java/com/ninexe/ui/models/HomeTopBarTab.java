/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nagesh on 12/11/15.
 */
public class HomeTopBarTab implements Parcelable {
    String id;
    String queryModifier;
    String title;

    public HomeTopBarTab(){

    }

    protected HomeTopBarTab(Parcel in) {
        id = in.readString();
        queryModifier = in.readString();
        title = in.readString();
    }

    public static final Creator<HomeTopBarTab> CREATOR = new Creator<HomeTopBarTab>() {
        @Override
        public HomeTopBarTab createFromParcel(Parcel in) {
            return new HomeTopBarTab(in);
        }

        @Override
        public HomeTopBarTab[] newArray(int size) {
            return new HomeTopBarTab[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getQueryModifier() {
        return queryModifier;
    }

    public void setQueryModifier(String queryModifier) {
        this.queryModifier = queryModifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(queryModifier);
        dest.writeString(title);
    }
}
