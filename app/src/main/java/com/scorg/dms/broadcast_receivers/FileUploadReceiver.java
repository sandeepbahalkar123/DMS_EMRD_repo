package com.scorg.dms.broadcast_receivers;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.model.chat.MQTTMessage;
import com.scorg.dms.model.chat.uploadfile.ChatFileUploadModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.services.MQTTService;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

import static com.scorg.dms.broadcast_receivers.ReplayBroadcastReceiver.MESSAGE_LIST;
import static com.scorg.dms.services.MQTTService.SEND_MESSAGE;
import static com.scorg.dms.util.DMSConstants.FILE_STATUS.COMPLETED;

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

        String doctorId = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, context);

        String prefix[] = uploadInfo.getUploadId().split("_");
        if (prefix[0].equals(doctorId))
            instance.updateMessageUploadStatus(uploadInfo.getUploadId(), DMSConstants.FILE_STATUS.FAILED);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(uploadInfo.getNotificationID());

        CommonMethods.Log("ImagedUploadIdHome", uploadInfo.getUploadId() + " onError");
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        if (instance == null)
            instance = new AppDBHelper(context);

        String doctorId = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, context);

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