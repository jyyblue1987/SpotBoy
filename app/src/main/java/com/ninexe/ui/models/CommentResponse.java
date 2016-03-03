/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 1/11/15.
 */
public class CommentResponse {

    String _id;
    String userProfilePic;
    String userName;
    String comment;
    String createdAt;

    public String get_id() {
        return _id;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public String getUserName() {
        return userName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getComment() {
        return comment;
    }

    public Comment getCommentResponse() {
        Comment commentResponse = new Comment();
        commentResponse.set_id(get_id());
        commentResponse.setUserName(getUserName());
        commentResponse.setUserProfilePic(getUserProfilePic());
        commentResponse.setCreatedAt(getCreatedAt());
        commentResponse.setComment(getComment());
        return commentResponse;
    }
}
