/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.network;


import com.ninexe.ui.models.AccessTokenModel;
import com.ninexe.ui.models.ArticleDetailResponse;
import com.ninexe.ui.models.ArticleResponse;
import com.ninexe.ui.models.CommentResponse;
import com.ninexe.ui.models.GenericEmailModel;
import com.ninexe.ui.models.GenericMessageModel;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.HomeData;
import com.ninexe.ui.models.HomeResponse;
import com.ninexe.ui.models.LoginDataModel;
import com.ninexe.ui.models.LoginUserModel;
import com.ninexe.ui.models.NotificationCategoryResponse;
import com.ninexe.ui.models.NotificationSubscriptionModel;
import com.ninexe.ui.models.NotificationSubscriptionResponse;
import com.ninexe.ui.models.PollResponse;
import com.ninexe.ui.models.QuizAnswerModel;
import com.ninexe.ui.models.QuizArticle;
import com.ninexe.ui.models.QuizResponse;
import com.ninexe.ui.models.ReactionModel;
import com.ninexe.ui.models.ReactionResponseModel;
import com.ninexe.ui.models.SectionResponse;
import com.ninexe.ui.models.SendNotificationSubscriptionModel;
import com.ninexe.ui.models.SendNotificationSubscriptionResponseModel;
import com.ninexe.ui.models.StaticUrlResponse;
import com.ninexe.ui.models.SubmitCommentBody;
import com.ninexe.ui.models.UserRegistrationData;
import com.ninexe.ui.models.VersionsResponseModel;
import com.ninexe.ui.models.ViewMoreCommentsResponse;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by nagesh on 14/9/15.
 */
public interface ApiService {
    /* Detail Page */

    /* Home Page */
    @GET("/screens/mobile_home")
    void getHomeData(Callback<HomeResponse> responseCallback);

    /*Get Sections*/
    @GET("/sections")
    void getSections(Callback<HomeData> responseCallback);

    /* Article Detail Screen*/
    @GET("/articles/{articleId}")
    void getArticleDetail(@Path("articleId") String articleId, Callback<ArticleDetailResponse> responseCallback);

    /* Article Detail Screen with view count increment*/
    @GET("/articles/{articleId}")
    void getArticleDetailWithUpdateViewCount(@Path("articleId") String articleId,
                                             @Query("updateCount") boolean value,
                                             Callback<ArticleDetailResponse> responseCallback);

    /* Section Data */
    @GET("/sections/{sectionId}")
    void getSectionData(@Path("sectionId") String sectionId, Callback<SectionResponse> responseCallback);

    /* Get Articles For Section Id*/
   /* @GET("/articles")
    void getArticles(@Query("section") String sectionId, @Query("page") int currentPage,
                     @Query("perPage") int perPage,
                     Callback<ArticleResponse> responseCallback);*/


    @GET("/articles")
    void getArticles(@Query("section") String sectionId, @Query("page") int currentPage,
                     @Query("perPage") int perPage,
                     @Query("sort") String sortOrder,
                     Callback<ArticleResponse> responseCallback);

    /* Get Articles For pagination*/
    @GET("/{url}")
    void getArticlesForQueryModifier(@Path("url") String url,
                                     @QueryMap Map<String, String> options,
                                     @Query("page") int currentPage,
                                     @Query("perPage") int perPage,
                                     Callback<ArticleResponse> responseCallback);

    /* Get Articles For pagination*/
   /* @GET("/{url}")
    void getArticlesForQueryModifier(@Path("url") String url,
                                     @QueryMap Map<String, String> options,
                                     @Query("page") int currentPage,
                                     @Query("perPage") int perPage,
                                     @Query("sort") String sortOrder,
                                     Callback<ArticleResponse> responseCallback);*/


    /* Get Articles For Search*/
    @GET("/articles")
    void getArticlesForSearchQuery(@Query("search") String searchQuery, @Query("page") int currentPage, @Query("perPage") int perPage, Callback<ArticleResponse> responseCallback);

    /* Get Static URLs*/
    @GET("/screens/static")
    void getStaticUrls(Callback<StaticUrlResponse> responseCallback);

    /* Register User*/
    /*@POST("/users")
    void registerUser(@Body UserRegistration userRegistration, Callback<GenericResponse<UserRegistrationData>> responseCallback);*/

    @Multipart
    @POST("/users")
    void registerUser(@Part("profilePic") TypedFile file,
                      @Part("name") TypedString name,
                      @Part("email") TypedString email,
                      @Part("password") TypedString password,
                      @Part("phone") TypedString phone,
                      Callback<GenericResponse<UserRegistrationData>> responseCallback);

    /* Login*/
    @Headers({"WWW-Authenticate: None"})
    @POST("/auth/email/login")
    void login(@Body LoginUserModel loginUserModel, Callback<GenericResponse<LoginDataModel>> responseCallback);

    /* Login*/
    @POST("/auth/email/login")
    void login(@Body String email, String password, Callback<GenericResponse<LoginDataModel>> responseCallback);

    /*Social Authentication*/
    @POST("/auth/{socialPlatform}/token")
    void socialAuthentication(@Path("socialPlatform") String socialPlatform,
                              @Body AccessTokenModel access_token,
                              Callback<GenericResponse<LoginDataModel>> responseCallback);

    /*Comment*/
    @POST("/articles/{articleId}/comments")
    void comment(@Path("articleId") String articleId, @Body SubmitCommentBody comment,
                 Callback<GenericResponse<CommentResponse>> responseCallback);


    //Article reaction
    @POST("/articles/{articleId}/reactions")
    void react(@Path(("articleId")) String articleId, @Body ReactionModel reactionModel, Callback<GenericResponse<ReactionResponseModel>> responseCallback);

    /*View More Comments*/
    @GET("/articles/{articleId}/comments")
    void viewMoreComments(@Path("articleId") String articleId,
                          @Query("page") int currentPage,
                          @Query("perPage") int perPage,
                          Callback<ViewMoreCommentsResponse> responseCallback);

    /*Forgot password */
    @POST("/resetPasswordEmail")
    void forgotPassword(@Body GenericEmailModel genericEmailModel, Callback<GenericResponse<GenericMessageModel>> restCallback);

    /*News letter Subscription*/
    @POST("/sendNewsletterSubscriptionVerificationEmail")
    void newsLetterSubscribe(@Body GenericEmailModel genericEmailModel, Callback<GenericResponse<GenericMessageModel>> restCallback);

    /* Article Detail Screen*/
    @GET("/articles/{articleId}")
    void getQuizDetail(@Path("articleId") String articleId, Callback<GenericResponse<QuizArticle>> responseCallback);

    /* Article Detail Screen with view count update*/
    @GET("/articles/{articleId}")
    void getQuizDetailWithViewCountUpdate(@Path("articleId") String articleId,
                                          @Query("updateCount") boolean value
            , Callback<GenericResponse<QuizArticle>> responseCallback);

    /*Quiz Answers*/
    @POST("/articles/{articleId}/userAnswers")
    void getQuizResult(@Path("articleId") String articleId, @Body QuizAnswerModel answers, Callback<GenericResponse<QuizResponse>> restCallback);

    /*Quiz Answers*/
    @POST("/articles/{articleId}/userAnswers")
    void getPollResult(@Path("articleId") String articleId, @Body QuizAnswerModel answers, Callback<GenericResponse<PollResponse>> restCallback);

    /* Register Notifications */
    @POST("/notifications")
    void subscribeForNotifications(@Body NotificationSubscriptionModel notificationSubscriptionModel,
                                   Callback<GenericResponse<NotificationSubscriptionResponse>> restCallback);

    /* Get Notification Categories */
    @GET("/notifications")
    void getNotificationCategories(@Query("deviceId") String deviceId,
                                   Callback<GenericResponse<NotificationCategoryResponse>> restCallback);

    /*Send Notification Subscriptions*/
    @POST("/notifications")
    void sendNotificationSubscriptions(@Body SendNotificationSubscriptionModel sendNotificationSubscriptionModel,
                                       Callback<GenericResponse<SendNotificationSubscriptionResponseModel>> restCallback);

    /* Versions */
    @GET("/versions")
    void getVersions(Callback<VersionsResponseModel> responseCallback);

}
