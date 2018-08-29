package com.scorg.dms.helpers.pending_approval;

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
import com.scorg.dms.model.pending_approval_list.RequestPendingApprovalData;
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



public class PendingApprovalHelper implements ConnectionListener {

    private String TAG = this.getClass().getName();
    private Context mContext;
    private HelperResponse mHelperResponseManager;

    public PendingApprovalHelper(Context context, HelperResponse loginActivity) {
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

    public void doGetPendingApprovalData(int pageNo,boolean isPending) {

        ConnectionFactory mConnectionFactory = new ConnectionFactory(mContext, this, null, true, DMSConstants.TASK_PENDING_APPROVAL_LIST, Request.Method.POST, true);
        RequestPendingApprovalData mRequestPendingApprovalData = new RequestPendingApprovalData();
        mRequestPendingApprovalData.setPageNo(pageNo);
        mRequestPendingApprovalData.setPending(isPending);
        mConnectionFactory.setPostParams(mRequestPendingApprovalData);
        mConnectionFactory.setHeaderParams();
        mConnectionFactory.setUrl(Config.URL_PENDING_APPROVAL_LIST);
        mConnectionFactory.createConnection(DMSConstants.TASK_PENDING_APPROVAL_LIST);
    }

}


