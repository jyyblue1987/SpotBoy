/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models.events;

import android.os.Parcel;
import android.os.Parcelable;

import com.ninexe.ui.models.Comment;

import java.util.ArrayList;

/**
 * Created by nagesh on 2/11/15.
 */
public class CommentNumberChangeEvent implements Parcelable {
    int numberOfComments;
    ArrayList<Comment> commentArrayList;

    public CommentNumberChangeEvent(int numOfComments, ArrayList<Comment> comments) {
        this.numberOfComments = numOfComments;
        this.commentArrayList = comments;
    }

    protected CommentNumberChangeEvent(Parcel in) {
        numberOfComments = in.readInt();
        commentArrayList = in.readArrayList(Comment.class.getClassLoader());
    }

    public static final Creator<CommentNumberChangeEvent> CREATOR = new Creator<CommentNumberChangeEvent>() {
        @Override
        public CommentNumberChangeEvent createFromParcel(Parcel in) {
            return new CommentNumberChangeEvent(in);
        }

        @Override
        public CommentNumberChangeEvent[] newArray(int size) {
            return new CommentNumberChangeEvent[size];
        }
    };

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public ArrayList<Comment> getCommentArrayList() {
        return commentArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numberOfComments);
        dest.writeTypedList(commentArrayList);
    }
}
