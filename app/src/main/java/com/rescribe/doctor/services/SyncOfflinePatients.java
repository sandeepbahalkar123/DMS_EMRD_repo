package com.rescribe.doctor.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.model.patient.add_new_patient.PatientDetail;
import com.rescribe.doctor.model.patient.add_new_patient.SyncPatientsRequest;
import com.rescribe.doctor.model.patient.doctor_patients.sync_resp.PatientUpdateDetail;
import com.rescribe.doctor.model.patient.doctor_patients.sync_resp.SyncPatientsModel;
import com.rescribe.doctor.network.RequestPool;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.singleton.Device;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;
import static com.rescribe.doctor.services.LoadAllPatientsService.STATUS;
import static com.rescribe.doctor.util.Config.ADD_PATIENTS_SYNC;
import static com.rescribe.doctor.util.Config.BASE_URL;
import static com.rescribe.doctor.util.RescribeConstants.SUCCESS;

public class SyncOfflinePatients {

    public static final String PATIENT_SYNC = "com.rescribe.doctor.PATIENT_SYNC";
    public static final String PATIENT_SYNC_LIST = "SyncList";
    private static final String LOG_TAG = "SyncOfflinePatients";
    private static final int SYNC_NOTIFICATION_ID = 122;

    private Context context;
    private AppDBHelper appDBHelper;
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotifyManager;
    private boolean isFailed = false;
    private Gson gson;

    SyncOfflinePatients() {
    }

    void onCreate(Context mContext) {
        this.context = mContext;
        appDBHelper = new AppDBHelper(context);
        gson = new Gson();
        if (RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, mContext).equals(RescribeConstants.YES))
            check();
    }

    public void check() {
        ArrayList<PatientDetail> offlineAddedPatients = appDBHelper.getOfflinePatientsToUpload();
        Log.e(LOG_TAG, "Checking offline patients " + offlineAddedPatients.size());

        if (!offlineAddedPatients.isEmpty()) {

            Intent notificationIntent = new Intent();
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                    notificationIntent, 0);

            mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(context);
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

            mBuilder.setContentTitle("Syncing patients")
                    .setTicker("Syncing")
                    .setContentText("Syncing patients")
                    .setSmallIcon(R.drawable.logosmall)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent).build();

            mBuilder.setContentText("Downloading patients")
                    // Removes the progress bar
                    .setProgress(0, 0, true);
            mNotifyManager.notify(SYNC_NOTIFICATION_ID, mBuilder.build());

            SyncPatientsRequest mSyncPatientsRequest = new SyncPatientsRequest();
            String id = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.DOC_ID, context);
            mSyncPatientsRequest.setDocId(id);
            mSyncPatientsRequest.setPatientDetails(offlineAddedPatients);

            JSONObject jsonObject = null;
            try {
                String jsonString = gson.toJson(mSyncPatientsRequest);
                CommonMethods.Log(LOG_TAG, "jsonRequest:--" + jsonString);
                if (!jsonString.equals("null"))
                    jsonObject = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(POST, BASE_URL + ADD_PATIENTS_SYNC, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            SyncPatientsModel mSyncPatientsModel = gson.fromJson(response.toString(), SyncPatientsModel.class);
                            if (mSyncPatientsModel.getCommon().getStatusCode().equals(SUCCESS)) {
                                ArrayList<PatientUpdateDetail> patientUpdateDetails = mSyncPatientsModel.getData().getPatientUpdateDetails();
                                if (!patientUpdateDetails.isEmpty()) {
                                    appDBHelper.updateOfflinePatientANDRecords(mSyncPatientsModel.getData().getPatientUpdateDetails());
                                    isFailed = false;
                                    synced(patientUpdateDetails);
                                } else {
                                    isFailed = true;
                                    synced(null);
                                }
                            } else {
                                isFailed = true;
                                synced(null);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isFailed = true;
                    synced(null);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Device device = Device.getInstance(context);
                    Map<String, String> headerParams = new HashMap<>();
                    String authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.AUTHTOKEN, context);
                    headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_JSON);
                    headerParams.put(RescribeConstants.AUTHORIZATION_TOKEN, authorizationString);
                    headerParams.put(RescribeConstants.DEVICEID, device.getDeviceId());
                    headerParams.put(RescribeConstants.OS, device.getOS());
                    headerParams.put(RescribeConstants.OSVERSION, device.getOSVersion());
                    headerParams.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());
                    CommonMethods.Log(LOG_TAG, "setHeaderParams:" + headerParams.toString());
                    return headerParams;
                }
            };
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(1000 * 60, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            jsonRequest.setTag("SyncingPatientsRequest");
            RequestPool.getInstance(context).addToRequestQueue(jsonRequest);
        }
    }

    private void synced(ArrayList<PatientUpdateDetail> list) {

        Intent intent = new Intent(PATIENT_SYNC);
        intent.putExtra(STATUS, isFailed);
        if (list != null)
            intent.putExtra(PATIENT_SYNC_LIST, list);
        context.sendBroadcast(intent);

        if (list != null) {
            mBuilder.setContentText(isFailed ? "Sync patients failed" : "Sync patients completed")
                    // Removes the progress bar
                    .setProgress(0, 100, false);
            mNotifyManager.notify(SYNC_NOTIFICATION_ID, mBuilder.build());
        } else
            mNotifyManager.cancel(SYNC_NOTIFICATION_ID);

        if (context instanceof MQTTService)
            ((MQTTService) context).checkPendingRecords();
    }

    void onDestroy() {
    }
}
