/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 12/10/15.
 */
public class Settings {

    private String mTitle;
    private boolean mHasToggle;
    private boolean mHasFwdArrow;
    private boolean mIsToggleSelected;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean hasToggle() {
        return mHasToggle;
    }

    public void setHasToggle(boolean mHasToggle) {
        this.mHasToggle = mHasToggle;
    }

    public boolean hasFwdArrow() {
        return mHasFwdArrow;
    }

    public void setHasFwdArrow(boolean mHasFwdArrow) {
        this.mHasFwdArrow = mHasFwdArrow;
    }

    public boolean isToggleSelected() {
        return mIsToggleSelected;
    }

    public void setToggleSelected(boolean mIsToggleSelected) {
        this.mIsToggleSelected = mIsToggleSelected;
    }
}
