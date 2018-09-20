package com.scorg.dms.ui.activities.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.BaseActivity;
import com.scorg.dms.ui.activities.SplashScreenActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.widget.TextView;

/**
 * Created by jeetal on 9/2/18.
 */

public class SettingsActivity extends BaseActivity implements  HelperResponse {
    private static final String TAG = "SettingsActivity";
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.userInfoTextView)
    TextView userInfoTextView;


    @BindView(R.id.change_ip_address)
    TextView change_ip_address;

    @BindView(R.id.clearImageCache)
    TextView clearImageCache;

    @BindView(R.id.dateTextview)
    TextView dateTextview;
    @BindView(R.id.logout)
    TextView logout;
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
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        initialize();

    }

    private void initialize() {
        logout.setTextColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        change_ip_address.setTextColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        clearImageCache.setTextColor(Color.parseColor(DMSApplication.COLOR_ACCENT));

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

    @OnClick({R.id.clearImageCache, R.id.backImageView, R.id.selectMenuLayout, R.id.change_ip_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.selectMenuLayout:
                showLogoutDialog();
                break;

            case R.id.change_ip_address:
                CommonMethods.showDialog("Current IP address:\n"+DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext)+"\n\n", getString(R.string.change_ip), this);
                break;

            case R.id.clearImageCache:
                DMSPreferencesManager.putString(DMSPreferencesManager.CACHE_TIME, CommonMethods.getCurrentDate("ddMMyyyyhhmmss"), mContext);
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

        float[] bottomLeftRadius = {0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), 0, 0};
        float[] bottomRightRadius = {0, 0, 0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8)};

        GradientDrawable buttonLeftBackground = new GradientDrawable();
        buttonLeftBackground.setShape(GradientDrawable.RECTANGLE);
        buttonLeftBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonLeftBackground.setCornerRadii(bottomLeftRadius);

        GradientDrawable buttonRightBackground = new GradientDrawable();
        buttonRightBackground.setShape(GradientDrawable.RECTANGLE);
        buttonRightBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonRightBackground.setCornerRadii(bottomRightRadius);

        Button buttonRight = dialog.findViewById(R.id.button_cancel);
        Button buttonLeft = dialog.findViewById(R.id.button_ok);

        buttonLeft.setBackground(buttonLeftBackground);
        buttonRight.setBackground(buttonRightBackground);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_EXIT, DMSConstants.BLANK, mContext);
                logout();
            }
        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void logout() {
        DMSPreferencesManager.clearSharedPref(mContext);
        String mServerPath = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
        String isValidConfig = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext);
        DMSPreferencesManager.clearSharedPref(mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mServerPath, mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, isValidConfig, mContext);
        Intent intent = new Intent(mContext, SplashScreenActivity.class);
        startActivity(intent);
        ((AppCompatActivity) mContext).finishAffinity();
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
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
