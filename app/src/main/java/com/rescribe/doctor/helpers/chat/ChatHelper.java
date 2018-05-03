package com.rescribe.doctor.helpers.chat;

import android.content.Context;

import com.android.volley.Request;
import com.rescribe.doctor.interfaces.ConnectionListener;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.chat.MessageList;
import com.rescribe.doctor.model.chat.MessageRequestModel;
import com.rescribe.doctor.network.ConnectRequest;
import com.rescribe.doctor.network.ConnectionFactory;
import com.rescribe.doctor.services.MQTTService;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.Config;
import com.rescribe.doctor.util.RescribeConstants;

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
        messageRequestModel.setMsgTime(CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN));
        messageRequestModel.setSender(MQTTService.DOCTOR);
        messageRequestModel.setUser1id(messageL.getDocId());
        messageRequestModel.setUser2id(messageL.getPatId());

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, RescribeConstants.SEND_MESSAGE, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(messageRequestModel);
        mConnectionFactory.setUrl(Config.SEND_MSG_TO_PATIENT);
        mConnectionFactory.createConnection(RescribeConstants.SEND_MESSAGE);
    }

    public void getChatHistory(int pgNmbr, int user1id, int user2id) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, RescribeConstants.CHAT_HISTORY, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.CHAT_HISTORY + "user1id=" + user1id + "&user2id=" + user2id + "&pgNmbr=" + pgNmbr);
        mConnectionFactory.createConnection(RescribeConstants.CHAT_HISTORY);
    }
}


