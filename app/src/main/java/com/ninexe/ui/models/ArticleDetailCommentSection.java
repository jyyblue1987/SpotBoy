/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.util.ArrayList;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailCommentSection implements IArticleDetailRecyclerViewItem {
    private String articleQuestion;
    private ArrayList<Comment> commentArrayList;
    private int commentsCount;
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setArticleQuestion(String articleQuestion) {
        this.articleQuestion = articleQuestion;
    }

    public String getArticleQuestion() {
        return articleQuestion;
    }

    public void setCommentArrayList(ArrayList<Comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
    }

    public ArrayList<Comment> getCommentArrayList() {
        return commentArrayList;
    }

    @Override
    public int getItemType() {
        return IArticleDetailRecyclerViewItem.TYPE_COMMENT_SECTION;
    }
}
