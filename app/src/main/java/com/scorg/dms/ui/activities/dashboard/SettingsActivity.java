package com.scorg.dms.ui.activities.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.scorg.dms.ui.activities.LoginActivity;
import com.scorg.dms.ui.activities.SplashScreenActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.widget.TextView;

import java.io.File;

/**
 * Created by jeetal on 9/2/18.
 */

public class SettingsActivity extends BaseActivity implements  HelperResponse {
    private static final String TAG = "SettingsActivity";
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    TextView titleTextView;

    @BindView(R.id.change_ip_address)
    TextView change_ip_address;

    @BindView(R.id.clearImageCache)
    TextView clearImageCache;

    @BindView(R.id.clearFileCache)
    TextView clearFileCache;

    @BindView(R.id.dateTextview)
    TextView dateTextview;
    @BindView(R.id.logout)
    TextView logout;
    @BindView(R.id.dashboardArrowIcon)
    ImageView dashboardArrowIcon;
    @BindView(R.id.selectMenuLayout)
    RelativeLayout selectMenuLayout;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_base_layout);
        ButterKnife.bind(this);
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        initialize();

    }

    private void initialize() {
        logout.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        change_ip_address.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        clearImageCache.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        clearFileCache.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        dashboardArrowIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mContext = SettingsActivity.this;
        LoginHelper loginHelper = new LoginHelper(mContext, this);
        titleTextView.setText(getString(R.string.settings));
        backImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @OnClick({R.id.clearImageCache,R.id.clearFileCache, R.id.backImageView, R.id.selectMenuLayout, R.id.change_ip_address })
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
                CommonMethods.showToast(this,"Images Refreshed.");
                break;

            case R.id.clearFileCache:
                //final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFViewCache/");
                boolean isClear=deleteCache(this);

                Log.e("isClear--","--"+isClear);
                CommonMethods.showToast(this,"File Cache Cleared.");
                break;
        }
    }
    public static boolean deleteCache(Context context) {
        try {
            File dir =  new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFViewCache/");
           return deleteDir(dir);
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }

    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
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
        ImageView dialogIcon = dialog.findViewById(R.id.dialogIcon);
        dialogIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

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

        String mServerPath = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
        String isValidConfig = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext);

        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mServerPath, mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, isValidConfig, mContext);
        DMSPreferencesManager.putString(DMSConstants.USERNAME, DMSConstants.BLANK, mContext);
        DMSPreferencesManager.putString(DMSConstants.PASSWORD, DMSConstants.BLANK, mContext);
        DMSPreferencesManager.putString(DMSConstants.LOGIN_SUCCESS, DMSConstants.FALSE, mContext);

        Intent intent = new Intent(mContext, LoginActivity.class);
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

    @Override
    public void onTimeOutError(String mOldDataTag, String timeOutErrorMessage) {

    }
}
