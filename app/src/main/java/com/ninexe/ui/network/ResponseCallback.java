/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.network;


public interface ResponseCallback<T> {

    void success(T t);

    void failure(RestError error);
}
