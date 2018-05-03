package com.rescribe.doctor.network;
/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;

import com.rescribe.doctor.interfaces.ConnectionListener;
import com.rescribe.doctor.ui.customesViews.CustomProgressDialog;

import java.util.Map;

public class Request {
    protected Context mContext;
    protected Map<String, String> mPostParams;
    protected Map<String, String> mHeaderParams;
    protected ConnectionListener mConnectionListener;
    protected CustomProgressDialog mProgressDialog;

    public Request() {
    }

    public Request(Context mContext) {
        this.mContext = mContext;
    }

    public Request(Context mContext, Map<String, String> mPostParams) {
        this(mContext);
        this.mPostParams = mPostParams;
    }

    public Request(Context mContext, ConnectionListener connectionListener) {
        this(mContext);
        this.mConnectionListener = mConnectionListener;
    }

    public Request(Context mContext, Map<String, String> mPostParams,
                   Map<String, String> mHeaderParams) {
        this(mContext, mPostParams);
        this.mHeaderParams = mHeaderParams;
    }

    public Request(Context mContext, Map<String, String> mPostParams,
                   Map<String, String> mHeaderParams, ConnectionListener mConnectionListener) {
        this(mContext, mPostParams, mHeaderParams);
        this.mConnectionListener = mConnectionListener;
    }


    public Request(Context mContext, Map<String, String> mPostParams,
                   Map<String, String> mHeaderParams,
                   ConnectionListener mConnectionListener,
                   CustomProgressDialog mProgressDialog) {

        this.mContext = mContext;
        this.mPostParams = mPostParams;
        this.mHeaderParams = mHeaderParams;
        this.mConnectionListener = mConnectionListener;
        this.mProgressDialog = mProgressDialog;
    }

    /**
     * @return the mContext
     */
    protected Context getmContext() {
        return mContext;
    }

    /**
     * @param mContext the mContext to set
     */
    protected void setContext(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * @return the mPostParams
     */
    protected Map<String, String> getPostParams() {
        return mPostParams;
    }

    public void setPostParams(Map<String, String> postParams) {
        this.mPostParams = postParams;
    }

    /**
     * @return the mHeaderParams
     */
    protected Map<String, String> getHeaderParams() {
        return mHeaderParams;
    }

    public void setHeaderParams(Map<String, String> headerParams) {
        this.mHeaderParams = headerParams;
    }

    /**
     * @return the mConnectionListener
     */
    protected ConnectionListener getConnectionListener() {
        return mConnectionListener;
    }

    /**
     * @param mConnectionListener the mConnectionListener to set
     */
    protected void setConnectionListener(ConnectionListener mConnectionListener) {
        this.mConnectionListener = mConnectionListener;
    }

    /**
     * @return the mProgressDialog
     */
    protected CustomProgressDialog getmProgressDialog() {
        return mProgressDialog;
    }

    /**
     * @param mProgressDialog the mProgressDialog to set
     */
    protected void setProgressDialog(CustomProgressDialog mProgressDialog) {
        this.mProgressDialog = mProgressDialog;
    }

}
