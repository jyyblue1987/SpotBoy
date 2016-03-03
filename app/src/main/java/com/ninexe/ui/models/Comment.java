/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nagesh on 16/10/15.
 */
public class Comment implements IViewMoreCommentsRecyclerViewItem, Parcelable {
    String _id;
    String userProfilePic;
    String userName;
    String comment;
    String createdAt;

    public Comment() {
    }

    protected Comment(Parcel in) {
        _id = in.readString();
        userProfilePic = in.readString();
        userName = in.readString();
        comment = in.readString();
        createdAt = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public String getUserName() {
        return userName;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int getItemType() {
        return IViewMoreCommentsRecyclerViewItem.TYPE_COMMENT;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(userProfilePic);
        dest.writeString(userName);
        dest.writeString(comment);
        dest.writeString(createdAt);
    }
}
