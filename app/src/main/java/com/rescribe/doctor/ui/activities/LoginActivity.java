package com.rescribe.doctor.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.google.gson.Gson;
import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.login.LoginHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.login.DocDetail;
import com.rescribe.doctor.model.login.LoginModel;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements HelperResponse {

    String TAG = this.getClass().getSimpleName();
    Context mContext;
    String mServerPath;
    @BindView(R.id.userName)
    EditText mUserName;

    @BindView(R.id.password)
    EditText mPassword;

    private LoginHelper mLoginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = getApplicationContext();
        // Log.d("Server PAth",mServerPath);

        ButterKnife.bind(this);
        mLoginHelper = new LoginHelper(this, this);

    }


    @OnClick(R.id.loginButton)
    public void doLogin() {
        if (!validate()) {
            mLoginHelper.doAppLogin(mUserName.getText().toString(), mPassword.getText().toString());
            // onSuccess(null, null);
        }
    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */
    private boolean validate() {
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
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
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mUserName.getText().toString(), mContext);
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.PASSWORD, mPassword.getText().toString(), mContext);

        //-- TODO, THIS IS DONE TO LOGIN TO DOC_APP, ONCE API RECEIVED, REMOVE THIS.
        try {
            InputStream is = mContext.getAssets().open("login.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            //After login user navigated to HomepageActivity
            LoginModel receivedModel = new Gson().fromJson(json, LoginModel.class);
            if (receivedModel.getCommon().isSuccess()) {

                DocDetail docDetail = receivedModel.getDoctorLoginData().getDocDetail();
                String authToken = receivedModel.getDoctorLoginData().getAuthToken();

                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, authToken, this);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, String.valueOf(docDetail.getDocId()), this);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, docDetail.getDocName(), this);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.PROFILE_PHOTO, docDetail.getDocImgUrl(), this);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.EMAIL, docDetail.getDocEmail(), this);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SPECIALITY, docDetail.getDocSpaciality(), this);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.ADDRESS, docDetail.getDocAddress(), this);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, this);
                //TODO: password hardcoded for dashboard data of doctor.
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.PASSWORD, "doctor", this);

                String doctorDetails = new Gson().toJson(docDetail);
                RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_INFO, doctorDetails, this);

                Intent intent = new Intent(this, HomePageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(this, errorMessage);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(this, serverErrorMessage);

    }
}

