package com.scorg.dms.helpers.patient_detail;

import android.content.Context;

import com.android.volley.Request;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.case_details.CaseDetailsModel;
import com.scorg.dms.model.case_details.VisitCommonData;
import com.scorg.dms.model.patient.delete_attachment_req_model.DeleteAttachmentReqModel;
import com.scorg.dms.model.patient.patient_history.PatientHistoryBaseModel;
import com.scorg.dms.model.patient.patient_history.PatientHistoryDataModel;
import com.scorg.dms.model.patient.patient_history.PatientHistoryInfo;
import com.scorg.dms.model.patient.patient_history.PatientHistoryInfoMonthContainer;
import com.scorg.dms.model.patient.patient_history.RequestForPatientHistory;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class PatientDetailHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;
    private Map<String, Map<String, ArrayList<PatientHistoryInfo>>> yearWiseSortedPatientHistoryInfo = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    public PatientDetailHelper(Context context, HelperResponse oneDayVisitActivity) {
        this.mContext = context;
        this.mHelperResponseManager = oneDayVisitActivity;
    }


    public Map<String, Map<String, ArrayList<PatientHistoryInfo>>> getYearWiseSortedPatientHistoryInfo() {
        return yearWiseSortedPatientHistoryInfo;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                switch (mOldDataTag) {
                    case DMSConstants.TASK_ONE_DAY_VISIT:
                        CaseDetailsModel model = (CaseDetailsModel) customResponse;
                        mHelperResponseManager.onSuccess(mOldDataTag, model.getData());
                        break;
                    case DMSConstants.TASK_PATIENT_HISTORY: {

                        PatientHistoryBaseModel newBaseModel = (PatientHistoryBaseModel) customResponse;
                        PatientHistoryDataModel dataModel = newBaseModel.getPatientHistoryDataModel();
                        PatientHistoryInfoMonthContainer patientHistoryInfoMonthContainer = dataModel.getPatientHistoryInfoMonthContainer();

                        if (patientHistoryInfoMonthContainer != null) {
                            Map<String, ArrayList<PatientHistoryInfo>> monthWiseSortedPatientHistory = patientHistoryInfoMonthContainer.getMonthWiseSortedPatientHistory();
                            if (monthWiseSortedPatientHistory.size() > 0)
                                yearWiseSortedPatientHistoryInfo.put(patientHistoryInfoMonthContainer.getYear(), monthWiseSortedPatientHistory);
                        }
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
                    }
                    break;
                    default:
                        mHelperResponseManager.onSuccess(mOldDataTag, customResponse);

                }
                break;
            case ConnectionListener.PARSE_ERR0R:
                CommonMethods.Log(TAG, mContext.getString(R.string.parse_error));
                mHelperResponseManager.onParseError(mOldDataTag, mContext.getString(R.string.parse_error));
                break;
            case ConnectionListener.SERVER_ERROR:
                CommonMethods.Log(TAG, mContext.getString(R.string.server_error));
                mHelperResponseManager.onServerError(mOldDataTag, mContext.getString(R.string.server_error));
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
                CommonMethods.Log(TAG, mContext.getString(R.string.default_error));
                break;
        }
    }

    @Override
    public void onTimeout(ConnectRequest request) {

    }

    //get case study list
    public void doGetOneDayVisit(String opdId, String patientID) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_ONE_DAY_VISIT, Request.Method.GET, false);
        mConnectionFactory.setHeaderParams();
        String docId = (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext));
        mConnectionFactory.setUrl(Config.ONE_DAY_VISIT_URL + opdId + "&patientId=" + patientID + "&docId=" + docId);
        mConnectionFactory.createConnection(DMSConstants.TASK_ONE_DAY_VISIT);
       /* try {
            InputStream is = mContext.getAssets().open("patient_details.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            CaseDetailsModel data = new Gson().fromJson(json, CaseDetailsModel.class);
            onResponse(ConnectionListener.RESPONSE_OK,data,DMSConstants.TASK_ONE_DAY_VISIT);

        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }

    public void doGetPatientHistory(String patientID, String year, boolean getPatientInfo, String hospitalPatid) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_PATIENT_HISTORY, Request.Method.POST, false);
        RequestForPatientHistory mRequestForPatientHistory = new RequestForPatientHistory();
        mRequestForPatientHistory.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext)));
        String date = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.YYYY_MM_DD);
        mRequestForPatientHistory.setPatientId(Integer.valueOf(patientID));
        mRequestForPatientHistory.setHospitalPatId(Integer.valueOf(hospitalPatid));
        mRequestForPatientHistory.setYear(year);
        mRequestForPatientHistory.setGetPatientInfo(getPatientInfo);
        mConnectionFactory.setPostParams(mRequestForPatientHistory);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_PATIENT_HISTORY);
        mConnectionFactory.createConnection(DMSConstants.TASK_PATIENT_HISTORY);
    }

    //get case study list
    public void deleteSelectedAttachments(HashSet<VisitCommonData> list, String patientID) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_DELETE_PATIENT_OPD_ATTCHMENTS, Request.Method.POST, false);

        ArrayList<DeleteAttachmentReqModel.AttachmentData> deleteAttList = new ArrayList<>();
        DeleteAttachmentReqModel delete = new DeleteAttachmentReqModel();

        for (VisitCommonData s : list) {
            DeleteAttachmentReqModel.AttachmentData i = new DeleteAttachmentReqModel.AttachmentData();
            i.setId("" + s.getId());
            String url = s.getUrl();
            i.setFileName("" + url.substring(url.lastIndexOf('/') + 1, url.length()));
            i.setPatientId(Integer.parseInt(patientID));
            deleteAttList.add(i);
        }
        delete.setAttachmentDetails(deleteAttList);

        mConnectionFactory.setPostParams(delete);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.DELETE_PATIENT_OPD_ATTCHMENTS);
        mConnectionFactory.createConnection(DMSConstants.TASK_DELETE_PATIENT_OPD_ATTCHMENTS);
    }
}

