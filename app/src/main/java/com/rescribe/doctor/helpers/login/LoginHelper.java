package com.rescribe.doctor.helpers.login;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.doctor.model.dms_models.responsemodel.loginresponsemodel.LoginResponseModel;
import com.rescribe.doctor.interfaces.ConnectionListener;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.dms_models.responsemodel.iptestresponsemodel.IpTestResponseModel;
import com.rescribe.doctor.model.login.ActiveRequest;
import com.rescribe.doctor.model.login.SignUpModel;
import com.rescribe.doctor.model.requestmodel.login.LoginRequestModel;
import com.rescribe.doctor.model.requestmodel.login.SignUpRequestModel;
import com.rescribe.doctor.model.requestmodel.login.SignUpVerifyOTPRequestModel;
import com.rescribe.doctor.network.ConnectRequest;
import com.rescribe.doctor.network.ConnectionFactory;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.Config;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ganeshshirole on 10/7/17.
 */

public class LoginHelper implements ConnectionListener {
    String TAG = this.getClass().getName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private String mServerPath;
    private String userName;
    private String password;

    public LoginHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                switch (mOldDataTag) {
                    case RescribeConstants.TASK_LOGIN_CODE:

                        LoginResponseModel model = (LoginResponseModel) customResponse;
                        RescribePreferencesManager.putString(RescribeConstants.LOGIN_SUCCESS, RescribeConstants.TRUE, mContext);
                        RescribePreferencesManager.putString(RescribeConstants.ACCESS_TOKEN, model.getAccessToken(), mContext);
                        RescribePreferencesManager.putString(RescribeConstants.TOKEN_TYPE, model.getTokenType(), mContext);
                        RescribePreferencesManager.putString(RescribeConstants.REFRESH_TOKEN, model.getRefreshToken(), mContext);
                        RescribePreferencesManager.putString(RescribeConstants.USERNAME, userName, mContext);
                        RescribePreferencesManager.putString(RescribeConstants.PASSWORD, password, mContext);
                        CommonMethods.Log(TAG, "Refersh token after login response: " + model.getRefreshToken());
                        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.USER_GENDER, model.getUserGender(), mContext);
                        mHelperResponseManager.onSuccess(mOldDataTag, model);

                        break;
                    case RescribeConstants.TASK_CHECK_SERVER_CONNECTION:
                        IpTestResponseModel ipTestResponseModel = (IpTestResponseModel) customResponse;
                        mHelperResponseManager.onSuccess(mOldDataTag, ipTestResponseModel);

                        break;
                    case RescribeConstants.TASK_SIGN_UP:
                        SignUpModel signUpModel = (SignUpModel) customResponse;
                        mHelperResponseManager.onSuccess(mOldDataTag, signUpModel);
                        break;
                    case RescribeConstants.TASK_VERIFY_SIGN_UP_OTP:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                    case RescribeConstants.TASK_LOGIN_WITH_PASSWORD:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                    case RescribeConstants.TASK_LOGIN_WITH_OTP:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                    case RescribeConstants.LOGOUT:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                    case RescribeConstants.ACTIVE_STATUS:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                        break;
                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onServerError(mOldDataTag, "server error");
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");
                break;
            default:
                CommonMethods.Log(TAG, "default error");
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    //-------DMS LOGIN AND IP CHECK APIS. : START

    public void doAppLogin(String userName, String password) {
        this.userName = userName;
        this.password = password;
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_LOGIN_CODE, Request.Method.POST, false);
        mConnectionFactory.setDMSHeaderParams();
        Map<String, String> testParams = new HashMap<String, String>();
        testParams.put(RescribeConstants.GRANT_TYPE_KEY, RescribeConstants.PASSWORD);
        testParams.put(RescribeConstants.USERNAME, userName);
        testParams.put(RescribeConstants.PASSWORD, password);
        testParams.put(RescribeConstants.CLIENT_ID_KEY, RescribeConstants.CLIENT_ID_VALUE);
        mConnectionFactory.setPostParams(testParams);
        //TODO: setDMSUrl added for temporary purpose, once done with real API, use setUrl method
        mConnectionFactory.setDMSUrl(Config.URL_LOGIN);
        mConnectionFactory.createConnection(RescribeConstants.TASK_LOGIN_CODE);
    }

    public void checkConnectionToServer(String serverPath) {
        this.mServerPath = serverPath;

        //TODO : IP CHECK API IN NOT IMPLEMENTED YET, HENCE COMMENTED BELOW CODE, N GOES INTO ONSUCEESS.
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_CHECK_SERVER_CONNECTION, Request.Method.GET, false);
        mConnectionFactory.setDMSUrl(Config.URL_CHECK_SERVER_CONNECTION);
        mConnectionFactory.createConnection(RescribeConstants.TASK_CHECK_SERVER_CONNECTION);

//        IpTestResponseModel i = new IpTestResponseModel();
//        Common c = new Common();
//        c.setStatusCode(RescribeConstants.SUCCESS);
//        i.setCommon(c);
//        onResponse(ConnectionListener.RESPONSE_OK, i, RescribeConstants.TASK_CHECK_SERVER_CONNECTION);
    }

    //-------DMS LOGIN AND IP CHECK APIS. : END


    //-------RESCRIBE EXITING APIs : START

    //Do login using Otp
    public void doLoginByOTP(String otp) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_LOGIN_WITH_OTP, Request.Method.POST, true);
        mConnectionFactory.setHeaderParams();
        LoginRequestModel loginRequestModel = new LoginRequestModel();
        //    loginRequestModel.setMobileNumber(otp);  TODO NOT CONFIRMED ABOUT THIS.
        mConnectionFactory.setPostParams(loginRequestModel);
        mConnectionFactory.setUrl(Config.LOGIN_WITH_OTP_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_LOGIN_WITH_OTP);
    }

    //Verify Otp sent
    public void doVerifyGeneratedSignUpOTP(SignUpVerifyOTPRequestModel requestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_VERIFY_SIGN_UP_OTP, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(requestModel);
        mConnectionFactory.setUrl(Config.VERIFY_SIGN_UP_OTP);
        mConnectionFactory.createConnection(RescribeConstants.TASK_VERIFY_SIGN_UP_OTP);
    }

    //SignUp
    public void doSignUp(SignUpRequestModel signUpRequestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.TASK_SIGN_UP, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(signUpRequestModel);
        mConnectionFactory.setUrl(Config.SIGN_UP_URL);
        mConnectionFactory.createConnection(RescribeConstants.TASK_SIGN_UP);
    }

    // Logout
    public void doLogout(ActiveRequest activeRequest) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.LOGOUT, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(activeRequest);
        mConnectionFactory.setUrl(Config.LOGOUT);
        mConnectionFactory.createConnection(RescribeConstants.LOGOUT);
    }

    // ActiveStatus
    public void doActiveStatus(ActiveRequest activeRequest) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.ACTIVE_STATUS, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(activeRequest);
        mConnectionFactory.setUrl(Config.ACTIVE);
        mConnectionFactory.createConnection(RescribeConstants.ACTIVE_STATUS);
    }

    //-------RESCRIBE EXITING APIs : END

}
