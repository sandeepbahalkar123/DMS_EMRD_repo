package com.rescribe.doctor.dms.network;

/**
 * @author Sandeep Bahalkar
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.rescribe.doctor.dms.interfaces.ConnectionListener;
import com.rescribe.doctor.dms.interfaces.Connector;
import com.rescribe.doctor.dms.interfaces.CustomResponse;
import com.rescribe.doctor.dms.model.responsemodel.annotationlistresponsemodel.AnnotationListResponseModel;
import com.rescribe.doctor.dms.model.responsemodel.filetreeresponsemodel.FileTreeResponseModel;
import com.rescribe.doctor.dms.model.responsemodel.getpdfdataresponsemodel.GetPdfDataResponseModel;
import com.rescribe.doctor.dms.model.responsemodel.iptestresponsemodel.IpTestResponseModel;
import com.rescribe.doctor.dms.model.responsemodel.loginresponsemodel.LoginResponseModel;
import com.rescribe.doctor.dms.model.responsemodel.patientnamelistresponsemodel.PatientNameListResponseModel;
import com.rescribe.doctor.dms.model.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.rescribe.doctor.dms.preference.DmsPreferencesManager;
import com.rescribe.doctor.dms.util.CommonMethods;
import com.rescribe.doctor.dms.util.Config;
import com.rescribe.doctor.dms.util.DmsConstants;
import com.rescribe.doctor.dms.util.NetworkUtil;
import com.rescribe.doctor.dms.views.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestManager extends ConnectRequest implements Connector, RequestTimer.RequestTimerListener {
    private static final String TAG = "DMS/RequestManager";
    private static final int CONNECTION_TIME_OUT = 1000 * 60;
    private static final int N0OF_RETRY = 0;
    private String requestTag;
    private int connectionType = Request.Method.POST;

    private String mDataTag;
    private RequestTimer requestTimer;
    private JsonObjectRequest jsonRequest;
    private StringRequest stringRequest;

    public RequestManager(Context mContext, ConnectionListener connectionListener, String dataTag, View viewById, boolean isProgressBarShown, String mOldDataTag, int connectionType) {
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
                jsonRequest();
            } else {
                jsonRequest();
            }
        } else {

            mConnectionListener.onResponse(ConnectionListener.NO_INTERNET, null, mOldDataTag);

            if (mViewById != null)
                CommonMethods.showSnack(mViewById, mContext.getString(R.string.internet));
            else
                CommonMethods.showToast(mContext, mContext.getString(R.string.internet));
        }
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
                        successResponse(response.toString(), false);
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
        jsonRequest.setRetryPolicy(new

                DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        jsonRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).

                addToRequestQueue(jsonRequest);


    }

    private void stringRequest(String url, int connectionType, final Map<String, String> headerParams, final Map<String, String> postParams, final boolean isTokenExpired) {

        // ganesh for string request and delete method with string request

        stringRequest = new StringRequest(connectionType, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        successResponse(response, isTokenExpired);
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

        stringRequest.setRetryPolicy(new

                DefaultRetryPolicy(CONNECTION_TIME_OUT, N0OF_RETRY,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)

        );
        stringRequest.setTag(requestTag);
        requestTimer.start();
        RequestPool.getInstance(this.mContext).

                addToRequestQueue(stringRequest);

        //

    }

    private void successResponse(String response, boolean isTokenExpired) {
        requestTimer.cancel();
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        parseJson(fixEncoding(response), isTokenExpired);
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
                    if (!isTokenExpired) {
                        tokenRefreshRequest();
                    }
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

                    /*String mServerPath = DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH,mContext);
                    String isValidConfig = DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG,mContext);
                    DmsPreferencesManager.clearSharedPref(mContext);
                    DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH,mServerPath,mContext);
                    DmsPreferencesManager.putString(DmsPreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG,isValidConfig,mContext);
                    Intent intent = new Intent(mContext, SplashScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);*/

                    // ganesh

                    loginRequest();

                } else
                    mConnectionListener.onResponse(ConnectionListener.SERVER_ERROR, null, mOldDataTag);
            } else if (error instanceof NetworkError) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
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

            Log.e(TAG, data);

            Gson gson = new Gson();

            if (isTokenExpired) {
                // This success response is for refresh token
                LoginResponseModel loginResponseModel = gson.fromJson(data, LoginResponseModel.class);
                DmsPreferencesManager.putString(DmsConstants.ACCESS_TOKEN, loginResponseModel.getAccessToken(), mContext);
                DmsPreferencesManager.putString(DmsConstants.TOKEN_TYPE, loginResponseModel.getTokenType(), mContext);
                DmsPreferencesManager.putString(DmsConstants.REFRESH_TOKEN, loginResponseModel.getRefreshToken(), mContext);

                String authorizationString = loginResponseModel.getTokenType()
                        + " " + loginResponseModel.getAccessToken();

                mHeaderParams.put(DmsConstants.AUTHORIZATION, authorizationString);

                connect();
            } else {
                // This success response is for respective api's

                switch (this.mDataTag) {
                    case DmsConstants.REGISTRATION_CODE: //This is sample code
//                    RegistrationModel  registrationModel = gson.fromJson(data, RegistrationModel.class);
//                    this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, registrationModel, mOldDataTag);
                        break;
                    case DmsConstants.TASK_LOGIN_CODE: //This is for login
                        LoginResponseModel loginResponseModel = gson.fromJson(data, LoginResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, loginResponseModel, mOldDataTag);
                        break;
                    case DmsConstants.TASK_PATIENT_LIST: //This is for patient list
                        ShowSearchResultResponseModel showSearchResultResponseModel = gson.fromJson(data, ShowSearchResultResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, showSearchResultResponseModel, mOldDataTag);
                        break;
                    case DmsConstants.TASK_ANNOTATIONS_LIST: //This is for annotation list
                        AnnotationListResponseModel annotationListResponseModel = gson.fromJson(data, AnnotationListResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, annotationListResponseModel, mOldDataTag);
                        break;
                    case DmsConstants.TASK_GET_ARCHIVED_LIST: //This is for get archived list
                        FileTreeResponseModel fileTreeResponseModel = gson.fromJson(data, FileTreeResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, fileTreeResponseModel, mOldDataTag);
                        break;

                    case DmsConstants.TASK_CHECK_SERVER_CONNECTION: //This is for get archived list
                        IpTestResponseModel ipTestResponseModel = gson.fromJson(data, IpTestResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, ipTestResponseModel, mOldDataTag);
                        break;
                    case DmsConstants.TASK_GET_PATIENT_NAME_LIST: //This is for get archived list
                        PatientNameListResponseModel patientNameListResponseModel = gson.fromJson(data, PatientNameListResponseModel.class);
                        this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, patientNameListResponseModel, mOldDataTag);
                        break;

                    default:
                        //This is for get PDF PatientNameListData
                        if (mOldDataTag.startsWith(DmsConstants.TASK_GET_PDF_DATA)) {
                            GetPdfDataResponseModel getPdfDataResponseModel = gson.fromJson(data, GetPdfDataResponseModel.class);
                            this.mConnectionListener.onResponse(ConnectionListener.RESPONSE_OK, getPdfDataResponseModel, mOldDataTag);
                        }

                }
            }

        } catch (JsonSyntaxException e) {
            Log.d(TAG, "JsonException" + e.getMessage());
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
        String url = DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext) + Config.URL_LOGIN;
        CommonMethods.Log(TAG, "Refersh token while sending refresh token api: " + DmsPreferencesManager.getString(DmsConstants.REFRESH_TOKEN, mContext));
        Map<String, String> headerParams = new HashMap<>();
        headerParams.putAll(mHeaderParams);
        headerParams.remove(DmsConstants.CONTENT_TYPE);
        headerParams.put(DmsConstants.CONTENT_TYPE, DmsConstants.APPLICATION_URL_ENCODED);

        Map<String, String> postParams = new HashMap<>();
        postParams.put(DmsConstants.GRANT_TYPE_KEY, DmsConstants.REFRESH_TOKEN);
        postParams.put(DmsConstants.REFRESH_TOKEN, DmsPreferencesManager.getString(DmsConstants.REFRESH_TOKEN, mContext));
        postParams.put(DmsConstants.CLIENT_ID_KEY, DmsConstants.CLIENT_ID_VALUE);
        stringRequest(url, Request.Method.POST, headerParams, postParams, true);
    }

    private void loginRequest() {
        String url = DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext) + Config.URL_LOGIN;
        CommonMethods.Log(TAG, "Refersh token while sending refresh token api: " + DmsPreferencesManager.getString(DmsConstants.REFRESH_TOKEN, mContext));
        Map<String, String> headerParams = new HashMap<>();
        headerParams.putAll(mHeaderParams);
        headerParams.remove(DmsConstants.CONTENT_TYPE);
        headerParams.put(DmsConstants.CONTENT_TYPE, DmsConstants.APPLICATION_URL_ENCODED);
        Map<String, String> postParams = new HashMap<String, String>();
        postParams.put(DmsConstants.GRANT_TYPE_KEY, DmsConstants.PASSWORD);
        postParams.put(DmsConstants.USERNAME, DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext));
        postParams.put(DmsConstants.PASSWORD, DmsPreferencesManager.getString(DmsPreferencesManager.DMS_PREFERENCES_KEY.PASSWORD, mContext));
        postParams.put(DmsConstants.CLIENT_ID_KEY, DmsConstants.CLIENT_ID_VALUE);
        stringRequest(url, Request.Method.POST, headerParams, postParams, true);
    }

}