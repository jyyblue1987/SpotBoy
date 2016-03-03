/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import com.ninexe.ui.models.ArticleDetailData;
import com.ninexe.ui.models.CommentBox;
import com.ninexe.ui.models.IViewMoreCommentsRecyclerViewItem;
import com.ninexe.ui.models.ViewMoreCommentsResponse;

import java.util.ArrayList;

/**
 * Created by nagesh on 20/10/15.
 */
public class ViewMoreCommentsDataProviders {

    private static final ViewMoreCommentsDataProviders viewMoreCommentsDataProviders
            = new ViewMoreCommentsDataProviders();

    private ViewMoreCommentsResponse mViewMoreCommentsResponse;
    private ArticleDetailData mArticleDetailData;


    private ViewMoreCommentsDataProviders() {

    }

    public static ViewMoreCommentsDataProviders getInstance() {
        return viewMoreCommentsDataProviders;
    }

    public void setArticleDetailData(ArticleDetailData articleDetailData) {
        this.mArticleDetailData = articleDetailData;
    }

    public void setViewMoreCommentsResponse(ViewMoreCommentsResponse viewMoreCommentsResponse) {
        this.mViewMoreCommentsResponse = viewMoreCommentsResponse;
    }

    public ViewMoreCommentsResponse getViewMoreCommentsResponse() {
        return mViewMoreCommentsResponse;
    }

    public ArrayList<IViewMoreCommentsRecyclerViewItem> getViewMoreCommentsData() {
        ArrayList<IViewMoreCommentsRecyclerViewItem> viewMoreCommentsArrayList = new ArrayList<>();
        viewMoreCommentsArrayList.addAll(mViewMoreCommentsResponse.getData());
        return viewMoreCommentsArrayList;
    }

    public CommentBox getCommentBox() {
        CommentBox commentBox = new CommentBox();
        commentBox.setArticleQuestion(getArticleDetailData().getArticleQuestion());
        commentBox.setNumberOfComments(getArticleDetailData().getCommentsCount());
        return commentBox;
    }

    public ArticleDetailData getArticleDetailData() {
        return mArticleDetailData;
    }
}
