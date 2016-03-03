/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.network;

import android.content.Context;
import android.text.TextUtils;

import com.ninexe.ui.R;
import com.ninexe.ui.utils.dataproviders.UserDataProvider;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by nagesh on 14/9/15.
 */
public class RestClient {

    private static final String HEADER_AUTHOURIZATION = "Authorization";
    private static final String BEARER = "Bearer";

    private final RestAdapter REST_ADAPTER;
    private final RestAdapter REST_ADAPTER_AUTH_HEADER;

    public static final int NO_AUTH_HEADER = 0;
    public static final int AUTH_HEADER = 1;
    private Context mContext;

    public RestClient(Context context) {
        mContext = context;
        String baseUrl = context.getString(R.string.base_url);
        REST_ADAPTER = buildRestAdapter(baseUrl, null);
        REST_ADAPTER_AUTH_HEADER = buildRestAdapter(baseUrl, new AuthRequestInterceptor(context));
    }

    private RestAdapter buildRestAdapter(String baseUrl, RequestInterceptor requestInterceptor) {
        RestAdapter.LogLevel logLevel = (!mContext.getResources().getBoolean(R.bool.isProduction))
                ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE;
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setLogLevel(logLevel);
        if (!TextUtils.isEmpty(baseUrl)) {
            builder.setEndpoint(baseUrl);
        }
        if (requestInterceptor != null) {
            builder.setRequestInterceptor(requestInterceptor);
        }
        return builder.build();
    }

    public ApiService getService(int headerType) {
        if (headerType == AUTH_HEADER) {
            return REST_ADAPTER_AUTH_HEADER.create(ApiService.class);
        } else {
            return REST_ADAPTER.create(ApiService.class);
        }
    }

    private class AuthRequestInterceptor implements RequestInterceptor {
        private final Context mContext;

        public AuthRequestInterceptor(Context context) {
            mContext = context;
        }

        @Override
        public void intercept(RequestFacade request) {
            if (null != UserDataProvider.getInstance().getAccessToken(mContext)) {
                String accessToken = BEARER + " " + UserDataProvider.getInstance().getAccessToken(mContext);
                request.addHeader(HEADER_AUTHOURIZATION, accessToken);
            }
        }
    }
}
