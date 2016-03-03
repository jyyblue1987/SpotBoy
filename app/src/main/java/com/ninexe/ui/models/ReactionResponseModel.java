/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;

public class ReactionResponseModel {

    private String WOW;
    private String LOL;
    private String WTF;
    private String OMG;
    public String reaction;

    public String getWOW() {
        return WOW;
    }

    public String getLOL() {
        return LOL;
    }

    public String getWTF() {
        return WTF;
    }

    public String getOMG() {
        return OMG;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

}
