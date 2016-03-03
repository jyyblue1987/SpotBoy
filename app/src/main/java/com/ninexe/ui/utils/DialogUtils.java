/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.ninexe.ui.R;
import com.ninexe.ui.custom.CustomProgressDialog;

/**
 * Created by nagesh on 29/9/15.
 */
public class DialogUtils {

    private static ProgressDialog mProgressDialog;
    private static CustomProgressDialog mCustomProgressDialog;
    private static Dialog dialog;

    public static void showProgressDialog(Context context, String message) {
        try {
            if (mProgressDialog == null || !mProgressDialog.isShowing()) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.setCancelable(false);
                mProgressDialog.setMessage(message);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.show();
            } else {
                mProgressDialog.setMessage(message);
            }
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (WindowManager.BadTokenException bte) {
            bte.printStackTrace();
        }
    }

    public static void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
        } catch (WindowManager.BadTokenException bte) {
            bte.printStackTrace();
        }
    }

    public static void showProgressBar(ProgressBar progressBar) {
        ViewUtils.showView(progressBar);
    }

    public static void hideProgressBar(ProgressBar progressBar) {
        ViewUtils.hideView(progressBar);
    }

    public static void showRetryFrame(View retryFrame) {
        ViewUtils.showView(retryFrame);
    }

    public static void hideRetryFrame(View retryFrame) {
        ViewUtils.hideView(retryFrame);
    }

    public static void showAlertDialogWithCallBack(Context context,
                                                   final String title, String message,
                                                   String positiveBtnText, String negativeBtnText,
                                                   boolean isNegativeBtnNeeded,
                                                   final DialogInterfaceCallBack callBack) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.positiveButtonClick(dialog);
            }
        });
        if (isNegativeBtnNeeded) {
            builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callBack.negativeButtonClick(dialog);
                }
            });
        }
        builder.create();
        builder.show();
    }

    public static void showAlertDialogWithCallBack(Context context,
                                                   final String title, String message,
                                                   String positiveBtnText, String negativeBtnText,
                                                   boolean isNegativeBtnNeeded,
                                                   final DialogInterfaceCallBack callBack, boolean isDismissible) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.positiveButtonClick(dialog);
            }
        });
        if (isNegativeBtnNeeded) {
            builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callBack.negativeButtonClick(dialog);
                }
            });
        }
        builder.setCancelable(isDismissible);
        builder.create();
        builder.show();
    }

    public static void showAlertDialogWithCallBackWithPositiveBanNonDismissible(Context context,
                                                                                final String title, String message,
                                                                                String positiveBtnText, String negativeBtnText,
                                                                                boolean isNegativeBtnNeeded,
                                                                                final DialogInterfaceCallBack callBack, boolean isDismissible,
                                                                                final BackButtonDialogListener backButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //this will never be called
                callBack.positiveButtonClick(dialog);
            }
        });
        if (isNegativeBtnNeeded) {
            builder.setNegativeButton(negativeBtnText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    callBack.negativeButtonClick(dialog);
                }
            });
        }
        builder.setCancelable(isDismissible);
        AlertDialog dlg = builder.create();
        dlg.show();
        //This listener will ensure that the dialog is not dismissed on the button click
        dlg.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.positiveButtonClick(dialog);
            }
        });

        dlg.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (event.getAction() == event.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    backButtonListener.onBackButtonClick();
                }
                return true;
            }
        });
    }

    public static void showAppQuitDialog(final Context context) {
        showAlertDialogWithCallBack(context, context.getString(R.string.exit_app),
                context.getString(R.string.exit_app_msg),
                context.getString(R.string.yes),
                context.getString(R.string.no),
                true, new DialogInterfaceCallBack() {
                    @Override
                    public void positiveButtonClick(DialogInterface dialog) {
                        dialog.dismiss();
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }
                    }

                    @Override
                    public void negativeButtonClick(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
    }

    public interface DialogInterfaceCallBack {
        void positiveButtonClick(DialogInterface dialog);

        void negativeButtonClick(DialogInterface dialog);
    }

    public interface SingleButtonDialogListener {
        void onButtonClick();
    }

    public interface BackButtonDialogListener {
        void onBackButtonClick();
    }

    public static void showSingleButtonAlertDialog(Context context, String title, String message,
                                                   String btnText,
                                                   final SingleButtonDialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (null != message) builder.setMessage(message);
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.onButtonClick();
            }
        });
        builder.create();
        builder.show();
    }

    public static void showSingleButtonAlertDialog(Context context, String title, String message,
                                                   String btnText,
                                                   final SingleButtonDialogListener listener,boolean isDismissible) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (null != message) builder.setMessage(message);
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.onButtonClick();
            }
        });
        builder.setCancelable(isDismissible);
        builder.create();
        builder.show();
    }

    public static void showSingleButtonAlertDialogWithPositiveBanNonDismissible(Context context, String title, String message,
                                                                                String btnText,
                                                                                final SingleButtonDialogListener listener, boolean isDismissible,
                                                                                final BackButtonDialogListener backButtonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (null != message) builder.setMessage(message);
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //This will never be called
                listener.onButtonClick();
            }
        });
        builder.setCancelable(isDismissible);
        AlertDialog dlg = builder.create();
        dlg.show();
        //This listener will ensure that the dialog is not dismissed on the button click
        dlg.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonClick();
            }
        });

        dlg.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (event.getAction() == event.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    backButtonListener.onBackButtonClick();
                }
                return true;
            }
        });
    }

    public static void showSingleButtonAlertDialog(Context context, String title, String message,
                                                   String btnText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        if (null != message) builder.setMessage(message);
        builder.setPositiveButton(btnText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    public static void showCustomProgressDialog(Context context, boolean center) {
       /* try {
            if ((mCustomProgressDialog != null && !mCustomProgressDialog.isShowing()) || mCustomProgressDialog == null) {
                if (center) {
                    mCustomProgressDialog = new CustomProgressDialog(context,
                            android.R.style.Theme_Dialog, center);
                } else {
                    mCustomProgressDialog = new CustomProgressDialog(context,
                            android.R.style.Theme_Dialog);
                }

                mCustomProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialog.setContentView(inflater.inflate(
                R.layout.progress_bar_layout, null));
        dialog.show();
    }

    public static void cancelProgressDialog() {
        try {
            if (dialog != null) {
                dialog.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNoNetworkDialog(Context context) {
        showSingleButtonAlertDialog(context, "", context.getString(R.string.no_network), context.getString(R.string.ok));
    }

}
