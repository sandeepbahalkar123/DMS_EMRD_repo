package com.rescribe.doctor.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.view.View;

import com.rescribe.doctor.interfaces.ConnectionListener;
import com.rescribe.doctor.interfaces.Connector;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.singleton.Device;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.Config;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.HashMap;
import java.util.Map;

public class ConnectionFactory extends ConnectRequest {

    private final String TAG = this.getClass().getName();
    private Connector connector = null;
    private Device device;

    public ConnectionFactory(Context context, ConnectionListener connectionListener, View viewById, boolean isProgressBarShown, String mOldDataTag, int reqPostOrGet, boolean isOffline) {
        super();
        this.mConnectionListener = connectionListener;
        this.mContext = context;
        this.mViewById = viewById;
        this.isProgressBarShown = isProgressBarShown;
        this.mOldDataTag = mOldDataTag;
        this.reqPostOrGet = reqPostOrGet;
        this.isOffline = isOffline;

        device = Device.getInstance(mContext);
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.mHeaderParams = headerParams;
    }

    public void setHeaderParams() {

        Map<String, String> headerParams = new HashMap<>();
        String authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, mContext);
        headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_JSON);
        headerParams.put(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
        headerParams.put(RescribeConstants.DEVICEID, device.getDeviceId());
        headerParams.put(RescribeConstants.OS, device.getOS());
        headerParams.put(RescribeConstants.OSVERSION, device.getOSVersion());
        headerParams.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());
        CommonMethods.Log(TAG, "setHeaderParams:" + headerParams.toString());
        this.mHeaderParams = headerParams;
    }


    //THis is done for now, as DMS API IS NOT AVAILABLE RIGHT NOW
    public void setDMSHeaderParams() {

        Map<String, String> headerParams = new HashMap<>();

        String authorizationString = "";
        String contentType = RescribePreferencesManager.getString(RescribeConstants.LOGIN_SUCCESS, mContext);

        if (contentType.equalsIgnoreCase(RescribeConstants.TRUE)) {
            authorizationString = RescribePreferencesManager.getString(RescribeConstants.TOKEN_TYPE, mContext)
                    + " " + RescribePreferencesManager.getString(RescribeConstants.ACCESS_TOKEN, mContext);
            headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_JSON);
        } else {
            headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_URL_ENCODED);
        }

        headerParams.put(RescribeConstants.AUTHORIZATION, authorizationString);
        headerParams.put(RescribeConstants.DEVICEID, device.getDeviceId());

        headerParams.put(RescribeConstants.OS, device.getOS());
        headerParams.put(RescribeConstants.DMS_OSVERSION, device.getOSVersion());
        //  headerParams.put(RescribeConstants.DEVICETYPE, device.getDeviceType());
//        headerParams.put(RescribeConstants.ACCESS_TOKEN, "");
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
        this.mURL = Config.BASE_URL + url;
        CommonMethods.Log(TAG, "mURL: " + this.mURL);
    }

    public void setDMSUrl(String url) {
        String baseUrl = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);

        this.mURL = baseUrl + url;
        CommonMethods.Log(TAG, "mURL: " + this.mURL);
    }

    public Connector createConnection(String type) {

        connector = new RequestManager(mContext, mConnectionListener, type, mViewById, isProgressBarShown, mOldDataTag, reqPostOrGet, isOffline);

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