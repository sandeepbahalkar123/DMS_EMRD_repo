package com.scorg.dms.helpers.myappointments;

import android.content.Context;

import com.android.volley.Request;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ConnectionListener;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.my_appointments.RequestAppointmentData;
import com.scorg.dms.model.my_appointments.request_cancel_or_complete_appointment.RequestAppointmentCancelModel;
import com.scorg.dms.model.my_patient_filter.LocationsRequest;
import com.scorg.dms.model.patient.add_new_patient.PatientDetail;
import com.scorg.dms.model.patient.add_new_patient.SyncPatientsRequest;
import com.scorg.dms.model.patient.doctor_patients.PatientList;
import com.scorg.dms.model.patient.template_sms.request_send_sms.ClinicListForSms;
import com.scorg.dms.model.patient.template_sms.request_send_sms.RequestSendSmsModel;
import com.scorg.dms.model.request_appointment_confirmation.RequestAppointmentConfirmationModel;
import com.scorg.dms.model.request_appointment_confirmation.Reschedule;
import com.scorg.dms.model.request_patients.RequestSearchPatients;
import com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list.RequestToAddWaitingList;
import com.scorg.dms.model.waiting_list.request_delete_waiting_list.RequestDeleteBaseModel;
import com.scorg.dms.model.waiting_list.request_drag_drop.RequestForDragAndDropBaseModel;
import com.scorg.dms.network.ConnectRequest;
import com.scorg.dms.network.ConnectionFactory;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.Config;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import static com.scorg.dms.util.DMSConstants.TASK_GET_TIME_SLOTS_TO_BOOK_APPOINTMENT;

/**
 * Created by jeetal on 31/1/18.
 */

public class AppointmentHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;

    public AppointmentHelper(Context context, HelperResponse loginActivity) {
        this.mContext = context;
        this.mHelperResponseManager = loginActivity;
    }

    @Override
    public void onResponse(int responseResult, CustomResponse customResponse, String mOldDataTag) {

        //CommonMethods.Log(TAG, customResponse.toString());
        switch (responseResult) {
            case ConnectionListener.RESPONSE_OK:
                mHelperResponseManager.onSuccess(mOldDataTag, customResponse);
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

    public void doGetAppointmentData(String userSelectedDate) {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_GET_APPOINTMENT_DATA, Request.Method.POST, true);
        RequestAppointmentData mRequestAppointmentData = new RequestAppointmentData();
        mRequestAppointmentData.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext)));
        mRequestAppointmentData.setDate(userSelectedDate);
        mConnectionFactory.setPostParams(mRequestAppointmentData);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_MY_APPOINTMENTS_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_APPOINTMENT_DATA);
    }


    public void doGetSearchResult(RequestSearchPatients mRequestSearchPatients, boolean isProgressShow) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, isProgressShow, DMSConstants.TASK_GET_SEARCH_RESULT_MY_PATIENT, Request.Method.POST, true);
        mConnectionFactory.setPostParams(mRequestSearchPatients);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_MY_PATIENTS_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_SEARCH_RESULT_MY_PATIENT);
    }

    public void doGetDoctorTemplate() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_GET_DOCTOR_SMS_TEMPLATE, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_SMS_TEMPLATE + Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext)));
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_DOCTOR_SMS_TEMPLATE);
    }

    public void doRequestSendSMS(ArrayList<ClinicListForSms> clinicListForSms) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_REQUEST_SEND_SMS, Request.Method.POST, true);
        RequestSendSmsModel mRequestSendSmsModel = new RequestSendSmsModel();
        mRequestSendSmsModel.setClinicListForSms(clinicListForSms);
        mConnectionFactory.setPostParams(mRequestSendSmsModel);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.REQUEST_SEND_SMS);
        mConnectionFactory.createConnection(DMSConstants.TASK_REQUEST_SEND_SMS);
    }

    public void doGetWaitingList() {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_GET_WAITING_LIST, Request.Method.POST, true);
        RequestAppointmentData mRequestAppointmentData = new RequestAppointmentData();
        mRequestAppointmentData.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext)));
        String date = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH);
        mRequestAppointmentData.setDate(date);
        //mRequestAppointmentData.setDate("16/08/2018"); // TODO, ADDED FOR DEVELOPMENT, REMOVE IT LATER ON.
        mConnectionFactory.setPostParams(mRequestAppointmentData);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_WAITING_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_WAITING_LIST);
    }

    public void doAddToWaitingListFromMyPatients(RequestToAddWaitingList mRequestForWaitingListPatients) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_ADD_TO_WAITING_LIST, Request.Method.POST, true);
        mConnectionFactory.setPostParams(mRequestForWaitingListPatients);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.ADD_TO_WAITING_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_ADD_TO_WAITING_LIST);
    }

    public void doDeleteWaitingList(RequestDeleteBaseModel mRequestDeleteBaseModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_DELETE_WAITING_LIST, Request.Method.POST, true);
        mConnectionFactory.setPostParams(mRequestDeleteBaseModel);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.DELETE_WAITING_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_DELETE_WAITING_LIST);
    }

    public void doAppointmentCancelOrComplete(RequestAppointmentCancelModel mRequestAppointmentCancelModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_APPOINTMENT_CANCEL_OR_COMPLETE, Request.Method.POST, true);
        mConnectionFactory.setPostParams(mRequestAppointmentCancelModel);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.CANCEL_OR_COMPLETE_APPOINTMENT);
        mConnectionFactory.createConnection(DMSConstants.TASK_APPOINTMENT_CANCEL_OR_COMPLETE);
    }

    public void doDargAndDropApi(RequestForDragAndDropBaseModel requestForDragAndDropBaseModel) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_DARG_DROP, Request.Method.POST, true);
        mConnectionFactory.setPostParams(requestForDragAndDropBaseModel);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.DRAG_AND_DROP_API);
        mConnectionFactory.createConnection(DMSConstants.TASK_DARG_DROP);
    }

    public void doGetCompletedOpdList() {
        String date = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.YYYY_MM_DD);
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_GET_COMPLETED_OPD, Request.Method.POST, true);
        RequestAppointmentData mRequestAppointmentData = new RequestAppointmentData();
        mRequestAppointmentData.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext)));
        mRequestAppointmentData.setDate(date);
        mConnectionFactory.setPostParams(mRequestAppointmentData);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_COMPELTED_OPD_URL);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_COMPLETED_OPD);
    }

    public void doGetNewPatientList() {
        String date = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.YYYY_MM_DD);
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_GET_NEW_PATIENT_LIST, Request.Method.POST, true);
        RequestAppointmentData mRequestAppointmentData = new RequestAppointmentData();
        mRequestAppointmentData.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext)));
        mRequestAppointmentData.setDate(date);
        mConnectionFactory.setPostParams(mRequestAppointmentData);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_NEW_PATIENTS_URL);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_NEW_PATIENT_LIST);
    }

    public void getFilterLocationList() {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_GET_DOCTOR_PATIENT_CITY, Request.Method.POST, true);
        LocationsRequest locationsRequest = new LocationsRequest();
        locationsRequest.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext)));
        mConnectionFactory.setPostParams(locationsRequest);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.GET_DOCTOR_PATIENT_CITY);
        mConnectionFactory.createConnection(DMSConstants.TASK_GET_DOCTOR_PATIENT_CITY);
    }

    public void getTimeSlotToBookAppointmentWithDoctor(String docId, int locationID, String date, boolean isReqDoctorData, int patientID) {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, TASK_GET_TIME_SLOTS_TO_BOOK_APPOINTMENT, Request.Method.GET, true);
        mConnectionFactory.setHeaderParams();

        String currentTimeStamp = CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.HH_mm);

        String url = Config.TIME_SLOT_TO_BOOK_APPOINTMENT + "docId=" + docId + "&locationId=" + locationID + "&date=" + date + "&time=" + currentTimeStamp + "&patientId=" + patientID + "&docDetailReq=" + isReqDoctorData;

        mConnectionFactory.setUrl(url);
        mConnectionFactory.createConnection(TASK_GET_TIME_SLOTS_TO_BOOK_APPOINTMENT);//DMSConstants.TASK_TIME_SLOT_TO_BOOK_APPOINTMENT

    }

    public void doConfirmAppointmentRequest(int docId, int locationID, String date, String fromTime, String toTime, int slotId, Reschedule reschedule, int patientID) {
        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_CONFIRM_APPOINTMENT, Request.Method.POST, false);
        mConnectionFactory.setHeaderParams();

        RequestAppointmentConfirmationModel mRequestAppointmentConfirmationModel = new RequestAppointmentConfirmationModel();
        mRequestAppointmentConfirmationModel.setDocId(docId);
        mRequestAppointmentConfirmationModel.setFromTime(fromTime);
        mRequestAppointmentConfirmationModel.setLocationId(locationID);
        mRequestAppointmentConfirmationModel.setToTime(toTime);
        mRequestAppointmentConfirmationModel.setDate(date);
        mRequestAppointmentConfirmationModel.setSlotId(slotId);
        mRequestAppointmentConfirmationModel.setReschedule(reschedule);
        mRequestAppointmentConfirmationModel.setPatientId(patientID);
        mConnectionFactory.setPostParams(mRequestAppointmentConfirmationModel);
        mConnectionFactory.setUrl(Config.CONFIRM_APPOINTMENT);
        mConnectionFactory.createConnection(DMSConstants.TASK_CONFIRM_APPOINTMENT);

    }

    public void addNewPatient(PatientList dataToAdd) {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_ADD_NEW_PATIENT, Request.Method.POST, false);
        PatientDetail patient = new PatientDetail();
        patient.setMobilePatientId(dataToAdd.getPatientId());

        //---------
        String[] split = dataToAdd.getPatientName().split(" ");
        patient.setPatientFname(split[0]);
        if (split.length > 1) {
            if (split[1].equalsIgnoreCase("|"))
                patient.setPatientMname("");
            else
                patient.setPatientMname(split[1]);
        }
        if (split.length > 2)
            patient.setPatientLname(split[2]);
        //---------
        patient.setPatientPhone(dataToAdd.getPatientPhone());
        patient.setPatientAge(dataToAdd.getAge());
        patient.setPatientGender(dataToAdd.getGender());
        patient.setClinicId(dataToAdd.getClinicId());
        patient.setPatientDob(dataToAdd.getDateOfBirth());
        patient.setOfflineReferenceID(dataToAdd.getReferenceID());

        SyncPatientsRequest mSyncPatientsRequest = new SyncPatientsRequest();
        String id = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext);
        mSyncPatientsRequest.setDocId(id);

        ArrayList<PatientDetail> add = new ArrayList<PatientDetail>();
        add.add(patient);
        mSyncPatientsRequest.setPatientDetails(add);

        mConnectionFactory.setPostParams(mSyncPatientsRequest);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.ADD_PATIENTS_SYNC);
        mConnectionFactory.createConnection(DMSConstants.TASK_ADD_NEW_PATIENT);
    }

}


