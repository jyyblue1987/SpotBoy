/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.common;

import android.content.Context;

/**
 * Created by nagesh on 3/9/15.
 */
public class Constants {

    // LOG
    public static final boolean DEV_ENABLE_LOG = true;
    public static final boolean PROD_ENABLE_LOG = false;


    // REST ERROR
    public static final int NETWORK_ERROR_CODE = 1001;

    //Navigation Drawer
    public static final String MENU_HOME = "home";
    public static final String MENU_HELLO_EDITOR = "hello editor";
    public static final String MENU_NOTIFICATION_HUB = "notifications";
    public static final String MENU_NEWS_LETTER = "news letter";
    public static final String MENU_SHARE_THE_APP = "share the app";
    public static final String MENU_SETTINGS = "settings";
    public static final String MENU_LOGOUT = "logout";

    //GridRecentArticle Drawable
    public static final String ARTICLE = "article";
    public static final String PHOTO = "photo";
    public static final String VIDEO = "video";
    public static final String LIVE_TV = "LiveTV";

    //Settings
    public static final String SETTINGS_NOTIFICATION = "Notifications";
    public static final String SETTINGS_THEME = "Theme";
    public static final String SETTINGS_TEXT_SIZE = "Text Size";
    public static final String SETTINGS_OFFLINE_CACHE = "Offline Caching";
    public static final String SETTINGS_FEEDBACK = "Feedback";
    public static final String SETTINGS_RATE_REVIEW = "Rate & Review";
    public static final String SETTINGS_TERMS_CONDITIONS = "T & C";
    public static final String SETTINGS_PRIVACY_POLICY = "Privacy Policy";
    public static final String SETTINGS_ABOUT_US = "About Us";
    public static final String SETTINGS_CONTACT_US = "Contact Us";
    public static final String SETTINGS_DISCLAIMER = "Disclaimer";
    public static final String SETTINGS_NOTIFICATION_SOUND = "Notification Sound";

    //Notification Settings
    public static final String NOTIFICATION_SETTINGS_DO_NOT_DISTURB
            = "Do Not Disturb ";

    //Article Detail
    public static final String ARTICLE_DETAIL_RELATED_ARTICLES_TEXT = "Related Articles";

    //Section Detail
    public static final int ARTICLES_PER_PAGE = 25;
    public static final int MAX_ARTICLES = 100;

    //View More Comments
    public static final int COMMENTS_PER_PAGE = 10;

    //WebView
    public static final String TEXT_HTML_TYPE = "text/html";
    public static final String ENCODING_UTF_8 = "UTF-8";

    //Log Tags
    public static final String APP_TAG = "SPOTBOYE";

    //Used to pass arguments
    public static final String EXTRA_ARTICLE_ID = "extra_article_id";
    public static final String EXTRA_ARTICLE_TITLE = "extra_article_title";
    public static final String EXTRA_ARTICLE_TYPE = "extra_article_type";
    public static final String EXTRA_PHOTO_GALLERY_VIEW_PAGER_CURRENT_POSITION
            = "extra_photo_gallery_view_pager_current_position";
    public static final String EXTRA_SUB_MENU_LIST = "extra_sub_menu_list";
    public static final String EXTRA_SUB_MENU_POSITION = "extra_sub_menu_position";
    public static final String EXTRA_ARTICLE_QUESTION = "extra_article_question";
    public static final String EXTRA_SUB_MENU = "extra_sub_menu";
    public static final String EXTRA_MENU_TITLE = "extra_menu_title";
    public static final String ACCESS_TOKEN = "access_token";
    public static final String EXTRA_COMMENT_CHANGE = "extra_comment_change";
    public static final String ARG_ARTICLE_ID = "arg_article_id";
    public static final String EXTRA_ARTICLE_MESSAGE = "extra_article_message";

    //Shared Preferences
    public static final String SP_TEXT_SIZE = "sp_text_size";
    public static final String SP_LOGGED_IN = "logged_in";
    public static final String SP_LOGIN_SKIPPED = "login_skipped";
    public static final String SP_USER_NAME = "user_name";
    public static final String SP_USER_PROFILE_PIC = "user_profile_pic";
    public static final String SP_USER_ACCESS_TOKEN = "user_acccess_token";
    public static final String SP_REACTION_MAP = "reaction_map";
    public static final String SP_OFFLINE_ARTICLE = "sp_offline_article";
    public static final String SP_IS_CACHING = "sp_is_caching";
    public static final String SP_PHOTO_GALLERY_DETAIL_CURRENT_POSITION = "sp_photo_gallery_detail_current_position";
    public static final String SP_OFFLINE_SECTION = "sp_offline_section";
    public static final String SP_GCM_TOKEN = "sp_gcm_token";
    public static final String SP_IS_SUBSCRIBED_TO_NOTIFICATIONS = "sp_is_subscribed_to_notifications";
    public static final String SP_GCM_DEVICE_ID = "sp_gcm_device_id";
    public static final String SP_IS_DND_ENABLED = "sp_is_dnd_enabled";
    public static final String SP_SHOW_NOTIFICATIONS_ENABLED = "sp_show_notifications_enabled";
    public static final String SP_WELCOME_SCREEN_SHOWN = "welcome_screen_shown";
    public static final String SP_VIEWED_ARTICLES = "sp_viewed_articles";
    public static final String SP_SHOW_RATE_POPUP = "sp_rate_show_popup";
    public static final String SP_NO_VIEWED_ARTICLES = "sp_number_of_viewed_articles";
    public static final String SP_SHOW_NOTIFICATIONS_SOUND_ENABLED = "sp_notification_enabled";


    //Twitter credentials
    public static final String TWITTER_CONSUMER_KEY = "9HcHtFLryNFRG5k7eQreeavtr";
    public static final String TWITTER_CONSUMER_SECRET = "0jFUCcq84zMjdQMN9aDScqEEfa0S5QWIb5YOPAuGwkvyIEi7t0";

    public static final String FACEBOOK = "facebook";
    public static final String GPLUS = "google";
    public static final String TWITTER = "twitter";
    public static final String TWITTER_SECRET_TOKEN = "twitter_secret_token";
    public static final String TWITTER_USER_ID = "twitter_user_id";

    public static final String WOW = "WOW";
    public static final String LOL = "LOL";
    public static final String WTF = "WTF";
    public static final String OMG = "OMG";
    public static final String GCM_REGISTRATION_COMPLETE = "gcm_registration_complete";

    public static final String PERSONALITY_TEST = "PersonalityTest";
    public static final String QUIZ = "Quiz";
    public static final String POLL = "Poll";
    public static final String CONTEST = "Contest";
    public static final String SP_POLL_DATA = "poll_data";

    //GCM
    public static final String PLATFORM_ANDROID = "android";
    public static final String VIDEO_TYPE = "Video";

    //Image Types
    public static final String IMAGE_THUMBNAIL = "_thumbnail";
    public static final String IMAGE_SMALL = "_small";
    public static final String IMAGE_TINY = "_tiny";

    public static final String HOME_RESPONSE = "homeDataRespone";

    //PhotoGallery
    public static final int PHOTO_GALLERY_DUMMY = -100;
    public static final String EXTRA_DEEPLINK = "extra_deeplink";

    public static final String SP_FILE = "sp_file";
    public static final String DNDTIME = "  10PM TO 6AM ";
    public static final String DEFAULT_SOUND = "default";
    public static final int NO_ARTICLE_RATE = 10;// Number of articles viewed before rate app pops up


    //Sorting order for articles
    public static final String ARTICLE_SORTING_ORDER = "-publishedAt";
    //public static final String ARTICLE_SORTING_ORDER = "";
}
