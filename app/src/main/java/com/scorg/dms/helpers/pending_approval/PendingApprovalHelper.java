package com.scorg.dms.helpers.pending_approval;

import android.content.Context;

import com.android.volley.Request;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.pending_approval_list.PendingRequestCancelModel;
import com.scorg.dms.model.pending_approval_list.RequestPendingApprovalData;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;



public class PendingApprovalHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;

    public PendingApprovalHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, mContext.getString(R.string.parse_error));
                mHelperResponseManager.onParseError(mOldDataTag, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                mHelperResponseManager.onServerError(mOldDataTag, mContext.getString(R.string.server_error));
                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;

            default:
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    public void doGetPendingApprovalData(int pageNo,boolean isPending) {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_PENDING_APPROVAL_LIST, Request.Method.POST, true);
        RequestPendingApprovalData mRequestPendingApprovalData = new RequestPendingApprovalData();
        mRequestPendingApprovalData.setPageNo(pageNo);
        mRequestPendingApprovalData.setPending(isPending);
        mConnectionFactory.setPostParams(mRequestPendingApprovalData);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.URL_PENDING_APPROVAL_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_PENDING_APPROVAL_LIST);
    }

    public void cancelRequest(PendingRequestCancelModel requestCancelModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_CANCEL_REQUEST_CONFIDENTIAL, Request.Method.POST, true);
        mConnectionFactory.setPostParams(requestCancelModel);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.URL_CANCEL_REQUEST_CONFIDENTIAL);
        mConnectionFactory.createConnection(DMSConstants.TASK_CANCEL_REQUEST_CONFIDENTIAL);
    }

}


