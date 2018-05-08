package com.scorg.dms.ui.customesViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.scorg.dms.R;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.util.DMSConstants;

/**
 * Created by Sandeep Bahalkar
 */
public class CustomEditText extends AppCompatEditText {
    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            return;
        }

        setTextColor(Color.parseColor(DMSConstants.TEXT_COLOR));
        setHintTextColor(context.getResources().getColor(R.color.placeHolder));
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont = a.getString(R.styleable.CustomTextView_customFont);

        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public void setCustomFont(Context ctx, String asset) {
        Typeface typeface = DMSApplication.get(ctx, "fonts/" + asset);
        setTypeface(typeface);
    }
}