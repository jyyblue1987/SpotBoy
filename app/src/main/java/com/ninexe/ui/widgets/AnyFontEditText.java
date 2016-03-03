/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import com.ninexe.ui.R;
import com.ninexe.ui.managers.FontManager;
import com.ninexe.ui.utils.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AnyFontEditText extends EditText {

    private static final String TAG = AnyFontEditText.class.getSimpleName();

    public AnyFontEditText(Context context) {
        super(context);
    }

    public AnyFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnyFontEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

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
                    LogUtils.LOGD(TAG, String.format("Could not create a font from asset: %s", fontAsset));
                }

            }
        }
    }
    /**
     * Keep track of which icon we used last
     */
    private Drawable lastErrorIcon = null;

    @Override
    public void setError(CharSequence error, Drawable icon) {
        super.setError(error, icon);
        lastErrorIcon = icon;

        // if the error is not null, and we are in JB, force
        // the error to show
        if (error != null /* !isFocused() && */) {
            showErrorIconHax(icon);
        }
    }

    private void showErrorIconHax(Drawable icon) {
        if (icon == null)
            return;

        // only for JB 4.2 and 4.2.1
        if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN &&
                android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.JELLY_BEAN_MR1)
            return;

        try {
            Class<?> textview = Class.forName("android.widget.TextView");
            Field tEditor = textview.getDeclaredField("mEditor");
            tEditor.setAccessible(true);
            Class<?> editor = Class.forName("android.widget.Editor");
            Method privateShowError = editor.getDeclaredMethod("setErrorIcon", Drawable.class);
            privateShowError.setAccessible(true);
            privateShowError.invoke(tEditor.get(this), icon);
        } catch (Exception e) {
            // e.printStackTrace(); // oh well, we tried
        }
    }
}
