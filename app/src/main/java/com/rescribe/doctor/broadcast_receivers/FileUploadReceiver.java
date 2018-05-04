package com.rescribe.doctor.broadcast_receivers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.model.chat.MQTTMessage;
import com.rescribe.doctor.model.chat.uploadfile.ChatFileUploadModel;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.services.MQTTService;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import static com.rescribe.doctor.broadcast_receivers.ReplayBroadcastReceiver.MESSAGE_LIST;
import static com.rescribe.doctor.services.MQTTService.SEND_MESSAGE;
import static com.rescribe.doctor.util.RescribeConstants.FILE_STATUS.COMPLETED;

public class FileUploadReceiver extends UploadServiceBroadcastReceiver {
    AppDBHelper instance;
    Gson gson = new Gson();

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onProgress " + uploadInfo.getProgressPercent());
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

        if (instance == null)
            instance = new AppDBHelper(context);

        String doctorId = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, context);

        String prefix[] = uploadInfo.getUploadId().split("_");
        if (prefix[0].equals(doctorId))
            instance.updateMessageUploadStatus(uploadInfo.getUploadId(), RescribeConstants.FILE_STATUS.FAILED);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(uploadInfo.getNotificationID());

        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onError");
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        if (instance == null)
            instance = new AppDBHelper(context);

        String doctorId = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, context);

            String prefix[] = uploadInfo.getUploadId().split("_");
            if (prefix[0].equals(doctorId)) {

                MQTTMessage mqttMessage = instance.getChatMessageByMessageId(uploadInfo.getUploadId());

                if (mqttMessage != null) {
                    String response = serverResponse.getBodyAsString();
                    ChatFileUploadModel chatFileUploadModel = gson.fromJson(response, ChatFileUploadModel.class);

                    String fileUrl = chatFileUploadModel.getData().getDocUrl();
                    // send via mqtt

                    mqttMessage.setFileUrl(fileUrl);
                    mqttMessage.setUploadStatus(COMPLETED);

                    Intent intentService = new Intent(context, MQTTService.class);
                    intentService.putExtra(SEND_MESSAGE, true);
                    intentService.putExtra(MESSAGE_LIST, mqttMessage);
                    context.startService(intentService);
                }
            }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(uploadInfo.getNotificationID());
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onCompleted");
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onCancelled");
    }
}