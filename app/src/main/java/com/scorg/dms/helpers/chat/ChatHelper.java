package com.scorg.dms.helpers.chat;

import android.content.Context;

import com.android.volley.Request;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.chat.MessageList;
import com.scorg.dms.model.chat.MessageRequestModel;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.services.MQTTService;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;

/**
 * Created by jeetal on 5/9/17.
 */

public class ChatHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;

    public ChatHelper(Context context) {
        this.mContext = context;
        this.mHelperResponseManager = (HelperResponse) context;
    }

    public ChatHelper(Context context, HelperResponse doctorConnectActivity) {
        this.mContext = context;
        this.mHelperResponseManager = doctorConnectActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, "parse error");
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onServerError(mOldDataTag, "server error");

                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, "no connection error");
                mHelperResponseManager.onNoConnectionError(mOldDataTag, "no connection error");

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

    public void sendMsgToPatient(MessageList messageL){
        MessageRequestModel messageRequestModel = new MessageRequestModel();
        messageRequestModel.setMsg(messageL.getMsg());
        // 2017-10-13 12:08:07
        messageRequestModel.setMsgTime(CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.UTC_PATTERN));
        messageRequestModel.setSender(MQTTService.DOCTOR);
        messageRequestModel.setUser1id(messageL.getDocId());
        messageRequestModel.setUser2id(messageL.getPatId());

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.SEND_MESSAGE, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(messageRequestModel);
        mConnectionFactory.setUrl(Config.SEND_MSG_TO_PATIENT);
        mConnectionFactory.createConnection(DMSConstants.SEND_MESSAGE);
    }

    public void getChatHistory(int pgNmbr, int user1id, int user2id) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, DMSConstants.CHAT_HISTORY, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.CHAT_HISTORY + "user1id=" + user1id + "&user2id=" + user2id + "&pgNmbr=" + pgNmbr);
        mConnectionFactory.createConnection(DMSConstants.CHAT_HISTORY);
    }
}


