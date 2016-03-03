/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.models;


import java.util.ArrayList;

/**
 * Created by nagesh on 8/11/15.
 */
public class NotificationCategoryResponse {
    ArrayList<NotificationTopic> topics;
    boolean dnd;

    public ArrayList<NotificationTopic> getTopics() {
        return topics;
    }

    public boolean isDnd() {
        return dnd;
    }
}
