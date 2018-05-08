package com.rescribe.doctor.ui.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.login.LoginHelper;
import com.rescribe.doctor.interfaces.CheckIpConnection;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.dms_models.responsemodel.iptestresponsemodel.IpTestResponseModel;
import com.rescribe.doctor.notification.MQTTServiceAlarmTask;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;


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

        MQTTServiceAlarmTask.cancelAlarm(mContext);
        new MQTTServiceAlarmTask(mContext).run();

         doAppCheckLogin();
    }

    private void doAppCheckLogin() {
        //handler to close the splash activity after the set time
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {

                String userName = RescribePreferencesManager.getString(RescribeConstants.USERNAME, mContext);
                String password = RescribePreferencesManager.getString(RescribeConstants.PASSWORD, mContext);

                Intent intentObj = null;

                if (RescribeConstants.BLANK.equalsIgnoreCase(userName) || RescribeConstants.BLANK.equalsIgnoreCase(password)) {
                    if (!RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext).equals(RescribeConstants.TRUE)) {
                        //alert dialog for serverpath
                        CommonMethods.showIPAlertDialog(SplashScreenActivity.this, getString(R.string.server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
                            @Override
                            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {
                                mDialog = dialog;
                                mContext = context;
                                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
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
        }, RescribeConstants.TIME_STAMPS.THREE_SECONDS);

    }

    @Override

    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        mDialog.dismiss();
        //TODO : IP CHECK API IN NOT IMPLEMENTED YET, HENCE COMMENTED BELOW CODE

        IpTestResponseModel ipTestResponseModel = (IpTestResponseModel) customResponse;
        if (ipTestResponseModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS)) {
            RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, RescribeConstants.TRUE, mContext);
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
        RescribePreferencesManager.putString(RescribeConstants.LOGIN_SUCCESS, RescribeConstants.FALSE, mContext);
        CommonMethods.showIPAlertDialog(SplashScreenActivity.this, getString(R.string.wrong_server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
            @Override
            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
                mLoginHelper.checkConnectionToServer(serverPath);
            }
        });

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        RescribePreferencesManager.putString(RescribeConstants.LOGIN_SUCCESS, RescribeConstants.FALSE, mContext);
        CommonMethods.showIPAlertDialog(SplashScreenActivity.this, getString(R.string.wrong_server_path) + "\n" + getString(R.string.for_example_server_path), new CheckIpConnection() {
            @Override
            public void onOkButtonClickListner(String serverPath, Context context, Dialog dialog) {

                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, serverPath, context);
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
