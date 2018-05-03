package com.rescribe.doctor.dms.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.rescribe.doctor.R;
import com.rescribe.doctor.dms.singleton.DmsApplication;
import com.rescribe.doctor.dms.util.DmsConstants;

/**
 * Created by Sandeep Bahalkar
 */

public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        setTextColor(Color.parseColor(DmsConstants.TEXT_COLOR));
        setLinkTextColor(Color.parseColor(DmsConstants.HEADER_COLOR));
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextView);
        String customFont = a.getString(R.styleable.TextView_fontName);

        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String asset) {
        Typeface typeface = DmsApplication.get(ctx, asset);
        setTypeface(typeface);
    }
}
