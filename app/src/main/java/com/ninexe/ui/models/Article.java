/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.ninexe.ui.common.Constants;

/**
 * Created by nagesh on 7/10/15.
 */
public class Article implements Parcelable {

    String _id;
    String title;
    String thumbnail;
    String publishedAt;
    String shortBody;
    String type;
    int views;
    Author author;
    String authorName;

    public Article() {

    }

    protected Article(Parcel in) {
        _id = in.readString();
        title = in.readString();
        thumbnail = in.readString();
        publishedAt = in.readString();
        shortBody = in.readString();
        type = in.readString();
        views = in.readInt();
        author = in.readParcelable(Author.class.getClassLoader());
        authorName = in.readString();
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String getId() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public Author getAuthor() {
        return author;
    }

    public String getShortBody() {
        return shortBody;
    }

    public int getViews() {
        return views;
    }

    public String getType() {
        return type;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public boolean isPhoto() {
        boolean isPhoto = false;
        if (null != type && type.equalsIgnoreCase(Constants.PHOTO)) {
            isPhoto = true;
        }
        return isPhoto;
    }

    public boolean isVideo() {
        boolean isVideo = false;
        if (null != type && type.equalsIgnoreCase(Constants.VIDEO)) {
            isVideo = true;
        }
        return isVideo;
    }

    public boolean isQuiz() {
        boolean isQuiz = false;
        if (null != type) {
            if (type.equalsIgnoreCase(Constants.QUIZ) || type.equalsIgnoreCase(Constants.POLL)
                    || type.equalsIgnoreCase(Constants.CONTEST) || type.equalsIgnoreCase(Constants.PERSONALITY_TEST)) {
                isQuiz = true;
            }
        }
        return isQuiz;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(title);
        dest.writeString(thumbnail);
        dest.writeString(publishedAt);
        dest.writeString(shortBody);
        dest.writeString(type);
        dest.writeInt(views);
        dest.writeParcelable(author, flags);
        dest.writeString(authorName);
    }

}
