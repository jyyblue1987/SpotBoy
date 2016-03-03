/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.network;


import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.GenericResponse;
import com.ninexe.ui.models.GenericServerError;
import com.ninexe.ui.utils.LogUtils;

import retrofit.Callback;
import retrofit.RetrofitError;


public abstract class RestCallback<T> implements Callback<T> {

    private static final String TAG = RestCallback.class.getName();

    public abstract void failure(RestError error);


    @Override
    public void failure(RetrofitError retrofitError) {
        RestError restError = null;
        if (retrofitError != null) {
            try {
                restError = (RestError) retrofitError.getBodyAs(RestError.class);
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            if (restError == null) {
                restError = new RestError(getRetrofitErrorMessage(retrofitError), getErrorCode(retrofitError));
            }
        }
        failure(restError);
    }

    private static String getRetrofitErrorMessage(RetrofitError retrofitError) {
        String message = "";
        if (null != retrofitError) {
            if (retrofitError.getMessage() != null) {
                return retrofitError.getMessage();
            } else {
                switch (retrofitError.getKind()) {
                    case NETWORK:
                        message = "Unable to reach server";
                        LogUtils.LOGD(TAG, "An IOException occurred while communicating to the server");
                        break;
                    case CONVERSION:
                        message = "Something went wrong";
                        LogUtils.LOGD(TAG, "An exception was thrown while (de)serializing a body");
                        break;
                    case HTTP:
                        message = "Error in Http response";
                        LogUtils.LOGD(TAG, "A non-200 HTTP status code was received from the server e.g. 502, 503, etc...");
                        break;
                    case UNEXPECTED:
                        message = "Unexpected Error";
                        LogUtils.LOGD(TAG, "An internal error occurred while attempting to execute a request");
                        break;
                }
            }
        }
        return message;
    }

    private static int getErrorCode(RetrofitError retrofitError) {
        RetrofitError.Kind kind = retrofitError.getKind();
        if (kind != null && kind == RetrofitError.Kind.NETWORK) {
            LogUtils.LOGD(TAG, "Network Error");
            return Constants.NETWORK_ERROR_CODE;
        }
        return 0;
    }
}
