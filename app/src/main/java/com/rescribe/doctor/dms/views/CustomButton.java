package com.rescribe.doctor.dms.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.rescribe.doctor.R;
import com.rescribe.doctor.dms.singleton.DmsApplication;
import com.rescribe.doctor.dms.util.DmsConstants;

/**
 * Created by Sandeep Bahalkar
 */
public class CustomButton extends AppCompatButton {
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);

//        setBackground(DmsConstants.getRectangleDrawable(DmsConstants.BUTTON_COLOR, "#00000000", 2, 10, 10, 10, 10));
        setTextColor(Color.parseColor(DmsConstants.BUTTON_TEXT_COLOR));
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
