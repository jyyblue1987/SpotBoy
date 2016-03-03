/*
 * Copyright © 2015, 9x Media Pvt. Ltd.
 * Written under contract by Robosoft Technologies Pvt. Ltd.
 */

package com.ninexe.ui.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * Created by nagesh on 9/11/15.
 */
public class ProgramaticallyCheckabeToggle extends ToggleButton
        implements ProgramaticallyCheckable {


    private CompoundButton.OnCheckedChangeListener mListener = null;

    public ProgramaticallyCheckabeToggle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ProgramaticallyCheckabeToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgramaticallyCheckabeToggle(Context context) {
        super(context);
    }

    @Override
    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        if (this.mListener == null) {
            this.mListener = listener;
        }
        super.setOnCheckedChangeListener(listener);
    }

    @Override
    public void setCheckedProgrammatically(boolean checked) {
        super.setOnCheckedChangeListener(null);
        super.setChecked(checked);
        super.setOnCheckedChangeListener(mListener);
    }
}
