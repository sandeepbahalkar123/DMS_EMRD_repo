package com.scorg.dms.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.view.View;

import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.Connector;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.Device;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

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

   /* public void setHeaderParams() {

        Map<String, String> headerParams = new HashMap<>();
        String authorizationString = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, mContext);
        headerParams.put(DMSConstants.CONTENT_TYPE, DMSConstants.APPLICATION_JSON);
        headerParams.put(DMSConstants.AUTHORIZATION_TOKEN, authorizationString);
        headerParams.put(DMSConstants.DEVICEID, device.getDeviceId());
        headerParams.put(DMSConstants.OS, device.getOS());
        headerParams.put(DMSConstants.OSVERSION, device.getOSVersion());
        headerParams.put(DMSConstants.DEVICE_TYPE, device.getDeviceType());
        CommonMethods.Log(TAG, "setHeaderParams:" + headerParams.toString());
        this.mHeaderParams = headerParams;
    }*/


    //THis is done for now, as DMS API IS NOT AVAILABLE RIGHT NOW
    public void setHeaderParams() {

        Map<String, String> headerParams = new HashMap<>();

        String authorizationString = "";
        String contentType = DMSPreferencesManager.getString(DMSConstants.LOGIN_SUCCESS, mContext);

        if (contentType.equalsIgnoreCase(DMSConstants.TRUE)) {
            authorizationString = DMSPreferencesManager.getString(DMSConstants.TOKEN_TYPE, mContext)
                    + " " + DMSPreferencesManager.getString(DMSConstants.ACCESS_TOKEN, mContext);
            headerParams.put(DMSConstants.CONTENT_TYPE, DMSConstants.APPLICATION_JSON);
        } else {
            headerParams.put(DMSConstants.CONTENT_TYPE, DMSConstants.APPLICATION_URL_ENCODED);
        }

        headerParams.put(DMSConstants.AUTHORIZATION, authorizationString);
        headerParams.put(DMSConstants.DEVICEID, device.getDeviceId());

        headerParams.put(DMSConstants.OS, device.getOS());
        headerParams.put(DMSConstants.DMS_OSVERSION, device.getOSVersion());
          headerParams.put(DMSConstants.DEVICE_TYPE, device.getDeviceType());
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
        String baseUrl = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);

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