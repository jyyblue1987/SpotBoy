/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.network;

import android.content.Context;

import com.ninexe.ui.common.Constants;
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
import com.ninexe.ui.models.UserRegistration;
import com.ninexe.ui.models.UserRegistrationData;
import com.ninexe.ui.models.VersionsResponseModel;
import com.ninexe.ui.models.ViewMoreCommentsResponse;
import com.ninexe.ui.utils.RateAppUtil;
import com.ninexe.ui.utils.dataproviders.OfflineArticleDataHandler;

import java.util.Map;

import retrofit.Callback;
import retrofit.client.Response;

/**
 * Created by nagesh on 14/9/15.
 */
public class NetworkAdapter {
    private static NetworkAdapter mNetworkAdapter;
    private final RestClient mRestClient;

    private NetworkAdapter(Context context) {
        mRestClient = new RestClient(context);
    }

    public static NetworkAdapter get(Context context) {
        if (null == mNetworkAdapter) {
            mNetworkAdapter = new NetworkAdapter(context);
        }
        return mNetworkAdapter;
    }


    public void getHomeScreenData(final ResponseCallback<HomeResponse> responseCallback) {
        Callback<HomeResponse> restCallback = new RestCallback<HomeResponse>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(HomeResponse bankResponse, Response response) {
                responseCallback.success(bankResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getHomeData(restCallback);
    }

    public void getArticleScreenDetail(final String articleId, final ResponseCallback<ArticleDetailResponse> responseCallback,
                                       final Context context) {
        Callback<ArticleDetailResponse> restCallback = new RestCallback<ArticleDetailResponse>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(ArticleDetailResponse articleDetailResponse, Response response) {
                responseCallback.success(articleDetailResponse);
                OfflineArticleDataHandler.addViewedArticle(context, articleId);
                if (OfflineArticleDataHandler.isOfflineCacheEnabled(context) && articleDetailResponse.getData().isArticle()) {
                    OfflineArticleDataHandler.addArticleResponse(articleDetailResponse.getData().getId(), articleDetailResponse, context);
                }
            }
        };
        if (OfflineArticleDataHandler.isArticleViewed(context, articleId)) {
            mRestClient.getService(RestClient.NO_AUTH_HEADER).getArticleDetail(articleId, restCallback);
        } else {
            RateAppUtil.incrementViewedArticles(context);
            mRestClient.getService(RestClient.NO_AUTH_HEADER).getArticleDetailWithUpdateViewCount(articleId, true, restCallback);
        }
    }

    public void getSectionScreenData(String sectionId, final ResponseCallback<SectionResponse> responseCallback) {
        Callback<SectionResponse> restCallback = new RestCallback<SectionResponse>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(SectionResponse sectionResponse, Response response) {
                responseCallback.success(sectionResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getSectionData(sectionId, restCallback);
    }

    public void getArticles(String sectionId, int currentPage, int perPage, final ResponseCallback<ArticleResponse> responseCallback) {
        Callback<ArticleResponse> restCallback = new RestCallback<ArticleResponse>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(ArticleResponse articleResponse, Response response) {
                responseCallback.success(articleResponse);
            }
        };
        /*mRestClient.getService(RestClient.NO_AUTH_HEADER).getArticles(sectionId, currentPage, perPage, restCallback);*/
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getArticles(sectionId, currentPage, perPage,
                Constants.ARTICLE_SORTING_ORDER, restCallback);
    }

    public void getArticlesForQueryModifier(String url,
                                            Map<String, String> options,
                                            int currentPage, int perPage,
                                            final ResponseCallback<ArticleResponse> responseCallback) {
        Callback<ArticleResponse> restCallback = new RestCallback<ArticleResponse>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(ArticleResponse articleResponse, Response response) {
                responseCallback.success(articleResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getArticlesForQueryModifier(url,
                options, currentPage, perPage, restCallback);
        /*mRestClient.getService(RestClient.NO_AUTH_HEADER).getArticlesForQueryModifier(url,
                options, currentPage, perPage, Constants.ARTICLE_SORTING_ORDER, restCallback);*/
    }

    public void getArticlesForSearch(String searchQuery, int currentPage, int perPage,
                                     final ResponseCallback<ArticleResponse> responseCallback) {
        Callback<ArticleResponse> restCallback = new RestCallback<ArticleResponse>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(ArticleResponse articleResponse, Response response) {
                responseCallback.success(articleResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getArticlesForSearchQuery(searchQuery, currentPage, perPage, restCallback);
    }

    public void getStaticUrls(final ResponseCallback<StaticUrlResponse> responseCallback) {
        Callback<StaticUrlResponse> restCallback = new RestCallback<StaticUrlResponse>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(StaticUrlResponse staticUrlResponse, Response response) {
                responseCallback.success(staticUrlResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getStaticUrls(restCallback);
    }

    public void registerUser(UserRegistration userRegistration,
                             final ResponseCallback<GenericResponse<UserRegistrationData>> responseCallback) {
        Callback<GenericResponse<UserRegistrationData>> restCallback = new RestCallback<GenericResponse<UserRegistrationData>>() {

            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(GenericResponse<UserRegistrationData> registrationDataGenericResponse, Response response) {
                responseCallback.success(registrationDataGenericResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).registerUser(userRegistration.getProfilePic(),
                userRegistration.getName(),
                userRegistration.getEmail(),
                userRegistration.getPassword(),
                userRegistration.getPhone(),
                restCallback);
    }

    public void login(LoginUserModel loginUserModel,
                      final ResponseCallback<GenericResponse<LoginDataModel>> responseCallback) {
        Callback<GenericResponse<LoginDataModel>> restCallback = new RestCallback<GenericResponse<LoginDataModel>>() {

            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(GenericResponse<LoginDataModel> loginResponse, Response response) {
                responseCallback.success(loginResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).login(loginUserModel, restCallback);
    }

    public void socialLogin(String socialPlatform, AccessTokenModel accessToken,
                            final ResponseCallback<GenericResponse<LoginDataModel>> responseCallback) {

        Callback<GenericResponse<LoginDataModel>> restCallback = new RestCallback<GenericResponse<LoginDataModel>>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(GenericResponse<LoginDataModel> loginResponse, Response response) {
                responseCallback.success(loginResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).socialAuthentication(socialPlatform, accessToken, restCallback);
    }

    public void comment(String articleId, SubmitCommentBody comment,
                        final ResponseCallback<GenericResponse<CommentResponse>> responseCallback) {

        Callback<GenericResponse<CommentResponse>> restCallback = new RestCallback<GenericResponse<CommentResponse>>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(GenericResponse<CommentResponse> commentResponse, Response response) {
                responseCallback.success(commentResponse);
            }
        };
        mRestClient.getService(RestClient.AUTH_HEADER).comment(articleId, comment, restCallback);
    }

    public void react(String articleId, ReactionModel reactionModel,
                      final ResponseCallback<GenericResponse<ReactionResponseModel>> responseCallback) {

        Callback<GenericResponse<ReactionResponseModel>> restCallback = new RestCallback<GenericResponse<ReactionResponseModel>>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(GenericResponse<ReactionResponseModel> reactionResponse, Response response) {
                responseCallback.success(reactionResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).react(articleId, reactionModel, restCallback);
    }

    public void viewMoreComments(String articleId, int currentPage, int commentsPerPage,
                                 final ResponseCallback<ViewMoreCommentsResponse> responseCallback) {
        Callback<ViewMoreCommentsResponse> restCallback = new RestCallback<ViewMoreCommentsResponse>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(ViewMoreCommentsResponse viewMoreCommentsResponse, Response response) {
                responseCallback.success(viewMoreCommentsResponse);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).viewMoreComments(articleId, currentPage, commentsPerPage, restCallback);
    }

    public void forgotPassword(String email, final ResponseCallback<GenericResponse<GenericMessageModel>> forgotResponseCallback) {
        final Callback<GenericResponse<GenericMessageModel>> restCallback = new RestCallback<GenericResponse<GenericMessageModel>>() {
            @Override
            public void success(GenericResponse<GenericMessageModel> forgotPasswordResponseGenericResponse, Response response) {
                forgotResponseCallback.success(forgotPasswordResponseGenericResponse);
            }

            @Override
            public void failure(RestError error) {
                forgotResponseCallback.failure(error);
            }
        };
        GenericEmailModel genericEmailModel = new GenericEmailModel();
        genericEmailModel.setEmail(email);
        mRestClient.getService(RestClient.NO_AUTH_HEADER).forgotPassword(genericEmailModel, restCallback);
    }

    public void emailSubscribe(String email, final ResponseCallback<GenericResponse<GenericMessageModel>> forgotResponseCallback) {
        final Callback<GenericResponse<GenericMessageModel>> restCallback = new RestCallback<GenericResponse<GenericMessageModel>>() {
            @Override
            public void success(GenericResponse<GenericMessageModel> forgotPasswordResponseGenericResponse, Response response) {
                forgotResponseCallback.success(forgotPasswordResponseGenericResponse);
            }

            @Override
            public void failure(RestError error) {
                forgotResponseCallback.failure(error);
            }
        };
        GenericEmailModel genericEmailModel = new GenericEmailModel();
        genericEmailModel.setEmail(email);
        mRestClient.getService(RestClient.NO_AUTH_HEADER).newsLetterSubscribe(genericEmailModel, restCallback);
    }


    public void getQuizArticleDetail(final Context context, final String articleId, final ResponseCallback<GenericResponse<QuizArticle>> quizArticleResponseCallback, boolean isLoggedIn) {
        Callback<GenericResponse<QuizArticle>> restCallback = new RestCallback<GenericResponse<QuizArticle>>() {
            @Override
            public void failure(RestError error) {
                quizArticleResponseCallback.failure(error);
            }

            @Override
            public void success(GenericResponse<QuizArticle> articleDetailResponse, Response response) {
                OfflineArticleDataHandler.addViewedArticle(context, articleId);
                quizArticleResponseCallback.success(articleDetailResponse);

            }
        };
        if (isLoggedIn) {
            if (OfflineArticleDataHandler.isArticleViewed(context, articleId)) {
                mRestClient.getService(RestClient.AUTH_HEADER).getQuizDetail(articleId, restCallback);
            } else {
                RateAppUtil.incrementViewedArticles(context);
                mRestClient.getService(RestClient.AUTH_HEADER).getQuizDetailWithViewCountUpdate(articleId, true, restCallback);
            }
        } else {
            if (OfflineArticleDataHandler.isArticleViewed(context, articleId)) {
                mRestClient.getService(RestClient.NO_AUTH_HEADER).getQuizDetail(articleId, restCallback);
            } else {
                RateAppUtil.incrementViewedArticles(context);
                mRestClient.getService(RestClient.NO_AUTH_HEADER).getQuizDetailWithViewCountUpdate(articleId, true, restCallback);
            }
        }
    }

    public void getQuizResult(String articleId, QuizAnswerModel quizAnswers, final ResponseCallback<GenericResponse<QuizResponse>> quizResponseCallBack) {
        final Callback<GenericResponse<QuizResponse>> restCallback = new RestCallback<GenericResponse<QuizResponse>>() {
            @Override
            public void failure(RestError error) {
                quizResponseCallBack.failure(error);
            }

            @Override
            public void success(GenericResponse<QuizResponse> quizResponse, Response response) {
                quizResponseCallBack.success(quizResponse);
            }
        };
        mRestClient.getService(RestClient.AUTH_HEADER).getQuizResult(articleId, quizAnswers, restCallback);
    }

    public void getPollResult(String articleId, QuizAnswerModel quizAnswers, final ResponseCallback<GenericResponse<PollResponse>> pollResponseCallBack) {
        final Callback<GenericResponse<PollResponse>> restCallback = new RestCallback<GenericResponse<PollResponse>>() {

            @Override
            public void success(GenericResponse<PollResponse> pollResponseGenericResponse, Response response) {
                pollResponseCallBack.success(pollResponseGenericResponse);
            }

            @Override
            public void failure(RestError error) {
                pollResponseCallBack.failure(error);
            }

        };
        mRestClient.getService(RestClient.AUTH_HEADER).getPollResult(articleId, quizAnswers, restCallback);
    }

    public void subscribeForNotifications(NotificationSubscriptionModel notificationSubscriptionModel,
                                          final ResponseCallback<GenericResponse<NotificationSubscriptionResponse>> responseCallback) {
        final Callback<GenericResponse<NotificationSubscriptionResponse>> restCallback = new RestCallback<GenericResponse<NotificationSubscriptionResponse>>() {

            @Override
            public void success(GenericResponse<NotificationSubscriptionResponse> notificationSubscriptionResponse, Response response) {
                responseCallback.success(notificationSubscriptionResponse);
            }

            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).subscribeForNotifications(notificationSubscriptionModel, restCallback);
    }

    public void getNotificationCatgeories(String deviceId,
                                          final ResponseCallback<GenericResponse<NotificationCategoryResponse>> responseCallback) {
        final Callback<GenericResponse<NotificationCategoryResponse>> restCallback = new RestCallback<GenericResponse<NotificationCategoryResponse>>() {

            @Override
            public void success(GenericResponse<NotificationCategoryResponse> notificationCategoryResponse, Response response) {
                responseCallback.success(notificationCategoryResponse);
            }

            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getNotificationCategories(deviceId, restCallback);
    }

    public void sendNotificationSubscriptions(SendNotificationSubscriptionModel sendNotificationSubscriptionModel,
                                              final ResponseCallback<GenericResponse<SendNotificationSubscriptionResponseModel>> responseCallback) {
        final Callback<GenericResponse<SendNotificationSubscriptionResponseModel>> restCallback
                = new RestCallback<GenericResponse<SendNotificationSubscriptionResponseModel>>() {


            @Override
            public void success(GenericResponse<SendNotificationSubscriptionResponseModel>
                                        sendNotificationSubscriptionResponse, Response response) {
                responseCallback.success(sendNotificationSubscriptionResponse);
            }

            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).sendNotificationSubscriptions(sendNotificationSubscriptionModel, restCallback);
    }

    public void getSections(final ResponseCallback<HomeData> responseCallback) {
        final Callback<HomeData> restCallback = new RestCallback<HomeData>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(HomeData homeData, Response response) {
                responseCallback.success(homeData);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getSections(restCallback);
    }

    public void getVersions(final ResponseCallback<VersionsResponseModel> responseCallback) {
        final Callback<VersionsResponseModel> restCallback = new RestCallback<VersionsResponseModel>() {
            @Override
            public void failure(RestError error) {
                responseCallback.failure(error);
            }

            @Override
            public void success(VersionsResponseModel versionsResponseModel, Response response) {
                responseCallback.success(versionsResponseModel);
            }
        };
        mRestClient.getService(RestClient.NO_AUTH_HEADER).getVersions(restCallback);
    }

}
