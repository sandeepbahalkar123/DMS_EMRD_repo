package com.rescribe.doctor.network;

import android.content.Context;
import android.view.View;

import com.rescribe.doctor.interfaces.ConnectionListener;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.ui.customesViews.CustomProgressDialog;

import java.util.Map;

public class ConnectRequest {
    protected Context mContext;
    protected CustomResponse customResponse;
    protected Map<String, String> mHeaderParams;
    protected Map<String, String> mPostParams;
    protected ConnectionListener mConnectionListener;
    protected CustomProgressDialog mProgressDialog;
    protected View mViewById;
    protected boolean isProgressBarShown;
    protected String mOldDataTag;
    protected String mURL;
    protected int reqPostOrGet;
    protected boolean isOffline;
}
