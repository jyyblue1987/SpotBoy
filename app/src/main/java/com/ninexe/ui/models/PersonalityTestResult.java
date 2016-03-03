/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonalityTestResult implements Parcelable{

    String optionTitle;
    String optionDescription;
    String optionImage;

    protected PersonalityTestResult(Parcel in) {
        optionTitle = in.readString();
        optionDescription = in.readString();
        optionImage = in.readString();
    }

    public static final Creator<PersonalityTestResult> CREATOR = new Creator<PersonalityTestResult>() {
        @Override
        public PersonalityTestResult createFromParcel(Parcel in) {
            return new PersonalityTestResult(in);
        }

        @Override
        public PersonalityTestResult[] newArray(int size) {
            return new PersonalityTestResult[size];
        }
    };

    public String getOptionDescription() {
        return optionDescription;
    }

    public void setOptionDescription(String optionDescription) {
        this.optionDescription = optionDescription;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public void setOptionTitle(String optionTitle) {
        this.optionTitle = optionTitle;
    }

    public String getOptionImage() {
        return optionImage;
    }

    public void setOptionImage(String optionImage) {
        this.optionImage = optionImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(optionTitle);
        dest.writeString(optionDescription);
        dest.writeString(optionImage);
    }
}
