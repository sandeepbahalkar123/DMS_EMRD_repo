package com.rescribe.doctor.services;

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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.model.patient.doctor_patients.MyPatientBaseModel;
import com.rescribe.doctor.model.patient.doctor_patients.PatientList;
import com.rescribe.doctor.model.request_patients.RequestSearchPatients;
import com.rescribe.doctor.network.RequestPool;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.singleton.Device;
import com.rescribe.doctor.ui.activities.my_patients.MyPatientsActivity;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.POST;
import static com.rescribe.doctor.util.Config.BASE_URL;
import static com.rescribe.doctor.util.Config.GET_MY_PATIENTS_LIST;
import static com.rescribe.doctor.util.Config.GET_PATIENTS_SYNC;
import static com.rescribe.doctor.util.RescribeConstants.SUCCESS;

public class LoadAllPatientsService extends Service {
    public static boolean RUNNING = false;

    private static final String LOG_TAG = "LoadAllPatientsService";
    public static final String STATUS = "status";
    public static final String LOAD_ALL_PATIENTS = "com.rescribe.doctor.LOAD_ALL_PATIENTS";

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private int pageCount = 0;
    private boolean isFailed = true;
    private AppDBHelper appDBHelper;
    private static final int RECORD_COUNT = 50;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();
        appDBHelper = new AppDBHelper(this);
        gson = new Gson();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {

        if (intent.getAction() != null) {
            if (intent.getAction().equals(RescribeConstants.STARTFOREGROUND_ACTION)) {

                Log.i(LOG_TAG, "Received Start Foreground Intent ");
                Intent notificationIntent = new Intent(this, MyPatientsActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                        notificationIntent, 0);

                mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(this);
                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

                Notification notification = mBuilder
                        .setContentTitle("Download all patients")
                        .setTicker("Downloading")
                        .setContentText("Downloading patients")
                        .setSmallIcon(R.drawable.logosmall)
                        .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setContentIntent(pendingIntent).build();

                startForeground(RescribeConstants.FOREGROUND_SERVICE, notification);

                // Start Downloading
                request(RescribePreferencesManager.getBoolean(RescribePreferencesManager.DMS_PREFERENCES_KEY.PATIENT_DOWNLOAD, LoadAllPatientsService.this));
            }
        } else stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void request(final boolean isDownloaded) {

        Log.i(LOG_TAG, "Checking is patients downloaded" + isDownloaded);

        RUNNING = true;
        mBuilder.setContentText("Downloading patients")
                // Removes the progress bar
                .setProgress(0, 0, true);
        mNotifyManager.notify(RescribeConstants.FOREGROUND_SERVICE, mBuilder.build());

        RequestSearchPatients mRequestSearchPatients = new RequestSearchPatients();

        String id = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, this);
        mRequestSearchPatients.setPageNo(pageCount);
        mRequestSearchPatients.setDocId(Integer.valueOf(id));
        mRequestSearchPatients.setSearchText("");
        mRequestSearchPatients.setPaginationSize(RECORD_COUNT);
        mRequestSearchPatients.setDate(CommonMethods.getCurrentTimeStamp(RescribeConstants.DATE_PATTERN.UTC_PATTERN));

        JSONObject jsonObject = null;
        try {
            String jsonString = gson.toJson(mRequestSearchPatients);
            CommonMethods.Log(LOG_TAG, "jsonRequest:--" + jsonString);
            if (!jsonString.equals("null"))
                jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(POST, isDownloaded ? BASE_URL + GET_PATIENTS_SYNC : BASE_URL + GET_MY_PATIENTS_LIST, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        MyPatientBaseModel myPatientBaseModel = gson.fromJson(response.toString(), MyPatientBaseModel.class);
                        if (myPatientBaseModel.getCommon().getStatusCode().equals(SUCCESS)) {
                            ArrayList<PatientList> patientList = myPatientBaseModel.getPatientDataModel().getPatientList();
                            if (patientList.isEmpty()) {
                                isFailed = false;
                                restored(patientList.size());
                            } else {
                                // add in database
                                for (PatientList patientL : patientList) {
                                    patientL.setOfflinePatientSynced(true);
                                    patientL.setReferenceID("");
                                    appDBHelper.addNewPatient(patientL);
                                }

                                if (patientList.size() < RECORD_COUNT) {
                                    // after add database
                                    isFailed = false;
                                    restored(patientList.size());
                                } else {
                                    request(isDownloaded);
                                    pageCount += 1;
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isFailed = true;
                restored(0);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Device device = Device.getInstance(LoadAllPatientsService.this);
                Map<String, String> headerParams = new HashMap<>();
                String authorizationString = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.AUTHTOKEN, LoadAllPatientsService.this);
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
        jsonRequest.setTag("MyPatientRequest");
        RequestPool.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void restored(int size) {

        Log.i(LOG_TAG, "Patients downloaded " + isFailed);
        if (size > 0)
            CommonMethods.showToast(LoadAllPatientsService.this, !isFailed ? "Downloaded all patients" : "Patients download failed");

        pageCount = 0;
        RUNNING = false;

        Intent intent = new Intent(LOAD_ALL_PATIENTS);
        intent.putExtra(STATUS, isFailed);
        sendBroadcast(intent);

        mBuilder.setContentText(!isFailed ? "Downloaded all patients" : "Patients download failed")
                // Removes the progress bar
                .setProgress(0, 0, false);
        mNotifyManager.notify(RescribeConstants.FOREGROUND_SERVICE, mBuilder.build());

        RescribePreferencesManager.putBoolean(RescribePreferencesManager.DMS_PREFERENCES_KEY.PATIENT_DOWNLOAD, !isFailed, LoadAllPatientsService.this);

        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }
}
