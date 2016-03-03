/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.Article;
import com.ninexe.ui.models.ArticleDetailArticleContentSection;
import com.ninexe.ui.models.ArticleDetailCommentSection;
import com.ninexe.ui.models.ArticleDetailData;
import com.ninexe.ui.models.ArticleDetailRelatedArticleSection;
import com.ninexe.ui.models.ArticleDetailRelatedArticleTextSection;
import com.ninexe.ui.models.ArticleDetailVideoSection;
import com.ninexe.ui.models.IArticleDetailRecyclerViewItem;
import com.ninexe.ui.models.ReactometerSection;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by nagesh on 15/10/15.
 */
public class ArticleDetailDataProvider {
    private static ArticleDetailData ARTICLE_DETAIL_DATA;
    private static ArrayList<IArticleDetailRecyclerViewItem> mArticleDetailDataList;

    public static ArrayList<IArticleDetailRecyclerViewItem> getArticleDetailDataList() {
        mArticleDetailDataList = new ArrayList<>();
        mArticleDetailDataList.add(getVideoSectionData());
        mArticleDetailDataList.add(getReactometerSectionData());
        mArticleDetailDataList.add(getArticleContentSectionData());
        mArticleDetailDataList.add(getCommentSectionData());
        if (!ARTICLE_DETAIL_DATA.getRelatedArticles().isEmpty()) {
            mArticleDetailDataList.add(getRelatedArticleTextSectionData());
            mArticleDetailDataList.addAll(getRelatedArticles());
        }
        return mArticleDetailDataList;
    }

    public static ArticleDetailData getArticleDetailData() {
        return ARTICLE_DETAIL_DATA;
    }

    private static Collection<? extends IArticleDetailRecyclerViewItem> getRelatedArticles() {
        ArrayList<IArticleDetailRecyclerViewItem> relatedArticlesList = new ArrayList<>();
        for (Article article : ARTICLE_DETAIL_DATA.getRelatedArticles()) {
            ArticleDetailRelatedArticleSection relatedArticleSection = new ArticleDetailRelatedArticleSection();
            relatedArticleSection.setArticle(article);
            relatedArticlesList.add(relatedArticleSection);
        }
        return relatedArticlesList;
    }

    private static IArticleDetailRecyclerViewItem getRelatedArticleTextSectionData() {
        ArticleDetailRelatedArticleTextSection relatedArticleTextSection
                = new ArticleDetailRelatedArticleTextSection();
        relatedArticleTextSection.setRelatedArticleText(Constants.ARTICLE_DETAIL_RELATED_ARTICLES_TEXT);
        return relatedArticleTextSection;
    }

    private static IArticleDetailRecyclerViewItem getCommentSectionData() {
        ArticleDetailCommentSection articleDetailCommentSection
                = new ArticleDetailCommentSection();
        articleDetailCommentSection.setArticleQuestion(ARTICLE_DETAIL_DATA.getArticleQuestion());
        articleDetailCommentSection.setCommentArrayList(ARTICLE_DETAIL_DATA.getComments());
        articleDetailCommentSection.setCommentsCount(ARTICLE_DETAIL_DATA.getCommentsCount());
        return articleDetailCommentSection;
    }

    private static IArticleDetailRecyclerViewItem getArticleContentSectionData() {
        ArticleDetailArticleContentSection articleDetailArticleContentSection =
                new ArticleDetailArticleContentSection();
        articleDetailArticleContentSection.setTitle(ARTICLE_DETAIL_DATA.getTitle());
        articleDetailArticleContentSection.setAuthor(ARTICLE_DETAIL_DATA.getAuthor());
        articleDetailArticleContentSection.setPublishedAt(ARTICLE_DETAIL_DATA.getPublishedAt());
        articleDetailArticleContentSection.setViews(ARTICLE_DETAIL_DATA.getViews());
        articleDetailArticleContentSection.setShortBody(ARTICLE_DETAIL_DATA.getShortBody());
        articleDetailArticleContentSection.setBody(ARTICLE_DETAIL_DATA.getBody());
        articleDetailArticleContentSection.setAuthorName(ARTICLE_DETAIL_DATA.getAuthorName());
        articleDetailArticleContentSection.setBodyTrimmed(ARTICLE_DETAIL_DATA.isBodyTrimmed());
        articleDetailArticleContentSection.setTrimmedBody(ARTICLE_DETAIL_DATA.getTrimmedBody());
        return articleDetailArticleContentSection;
    }

    private static IArticleDetailRecyclerViewItem getReactometerSectionData() {
        ReactometerSection reactometerSection = new ReactometerSection();
        reactometerSection.setReaction(ARTICLE_DETAIL_DATA.getReaction());
        reactometerSection.setWowValue(ARTICLE_DETAIL_DATA.getReactionResponseModel().getWOW());
        reactometerSection.setLolValue(ARTICLE_DETAIL_DATA.getReactionResponseModel().getLOL());
        reactometerSection.setOmgValue(ARTICLE_DETAIL_DATA.getReactionResponseModel().getOMG());
        reactometerSection.setWtfValue(ARTICLE_DETAIL_DATA.getReactionResponseModel().getWTF());
        return reactometerSection;
    }

    private static IArticleDetailRecyclerViewItem getVideoSectionData() {
        ArticleDetailVideoSection articleDetailVideoSection = new ArticleDetailVideoSection();
        articleDetailVideoSection.setThumbnail(ARTICLE_DETAIL_DATA.getThumbnail());
        articleDetailVideoSection.setMedia(ARTICLE_DETAIL_DATA.getMedia());
        return articleDetailVideoSection;
    }

    public static void setArticleDetailData(ArticleDetailData articleDetailData) {
        ARTICLE_DETAIL_DATA = articleDetailData;
    }

    public static void setArticleReaction(String reaction) {
        ARTICLE_DETAIL_DATA.setReaction(reaction);
    }


}
