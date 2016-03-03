/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ninexe.ui.R;

public class NetworkCheckUtility {

    /**
     * Checking for all possible Internet providers
     **/

    public static Boolean isNetworkAvailable(Context context) {
        if (null == context) return false;

        Boolean isConnected = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null) {
                for (int index = 0; index < networkInfo.length; index++) {
                    if (networkInfo[index].isConnected()) {
                        isConnected = true;
                        break;
                    }
                }
            }
        }
        return isConnected;
    }

    public static void showNoNetworkDialog(final Context context, final boolean finishCurrentActivity) {
        String title = context.getString(R.string.no_network);
        String message = context.getString(R.string.please_check_internet_connection);
        DialogUtils.showAlertDialogWithCallBack(context,
                title,
                message,
                context.getString(R.string.ok),
                null,
                false,
                new DialogUtils.DialogInterfaceCallBack() {
                    @Override
                    public void positiveButtonClick(DialogInterface dialog) {
                        dialog.dismiss();
                        if (finishCurrentActivity && context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }

                    @Override
                    public void negativeButtonClick(DialogInterface dialog) {

                    }
                });
    }


}
