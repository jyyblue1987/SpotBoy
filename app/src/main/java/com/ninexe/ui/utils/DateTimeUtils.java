/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by nagesh on 12/10/15.
 */
public class DateTimeUtils {

    public static String getDate(String time) {

        if (null == time) return null;

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String DEFAULT_DATE_FORMAT = "dd MMM yyyy hh:mm aa";
        SimpleDateFormat newDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        try {
            Date date = dateFormatter.parse(time);
            newDateFormat.setTimeZone(TimeZone.getDefault());
            return newDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean isTimeWithInNotificationDNDRange() {
        boolean isWithInRange = false;
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        if (hour >= 22 || hour <= 06) {
            isWithInRange = true;
        }
        return isWithInRange;
    }

}
