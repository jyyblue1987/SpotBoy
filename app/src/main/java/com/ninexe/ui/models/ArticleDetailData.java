/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import com.ninexe.ui.common.Constants;

import java.util.ArrayList;

/**
 * Created by nagesh on 16/10/15.
 */
public class ArticleDetailData {
    String _id;
    String thumbnail;
    Author author;
    String authorName;
    String publishedAt;
    int views;
    ArrayList<Comment> comments;
    String title;
    String shortBody;
    String body;
    String trimmedBody;
    boolean bodyTrimmed;
    String articleQuestion;
    String reaction;
    ArrayList<Article> related;
    ArrayList<ArticleMedia> media;
    ReactionResponseModel reactometer;
    String type;
    String shareURL;

    public String getShareURL() {
        return shareURL;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

    public ReactionResponseModel getReactionResponseModel() {
        return reactometer;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    int commentsCount;

    public String getId() {
        return _id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public int getViews() {
        return views;
    }

    public String getShortBody() {
        return shortBody;
    }

    public String getBody() {
        return body;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public String getArticleQuestion() {
        return articleQuestion;
    }

    public String getTrimmedBody() {
        return trimmedBody;
    }

    public ArrayList<Article> getRelatedArticles() {
        return related;
    }

    public ArrayList<ArticleMedia> getMedia() {
        return media;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean isBodyTrimmed() {
        return bodyTrimmed;
    }

    public boolean isArticle() {
        return type.equalsIgnoreCase(Constants.ARTICLE);
    }
}
