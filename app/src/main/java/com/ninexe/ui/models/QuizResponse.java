/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizResponse implements Parcelable {
    int points;
    String thumbnail;
    String type;
    String title;
    String text;
    String question;
    PersonalityTestResult personality;
    String shareURL;

    public String getShareURL() {
        return shareURL;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public PersonalityTestResult getPersonality() {
        return personality;
    }

    public void setPersonality(PersonalityTestResult personality) {
        this.personality = personality;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    protected QuizResponse(Parcel in) {
        points = in.readInt();
        thumbnail = in.readString();
        type = in.readString();
        question = in.readString();
        personality = in.readParcelable(PersonalityTestResult.class.getClassLoader());
    }

    public static final Creator<QuizResponse> CREATOR = new Creator<QuizResponse>() {
        @Override
        public QuizResponse createFromParcel(Parcel in) {
            return new QuizResponse(in);
        }

        @Override
        public QuizResponse[] newArray(int size) {
            return new QuizResponse[size];
        }
    };

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(points);
        dest.writeString(thumbnail);
        dest.writeString(type);
        dest.writeString(question);
        dest.writeParcelable(personality, flags);
    }
}
