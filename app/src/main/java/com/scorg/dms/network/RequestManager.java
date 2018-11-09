package com.scorg.dms.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.Connector;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.admitted_patient.AdmittedPatientBaseModel;
import com.scorg.dms.model.dashboard.DashboardBaseModel;
import com.scorg.dms.model.dms_models.requestmodel.archive.UnlockRequestResponseBaseModel;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationListResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.EpisodeResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.getpdfdataresponsemodel.GetPdfDataResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.iptestresponsemodel.IpTestResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.loginresponsemodel.LoginResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel.PatientNameListResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.model.my_appointments.MyAppointmentsBaseModel;
import com.scorg.dms.model.pending_approval_list.CancelUnlockRequestResponseBaseModel;
import com.scorg.dms.model.pending_approval_list.RequestedArchivedBaseModel;
import com.scorg.dms.model.waiting_list.WaitingListBaseModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.Device;
import com.scorg.dms.ui.activities.SplashScreenActivity;
import com.scorg.dms.ui.customesViews.CustomProgressDialog;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.NetworkUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestManager extends ConnectRequest implements Connector, RequestTimer.RequestTimerListener {
    private final String TAG = this.getClass().getName();
    private static final int CONNECTION_TIME_OUT = 1000 * 0;
    private static final int N0OF_RETRY = 0;
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
    }

    @Override
    public void connect() {

        if (NetworkUtil.isInternetAvailable(mContext)) {

            RequestPool.getInstance(this.mContext).cancellAllPreviousRequestWithSameTag(requestTag);

            if (isProgressBarShown) {
                mProgressDialog.setCancelable(false);
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
            mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
            if (mViewById != null)
                CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
//            else
//                CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
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
        jsonRequest.setShouldCache(false);
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
        jsonRequest.setShouldCache(false);
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
            public Map<String, String> getHeaders() {
                return headerParams;
            }

            @Override
            protected Map<String, String> getParams() {
                return postParams;
            }
        };
        stringRequest.setShouldCache(false);
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
                // mConnectionListener.onResponse(ConnectionListener.TIMEOUT_ERROR, null, mOldDataTag);
                if (mContext instanceof AppCompatActivity) {
                    ((AppCompatActivity) this.mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                        }
                    });
                }

                RequestPool.getInstance(mContext)
                        .cancellAllPreviousRequestWithSameTag(requestTag);
                mConnectionListener.onTimeout(this, mOldDataTag);

            } else if (error instanceof NoConnectionError) {

                if (error.getMessage().equalsIgnoreCase("java.io.IOException: No authentication challenges found") || error.getMessage().equalsIgnoreCase("invalid_grant")) {
                    /*if (!isTokenExpired) {
                        tokenRefreshRequest();
                    }*/
                } else {
                }

                if (mViewById != null)
                    CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
                else {
                    mConnectionListener.onResponse(ConnectionListener.NO_CONNECTION_ERROR, null, mOldDataTag);
                }

            } else if (error instanceof ServerError) {
                if (isTokenExpired) {// this is refresh token 400 http response code when password changed happened at web app side.
                    DMSPreferencesManager.clearSharedPref(mContext);
                    // Redirect to Login screen
                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
                    mContext.startActivity(intent);
                    ((AppCompatActivity) mContext).finishAffinity();
                } else {
                    mConnectionListener.onResponse(ConnectionListener.SERVER_ERROR, null, mOldDataTag);

                    if (!DMSConstants.TASK_LOGIN_CODE.equals(mOldDataTag))
                        CommonMethods.showToast(mContext, mContext.getResources().getString(R.string.server_error));

                }
            } else if (error instanceof NetworkError) {
                mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);
                if (mViewById != null)
                    CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
//                else
//                    CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
            } else if (error instanceof ParseError) {
                mConnectionListener.onResponse(ConnectionListener.PARSE_ERR0R, null, mOldDataTag);
            } else if (error instanceof AuthFailureError) {
                if (!isTokenExpired) {
                    tokenRefreshRequest();
                }
            } else {
                CommonMethods.Log(TAG, error.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void parseJson(String data, boolean isTokenExpired) {
        try {
            CommonMethods.Log(TAG, data);
            Gson gson = new Gson();
            if (isTokenExpired) {
                // This success response is for refresh token
                // Need to Add
                LoginResponseModel loginResponseModel = gson.fromJson(data, LoginResponseModel.class);
                DMSPreferencesManager.putString(DMSConstants.LOGIN_SUCCESS, DMSConstants.TRUE, mContext);
                DMSPreferencesManager.putString(DMSConstants.ACCESS_TOKEN, loginResponseModel.getAccessToken(), mContext);
                DMSPreferencesManager.putString(DMSConstants.TOKEN_TYPE, loginResponseModel.getTokenType(), mContext);
                DMSPreferencesManager.putString(DMSConstants.REFRESH_TOKEN, loginResponseModel.getRefreshToken(), mContext);
                DMSPreferencesManager.putString(DMSConstants.USERNAME, DMSPreferencesManager.getString(DMSConstants.USERNAME, mContext), mContext);
                DMSPreferencesManager.putString(DMSConstants.PASSWORD, DMSPreferencesManager.getString(DMSConstants.PASSWORD, mContext), mContext);
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, String.valueOf(loginResponseModel.getDoctorId()), mContext);
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_GENDER, loginResponseModel.getUserGender(), mContext);
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_NAME, loginResponseModel.getDoctorName(), mContext);
                DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.HOSPITAL_NAME, loginResponseModel.getHospitalName(), mContext);

                String authorizationString = "";
                String contentType = DMSPreferencesManager.getString(DMSConstants.LOGIN_SUCCESS, mContext);
                if (contentType.equalsIgnoreCase(DMSConstants.TRUE)) {
                    authorizationString = DMSPreferencesManager.getString(DMSConstants.TOKEN_TYPE, mContext)
                            + " " + DMSPreferencesManager.getString(DMSConstants.ACCESS_TOKEN, mContext);
                }
                mHeaderParams.put(DMSConstants.AUTHORIZATION, authorizationString);
                connect();

            } else {
                // This success response is for respective api's
                switch (this.mDataTag) {

                    case DMSConstants.TASK_LOGIN_CODE: //This is LOGIN
                        LoginResponseModel mLoginResponseModel = new Gson().fromJson(data, LoginResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mLoginResponseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_GET_APPOINTMENT_DATA: //This is for get appointment list
                        MyAppointmentsBaseModel mMyAppointmentsBaseModel = new Gson().fromJson(data, MyAppointmentsBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mMyAppointmentsBaseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_GET_WAITING_LIST: //This is for get waiting list
                        WaitingListBaseModel mWaitingListBaseModel = new Gson().fromJson(data, WaitingListBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mWaitingListBaseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_GET_DASHBOARD_RESPONSE: //This is for get dashboard response
                        DashboardBaseModel mDashboardBaseModel = new Gson().fromJson(data, DashboardBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, mDashboardBaseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_PATIENT_LIST: //This is for get total patient list
                        ShowSearchResultResponseModel showSearchResultResponseModel = gson.fromJson(data, ShowSearchResultResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, showSearchResultResponseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_ANNOTATIONS_LIST: //This is for annotation list
                        AnnotationListResponseModel annotationListResponseModel = gson.fromJson(data, AnnotationListResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, annotationListResponseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_GET_ARCHIVED_LIST: //This is for get archived list
                        FileTreeResponseModel fileTreeResponseModel = gson.fromJson(data, FileTreeResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, fileTreeResponseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_CHECK_SERVER_CONNECTION: //This is for chck connection to server
                        //this is done bcaz, No common key is added in response from API.
                        String editedResponse = "{ \"common\":" + data + "}";
                        IpTestResponseModel ipTestResponseModel = gson.fromJson(editedResponse, IpTestResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, ipTestResponseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_GET_PATIENT_NAME_LIST: //This is for get patient list on the basis of search string
                        PatientNameListResponseModel patientNameListResponseModel = gson.fromJson(data, PatientNameListResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, patientNameListResponseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_GET_EPISODE_LIST: //This is for get episode list
                        EpisodeResponseModel episodeResponseModel = gson.fromJson(data, EpisodeResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, episodeResponseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_PENDING_APPROVAL_LIST: //This is for get pending approval list
                        RequestedArchivedBaseModel requestedArchivedBaseModel = gson.fromJson(data, RequestedArchivedBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, requestedArchivedBaseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_RAISE_REQUEST_CONFIDENTIAL: //This is for unlock confidential file /folder
                        UnlockRequestResponseBaseModel unlockRequestResponseBaseModel = gson.fromJson(data, UnlockRequestResponseBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, unlockRequestResponseBaseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_CANCEL_REQUEST_CONFIDENTIAL: //This is for cancel request for unlock confidential file /folder
                        CancelUnlockRequestResponseBaseModel cancelUnlockRequestResponseBaseModel = gson.fromJson(data, CancelUnlockRequestResponseBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, cancelUnlockRequestResponseBaseModel, mOldDataTag);
                        break;

                    case DMSConstants.TASK_ADMITTED_PATIENT_DATA: //This is for get admitted patient data
                        AdmittedPatientBaseModel admittedPatientBaseModel = gson.fromJson(data, AdmittedPatientBaseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, admittedPatientBaseModel, mOldDataTag);
                        break;

                    default:
                        //This is for get PDF PatientNameListData
                        if (mOldDataTag.startsWith(DMSConstants.TASK_GET_PDF_DATA)) {
                            GetPdfDataResponseModel getPdfDataResponseModel = gson.fromJson(data, GetPdfDataResponseModel.class);
                            this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, getPdfDataResponseModel, mOldDataTag);
                        }


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

//        if (mContext instanceof AppCompatActivity) {
//            ((AppCompatActivity) this.mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                        mProgressDialog.dismiss();
//                    }
//                }
//            });
//        }
//
//        RequestPool.getInstance(mContext)
//                .cancellAllPreviousRequestWithSameTag(requestTag);
//        mConnectionListener.onTimeout(this,mOldDataTag);

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
        String baseUrl = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
        String url = baseUrl + Config.URL_LOGIN;

        if (!(DMSConstants.BLANK.equalsIgnoreCase(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext)) &&
                DMSConstants.BLANK.equalsIgnoreCase(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.PASSWORD, mContext)))) {
            Map<String, String> headerParams = new HashMap<>();
            Device device = Device.getInstance(mContext);
            headerParams.put(DMSConstants.CONTENT_TYPE, DMSConstants.APPLICATION_URL_ENCODED);
            headerParams.put(DMSConstants.DEVICEID, device.getDeviceId());
            headerParams.put(DMSConstants.OS, device.getOS());
            headerParams.put(DMSConstants.DMS_OSVERSION, device.getOSVersion());
            headerParams.put(DMSConstants.DEVICE_TYPE, mContext.getResources().getString(R.string.device));
            CommonMethods.Log(TAG, "setHeaderParams:" + headerParams.toString());
            Map<String, String> postParams = new HashMap<String, String>();
            postParams.put(DMSConstants.GRANT_TYPE_KEY, DMSConstants.REFRESH_TOKEN);
            postParams.put(DMSConstants.REFRESH_TOKEN, DMSPreferencesManager.getString(DMSConstants.REFRESH_TOKEN, mContext));
            postParams.put(DMSConstants.CLIENT_ID_KEY, DMSConstants.CLIENT_ID_VALUE);
            CommonMethods.Log(TAG, "setPostParams:" + postParams.toString());

            stringRequest(url, Request.Method.POST, headerParams, postParams, true);
        } else {
            mConnectionListener.onResponse(ConnectionListener.PARSE_ERR0R, null, mOldDataTag);
        }
    }
}