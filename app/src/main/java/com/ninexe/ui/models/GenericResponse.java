/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 28/10/15.
 */
public class GenericResponse<T> {
    private T data;
    private GenericServerError errors;

    public GenericServerError getGenericServerError() {
        return errors;
    }

    public T getData() {
        return data;
    }
}
