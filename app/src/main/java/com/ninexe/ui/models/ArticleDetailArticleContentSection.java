/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailArticleContentSection implements IArticleDetailRecyclerViewItem {

    String title;
    String shortBody;
    String trimmedBody;
    String body;
    boolean bodyTrimmed;
    String publishedAt;
    int views;
    Author author;
    String authorName;
    boolean isReadMoreClicked;

    public boolean isReadMoreClicked() {
        return isReadMoreClicked;
    }

    public void setIsReadMoreClicked(boolean isReadMoreClicked) {
        this.isReadMoreClicked = isReadMoreClicked;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setShortBody(String shortBody) {
        this.shortBody = shortBody;
    }

    public String getShortBody() {
        return shortBody;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getViews() {
        return views;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        return author;
    }

    public String getTrimmedBody() {
        return trimmedBody;
    }

    public void setTrimmedBody(String trimmedBody) {
        this.trimmedBody = trimmedBody;
    }

    public String getAuthorName() {
        return authorName;
    }

    public boolean isBodyTrimmed() {
        return bodyTrimmed;
    }

    public void setBodyTrimmed(boolean bodyTrimmed) {
        this.bodyTrimmed = bodyTrimmed;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    @Override
    public int getItemType() {
        return IArticleDetailRecyclerViewItem.TYPE_ARTICLE_CONTENT_SECTION;
    }
}
