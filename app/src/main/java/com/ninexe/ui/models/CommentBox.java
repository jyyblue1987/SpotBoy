/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 20/10/15.
 */
public class CommentBox implements IViewMoreCommentsRecyclerViewItem {
    private String articleQuestion;
    private int numberOfComments;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public int getItemType() {
        return IViewMoreCommentsRecyclerViewItem.TYPE_COMMENT_BOX;
    }

    public void setArticleQuestion(String articleQuestion) {
        this.articleQuestion = articleQuestion;
    }

    public String getArticleQuestion() {
        return articleQuestion;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public int getNumberOfComments() {
        return numberOfComments;
    }
}
