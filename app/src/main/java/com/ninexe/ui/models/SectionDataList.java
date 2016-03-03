/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class SectionDataList<T> implements ParameterizedType {

    private Class<?> wrapped;

    public SectionDataList(Class<T> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return new Type[]{wrapped};
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public Type getRawType() {
        return ArrayList.class;
    }
}
