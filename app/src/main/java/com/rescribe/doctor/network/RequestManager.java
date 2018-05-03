package com.rescribe.doctor.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.model.patient.doctor_patients.MyPatientBaseModel;
import com.rescribe.doctor.interfaces.ConnectionListener;
import com.rescribe.doctor.interfaces.Connector;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;
import com.rescribe.doctor.model.CommonBaseModelContainer;
import com.rescribe.doctor.model.case_details.CaseDetailsModel;
import com.rescribe.doctor.model.chat.SendMessageModel;
import com.rescribe.doctor.model.chat.history.ChatHistoryModel;
import com.rescribe.doctor.model.completed_opd.CompletedOpdBaseModel;
import com.rescribe.doctor.model.dashboard.DashboardBaseModel;
import com.rescribe.doctor.model.doctor_connect.DoctorConnectBaseModel;
import com.rescribe.doctor.model.doctor_connect_chat.DoctorConnectChatBaseModel;
import com.rescribe.doctor.model.doctor_connect_search.DoctorConnectSearchBaseModel;
import com.rescribe.doctor.model.doctor_location.DoctorLocationBaseModel;
import com.rescribe.doctor.model.login.ActiveStatusModel;
import com.rescribe.doctor.model.login.LoginModel;
import com.rescribe.doctor.model.login.SignUpModel;
import com.rescribe.doctor.model.my_appointments.MyAppointmentsBaseModel;
import com.rescribe.doctor.model.my_patient_filter.LocationsModel;
import com.rescribe.doctor.model.new_patient.NewPatientBaseModel;
import com.rescribe.doctor.model.patient.doctor_patients.sync_resp.SyncPatientsModel;
import com.rescribe.doctor.model.patient.patient_connect.ChatPatientConnectModel;
import com.rescribe.doctor.model.patient.patient_connect.PatientConnectBaseModel;
import com.rescribe.doctor.model.patient.patient_history.PatientHistoryBaseModel;
import com.rescribe.doctor.model.patient.template_sms.TemplateBaseModel;
import com.rescribe.doctor.model.request_appointment_confirmation.ResponseAppointmentConfirmationModel;
import com.rescribe.doctor.model.requestmodel.login.LoginRequestModel;
import com.rescribe.doctor.model.select_slot_book_appointment.TimeSlotListBaseModel;
import com.rescribe.doctor.model.waiting_list.WaitingListBaseModel;
import com.rescribe.doctor.model.waiting_list.response_add_to_waiting_list.AddToWaitingListBaseModel;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.singleton.Device;
import com.rescribe.doctor.ui.customesViews.CustomProgressDialog;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.Config;
import com.rescribe.doctor.util.NetworkUtil;
import com.rescribe.doctor.util.RescribeConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.rescribe.doctor.util.RescribeConstants.SUCCESS;

public class RequestManager extends ConnectRequest implements Connector, RequestTimer.RequestTimerListener {
    private final String TAG = this.getClass().getName();
    private static final int CONNECTION_TIME_OUT = 1000 * 60;
    private static final int N0OF_RETRY = 0;
    private AppDBHelper dbHelper;
    private String requestTag;
    private int connectionType;

    private String mDataTag;
    private RequestTimer requestTimer;
    private JsonObjectRequest jsonRequest;
    private StringRequest stringRequest;

    public RequestManager(Context mContext, ConnectionListener connectionListener, String dataTag, View viewById, boolean isProgressBarShown, String mOldDataTag, int connectionType, boolean isOffline) {
        super();
        this.mConnectionListener = connectionListener;
        this.mContext = mContext;
        this.mDataTag = dataTag;
        this.mViewById = viewById;
        this.isProgressBarShown = isProgressBarShown;
        this.mOldDataTag = mOldDataTag;
        this.requestTag = String.valueOf(dataTag);
        this.requestTimer = new RequestTimer();
        this.requestTimer.setListener(this);
        this.mProgressDialog = new CustomProgressDialog(mContext);
        this.connectionType = connectionType;
        this.isOffline = isOffline;
        this.dbHelper = new AppDBHelper(mContext);
    }

    @Override
    public void connect() {

        if (NetworkUtil.isInternetAvailable(mContext)) {

            RequestPool.getInstance(this.mContext).cancellAllPreviousRequestWithSameTag(requestTag);

            if (isProgressBarShown) {
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();
            } else {
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }

            if (mPostParams != null) {
                stringRequest(mURL, connectionType, mHeaderParams, mPostParams, false);
            } else if (customResponse != null) {
                jsonRequest(mURL, connectionType, mHeaderParams, customResponse, false);
            } else {
                jsonRequest();
            }
        } else {

            if (isOffline) {
                if (getOfflineData() != null)
                    succesResponse(getOfflineData(), false);
                else
                    mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
            } else {
                mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
            }

            if (mViewById != null)
                CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
            else
                CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
        }
    }

    private void jsonRequest(String url, int connectionType, final Map<String, String> headerParams, CustomResponse customResponse, final boolean isTokenExpired) {

        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            CommonMethods.Log(TAG, "customResponse:--" + customResponse.toString());
            String jsonString = gson.toJson(customResponse);

            CommonMethods.Log(TAG, "jsonRequest:--" + jsonString);

            if (!jsonString.equals("null"))
                jsonObject = new JSONObject(jsonString);
        } catch (JSONException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        jsonRequest = new JsonObjectRequest(connectionType, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        succesResponse(response.toString(), isTokenExpired);
                        if (isOffline)
                            dbHelper.insertData(mDataTag, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorResponse(error, isTokenExpired);
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headerParams == null) {
                    return Collections.emptyMap();
                } else {
                    return headerParams;
                }

            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).addToRequestQueue(jsonRequest);
    }


    private void jsonRequest() {

        Gson gson = new Gson();
        JSONObject jsonObject = null;
        try {
            String jsonString = gson.toJson(customResponse);

            CommonMethods.Log(TAG, "jsonRequest:--" + jsonString);

            if (!jsonString.equals("null"))
                jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        jsonRequest = new JsonObjectRequest(connectionType, this.mURL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        succesResponse(response.toString(), false);
                        if (isOffline)
                            dbHelper.insertData(mDataTag, response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                errorResponse(error, false);
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (mHeaderParams == null) {
                    return Collections.emptyMap();
                } else {
                    return mHeaderParams;
                }

            }
        };
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).addToRequestQueue(jsonRequest);
    }

    private void stringRequest(String url, int connectionType, final Map<String, String> headerParams, final Map<String, String> postParams, final boolean isTokenExpired) {

        // ganesh for string request and delete method with string request

        stringRequest = new StringRequest(connectionType, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        succesResponse(response, isTokenExpired);
                        if (isOffline)
                            dbHelper.insertData(mDataTag, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        errorResponse(error, isTokenExpired);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headerParams;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return postParams;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).addToRequestQueue(stringRequest);
    }

    private void succesResponse(String response, boolean isTokenExpired) {
        requestTimer.cancel();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        parseJson(response, isTokenExpired);
    }

    private void errorResponse(VolleyError error, boolean isTokenExpired) {

        requestTimer.cancel();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        try {

//            VolleyError error1 = new VolleyError(new String(error.networkResponse.data));
//            error = error1;
//            CommonMethods.Log("Error Message", error.getMessage() + "\n error Localize message" + error.getLocalizedMessage());
            CommonMethods.Log(TAG, "Goes into error response condition");

            if (error instanceof TimeoutError) {

                if (error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found") || error.getMessage().equalsIgnoreCase("invalid_grant")) {
                    if (!isTokenExpired) {
                        tokenRefreshRequest();
                    }
                }

//                if (error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found") || error.getMessage().equalsIgnoreCase("invalid_grant")) {
//                    if (mViewById != null)
//                        CommonMethods.showSnack(mViewById, mContext.getString(R.string.authentication));
//                    else
//                        CommonMethods.showToast(mContext, mContext.getString(R.string.authentication));
//                } else if (error.getMessage().equalsIgnoreCase("javax.net.ssl.SSLHandshakeException: java.security.cert.CertPathValidatorException: Trust anchor for certification path not found.")) {
//                    showErrorDialog("Something went wrong.");
//                }

                if (mViewById != null)
                    CommonMethods.showSnack(mViewById, mContext.getString(R.string.timeout));
                else
                    CommonMethods.showToast(mContext, mContext.getString(R.string.timeout));

            } else if (error instanceof NoConnectionError) {

                if (error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found") || error.getMessage().equalsIgnoreCase("invalid_grant")) {
                    /*if (!isTokenExpired) {
                        tokenRefreshRequest();
                    }*/
                } else {
                    if (getOfflineData() != null)
                        succesResponse(getOfflineData(), false);
                }

                if (mViewById != null)
                    CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
                else {
                    mConnectionListener.onResponse(ConnectionListener.NO_CONNECTION_ERROR, null, mOldDataTag);
                }

            } else if (error instanceof ServerError) {
                if (isTokenExpired) {
                    // Redirect to SplashScreen then Login
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    mContext.startActivity(intent);
//                    ((AppCompatActivity) mContext).finishAffinity();

//                    RescribePreferencesManager.clearSharedPref(mContext);
//                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    mContext.startActivity(intent);

                    //  loginRequest();
                } else {
                    mConnectionListener.onResponse(ConnectionListener.SERVER_ERROR, null, mOldDataTag);
                    CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.server_error));
                }
            } else if (error instanceof NetworkError) {

                if (isOffline) {
                    succesResponse(getOfflineData(), false);
                } else {
                    mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
                }

                if (mViewById != null)
                    CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
                else
                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
            } else if (error instanceof ParseError) {
                mConnectionListener.onResponse(ConnectionListener.PARSE_ERR0R, null, mOldDataTag);
            } else if (error instanceof AuthFailureError) {
                if (!isTokenExpired) {
                    tokenRefreshRequest();
                }
            } else {
                CommonMethods.showToast(mContext, error.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getOfflineData() {
        if (dbHelper.dataTableNumberOfRows(this.mDataTag) > 0) {
            Cursor cursor = dbHelper.getData(this.mDataTag);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(AppDBHelper.COLUMN_DATA));
        } else {
            return null;
        }
    }

    private String fixEncoding(String response) {
        try {
            byte[] u = response.getBytes("ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    @Override
    public void parseJson(String data, boolean isTokenExpired) {
        try {
            CommonMethods.Log(TAG, data);
            Gson gson = new Gson();

            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(data);
                Common common = gson.fromJson(jsonObject.optString("common"), Common.class);
                if (!common.getStatusCode().equals(SUCCESS)) {
                    CommonMethods.showToast(mContext, common.getStatusMessage());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /*MessageModel messageModel = gson.fromJson(data, MessageModel.class);
            if (!messageModel.getCommon().getStatusCode().equals(RescribeConstants.SUCCESS))
                CommonMethods.showToast(mContext, messageModel.getCommon().getStatusMessage());*/

            if (isTokenExpired) {
                // This success response is for refresh token
                // Need to Add
                LoginModel loginModel = gson.fromJson(data, LoginModel.class);
                if (loginModel.getCommon().getStatusCode().equals(SUCCESS)) {
                    RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.AUTHTOKEN, loginModel.getDoctorLoginData().getAuthToken(), mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.LOGIN_STATUS, RescribeConstants.YES, mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.DOC_ID, String.valueOf(loginModel.getDoctorLoginData().getDocDetail().getDocId()), mContext);

                    mHeaderParams.put(RescribeConstants.AUTHORIZATION_TOKEN, loginModel.getDoctorLoginData().getAuthToken());
                    connect();
                } else {
                    CommonMethods.showToast(mContext, loginModel.getCommon().getStatusMessage());
                }

            } else {
                // This success response is for respective api's

                switch (this.mDataTag) {
                    // Need to add
                    case RescribeConstants.TASK_LOGIN: //This is for get archived list
                        LoginModel loginModel = gson.fromJson(data, LoginModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, loginModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_LOGIN_WITH_PASSWORD: //This is for get archived list
                        LoginModel loginWithPasswordModel = new Gson().fromJson(data, LoginModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, loginWithPasswordModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_LOGIN_WITH_OTP: //This is for get archived list
                        LoginModel loginWithOtpModel = new Gson().fromJson(data, LoginModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, loginWithOtpModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_SIGN_UP: //This is for get sign-up
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, new Gson().fromJson(data, SignUpModel.class), mOldDataTag);
                        break;
                    case RescribeConstants.TASK_VERIFY_SIGN_UP_OTP: //This is for to verify sign-up otp
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, new Gson().fromJson(data, LoginModel.class), mOldDataTag);
                        break;

                    case RescribeConstants.TASK_DOCTOR_CONNECT_CHAT: //This is for get archived list
                        DoctorConnectChatBaseModel doctorConnectChatBaseModel = new Gson().fromJson(data, DoctorConnectChatBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorConnectChatBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DOCTOR_CONNECT: //This is for get archived list
                        DoctorConnectBaseModel doctorConnectBaseModel = new Gson().fromJson(data, DoctorConnectBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorConnectBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DOCTOR_FILTER_DOCTOR_SPECIALITY_LIST: //This is for get archived list
                        DoctorConnectSearchBaseModel doctorConnectSearchBaseModel = new Gson().fromJson(data, DoctorConnectSearchBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, doctorConnectSearchBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_PATIENT_LIST: //This is for get archived list
                        PatientConnectBaseModel patientConnectBaseModel = new Gson().fromJson(data, PatientConnectBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, patientConnectBaseModel, mOldDataTag);
                        break;

                    case RescribeConstants.GET_PATIENT_CHAT_LIST: //This is for get archived list
                        ChatPatientConnectModel patientConnectModel = new Gson().fromJson(data, ChatPatientConnectModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, patientConnectModel, mOldDataTag);
                        break;

                    case RescribeConstants.SEND_MESSAGE: //This is for get archived list
                        SendMessageModel sendMessageModel = new Gson().fromJson(data, SendMessageModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, sendMessageModel, mOldDataTag);
                        break;

                    case RescribeConstants.CHAT_HISTORY: //This is for get archived list
                        ChatHistoryModel chatHistoryModel = new Gson().fromJson(data, ChatHistoryModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, chatHistoryModel, mOldDataTag);
                        break;

                    case RescribeConstants.ACTIVE_STATUS: //This is for get archived list
                        ActiveStatusModel activeStatusModel = new Gson().fromJson(data, ActiveStatusModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, activeStatusModel, mOldDataTag);
                        break;

                    case RescribeConstants.LOGOUT: //This is for get archived list
                        ActiveStatusModel activeStatusLogout = new Gson().fromJson(data, ActiveStatusModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, activeStatusLogout, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_APPOINTMENT_DATA: //This is for get archived list
                        MyAppointmentsBaseModel mMyAppointmentsBaseModel = new Gson().fromJson(data, MyAppointmentsBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mMyAppointmentsBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_PATIENT_DATA: //This is for get archived list
                        MyPatientBaseModel mMyPatientBaseModel = new Gson().fromJson(data, MyPatientBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mMyPatientBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_ONE_DAY_VISIT: //This is for get archived list
                        CaseDetailsModel mCaseDetailsModel = new Gson().fromJson(data, CaseDetailsModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mCaseDetailsModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_DOCTOR_SMS_TEMPLATE: //This is for get archived list
                        TemplateBaseModel mTemplateBaseModel = new Gson().fromJson(data, TemplateBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mTemplateBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_REQUEST_SEND_SMS: //This is for get archived list
                        TemplateBaseModel mTemplateBaseModelforCommon = new Gson().fromJson(data, TemplateBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mTemplateBaseModelforCommon, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_WAITING_LIST: //This is for get archived list
                        WaitingListBaseModel mWaitingListBaseModel = new Gson().fromJson(data, WaitingListBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mWaitingListBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_SEARCH_RESULT_MY_PATIENT: //This is for get archived list
                        MyPatientBaseModel mMyPatientBaseModelSearch = new Gson().fromJson(data, MyPatientBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mMyPatientBaseModelSearch, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_PATIENT_HISTORY: //This is for get archived list
                        PatientHistoryBaseModel mPatientHistoryBaseModel = new Gson().fromJson(data, PatientHistoryBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mPatientHistoryBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_ADD_TO_WAITING_LIST: //This is for get archived list
                        AddToWaitingListBaseModel mAddToWaitingListBaseModel = new Gson().fromJson(data, AddToWaitingListBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mAddToWaitingListBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_LOCATION_LIST: //This is for get archived list
                        DoctorLocationBaseModel mDoctorLocationBaseModel = new Gson().fromJson(data, DoctorLocationBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mDoctorLocationBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_DASHBOARD_RESPONSE: //This is for get archived list
                        DashboardBaseModel mDashboardBaseModel = new Gson().fromJson(data, DashboardBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mDashboardBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DELETE_WAITING_LIST: //This is for get archived list
                        TemplateBaseModel mDeleteWaitingList = new Gson().fromJson(data, TemplateBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mDeleteWaitingList, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_APPOINTMENT_CANCEL_OR_COMPLETE: //This is for get archived list
                        TemplateBaseModel mCompleteCancel = new Gson().fromJson(data, TemplateBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mCompleteCancel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DARG_DROP: //This is for get archived list
                        TemplateBaseModel mDragAndDrop = new Gson().fromJson(data, TemplateBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mDragAndDrop, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_COMPLETED_OPD: //This is for get archived list
                        CompletedOpdBaseModel mCompletedOpdBaseModel = new Gson().fromJson(data, CompletedOpdBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mCompletedOpdBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_NEW_PATIENT_LIST: //This is for get archived list
                        NewPatientBaseModel mNewPatientBaseModel = new Gson().fromJson(data, NewPatientBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mNewPatientBaseModel, mOldDataTag);
                        break;

                    case RescribeConstants.TASK_GET_DOCTOR_PATIENT_CITY: //This is for get archived list
                        LocationsModel locationsModel = new Gson().fromJson(data, LocationsModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, locationsModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_DELETE_PATIENT_OPD_ATTCHMENTS: //This is for delete attachments
                        CommonBaseModelContainer commonModel = new Gson().fromJson(data, CommonBaseModelContainer.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, commonModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_GET_TIME_SLOTS_TO_BOOK_APPOINTMENT: //This is for delete attachments
                        TimeSlotListBaseModel mTimeSlotListBaseModel = new Gson().fromJson(data, TimeSlotListBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mTimeSlotListBaseModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_CONFIRM_APPOINTMENT: //This is for delete attachments
                        ResponseAppointmentConfirmationModel mResponseAppointmentConfirmationModel = new Gson().fromJson(data, ResponseAppointmentConfirmationModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mResponseAppointmentConfirmationModel, mOldDataTag);
                        break;
                    case RescribeConstants.TASK_ADD_NEW_PATIENT: //This is for delete attachments
                        SyncPatientsModel model = new Gson().fromJson(data, SyncPatientsModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, model, mOldDataTag);
                        break;


                    default:


                }
            }

        } catch (JsonSyntaxException e) {
            CommonMethods.Log(TAG, "JsonException" + e.getMessage());
            mConnectionListener.onResponse(ConnectionListener.PARSE_ERR0R, null, mOldDataTag);
        }

    }

    @Override
    public void setPostParams(CustomResponse customResponse) {
        this.customResponse = customResponse;
    }

    @Override
    public void setPostParams(Map<String, String> postParams) {
        this.mPostParams = postParams;
    }

    @Override
    public void setHeaderParams(Map<String, String> headerParams) {
        this.mHeaderParams = headerParams;
    }

    @Override
    public void abort() {
        if (jsonRequest != null)
            jsonRequest.cancel();
        if (stringRequest != null)
            stringRequest.cancel();
    }

    @Override
    public void setUrl(String url) {
        this.mURL = url;
    }

    @Override
    public void onTimeout(RequestTimer requestTimer) {
        if (mContext instanceof AppCompatActivity) {
            if (mContext != null) {
                ((AppCompatActivity) this.mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                });
            }
        }

        RequestPool.getInstance(mContext)
                .cancellAllPreviousRequestWithSameTag(requestTag);
        mConnectionListener.onTimeout(this);
    }

    public void showErrorDialog(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void tokenRefreshRequest() {
        loginRequest();
    }

    private void loginRequest() {
        CommonMethods.Log(TAG, "Refresh token while sending refresh token api: ");
        String url = Config.BASE_URL + Config.LOGIN_URL;

        LoginRequestModel loginRequestModel = new LoginRequestModel();

        loginRequestModel.setEmailId(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.EMAIL, mContext));
        loginRequestModel.setPassword(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.PASSWORD, mContext));
        if (!(RescribeConstants.BLANK.equalsIgnoreCase(loginRequestModel.getEmailId()) &&
                RescribeConstants.BLANK.equalsIgnoreCase(loginRequestModel.getPassword()))) {
            Map<String, String> headerParams = new HashMap<>();
            headerParams.putAll(mHeaderParams);
            Device device = Device.getInstance(mContext);

            headerParams.put(RescribeConstants.CONTENT_TYPE, RescribeConstants.APPLICATION_JSON);
            headerParams.put(RescribeConstants.DEVICEID, device.getDeviceId());
            headerParams.put(RescribeConstants.OS, device.getOS());
            headerParams.put(RescribeConstants.OSVERSION, device.getOSVersion());
            headerParams.put(RescribeConstants.DEVICE_TYPE, device.getDeviceType());
            CommonMethods.Log(TAG, "setHeaderParams:" + headerParams.toString());
            jsonRequest(url, Request.Method.POST, headerParams, loginRequestModel, true);
        } else {
            mConnectionListener.onResponse(ConnectionListener.PARSE_ERR0R, null, mOldDataTag);
        }
    }
}