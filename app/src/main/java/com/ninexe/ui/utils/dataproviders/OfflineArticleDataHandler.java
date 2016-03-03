/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils.dataproviders;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ninexe.ui.activities.GooglePlusActivity;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.ArticleDetailResponse;
import com.ninexe.ui.models.ArticleResponse;
import com.ninexe.ui.models.FeaturedArticle;
import com.ninexe.ui.models.GridRecentArticle;
import com.ninexe.ui.models.HomeData;
import com.ninexe.ui.models.HomeRecentArticle;
import com.ninexe.ui.models.HomeResponse;
import com.ninexe.ui.models.ITabSectionRecyclerViewItem;
import com.ninexe.ui.models.OfflineSectionModel;
import com.ninexe.ui.models.SectionDataList;
import com.ninexe.ui.utils.LogUtils;
import com.ninexe.ui.utils.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class OfflineArticleDataHandler extends OfflineDataHandler {

    private static final boolean DEFAULT_CACHING_MODE = true;
    private static JSONObject mJsonObject;

    public static void addArticleResponse(String articleId, ArticleDetailResponse articleDetailResponse, Context context) {
        Gson gson = new Gson();
        String articleString = gson.toJson(articleDetailResponse);
        addContent(context, articleId, articleString, Constants.SP_OFFLINE_ARTICLE);
    }

    public static boolean isArticleCached(String articleId, Context context) {
        return isContentCached(context, articleId, Constants.SP_OFFLINE_ARTICLE);
    }

    public static ArticleDetailResponse getArticle(String articleId, Context context) {
        if (isArticleCached(articleId, context)) {
            mJsonObject = getContent(context, Constants.SP_OFFLINE_ARTICLE);
            try {
                Gson gson = new Gson();
                return gson.fromJson(mJsonObject.getString(articleId), ArticleDetailResponse.class);
            } catch (JSONException e) {
                return null;
            }
        }
        return null;
    }

    public static void toggleOfflineCacheMode(Context context, boolean isChecked) {
        PreferenceManager.putBoolean(context, Constants.SP_IS_CACHING, isChecked);
        if (!isChecked) {
            PreferenceManager.clearField(Constants.SP_OFFLINE_ARTICLE, context);
            PreferenceManager.clearField(Constants.SP_OFFLINE_SECTION, context);
        }
    }

    public static boolean isOfflineCacheEnabled(Context context) {
        return PreferenceManager.getBoolean(context, Constants.SP_IS_CACHING, DEFAULT_CACHING_MODE);
    }

    public static void setHomeResponse(Context context, HomeData homeDataResponse) {
        Gson gson = new Gson();
        addContent(context, Constants.HOME_RESPONSE, gson.toJson(homeDataResponse), Constants.SP_OFFLINE_ARTICLE);
    }

    public static HomeData getHomeResponse(Context context) {
        Gson gson = new Gson();
        mJsonObject = getContent(context, Constants.SP_OFFLINE_ARTICLE);
        try {
            return gson.fromJson(mJsonObject != null ? mJsonObject.getString(Constants.HOME_RESPONSE)
                    : null, HomeData.class);
        } catch (JSONException e) {
            return null;
        }
    }

    public static boolean isHomeScreenCached(Context context) {
        return isContentCached(context, Constants.HOME_RESPONSE, Constants.SP_OFFLINE_ARTICLE);
    }

    public static boolean isSectionCached(Context context, String sectionId) {
        return isContentCached(context, sectionId, Constants.SP_OFFLINE_SECTION);
    }

    public static void saveSectionData(Context context, String sectionId, ArrayList<ITabSectionRecyclerViewItem> sectionDetailList) {
        OfflineSectionModel offlineSectionModel = new OfflineSectionModel();
        if (null != sectionDetailList && !sectionDetailList.isEmpty()) {
            if (sectionDetailList.get(0) instanceof FeaturedArticle) {
                offlineSectionModel.setFeaturedArticle((FeaturedArticle) sectionDetailList.get(0));
                sectionDetailList.remove(0);
            }

            for (ITabSectionRecyclerViewItem gridRecentArticle : sectionDetailList) {
                if (gridRecentArticle instanceof GridRecentArticle) {
                    offlineSectionModel.addGridRecentArticle((GridRecentArticle) gridRecentArticle);
                } else if (gridRecentArticle instanceof HomeRecentArticle) {
                    offlineSectionModel.addHomeRecentArticle((HomeRecentArticle) gridRecentArticle);
                }
            }
            Gson gson = new Gson();
            addContent(context, sectionId, gson.toJson(offlineSectionModel), Constants.SP_OFFLINE_SECTION);

        }
    }

    public static ArrayList<ITabSectionRecyclerViewItem> getSectionData(Context context, String sectionId) {
        Gson gson = new Gson();
        OfflineSectionModel offlineSectionModel = null;

        mJsonObject = getContent(context, Constants.SP_OFFLINE_SECTION);
        try {
            offlineSectionModel = gson.fromJson(mJsonObject != null ? mJsonObject.getString(sectionId) : null, OfflineSectionModel.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<ITabSectionRecyclerViewItem> sectionList = new ArrayList<>();
        if (null != offlineSectionModel) {

            if (null != offlineSectionModel.getFeaturedArticle()) {
                sectionList.add(offlineSectionModel.getFeaturedArticle());
            }
            if (null != offlineSectionModel.getGridRecentArticles()) {
                sectionList.addAll(offlineSectionModel.getGridRecentArticles());
            }
            if (null != offlineSectionModel.getHomeRecentArticles()) {
                sectionList.addAll(offlineSectionModel.getHomeRecentArticles());
            }
        }
        return sectionList;
    }

    public static void addViewedArticle(Context context, String articleId) {
        addContent(context, articleId, articleId, Constants.SP_VIEWED_ARTICLES);
    }

    public static boolean isArticleViewed(Context context, String articledId) {
        mJsonObject = getContent(context, Constants.SP_VIEWED_ARTICLES);
        boolean b = mJsonObject != null && mJsonObject.has(articledId);
        return mJsonObject != null && mJsonObject.has(articledId);
    }


}
