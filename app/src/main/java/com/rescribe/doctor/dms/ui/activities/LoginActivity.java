package com.rescribe.doctor.dms.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.rescribe.doctor.R;
import com.rescribe.doctor.dms.helpers.login.LoginHelper;
import com.rescribe.doctor.dms.interfaces.CustomResponse;
import com.rescribe.doctor.dms.interfaces.HelperResponse;
import com.rescribe.doctor.dms.preference.DmsPreferencesManager;
import com.rescribe.doctor.dms.util.CommonMethods;

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
        DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME,mUserName.getText().toString(),mContext);
        DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.PASSWORD,mPassword.getText().toString(),mContext);
        Intent intent = new Intent(this, PatientList.class);
        startActivity(intent);

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

