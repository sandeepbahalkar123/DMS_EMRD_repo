package com.scorg.dms.helpers.login;

import android.content.Context;

import com.android.volley.Request;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.responsemodel.iptestresponsemodel.IpTestResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.loginresponsemodel.LoginResponseModel;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;

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
                    case DMSConstants.TASK_LOGIN_CODE:
                        LoginResponseModel model = (LoginResponseModel) customResponse;
                        CommonMethods.Log(TAG, "Refersh token after login response: " + model.getRefreshToken());
                        mHelperResponseManager.onSuccess(mOldDataTag, model);

                        break;
                    case DMSConstants.TASK_CHECK_SERVER_CONNECTION:
                        IpTestResponseModel ipTestResponseModel = (IpTestResponseModel) customResponse;
                        mHelperResponseManager.onSuccess(mOldDataTag, ipTestResponseModel);

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
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_LOGIN_CODE, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        Map<String, String> testParams = new HashMap<String, String>();
        testParams.put(DMSConstants.GRANT_TYPE_KEY, DMSConstants.PASSWORD);
        testParams.put(DMSConstants.USERNAME, userName);
        testParams.put(DMSConstants.PASSWORD, password);
        testParams.put(DMSConstants.CLIENT_ID_KEY, DMSConstants.CLIENT_ID_VALUE);
        mConnectionFactory.setPostParams(testParams);
        //TODO: setDMSUrl added for temporary purpose, once done with real API, use setUrl method
        mConnectionFactory.setUrl(Config.URL_LOGIN);
        mConnectionFactory.createConnection(DMSConstants.TASK_LOGIN_CODE);
    }

    public void checkConnectionToServer(String serverPath) {
        this.mServerPath = serverPath;

        //TODO : IP CHECK API IN NOT IMPLEMENTED YET, HENCE COMMENTED BELOW CODE, N GOES INTO ONSUCEESS.
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_CHECK_SERVER_CONNECTION, Request.Method.GET, false);
        mConnectionFactory.setUrl(Config.URL_CHECK_SERVER_CONNECTION);
        mConnectionFactory.createConnection(DMSConstants.TASK_CHECK_SERVER_CONNECTION);

//        IpTestResponseModel i = new IpTestResponseModel();
//        Common c = new Common();
//        c.setStatusCode(DMSConstants.SUCCESS);
//        i.setCommon(c);
//        onResponse(ConnectionListener.RESPONSE_OK, i, DMSConstants.TASK_CHECK_SERVER_CONNECTION);
    }



}
