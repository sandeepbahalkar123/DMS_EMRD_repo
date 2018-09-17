package com.scorg.dms.ui.activities.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.ui.activities.SplashScreenActivity;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 9/2/18.
 */

public class SettingsActivity extends AppCompatActivity implements  HelperResponse {
    private static final String TAG = "SettingsActivity";
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.dateTextview)
    CustomTextView dateTextview;
    @BindView(R.id.logout)
    CustomTextView logout;
    @BindView(R.id.dashboardArrowIcon)
    ImageView dashboardArrowIcon;
    @BindView(R.id.selectMenuLayout)
    RelativeLayout selectMenuLayout;

    private Context mContext;
    private LoginHelper loginHelper;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_base_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        mContext = SettingsActivity.this;
        docId = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext);
        loginHelper = new LoginHelper(mContext, this);
        titleTextView.setText(getString(R.string.settings));
        backImageView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @OnClick({R.id.backImageView, R.id.selectMenuLayout, R.id.change_ip_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.selectMenuLayout:
                showLogoutDialog();
                break;

            case R.id.change_ip_address:
                CommonMethods.showDialog("Current IP:-\n"+DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext)+"\n\n", getString(R.string.change_ip), this);
                break;
        }
    }

    private void showLogoutDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        TextView textView = (TextView) dialog.findViewById(R.id.textview_sucess);
        textView.setText(getString(R.string.do_you_logout));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_EXIT, DMSConstants.BLANK, mContext);
                logout();

            }
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();
    }


    private void logout() {
        int version_code;
        boolean isLaterClicked;
        boolean isSkippedClicked;
        version_code = DMSPreferencesManager.getInt(DMSPreferencesManager.DMS_PREFERENCES_KEY.VERSION_CODE_FROM_SERVER, mContext);
        isLaterClicked = DMSPreferencesManager.getBoolean(DMSPreferencesManager.DMS_PREFERENCES_KEY.isLaterClicked, mContext);
        isSkippedClicked = DMSPreferencesManager.getBoolean(DMSPreferencesManager.DMS_PREFERENCES_KEY.isSkippedClicked, mContext);
        DMSPreferencesManager.clearSharedPref(mContext);

        DMSPreferencesManager.putInt(DMSPreferencesManager.DMS_PREFERENCES_KEY.VERSION_CODE_FROM_SERVER, version_code, mContext);
        if (isLaterClicked) {
            DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SHOW_UPDATE_DIALOG, DMSConstants.YES, mContext);
        }
        if (isSkippedClicked) {
            DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SHOW_UPDATE_DIALOG, DMSConstants.NO, mContext);
        }
        //-------------
        String mServerPath = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
        String isValidConfig = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext);
        DMSPreferencesManager.clearSharedPref(mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mServerPath, mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, isValidConfig, mContext);
        Intent intent = new Intent(mContext, SplashScreenActivity.class);
        startActivity(intent);
        ((AppCompatActivity) mContext).finishAffinity();

        //-------------
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
//        if (mOldDataTag.equals(DMSConstants.LOGOUT))
//            if (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_EXIT, mContext).equalsIgnoreCase(DMSConstants.BLANK)) {
//                logout();
//            }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }
}
