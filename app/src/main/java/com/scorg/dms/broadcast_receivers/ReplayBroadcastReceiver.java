package com.scorg.dms.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.model.chat.MQTTMessage;
import com.scorg.dms.notification.MessageNotification;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.services.MQTTService;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import static com.scorg.dms.services.MQTTService.DOCTOR;
import static com.scorg.dms.services.MQTTService.MESSAGE_TOPIC;
import static com.scorg.dms.services.MQTTService.REPLY_ACTION;
import static com.scorg.dms.services.MQTTService.SEND_MESSAGE;
import static com.scorg.dms.ui.activities.PatientConnectActivity.FREE;

public class ReplayBroadcastReceiver extends BroadcastReceiver /*implements HelperResponse*/ {
    public static final String MESSAGE_LIST = "message_list";
    private MQTTMessage recievedMessage;
    private AppDBHelper appDBHelper;

    public static Intent getReplyMessageIntent(Context context, MQTTMessage MQTTMessage) {
        Intent intent = new Intent(context, ReplayBroadcastReceiver.class);
        intent.setAction(REPLY_ACTION);
        intent.putExtra(MESSAGE_LIST, MQTTMessage);
        return intent;
    }

    public ReplayBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REPLY_ACTION.equals(intent.getAction())) {
            // do whatever you want with the message. Send to the server or add to the db.
            // for this tutorial, we'll just show it in a toast;
            CharSequence message = MQTTService.getReplyMessage(intent);
            appDBHelper = new AppDBHelper(context);
            recievedMessage = intent.getParcelableExtra(MESSAGE_LIST);

            MQTTMessage messageL = new MQTTMessage();
            messageL.setTopic(MQTTService.TOPIC[MESSAGE_TOPIC]);
            messageL.setMsg(message != null ? message.toString() : "");
            String generatedId = recievedMessage.getDocId() + "_" + 0 + System.nanoTime();
            messageL.setMsgId(generatedId);

            messageL.setDocId(recievedMessage.getDocId());
            messageL.setPatId(recievedMessage.getPatId());

            String doctorName = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, context);
            String imageUrl = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.PROFILE_PHOTO, context);
            String speciality = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SPECIALITY, context);

            messageL.setSenderName(doctorName);
            messageL.setSenderImgUrl(imageUrl);
            messageL.setOnlineStatus(DMSConstants.USER_STATUS.ONLINE);

            messageL.setSalutation(recievedMessage.getSalutation());
            messageL.setReceiverName(recievedMessage.getReceiverName());
            messageL.setReceiverImgUrl(recievedMessage.getReceiverImgUrl());

            messageL.setFileUrl("");
            messageL.setSpecialization(speciality);
            messageL.setPaidStatus(FREE);
            messageL.setFileType("");

            messageL.setSender(DOCTOR);

            // 2017-10-13 13:08:07
            String msgTime = CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.UTC_PATTERN);
            messageL.setMsgTime(msgTime);

            // send msg by http api

//            ChatHelper chatHelper = new ChatHelper(context, ReplayBroadcastReceiver.this);
//            chatHelper.sendMsgToPatient(messageL);

            // send via mqtt
            Intent intentService = new Intent(context, MQTTService.class);
            intentService.putExtra(SEND_MESSAGE, true);
            intentService.putExtra(MESSAGE_LIST, messageL);
            context.startService(intentService);
            MessageNotification.cancel(context, recievedMessage.getPatId()); // Change
            appDBHelper.markAsAReadChatMessageByPatientId(recievedMessage.getPatId()); // Change

        }
    }

   /* @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (customResponse instanceof SendMessageModel) {
            if (recievedMessage != null) {
                MessageNotification.cancel(context, recievedMessage.getPatId()); // Change
                appDBHelper.deleteUnreadMessage(recievedMessage.getPatId()); // Change
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }*/
}