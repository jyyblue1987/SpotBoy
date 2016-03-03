/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PollResultModel implements Parcelable {
    String option;
    String percentage;

    protected PollResultModel(Parcel in) {
        option = in.readString();
        percentage = in.readString();
    }

    public PollResultModel() {

    }

    public static final Creator<PollResultModel> CREATOR = new Creator<PollResultModel>() {
        @Override
        public PollResultModel createFromParcel(Parcel in) {
            return new PollResultModel(in);
        }

        @Override
        public PollResultModel[] newArray(int size) {
            return new PollResultModel[size];
        }
    };

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(option);
        dest.writeString(percentage);
    }
}
