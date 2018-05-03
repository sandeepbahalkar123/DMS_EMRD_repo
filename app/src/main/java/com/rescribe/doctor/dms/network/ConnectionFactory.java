
package com.rescribe.doctor.dms.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.view.View;

import com.rescribe.doctor.dms.interfaces.ConnectionListener;
import com.rescribe.doctor.dms.interfaces.Connector;
import com.rescribe.doctor.dms.interfaces.CustomResponse;
import com.rescribe.doctor.dms.preference.DmsPreferencesManager;
import com.rescribe.doctor.dms.singleton.Device;
import com.rescribe.doctor.dms.util.CommonMethods;
import com.rescribe.doctor.dms.util.DmsConstants;

import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory extends ConnectRequest {

    private static final String TAG = "ConnectionFactory";
    Connector connector = null;
    private Device device;

    public ConnectionFactory(Context context, ConnectionListener connectionListener, View viewById, boolean isProgressBarShown, String mOldDataTag, int reqPostOrGet) {
        super();
        this.mConnectionListener = connectionListener;
        this.mContext = context;
        this.mViewById = viewById;
        this.isProgressBarShown = isProgressBarShown;
        this.mOldDataTag = mOldDataTag;
        this.reqPostOrGet = reqPostOrGet;

        device = Device.getInstance(mContext);
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.mHeaderParams = headerParams;
    }

    public void setHeaderParams() {

        Map<String, String> headerParams = new HashMap<>();

        String authorizationString = "";
        String contentType = DmsPreferencesManager.getString(DmsConstants.LOGIN_SUCCESS, mContext);

        if (contentType.equalsIgnoreCase(DmsConstants.TRUE)) {
            authorizationString = DmsPreferencesManager.getString(DmsConstants.TOKEN_TYPE, mContext)
                    + " " + DmsPreferencesManager.getString(DmsConstants.ACCESS_TOKEN, mContext);
            headerParams.put(DmsConstants.CONTENT_TYPE, DmsConstants.APPLICATION_JSON);
        } else {
            headerParams.put(DmsConstants.CONTENT_TYPE, DmsConstants.APPLICATION_URL_ENCODED);
        }

        headerParams.put(DmsConstants.AUTHORIZATION, authorizationString);
        headerParams.put(DmsConstants.DEVICEID, device.getDeviceId());

        headerParams.put(DmsConstants.OS, device.getOS());
        headerParams.put(DmsConstants.OSVERSION, device.getOSVersion());
        //  headerParams.put(DmsConstants.DEVICETYPE, device.getDeviceType());
//        headerParams.put(DmsConstants.ACCESS_TOKEN, "");
        CommonMethods.Log(TAG, "setHeaderParams:" + headerParams.toString());
        this.mHeaderParams = headerParams;
    }

    public void setPostParams(CustomResponse customResponse) {
        this.customResponse = customResponse;
    }

    public void setPostParams(Map<String, String> postParams) {
        this.mPostParams = postParams;
    }

    public void setUrl(String url) {
        String baseUrl = DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
        this.mURL = baseUrl + url;
        CommonMethods.Log(TAG,"mURL: "+this.mURL);
    }

    public Connector createConnection(String type) {

        connector = new RequestManager(mContext, mConnectionListener, type, mViewById, isProgressBarShown, mOldDataTag, reqPostOrGet);

        if (customResponse != null) connector.setPostParams(customResponse);

        if (mPostParams != null) connector.setPostParams(mPostParams);

        if (mHeaderParams != null) connector.setHeaderParams(mHeaderParams);

        if (mURL != null) connector.setUrl(mURL);

        connector.connect();

        return connector;
    }

    public void cancel() {
        connector.abort();
    }
}