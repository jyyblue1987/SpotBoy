/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.widgets;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.ninexe.ui.R;

/**
 * Created by nagesh on 16/10/15.
 */
public class TintedProgressBar extends ProgressBar {
    public TintedProgressBar(Context context) {
        super(context);
        init();
    }

    public TintedProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TintedProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryBlue), PorterDuff.Mode.SRC_IN);
        getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimaryBlue), PorterDuff.Mode.SRC_IN);
    }
}
