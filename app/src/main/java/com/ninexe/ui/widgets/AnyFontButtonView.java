/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.Button;

import com.ninexe.ui.R;
import com.ninexe.ui.managers.FontManager;

public class AnyFontButtonView extends Button {

    public AnyFontButtonView(Context context) {
        this(context, null);
    }

    public AnyFontButtonView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnyFontButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode()) return;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AnyFontTextView);

        if (ta != null) {
            String fontAsset = ta.getString(R.styleable.AnyFontTextView_typefaceAsset);

            if (!TextUtils.isEmpty(fontAsset)) {
                Typeface tf = FontManager.getInstance(context).getFont(fontAsset);
                int style = Typeface.NORMAL;

                if (getTypeface() != null) {
                    style = getTypeface().getStyle();
                }
                if (tf != null) {
                    setTypeface(tf, style);
                } else {
                    //Log.d("FontText", String.format("Could not create a font from asset: %s", fontAsset));
                }

            }
        }
    }

}
