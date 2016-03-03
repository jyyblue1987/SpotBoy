/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.ninexe.ui.NixonApplication;
import com.ninexe.ui.R;
import com.ninexe.ui.activities.NotificationDialogActivity;
import com.ninexe.ui.common.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by nagesh on 8/11/15.
 */
public class NotificationUtils {
    private String TAG = NotificationUtils.class.getSimpleName();
    private final static int FOREGROUND = 2;

    private Context mContext;
    public long NOTIFICATION_ID;

    public NotificationUtils() {
    }

    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void showDefaultNotificationMessage(String title, String message, Intent intent, Uri sound, Bitmap notificationLargeImage) {

        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;
        NOTIFICATION_ID = System.currentTimeMillis();
        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, (int) NOTIFICATION_ID, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        // NotificationCompat.BigPictureStyle notificationStyle = new NotificationCompat.BigPictureStyle().bigPicture(notificationLargeImage);

        Notification notification = mBuilder.setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setLargeIcon(notificationLargeImage)
                .setContentIntent(resultPendingIntent)
                        // .setStyle(notificationStyle)
                .setSound(sound)
                .build();
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) NOTIFICATION_ID, notification);
    }


    public void showCustomNotificationMessage(String title, String message, Intent intent, Uri sound, Bitmap notificationLargeImage) {
        // Check for empty push message
        if (TextUtils.isEmpty(message))
            return;
        NOTIFICATION_ID = System.currentTimeMillis();
        RemoteViews contentView = new RemoteViews(mContext.getPackageName(), R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.mipmap.app_icon);
        contentView.setTextViewText(R.id.title, title);
        contentView.setTextViewText(R.id.text, message);
        //  NotificationCompat.BigPictureStyle notificationStyle = new NotificationCompat.BigPictureStyle().bigPicture(notificationLargeImage);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext, (int) NOTIFICATION_ID, intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext);
        Notification notification = mBuilder.setSmallIcon(getNotificationIcon())
                .setContent(contentView)
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setLargeIcon(notificationLargeImage)
                        //  .setStyle(notificationStyle)
                .setSound(sound)
                .build();


        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) NOTIFICATION_ID, notification);
    }

    public void showNotification(Context context, String title, String message, Intent intent, String sound, String imageUrl) {
        if (FOREGROUND != NixonApplication.isApplicationInBackground()) {
            new SendNotification(context, title, message, intent, sound, imageUrl).execute();
        } else {
            if (null != getNotificationSoundURI(context, sound))
                MediaPlayer.create(context, getNotificationSoundURI(context, sound)).start();
            Intent dialogIntent = new Intent(context, NotificationDialogActivity.class);
            dialogIntent.putExtra(Constants.EXTRA_ARTICLE_ID, intent.getStringExtra(Constants.EXTRA_ARTICLE_ID));
            dialogIntent.putExtra(Constants.EXTRA_ARTICLE_TYPE, intent.getStringExtra(Constants.EXTRA_ARTICLE_TYPE));
            dialogIntent.putExtra(Constants.EXTRA_ARTICLE_TITLE, intent.getStringExtra(Constants.EXTRA_ARTICLE_TITLE));
            dialogIntent.putExtra(Constants.EXTRA_ARTICLE_MESSAGE, intent.getStringExtra(Constants.EXTRA_ARTICLE_MESSAGE));
            dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(dialogIntent);
        }


    }

    private Uri getNotificationSoundURI(Context context, String sound) {
        if (sound == null) return null;
        if (sound.contains("."))
            sound = sound.substring(0, sound.indexOf("."));
        if (PreferenceManager.isNotificationSoundEnabled(context)) {
            Resources res = context.getResources();
            int soundId = res.getIdentifier(sound, "raw", context.getPackageName());
            if (sound.equalsIgnoreCase(Constants.DEFAULT_SOUND))
                return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            else {
                Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + soundId);
                if (0 != context.getResources().getIdentifier("raw/" + sound,
                        "raw", context.getPackageName()))
                    return soundUri;
                else
                    return null;
            }
        } else {
            return null;
        }
    }

    private int getNotificationIcon() {
        boolean whiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return whiteIcon ? R.drawable.statusbar_notification_icon : R.mipmap.app_icon;
    }

    public class SendNotification extends AsyncTask<Void, Void, Bitmap> {

        private Context context;
        private String title;
        private String message;
        private Intent intent;
        private String sound;
        private String imageUrl;

        public SendNotification(Context context, String title, String message, Intent intent, String sound, String imageUrl) {
            this.context = context;
            this.title = title;
            this.message = message;
            this.intent = intent;
            this.sound = sound;
            this.imageUrl = imageUrl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
          /*  URL url = null;
            try {
                url = new URL(this.imageUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
            HttpURLConnection connection = null;
            try {
                InputStream in;
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                showCustomNotificationMessage(title, message, intent, getNotificationSoundURI(this.context, sound), bitmap);
            } else {
                showDefaultNotificationMessage(title, message, intent, getNotificationSoundURI(this.context, sound), bitmap);
            }

        }
    }
}