package com.scorg.dms.helpers.patient_list;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.requestmodel.archive.GetArchiveRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.archive.RaiseUnlockRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.showfile_data.GetEncryptedPDFRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;

/**
 * Created by riteshpandhurkar on 1/3/17.
 */

public class DMSPatientsHelper implements ConnectionListener {

    private String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;

    public DMSPatientsHelper(Context context, HelperResponse loginActivity1) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity1;
    }

    //-- TO get Patient list from server
    public void doGetPatientList(ShowSearchResultRequestModel showSearchResultRequestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_PATIENT_LIST, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(showSearchResultRequestModel);
        mConnectionFactory.setUrl(Config.URL_PATIENT_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_PATIENT_LIST);
    }


    public void doGetAllAnnotations() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, DMSConstants.TASK_ANNOTATIONS_LIST, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.URL_ANNOTATIONS_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_ANNOTATIONS_LIST);
    }

    public void doGetArchivedList(GetArchiveRequestModel fileTreeRequestModel) {


        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, DMSConstants.TASK_GET_ARCHIVED_LIST, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(fileTreeRequestModel);

        mConnectionFactory.setUrl(Config.URL_GET_ARCHIVED_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_ARCHIVED_LIST);
    }


    public void raiseUnlockRequestArchivedFile(RaiseUnlockRequestModel unlockRequestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_RAISE_REQUEST_CONFIDENTIAL, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(unlockRequestModel);
        mConnectionFactory.setUrl(Config.URL_RAISE_REQUEST_CONFIDENTIAL);
        mConnectionFactory.createConnection(DMSConstants.TASK_RAISE_REQUEST_CONFIDENTIAL);
    }


    public void getPdfData(GetEncryptedPDFRequestModel getEncryptedPDFRequestModel, String taskID) {

        //---------------
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, taskID, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(getEncryptedPDFRequestModel);
        mConnectionFactory.setUrl(Config.URL_GET_PDF_DATA);
        mConnectionFactory.createConnection(taskID);
    }


    //-------------------------
    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:

                if (String.valueOf(mOldDataTag).equalsIgnoreCase("" + DMSConstants.TASK_GET_ARCHIVED_LIST)) {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                } else {
                    mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                }
                break;

            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, "parse error");
                mHelperResponseManager.onParseError(mOldDataTag, mContext.getString(R.string.something_went_wrong_error));
                break;

            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, "server error");
                mHelperResponseManager.onServerError(mOldDataTag, mContext.getString(R.string.something_went_wrong_error));
                break;
            case ConnectionListener.TIMEOUT_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.timeout_error));
                mHelperResponseManager.onTimeOutError(mOldDataTag, mContext.getString(R.string.timeout_error));
                break;
            case ConnectionListener.NO_INTERNET:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            case ConnectionListener.NO_CONNECTION_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.no_connection_error));
                mHelperResponseManager.onNoConnectionError(mOldDataTag, mContext.getString(R.string.no_connection_error));
                break;
            default:
                mHelperResponseManager.onParseError(mOldDataTag, mContext.getString(R.string.something_went_wrong_error));
                CommonMethods.Log(TAG, "default error");
                break;

        }

    }

    @Override
    public void onTimeout(ConnectRequest request, String mOldDataTag) {

    }

    public void doGetPatientNameList(String enteredPatientName) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, false, DMSConstants.TASK_GET_PATIENT_NAME_LIST, Request.Method.POST, false);

        SearchPatientNameText s = new SearchPatientNameText();
        s.setSearchPatientName(enteredPatientName);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(s);
        mConnectionFactory.setUrl(Config.URL_PATIENT_NAME_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_PATIENT_NAME_LIST);
    }

    public void doGetPatientEpisodeList(ShowSearchResultRequestModel showSearchResultRequestModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_GET_EPISODE_LIST, Request.Method.POST, false);

        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setPostParams(showSearchResultRequestModel);
        mConnectionFactory.setUrl(Config.GET_EPISODE_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_EPISODE_LIST);
    }

    //--- Ideally, this method should be of type GET, but made as POST.
    // Creating separate class for single used is not proper, hence made it inner class.
    public class SearchPatientNameText implements CustomResponse {
        @SerializedName("searchPatientName")
        @Expose
        private String searchPatientName;

        public String getSearchPatientName() {
            return searchPatientName;
        }

        public void setSearchPatientName(String searchPatientName) {
            this.searchPatientName = searchPatientName;
        }
    }

    //---------------
    public class Test implements CustomResponse {
        @SerializedName("patId")
        @Expose
        private String patId = "200145";

        public String getPatId() {
            return this.patId;
        }

        public void setPatId(String patId) {
            this.patId = patId;
        }
    }
    //---------------

}
