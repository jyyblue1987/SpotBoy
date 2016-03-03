/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ninexe.ui.R;
import com.ninexe.ui.common.Constants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DeepLinkingListenerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking_listener);

        String type = null, articleId, title = null;

        if (getIntent() == null || getIntent().getData() == null) return;

        String[] segments = getIntent().getData().getPath().split("/");

        String data = getIntent().getData().toString();
        articleId = getIntent().getData().getAuthority();
        data = data.toLowerCase();
        try {
            title = URLDecoder.decode(segments[2], "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        if (data.contains(Constants.QUIZ.toLowerCase())) {
            type = Constants.QUIZ;
        } else if (data.contains(Constants.PHOTO.toLowerCase())) {
            type = Constants.PHOTO;
        } else if (data.contains(Constants.CONTEST.toLowerCase())) {
            type = Constants.CONTEST;
        } else if (data.contains(Constants.PERSONALITY_TEST.toLowerCase())) {
            type = Constants.PERSONALITY_TEST;
        } else if (data.contains(Constants.POLL.toLowerCase())) {
            type = Constants.POLL;
        } else {
            type = Constants.ARTICLE;
        }


        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.EXTRA_DEEPLINK, true);
        intent.putExtra(Constants.EXTRA_ARTICLE_ID, articleId);
        intent.putExtra(Constants.EXTRA_ARTICLE_TYPE, type);
        intent.putExtra(Constants.EXTRA_ARTICLE_TITLE, title);
        finish();
        startActivity(intent);


    }


}
