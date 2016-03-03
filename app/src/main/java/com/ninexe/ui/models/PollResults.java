/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PollResults implements Parcelable {
    ArrayList<PollResultModel> pollResultModels;
    String thumbnail;
    String shareURL;

    public String getShareURL() {
        return shareURL;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public PollResults() {

    }

    public ArrayList<PollResultModel> getPollResultModels() {
        return pollResultModels;
    }

    public void setPollResultModels(ArrayList<PollResultModel> pollResultModels) {
        this.pollResultModels = pollResultModels;
    }

    protected PollResults(Parcel in) {
        pollResultModels = in.createTypedArrayList(PollResultModel.CREATOR);
        thumbnail = in.readString();
    }

    public static final Creator<PollResults> CREATOR = new Creator<PollResults>() {
        @Override
        public PollResults createFromParcel(Parcel in) {
            return new PollResults(in);
        }

        @Override
        public PollResults[] newArray(int size) {
            return new PollResults[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(pollResultModels);
        dest.writeString(thumbnail);
    }
}
