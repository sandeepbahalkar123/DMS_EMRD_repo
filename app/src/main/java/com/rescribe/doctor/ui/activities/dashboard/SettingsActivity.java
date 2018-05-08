package com.rescribe.doctor.ui.activities.dashboard;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rescribe.doctor.R;
import com.rescribe.doctor.bottom_menus.BottomMenu;
import com.rescribe.doctor.bottom_menus.BottomMenuActivity;
import com.rescribe.doctor.bottom_menus.BottomMenuAdapter;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.helpers.login.LoginHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.login.ActiveRequest;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.ui.activities.LoginActivity;
import com.rescribe.doctor.ui.activities.ProfileActivity;
import com.rescribe.doctor.ui.activities.SplashScreenActivity;
import com.rescribe.doctor.ui.activities.dms_patient_list.PatientList;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.ui.customesViews.SwitchButton;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import net.gotev.uploadservice.UploadService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 9/2/18.
 */

public class SettingsActivity extends BottomMenuActivity implements BottomMenuAdapter.OnBottomMenuClickListener, HelperResponse {
    private static final String TAG = "SettingsActivity";
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.dateTextview)
    CustomTextView dateTextview;
    @BindView(R.id.menuIcon)
    ImageView menuIcon;
    @BindView(R.id.logout)
    CustomTextView logout;
    @BindView(R.id.dashboardArrowIcon)
    ImageView dashboardArrowIcon;
    @BindView(R.id.selectMenuLayout)
    RelativeLayout selectMenuLayout;
    @BindView(R.id.addPatientRadioSwitch)
    SwitchButton mAddPatientRadioSwitch;
    private AppDBHelper appDBHelper;
    private Context mContext;
    private LoginHelper loginHelper;
    private String docId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_base_layout);
        ButterKnife.bind(this);
        initialize();
        setCurrentActivityTab(getString(R.string.settings));
    }

    private void initialize() {
        mContext = SettingsActivity.this;
        docId = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext);
        appDBHelper = new AppDBHelper(mContext);
        loginHelper = new LoginHelper(mContext, this);
        titleTextView.setText(getString(R.string.settings));
        backImageView.setVisibility(View.GONE);

        mAddPatientRadioSwitch.setCheckedNoEvent(RescribePreferencesManager.getBoolean(RescribePreferencesManager.DMS_PREFERENCES_KEY.ADD_PATIENT_OFFLINE_SETTINGS, mContext));

        mAddPatientRadioSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RescribePreferencesManager.putBoolean(RescribePreferencesManager.DMS_PREFERENCES_KEY.ADD_PATIENT_OFFLINE_SETTINGS, isChecked, mContext);
                mAddPatientRadioSwitch.setChecked(isChecked);
            }
        });
    }


    @Override
    public void onBottomMenuClick(BottomMenu bottomMenu) {

        if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.support))) {
            Intent intent = new Intent(this, SupportActivity.class);
            startActivity(intent);
            finish();
        } else if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.home))) {
            finish();
        } else if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.profile))) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
        super.onBottomMenuClick(bottomMenu);
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
                CommonMethods.showDialog(RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext), getString(R.string.change_ip), this);
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
                ActiveRequest activeRequest = new ActiveRequest();
                activeRequest.setId(Integer.parseInt(docId));
                loginHelper.doLogout(activeRequest);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.IS_EXIT, RescribeConstants.BLANK, mContext);


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

        // cancel all uploadings.
        UploadService.stopAllUploads();

        String mobileNoGmail = "";
        String passwordGmail = "";
        String mobileNoFacebook = "";
        String passwordFacebook = "";
        String gmailLogin = "";
        String facebookLogin = "";
        int version_code;
        boolean isLaterClicked;
        boolean isSkippedClicked;

        //Logout functionality
        if (RescribePreferencesManager.getString(RescribeConstants.GMAIL_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_gmail))) {
            gmailLogin = RescribePreferencesManager.getString(RescribeConstants.GMAIL_LOGIN, mContext);
            mobileNoGmail = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.MOBILE_NUMBER_GMAIL, mContext);
            passwordGmail = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.PASSWORD_GMAIL, mContext);
        }

        if (RescribePreferencesManager.getString(RescribeConstants.FACEBOOK_LOGIN, mContext).equalsIgnoreCase(getString(R.string.login_with_facebook))) {
            facebookLogin = RescribePreferencesManager.getString(RescribeConstants.FACEBOOK_LOGIN, mContext);
            mobileNoFacebook = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK, mContext);
            passwordFacebook = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.PASSWORD_FACEBOOK, mContext);
        }

        version_code = RescribePreferencesManager.getInt(RescribePreferencesManager.DMS_PREFERENCES_KEY.VERSION_CODE_FROM_SERVER, mContext);
        isLaterClicked = RescribePreferencesManager.getBoolean(RescribePreferencesManager.DMS_PREFERENCES_KEY.isLaterClicked, mContext);
        isSkippedClicked = RescribePreferencesManager.getBoolean(RescribePreferencesManager.DMS_PREFERENCES_KEY.isSkippedClicked, mContext);
        RescribePreferencesManager.clearSharedPref(mContext);

        RescribePreferencesManager.putInt(RescribePreferencesManager.DMS_PREFERENCES_KEY.VERSION_CODE_FROM_SERVER, version_code, mContext);
        RescribePreferencesManager.putString(RescribeConstants.GMAIL_LOGIN, gmailLogin, mContext);
        if (isLaterClicked) {
            RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SHOW_UPDATE_DIALOG, RescribeConstants.YES, mContext);
        }
        if (isSkippedClicked) {
            RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SHOW_UPDATE_DIALOG, RescribeConstants.NO, mContext);
        }
        RescribePreferencesManager.putBoolean(RescribePreferencesManager.DMS_PREFERENCES_KEY.isSkippedClicked, isSkippedClicked, mContext);
        RescribePreferencesManager.putBoolean(RescribePreferencesManager.DMS_PREFERENCES_KEY.isLaterClicked, isLaterClicked, mContext);
        RescribePreferencesManager.putString(RescribeConstants.FACEBOOK_LOGIN, facebookLogin, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.MOBILE_NUMBER_GMAIL, mobileNoGmail, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.PASSWORD_GMAIL, passwordGmail, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.MOBILE_NUMBER_FACEBOOK, mobileNoFacebook, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.PASSWORD_FACEBOOK, passwordFacebook, mContext);
        RescribePreferencesManager.putString(getString(R.string.logout), "" + 1, mContext);

        appDBHelper.deleteDatabase();

//        Intent intent = new Intent(mContext, LoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//        finish();

        //-------------
        String mServerPath = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
        String isValidConfig = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext);
        RescribePreferencesManager.clearSharedPref(mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mServerPath, mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, isValidConfig, mContext);
        Intent intent = new Intent(mContext, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //-------------
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(RescribeConstants.LOGOUT))
            if (RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.IS_EXIT, mContext).equalsIgnoreCase(RescribeConstants.BLANK)) {
                logout();
            }
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
