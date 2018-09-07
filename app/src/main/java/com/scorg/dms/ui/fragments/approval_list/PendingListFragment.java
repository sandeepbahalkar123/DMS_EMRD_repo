package com.scorg.dms.ui.fragments.approval_list;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.adapters.pending_approvals.RequestListAdapter;
import com.scorg.dms.helpers.pending_approval.PendingApprovalHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResultData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.model.pending_approval_list.CancelUnlockRequestResponseBaseModel;
import com.scorg.dms.model.pending_approval_list.PendingRequestCancelModel;
import com.scorg.dms.model.pending_approval_list.RequestedArchivedBaseModel;
import com.scorg.dms.model.pending_approval_list.RequestedArchivedDetailList;
import com.scorg.dms.model.waiting_list.WaitingPatientData;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.ui.activities.SplashScreenActivity;
import com.scorg.dms.ui.activities.dms_patient_list.FileTypeViewerActivity;
import com.scorg.dms.ui.activities.dms_patient_list.PatientDetailsActivity;
import com.scorg.dms.ui.activities.pending_approval_list.RequestedArchivedMainListActivity;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.scorg.dms.util.DMSConstants.PATIENT_DETAILS;

/**
 * Created by jeetal on 22/2/18.
 */
@RuntimePermissions
public class PendingListFragment extends Fragment implements RequestListAdapter.OnItemClickListener, HelperResponse {

    @BindView(R.id.clinicListSpinner)
    Spinner clinicListSpinner;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.bulletImageView)
    CircularImageView bulletImageView;
    @BindView(R.id.clinicNameTextView)
    CustomTextView clinicNameTextView;
    @BindView(R.id.clinicAddress)
    CustomTextView clinicAddress;
    @BindView(R.id.hospitalDetailsLinearLayout)
    RelativeLayout hospitalDetailsLinearLayout;

    @BindView(R.id.noRecords)
    LinearLayout noRecords;

    private Unbinder unbinder;


    private ArrayList<RequestedArchivedDetailList> requestedArchivedDetailList;
    private RequestedArchivedMainListActivity mParentActivity;
    private RequestListAdapter mPendingListAdapter;
    private long mClickedPhoneNumber;
    private PendingApprovalHelper mPendingApprovalHelper;

    public PendingListFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unbind
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.waiting_content_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        mParentActivity = (RequestedArchivedMainListActivity) getActivity();
        mPendingApprovalHelper = new PendingApprovalHelper(mParentActivity, this);
        mPendingApprovalHelper.doGetPendingApprovalData(1, true);

    }


    public static PendingListFragment newInstance(Bundle bundle) {
        PendingListFragment fragment = new PendingListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onRequestPermssionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // ActivePatientListFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
//        WaitingListDataModel waitingListDataModel = mParentActivity.getWaitingListDataModel();
//        waitingPatientTempList = compare(waitingListDataModel.getWaitingPatientDataList());
//
//        mWaitingClinicLists = waitingListDataModel.getWaitingClinicList();
//        if (mWaitingClinicLists.size() > 1) {
//            clinicListSpinner.setVisibility(View.VISIBLE);
//            hospitalDetailsLinearLayout.setVisibility(View.GONE);
//            WaitingListSpinnerAdapter mWaitingListSpinnerAdapter = new WaitingListSpinnerAdapter(getActivity(), mWaitingClinicLists);
//            clinicListSpinner.setAdapter(mWaitingListSpinnerAdapter);
//        }
//        if (!mWaitingClinicLists.isEmpty()) {
//            clinicListSpinner.setVisibility(View.GONE);
//            hospitalDetailsLinearLayout.setVisibility(View.GONE);
//            clinicNameTextView.setText(mWaitingClinicLists.get(0).getHosName() + " - ");
//            clinicAddress.setText(mWaitingClinicLists.get(0).getHosAddress1());
//            mRecyclerView.setVisibility(View.VISIBLE);
//            mRecyclerView.setClipToPadding(false);
//            setAdapter();
//        }
    }

    private void setAdapter() {

        if (requestedArchivedDetailList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            noRecords.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noRecords.setVisibility(View.GONE);
            mPendingListAdapter = new RequestListAdapter(this.getContext(), requestedArchivedDetailList, this, true);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearlayoutManager);
            mRecyclerView.setAdapter(mPendingListAdapter);
        }
    }

    @Override
    public void onItemClick(WaitingPatientData clickItem) {


        Intent intent = new Intent(getActivity(), FileTypeViewerActivity.class);
        Bundle extra = new Bundle();
        //ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
        //dataToSend.add(childElement);
        //  SearchResult searchPatientInformation = patientExpandableListAdapter.searchPatientInfo("" + groupHeader.getPatientId());
        //todo: filepath(pdf url is not getting in api)
        // extra.putSerializable(getString(R.string.compare), dataToSend);
        // extra.putSerializable(getString(R.string.compare), new ArrayList<PatientFileData>());

        extra.putString(DMSConstants.PATIENT_ADDRESS, clickItem.getPatAddress());
        extra.putString(DMSConstants.DOCTOR_NAME, "");
        extra.putString(DMSConstants.PATIENT_ID, clickItem.getPatientId());
        extra.putString(DMSConstants.PAT_ID, clickItem.getPatId());

        extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + clickItem.getPatientName());
        intent.putExtra(DMSConstants.DATA, extra);
        startActivity(intent);

    }


    @Override
    public void onPhoneNoClick(long phoneNumber) {
        mClickedPhoneNumber = phoneNumber;
        //ActivePatientListFragmentPermissionsDispatcher.doCallSupportWithCheck(this);
    }

    @Override
    public void onClickedOfCancelRequestButton(final RequestedArchivedDetailList archivedDetailLists) {

        final Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_alert);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.textview_sucess)).setText(getContext().getResources().getString(R.string.do_you_want_to_cancel));
        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PendingRequestCancelModel requestCancelModel = new PendingRequestCancelModel();
                requestCancelModel.setRequestId(String.valueOf(archivedDetailLists.getRequestID()));
                requestCancelModel.setWorkFlowId("3");// this value will be get in API
                requestCancelModel.setStatusName("CANCEL");
                requestCancelModel.setStageId(String.valueOf(archivedDetailLists.getCurrentStageID()));
                requestCancelModel.setComments("");
                requestCancelModel.setValidity("");
                requestCancelModel.setIsWorkFlowProcess("true");
                requestCancelModel.setStartDate("");
                requestCancelModel.setEnddate("");
                requestCancelModel.setListOfDates("");
                requestCancelModel.setFileRefId(archivedDetailLists.getFileRefID());
                mPendingApprovalHelper.cancelRequest(requestCancelModel);
            }
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mClickedPhoneNumber));
        startActivity(callIntent);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case DMSConstants.TASK_PATIENT_LIST: {
                ShowSearchResultResponseModel showSearchResultResponseModel = (ShowSearchResultResponseModel) customResponse;
                SearchResultData searchResultData = showSearchResultResponseModel.getSearchResultData();

                if (searchResultData != null) {
                    List<SearchResult> searchResultList = searchResultData.getSearchResult();
                    if (!searchResultList.isEmpty()) {
                        SearchResult searchPatientInformation = searchResultList.get(0);
                        //TODO : as API response chnaged, hence need to fix this too.
                        /*List<PatientFileData> patientFileDataList = searchPatientInformation.getPatientFileData();
                        if (patientFileDataList != null) {
                            if (!patientFileDataList.isEmpty()) {
                                PatientFileData childElement = patientFileDataList.get(0);
                                Intent intent = new Intent(getActivity(), FileTypeViewerActivity.class);
                                Bundle extra = new Bundle();
                                ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
                                dataToSend.add(childElement);
                                extra.putSerializable(getString(R.string.compare), dataToSend);
                                extra.putString(DMSConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
                                extra.putString(DMSConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
                                extra.putString(DMSConstants.ID, childElement.getRespectiveParentPatientID());
                                extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + searchPatientInformation.getPatientName());
                                intent.putExtra(DMSConstants.DATA, extra);
                                startActivity(intent);
                            }
                        }*/
                    }
                }
            }
            break;
            case DMSConstants.TASK_PENDING_APPROVAL_LIST: {
                if (customResponse != null) {
                    RequestedArchivedBaseModel requestedArchivedBaseModel = (RequestedArchivedBaseModel) customResponse;
                    requestedArchivedDetailList = (ArrayList<RequestedArchivedDetailList>) requestedArchivedBaseModel.getPendingApprovalDataModel().getRequestedArchivedDetailList();
                    setAdapter();
                }

            }
            break;
            case DMSConstants.TASK_CANCEL_REQUEST_CONFIDENTIAL: {
                if (customResponse != null) {
                    CancelUnlockRequestResponseBaseModel cancelUnlockRequestResponseBaseModel = (CancelUnlockRequestResponseBaseModel) customResponse;
                    if (cancelUnlockRequestResponseBaseModel.getCommon().getStatusCode() == 200)
                        CommonMethods.showToast(getActivity(), "Successfully Canceled");
                    mPendingApprovalHelper.doGetPendingApprovalData(1, true);
                }

            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

}