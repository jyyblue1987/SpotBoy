/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by nagesh on 4/11/15.
 */
public class EmailUtils {

    public static void sendEmail(Context context, String emailTo, String emailSubject, String dialogTitle) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", emailTo, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        context.startActivity(Intent.createChooser(emailIntent, dialogTitle));
    }
}
