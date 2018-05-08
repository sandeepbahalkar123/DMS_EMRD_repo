package com.scorg.dms.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.scorg.dms.R;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.model.chat.MQTTMessage;
import com.scorg.dms.model.chat.history.ChatHistory;
import com.scorg.dms.model.chat.history.ChatHistoryModel;
import com.scorg.dms.model.patient.patient_connect.ChatPatientConnectModel;
import com.scorg.dms.model.patient.patient_connect.PatientData;
import com.scorg.dms.network.RequestPool;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.Device;
import com.scorg.dms.ui.activities.PatientConnectActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.scorg.dms.util.DMSConstants.FILE_STATUS.FAILED;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.READ;
import static com.scorg.dms.util.DMSConstants.MESSAGE_STATUS.SENT;
import static com.scorg.dms.util.DMSConstants.SUCCESS;

public class ChatBackUpService extends Service {
    public static boolean RUNNING = false;

    private static final String LOG_TAG = "ChatBackUpService";

    public static final String STATUS = "status";
    public static final String CHAT_BACKUP = "com.scorg.dms.CHAT_BACKUP";

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private int patientIndex = 0;
    private boolean isFailed = true;
    private AppDBHelper appDBHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        appDBHelper = new AppDBHelper(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if (intent.getAction() != null) {
            if (intent.getAction().equals(DMSConstants.STARTFOREGROUND_ACTION)) {

                Log.i(LOG_TAG, "Received Start Foreground Intent ");
                Intent notificationIntent = new Intent(this, PatientConnectActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

                mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(this);
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

                Notification notification = mBuilder
                        .setContentTitle("Chat Backup")
                        .setTicker("Restoring messages")
                        .setContentText("Restoring messages")
                        .setSmallIcon(R.drawable.logosmall)
                        .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentIntent(pendingIntent).build();

                startForeground(DMSConstants.FOREGROUND_SERVICE, notification);

                // Start Downloading
                request();
            }
        } else stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void request() {

        RUNNING = true;

        mBuilder.setContentText("Backup Restoring")
                // Removes the progress bar
                .setProgress(0, 0, true);
        mNotifyManager.notify(DMSConstants.FOREGROUND_SERVICE, mBuilder.build());

        String id = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, this);
        StringRequest stringRequest = new StringRequest(GET, Config.BASE_URL + Config.GET_PATIENT_LIST + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ChatPatientConnectModel patientConnectModel = new Gson().fromJson(response, ChatPatientConnectModel.class);
                        if (patientConnectModel.getCommon().getStatusCode().equals(SUCCESS)) {
                            ArrayList<PatientData> patientDataList = patientConnectModel.getPatientListData().getPatientDataList();
                            if (patientDataList.isEmpty()) {
                                CommonMethods.showToast(ChatBackUpService.this, patientConnectModel.getCommon().getStatusMessage());
                                isFailed = false;
                                restored();
                            } else
                                restoreMessages(patientDataList);
                        } else {
                            restored();
                            CommonMethods.showToast(ChatBackUpService.this, patientConnectModel.getCommon().getStatusMessage());
                            DMSPreferencesManager.putBoolean(DMSPreferencesManager.DMS_PREFERENCES_KEY.BACK_UP, true, ChatBackUpService.this);
                            stopSelf();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        restored();
                    }
                }
        )

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Device device = Device.getInstance(ChatBackUpService.this);
                Map<String, String> headerParams = new HashMap<>();
                String authorizationString = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, ChatBackUpService.this);
                headerParams.put(DMSConstants.CONTENT_TYPE, DMSConstants.APPLICATION_JSON);
                headerParams.put(DMSConstants.AUTHORIZATION_TOKEN, authorizationString);
                headerParams.put(DMSConstants.DEVICEID, device.getDeviceId());
                headerParams.put(DMSConstants.OS, device.getOS());
                headerParams.put(DMSConstants.OSVERSION, device.getOSVersion());
                headerParams.put(DMSConstants.DEVICE_TYPE, device.getDeviceType());
                CommonMethods.Log(LOG_TAG, "setHeaderParams:" + headerParams.toString());
                return headerParams;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("BackUpRequest");
        RequestPool.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void restoreMessages(final ArrayList<PatientData> patientDataList) {

        PatientData patientData = patientDataList.get(patientIndex);

        String docId = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, this);
        String url = Config.BASE_URL + Config.CHAT_HISTORY + "user1id=" + docId + "&user2id=" + patientData.getId();

        patientIndex += 1;

        StringRequest stringRequest = new StringRequest(GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ChatHistoryModel chatHistoryModel = new Gson().fromJson(response, ChatHistoryModel.class);
                        if (chatHistoryModel.getCommon().getStatusCode().equals(SUCCESS)) {
                            List<ChatHistory> chatHistory = chatHistoryModel.getHistoryData().getChatHistory();

                            for (int index = chatHistory.size() - 1; index >= 0; index -= 1) {

                                ChatHistory chatH = chatHistory.get(index);

                                MQTTMessage messageL = new MQTTMessage();
                                messageL.setMsgId(chatH.getChatId());
                                messageL.setMsg(chatH.getMsg());
                                messageL.setDocId(chatH.getUser1Id());
                                messageL.setPatId(chatH.getUser2Id());
                                messageL.setSender(chatH.getSender());

                                messageL.setSenderName(chatH.getSenderName());
                                messageL.setSenderImgUrl(chatH.getSenderImgUrl());

                                messageL.setReceiverName(chatH.getReceiverName());
                                messageL.setReceiverImgUrl(chatH.getReceiverImgUrl());

                                messageL.setSpecialization(chatH.getSpecialization());
                                messageL.setOnlineStatus(chatH.getOnlineStatus());
                                messageL.setPaidStatus(chatH.getPaidStatus());
                                messageL.setFileType(chatH.getFileType());
                                messageL.setFileUrl(chatH.getFileUrl());
                                messageL.setMsgTime(chatH.getMsgTime());
                                messageL.setMsgStatus(chatH.getMsgStatus() == null ? SENT : chatH.getMsgStatus());
                                messageL.setUploadStatus(FAILED);
                                messageL.setDownloadStatus(FAILED);
                                messageL.setSalutation(chatH.getSalutation());

                                messageL.setReadStatus(READ);

                                appDBHelper.insertChatMessage(messageL);
                            }

                            if (patientDataList.size() > patientIndex) {
                                restoreMessages(patientDataList);
                            } else {
                                isFailed = false;
                                restored();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (patientDataList.size() > patientIndex) {
                            restoreMessages(patientDataList);
                        } else {
                            restored();
                        }
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Device device = Device.getInstance(ChatBackUpService.this);
                Map<String, String> headerParams = new HashMap<>();
                String authorizationString = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, ChatBackUpService.this);
                headerParams.put(DMSConstants.CONTENT_TYPE, DMSConstants.APPLICATION_JSON);
                headerParams.put(DMSConstants.AUTHORIZATION_TOKEN, authorizationString);
                headerParams.put(DMSConstants.DEVICEID, device.getDeviceId());
                headerParams.put(DMSConstants.OS, device.getOS());
                headerParams.put(DMSConstants.OSVERSION, device.getOSVersion());
                headerParams.put(DMSConstants.DEVICE_TYPE, device.getDeviceType());
                CommonMethods.Log(LOG_TAG, "setHeaderParams:" + headerParams.toString());
                return headerParams;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag("BackUpRequest");
        RequestPool.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void restored() {

        RUNNING = false;

        patientIndex = 0;

        Intent intent = new Intent(CHAT_BACKUP);
        intent.putExtra(STATUS, isFailed);
        sendBroadcast(intent);

        mBuilder.setContentText(!isFailed ? "Backup Restored" : "Backup Restore Failed")
                // Removes the progress bar
                .setProgress(0, 0, false);
        mNotifyManager.notify(DMSConstants.FOREGROUND_SERVICE, mBuilder.build());

        DMSPreferencesManager.putBoolean(DMSPreferencesManager.DMS_PREFERENCES_KEY.BACK_UP, !isFailed, ChatBackUpService.this);

        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }
}
