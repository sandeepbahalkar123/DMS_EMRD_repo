package com.scorg.dms.ui.customesViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.scorg.dms.R;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.util.DMSConstants;

/**
 * Created by Sandeep Bahalkar
 */
public class CustomButton extends AppCompatButton {
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

//        setBackground(DMSConstants.getRectangleDrawable(DMSConstants.BUTTON_COLOR, "#00000000", 2, 10, 10, 10, 10));
        setTextColor(Color.parseColor(DMSConstants.BUTTON_TEXT_COLOR));
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
