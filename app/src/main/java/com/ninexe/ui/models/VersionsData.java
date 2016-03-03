/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */
package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by swathi on 27/1/16.
 */
public class VersionsData {

    VersionsAndroidData android;

    public VersionsAndroidData getVersionsAndroidData() {
        return android;
    }

    public class VersionsAndroidData implements Parcelable {

        String currentVersionName;
        String currentVersionNumber;
        String minimumVersionName;
        String minimumVersionNumber;
        String optionalUpdateString;
        String forceUpdateString;

        protected VersionsAndroidData(Parcel in) {
            currentVersionName = in.readString();
            currentVersionNumber = in.readString();
            minimumVersionName = in.readString();
            minimumVersionNumber = in.readString();
            optionalUpdateString = in.readString();
            forceUpdateString = in.readString();
        }

        public final Creator<VersionsAndroidData> CREATOR = new Creator<VersionsAndroidData>() {
            @Override
            public VersionsAndroidData createFromParcel(Parcel in) {
                return new VersionsAndroidData(in);
            }

            @Override
            public VersionsAndroidData[] newArray(int size) {
                return new VersionsAndroidData[size];
            }
        };

        public String getCurrentVersionName() {
            return currentVersionName;
        }

        public String getCurrentVersionNumber() {
            return currentVersionNumber;
        }

        public String getMinimumVersionName() {
            return minimumVersionName;
        }

        public String getMinimumVersionNumber() {
            return minimumVersionNumber;
        }

        public String getOptionalUpdateString() {
            return optionalUpdateString;
        }

        public String getForceUpdateString() {
            return forceUpdateString;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(currentVersionName);
            dest.writeString(currentVersionNumber);
            dest.writeString(minimumVersionName);
            dest.writeString(minimumVersionNumber);
            dest.writeString(optionalUpdateString);
            dest.writeString(forceUpdateString);
        }

    }

}
