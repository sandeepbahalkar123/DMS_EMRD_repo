package com.scorg.dms.ui.customesViews;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.scorg.dms.R;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER_HORIZONTAL;
        getWindow().setAttributes(wlmp);
        setContentView(R.layout.mydialog);
        ImageView imageView = findViewById(R.id.progressIcon);
        CommonMethods.setImageUrl(context, DMSConstants.Images.IC_ACTIONBAR_LOGO, imageView, R.drawable.ic_launcher);

        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
    }

}
