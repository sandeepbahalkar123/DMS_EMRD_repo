package com.rescribe.doctor.dms.helpers.login;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.doctor.dms.interfaces.ConnectionListener;
import com.rescribe.doctor.dms.interfaces.CustomResponse;
import com.rescribe.doctor.dms.interfaces.HelperResponse;
import com.rescribe.doctor.dms.model.responsemodel.iptestresponsemodel.IpTestResponseModel;
import com.rescribe.doctor.dms.model.responsemodel.loginresponsemodel.LoginResponseModel;
import com.rescribe.doctor.dms.network.ConnectRequest;
import com.rescribe.doctor.dms.network.ConnectionFactory;
import com.rescribe.doctor.dms.preference.DmsPreferencesManager;
import com.rescribe.doctor.dms.util.CommonMethods;
import com.rescribe.doctor.dms.util.Config;
import com.rescribe.doctor.dms.util.DmsConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class LoginHelper implements ConnectionListener {

    String TAG = this.getClass().getSimpleName();
    Context mContext;
    HelperResponse mHelperResponseManager;
    private String userName;
    private String mServerPath;
    private String password;
    private String userGender;

    public LoginHelper(Context context, HelperResponse loginActivity1) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity1;
    }


    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                if (mOldDataTag == DmsConstants.TASK_LOGIN_CODE) {
                    LoginResponseModel model = (LoginResponseModel) customResponse;
                    DmsPreferencesManager.putString(DmsConstants.LOGIN_SUCCESS, DmsConstants.TRUE, mContext);
                    DmsPreferencesManager.putString(DmsConstants.ACCESS_TOKEN, model.getAccessToken(), mContext);
                    DmsPreferencesManager.putString(DmsConstants.TOKEN_TYPE, model.getTokenType(), mContext);
                    DmsPreferencesManager.putString(DmsConstants.REFRESH_TOKEN, model.getRefreshToken(), mContext);
                    DmsPreferencesManager.putString(DmsConstants.USERNAME, userName, mContext);
                    DmsPreferencesManager.putString(DmsConstants.PASSWORD, password, mContext);
                    CommonMethods.Log(TAG,"Refersh token after login response: "+model.getRefreshToken());
                    DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.USER_GENDER, model.getUserGender(), mContext);
                    mHelperResponseManager.onSuccess(mOldDataTag, model);

                } else if (mOldDataTag == DmsConstants.TASK_CHECK_SERVER_CONNECTION) {

                    IpTestResponseModel ipTestResponseModel = (IpTestResponseModel) customResponse;
                    mHelperResponseManager.onSuccess(mOldDataTag, ipTestResponseModel);


                }
                break;


            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, "parse error");
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


    public void doAppLogin(String userName, String password) {
        this.userName = userName;
        this.password = password;
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DmsConstants.TASK_LOGIN_CODE, Request.Method.POST);
        mConnectionFactory.setHeaderParams();
        Map<String, String> testParams = new HashMap<String, String>();
        testParams.put(DmsConstants.GRANT_TYPE_KEY, DmsConstants.PASSWORD);
        testParams.put(DmsConstants.USERNAME, userName);
        testParams.put(DmsConstants.PASSWORD, password);
        testParams.put(DmsConstants.CLIENT_ID_KEY, DmsConstants.CLIENT_ID_VALUE);
        mConnectionFactory.setPostParams(testParams);
        mConnectionFactory.setUrl(Config.URL_LOGIN);
        mConnectionFactory.createConnection(DmsConstants.TASK_LOGIN_CODE);
    }

    public void checkConnectionToServer(String serverPath) {
        this.mServerPath = serverPath;
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DmsConstants.TASK_CHECK_SERVER_CONNECTION, Request.Method.GET);
        mConnectionFactory.setUrl(Config.URL_CHECK_SERVER_CONNECTION);
        mConnectionFactory.createConnection(DmsConstants.TASK_CHECK_SERVER_CONNECTION);
    }
}
