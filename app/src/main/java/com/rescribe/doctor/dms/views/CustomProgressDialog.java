package com.rescribe.doctor.dms.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.rescribe.doctor.R;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);

        setContentView(R.layout.mydialog);
        ProgressBar mProgressBar = (ProgressBar) this.findViewById(R.id.customProgressBar);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
    }

}
