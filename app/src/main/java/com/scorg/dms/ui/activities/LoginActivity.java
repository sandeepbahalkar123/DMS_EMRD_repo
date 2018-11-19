package com.scorg.dms.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.scorg.dms.R;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.responsemodel.loginresponsemodel.LoginResponseModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements HelperResponse {

    String TAG = this.getClass().getSimpleName();
    Context mContext;
    String mServerPath;
    @BindView(R.id.userName)
    EditText mUserName;
    @BindView(R.id.password)
    EditText mPassword;

    @BindView(R.id.loginBackground)
    ImageView loginBackground;
    @BindView(R.id.loginLogo)
    ImageView loginLogo;

    @BindView(R.id.loginButton)
    Button loginButton;

    private LoginHelper mLoginHelper;
    String userName = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        ButterKnife.bind(this);

        CommonMethods.setImageUrl(this, DMSConstants.Images.IC_LOGIN_BACKGROUD, loginBackground, R.drawable.login_background);
        CommonMethods.setImageUrl(this, DMSConstants.Images.IC_LOGIN_LOGO, loginLogo, R.drawable.login_logo);

        mLoginHelper = new LoginHelper(this, this);
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setShape(GradientDrawable.RECTANGLE);
        buttonBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackground.setCornerRadius(getResources().getDimension(R.dimen.dp5));
        loginButton.setBackground(buttonBackground);
    }


    @OnClick(R.id.loginButton)
    public void doLogin() {
        if (!validate()) {
            mLoginHelper.doAppLogin(mUserName.getText().toString().trim(), mPassword.getText().toString().trim());
            // onSuccess(null, null);
        }
    }



    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate() {
        userName = mUserName.getText().toString().trim();
        password = mPassword.getText().toString();
        String message = null;
        if (userName.isEmpty() || password.isEmpty()) {
            message = getString(R.string.error_empty_fields);
        }

        if (message != null) {
            CommonMethods.showSnack(mUserName, message);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

        LoginResponseModel model = (LoginResponseModel) customResponse;
        DMSPreferencesManager.putString(DMSConstants.LOGIN_SUCCESS, DMSConstants.TRUE, mContext);
        DMSPreferencesManager.putString(DMSConstants.ACCESS_TOKEN, model.getAccessToken(), mContext);
        DMSPreferencesManager.putString(DMSConstants.TOKEN_TYPE, model.getTokenType(), mContext);
        DMSPreferencesManager.putString(DMSConstants.REFRESH_TOKEN, model.getRefreshToken(), mContext);
        DMSPreferencesManager.putString(DMSConstants.USERNAME, userName, mContext);
        DMSPreferencesManager.putString(DMSConstants.PASSWORD, password, mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, String.valueOf(model.getDoctorId()), mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_GENDER, model.getUserGender(), mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_NAME, model.getDoctorName(), mContext);
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.HOSPITAL_NAME, model.getHospitalName(), mContext);

        Intent intent = new Intent(this, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showErrorDialog(errorMessage,this,false, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
            }
        });
        Log.e("loginResponce","onParseError");
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showErrorDialog(serverErrorMessage,this,false, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
            }
        });
        Log.e("loginResponce","onServerError");
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showErrorDialog(serverErrorMessage,this, false,new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
            }
        });
        Log.e("loginResponce","onNoConnectionError");

    }

    @Override
    public void onTimeOutError(String mOldDataTag, String timeOutErrorMessage) {
        CommonMethods.showErrorDialog(timeOutErrorMessage,this,true, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
                if (!validate()) {
                    mLoginHelper.doAppLogin(mUserName.getText().toString().trim(), mPassword.getText().toString());
                    // onSuccess(null, null);
                }
            }
        });
        Log.e("loginResponce","onTimeOutError");
    }
}

