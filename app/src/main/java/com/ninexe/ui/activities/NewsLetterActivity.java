/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.activities;

import android.os.Bundle;

import com.ninexe.ui.R;
import com.ninexe.ui.fragments.BaseFragment;
import com.ninexe.ui.fragments.NewsLetterFragment;

/**
 * Created by nagesh on 20/10/15.
 */
public class NewsLetterActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_letter);
        loadNewsLetterFragment();
    }

    private void loadNewsLetterFragment() {
        NewsLetterFragment newsLetterFragment = NewsLetterFragment.newInstance();
        loadFragment(R.id.fragment_container, newsLetterFragment, NewsLetterFragment.NEWS_LETTER_FRAGMENT, 0, 0,
                BaseFragment.FragmentTransactionType.REPLACE);
    }

}
