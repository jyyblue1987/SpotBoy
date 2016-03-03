/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

/**
 * Created by nagesh on 17/10/15.
 */
public class ReactometerSection implements IArticleDetailRecyclerViewItem {

    private String lolValue;
    private boolean isFullscreen;
    private String wtfValue;
    private String omgValue;
    private String wowValue;
    String reaction;

    public String getWtfValue() {
        return wtfValue;
    }

    public String getReaction() {
        return reaction;
    }

    public void setWtfValue(String wtfValue) {
        this.wtfValue = wtfValue;
    }

    public String getLolValue() {
        return lolValue;
    }

    public void setLolValue(String lolValue) {
        this.lolValue = lolValue;
    }

    public String getOmgValue() {
        return omgValue;
    }

    public void setOmgValue(String omgValue) {
        this.omgValue = omgValue;
    }

    public String getWowValue() {
        return wowValue;
    }

    public void setWowValue(String wowValue) {
        this.wowValue = wowValue;
    }


    public boolean isFullscreen() {
        return isFullscreen;
    }

    public void setIsFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
    }


    public void setReaction(String reaction) {
        this.reaction = reaction;
    }


    @Override
    public int getItemType() {
        return IArticleDetailRecyclerViewItem.TYPE_REACTOMETER_SECTION;
    }
}
