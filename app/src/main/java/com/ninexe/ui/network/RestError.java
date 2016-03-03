/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.network;


import com.ninexe.ui.common.Constants;
import com.ninexe.ui.models.GenericServerError;

public class RestError {

    private int code;
    private String message;
    GenericServerError errors;

    public RestError(String message, int errorCode) {
        this.message = message;
        this.code = errorCode;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isNetworkError() {
        return this.code == Constants.NETWORK_ERROR_CODE;
    }

    public String getServerErrorMessage() {
        if (errors == null)
            return "Please try after some time";
        return errors.getMessage();
    }
}
