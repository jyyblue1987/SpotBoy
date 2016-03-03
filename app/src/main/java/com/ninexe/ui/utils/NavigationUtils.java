/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ninexe.ui.activities.ArticleDetailActivity;
import com.ninexe.ui.activities.HomeActivity;
import com.ninexe.ui.activities.LoginActivity;
import com.ninexe.ui.activities.NewsLetterActivity;
import com.ninexe.ui.activities.NotificationHubActivity;
import com.ninexe.ui.activities.PhotoGalleryActivity;
import com.ninexe.ui.activities.PhotoGalleyDetailActivity;
import com.ninexe.ui.activities.QuizActivity;
import com.ninexe.ui.activities.SearchActivity;
import com.ninexe.ui.activities.SectionDetailActivity;
import com.ninexe.ui.activities.SettingsActivity;
import com.ninexe.ui.activities.TutorialScreenActivity;
import com.ninexe.ui.activities.ViewMoreCommentsActivity;
import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.NotificationHub;
import com.ninexe.ui.models.SubMenu;

import java.util.ArrayList;

/**
 * Created by nagesh on 16/10/15.
 */

// Used to launch activities
public class NavigationUtils {

    public static void startArticleDetailActivity(Context context, String articleId) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        context.startActivity(intent);
    }

    public static void startArticleDetailActivity(Context context, String articleId, String title) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.EXTRA_ARTICLE_TITLE, title);
        context.startActivity(intent);
    }

    public static void startSectionDetailActivity(Context context,
                                                  ArrayList<SubMenu> subMenuArrayList,
                                                  int childPosition, String title) {
        Intent intent = new Intent(context, SectionDetailActivity.class);
        intent.putExtra(Constants.EXTRA_SUB_MENU_LIST, subMenuArrayList);
        intent.putExtra(Constants.EXTRA_SUB_MENU_POSITION, childPosition);
        intent.putExtra(Constants.EXTRA_MENU_TITLE, title);
        context.startActivity(intent);
    }

    public static void startViewMoreCommentsActivity(Context context, String articleId) {
        Intent intent = new Intent(context, ViewMoreCommentsActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        context.startActivity(intent);
    }

    public static void startSettingsActivity(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    public static void startPhotoGalleryActivity(Context context, String articleId) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        context.startActivity(intent);
    }


    public static void startPhotoGalleryDetailActivity(Context context, int position) {
        Intent intent = new Intent(context, PhotoGalleyDetailActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_GALLERY_VIEW_PAGER_CURRENT_POSITION, position);
        context.startActivity(intent);
    }

    public static void startSearchActivity(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startNewsLetterActivity(Context context) {
        Intent intent = new Intent(context, NewsLetterActivity.class);
        context.startActivity(intent);
    }

    public static void startHomeActivity(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void startQuizActivity(Context context, String articleId, String type) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.EXTRA_ARTICLE_TYPE, type);
        context.startActivity(intent);
    }

    public static void startNotificationHubActivity(Context context) {
        Intent intent = new Intent(context, NotificationHubActivity.class);
        context.startActivity(intent);
    }

    /*public static void startWelcomeActivity(Context context) {
        Intent intent = new Intent(context, TutorialScreenActivity.class);
        context.startActivity(intent);
    }*/

    public static void startPhotoGalleryActivityForResult(Activity context, String articleId) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        context.startActivityForResult(intent, 101);
    }

    public static void startQuizActivityForResult(Activity context, String articleId, String type) {
        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.EXTRA_ARTICLE_TYPE, type);
        context.startActivityForResult(intent, 102);
    }

    public static void startArticleDetailActivityForResult(Activity context, String articleId, String title) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.EXTRA_ARTICLE_TITLE, title);
        context.startActivityForResult(intent, 103);
    }
}
