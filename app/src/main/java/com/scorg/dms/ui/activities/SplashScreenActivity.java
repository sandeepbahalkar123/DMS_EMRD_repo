package com.scorg.dms.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CheckIpConnection;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.responsemodel.iptestresponsemodel.IpTestResponseModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;


public class SplashScreenActivity extends AppCompatActivity implements HelperResponse {

    private Context mContext;
    private LoginHelper mLoginHelper;
    private Dialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        mContext = SplashScreenActivity.this;
        mLoginHelper = new LoginHelper(this, this);
        ImageView image = findViewById(R.id.image);
        if (DMSPreferencesManager.getString(DMSPreferencesManager.CACHE_TIME, mContext).isEmpty())
            DMSPreferencesManager.putString(DMSPreferencesManager.CACHE_TIME, CommonMethods.getCurrentTimeStamp("ddMMyyyyhhmmss"), mContext);

        CommonMethods.setImageUrl(this, DMSConstants.Images.SPLASHSCREEN, image, R.drawable.splashscreen);

        doAppCheckLogin();
    }

    private void doAppCheckLogin() {
        //handler to close the splash activity after the set time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String userName = DMSPreferencesManager.getString(DMSConstants.USERNAME, mContext);
                String password = DMSPreferencesManager.getString(DMSConstants.PASSWORD, mContext);

                Intent intentObj = null;

                if (DMSConstants.BLANK.equalsIgnoreCase(userName) || DMSConstants.BLANK.equalsIgnoreCase(password)) {
                    if (!DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext).equals(DMSConstants.TRUE)) {
                        //alert dialog for serverpath
                        CommonMethods.showIPAlertDialog(SplashScreenActivity.this, getString(R.string.server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
                            @Override
                            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {
                                mDialog = dialog;
                                mContext = context;
                                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
                                mLoginHelper.checkConnectionToServer(serverPath);


                            }
                        });
                    } else {
                        intentObj = new Intent(mContext, LoginActivity.class);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intentObj);

                        finish();
                        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                    }
                } else {
                    //------Check Remember ME first , then only move on next screen.
                    intentObj = new Intent(mContext, HomePageActivity.class);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intentObj);

                    finish();
                    overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            }
        }, DMSConstants.TIME_STAMPS.THREE_SECONDS);

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        mDialog.dismiss();
        //TODO : IP CHECK API IN NOT IMPLEMENTED YET, HENCE COMMENTED BELOW CODE

        IpTestResponseModel ipTestResponseModel = (IpTestResponseModel) customResponse;
        if (ipTestResponseModel.getCommon().getStatusCode().equals(DMSConstants.SUCCESS)) {
            DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, DMSConstants.TRUE, mContext);
            Intent intentObj = new Intent(mContext, LoginActivity.class);
            intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intentObj.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intentObj.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intentObj);
            finish();

        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        DMSPreferencesManager.putString(DMSConstants.LOGIN_SUCCESS, DMSConstants.FALSE, mContext);
        CommonMethods.showIPAlertDialog(SplashScreenActivity.this, getString(R.string.wrong_server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
            @Override
            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
                mLoginHelper.checkConnectionToServer(serverPath);
            }
        });

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        DMSPreferencesManager.putString(DMSConstants.LOGIN_SUCCESS, DMSConstants.FALSE, mContext);
        CommonMethods.showIPAlertDialog(SplashScreenActivity.this, getString(R.string.wrong_server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
            @Override
            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {

                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
                mLoginHelper.checkConnectionToServer(serverPath);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
