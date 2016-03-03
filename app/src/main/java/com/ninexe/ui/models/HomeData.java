/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by nagesh on 7/10/15.
 */
public class HomeData implements Parcelable{

    ArrayList<Section> data;

    protected HomeData(Parcel in) {
        data = in.createTypedArrayList(Section.CREATOR);
    }

    public static final Creator<HomeData> CREATOR = new Creator<HomeData>() {
        @Override
        public HomeData createFromParcel(Parcel in) {
            return new HomeData(in);
        }

        @Override
        public HomeData[] newArray(int size) {
            return new HomeData[size];
        }
    };

    public ArrayList<Section> getSections() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(data);
    }
}
