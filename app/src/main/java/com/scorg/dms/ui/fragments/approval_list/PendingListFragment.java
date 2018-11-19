package com.scorg.dms.ui.fragments.approval_list;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.adapters.pending_approvals.RequestListAdapter;
import com.scorg.dms.helpers.pending_approval.PendingApprovalHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResultData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.model.pending_approval_list.CancelUnlockRequestResponseBaseModel;
import com.scorg.dms.model.pending_approval_list.PendingRequestCancelModel;
import com.scorg.dms.model.pending_approval_list.RequestedArchivedBaseModel;
import com.scorg.dms.model.pending_approval_list.RequestedArchivedDetailList;
import com.scorg.dms.model.waiting_list.WaitingPatientData;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.dms_patient_list.FileTypeViewerActivity;
import com.scorg.dms.ui.activities.pending_approval_list.RequestedArchivedMainListActivity;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.drag_drop_recyclerview_helper.EndlessRecyclerViewScrollListener;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 22/2/18.
 */
@RuntimePermissions
public class PendingListFragment extends Fragment implements RequestListAdapter.OnItemClickListener, HelperResponse {

    @BindView(R.id.clinicListSpinner)
    Spinner clinicListSpinner;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;

    @BindView(R.id.bulletImageView)
    CircularImageView bulletImageView;
    @BindView(R.id.clinicNameTextView)
    TextView clinicNameTextView;
    @BindView(R.id.clinicAddress)
    TextView clinicAddress;
    @BindView(R.id.hospitalDetailsLinearLayout)
    RelativeLayout hospitalDetailsLinearLayout;

    @BindView(R.id.noRecords)
    LinearLayout noRecords;

    @BindView(R.id.imgNoRecordFound)
    ImageView imgNoRecordFound;
    LinearLayoutManager linearlayoutManager;
    private Unbinder unbinder;
    private boolean mIsLoadMorePatients;
    private int currentPage = 1;
    private ArrayList<RequestedArchivedDetailList> requestedArchivedDetailList = new ArrayList<>();
    private RequestedArchivedMainListActivity mParentActivity;
    private RequestListAdapter mPendingListAdapter;
    private long mClickedPhoneNumber;
    private PendingApprovalHelper mPendingApprovalHelper;

    public PendingListFragment() {
    }

    public static PendingListFragment newInstance(Bundle bundle) {
        PendingListFragment fragment = new PendingListFragment();
        fragment.setArguments(bundle);
        return fragment;
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
        View mRootView = inflater.inflate(R.layout.pending_approval_content_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        mParentActivity = (RequestedArchivedMainListActivity) getActivity();
        mPendingApprovalHelper = new PendingApprovalHelper(mParentActivity, this);
        linearlayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearlayoutManager);
        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mPendingListAdapter = new RequestListAdapter(this.getContext(), requestedArchivedDetailList, this, true);
        mRecyclerView.setAdapter(mPendingListAdapter);

        mPendingApprovalHelper.doGetPendingApprovalData(1, true);

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                mPendingApprovalHelper.doGetPendingApprovalData(currentPage, true);
            }
        });

        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (mIsLoadMorePatients) {
                    currentPage = currentPage + 1;
                    mPendingApprovalHelper.doGetPendingApprovalData(currentPage, true);
                }
            }
        });
    }

    public void onRequestPermssionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // ActivePatientListFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();

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
        dialog.findViewById(R.id.button_ok).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        dialog.findViewById(R.id.button_cancel).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        ((TextView) dialog.findViewById(R.id.textview_sucess)).setText(getContext().getResources().getString(R.string.do_you_want_to_cancel));

        float[] bottomLeftRadius = {0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), 0, 0};
        float[] bottomRightRadius = {0, 0, 0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8)};
        GradientDrawable buttonLeftBackground = new GradientDrawable();
        buttonLeftBackground.setShape(GradientDrawable.RECTANGLE);
        buttonLeftBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonLeftBackground.setCornerRadii(bottomRightRadius);

        GradientDrawable buttonRightBackground = new GradientDrawable();
        buttonRightBackground.setShape(GradientDrawable.RECTANGLE);
        buttonRightBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonRightBackground.setCornerRadii(bottomLeftRadius);

        Button buttonRight = dialog.findViewById(R.id.button_cancel);
        Button buttonLeft = dialog.findViewById(R.id.button_ok);
        ImageView dialogIcon = dialog.findViewById(R.id.dialogIcon);
        dialogIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        buttonLeft.setBackground(buttonLeftBackground);
        buttonRight.setBackground(buttonRightBackground);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
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

        buttonRight.setOnClickListener(new View.OnClickListener() {
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
        swipeToRefresh.setRefreshing(false);
        switch (mOldDataTag) {
            case DMSConstants.TASK_PATIENT_LIST: {
                ShowSearchResultResponseModel showSearchResultResponseModel = (ShowSearchResultResponseModel) customResponse;
                SearchResultData searchResultData = showSearchResultResponseModel.getSearchResultData();

                if (searchResultData != null) {
                    List<SearchResult> searchResultList = searchResultData.getSearchResult();
                    if (!searchResultList.isEmpty()) {

                    }
                }
            }
            break;
            case DMSConstants.TASK_PENDING_APPROVAL_LIST: {
                if (customResponse != null) {
                    RequestedArchivedBaseModel requestedArchivedBaseModel = (RequestedArchivedBaseModel) customResponse;
                    if (currentPage == 1)
                        requestedArchivedDetailList.clear();
                    requestedArchivedDetailList.addAll(requestedArchivedBaseModel.getPendingApprovalDataModel().getRequestedArchivedDetailList());
                    mIsLoadMorePatients = requestedArchivedBaseModel.getPendingApprovalDataModel().isPaggination();
                    mPendingListAdapter.notifyDataSetChanged();

                    if (requestedArchivedDetailList.isEmpty()) {
                        mRecyclerView.setVisibility(View.GONE);
                        noRecords.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        noRecords.setVisibility(View.GONE);

                    }
                }

            }
            break;
            case DMSConstants.TASK_CANCEL_REQUEST_CONFIDENTIAL: {
                if (customResponse != null) {
                    CancelUnlockRequestResponseBaseModel cancelUnlockRequestResponseBaseModel = (CancelUnlockRequestResponseBaseModel) customResponse;
                    if (cancelUnlockRequestResponseBaseModel.getCommon().getStatusCode() == 200)
                        CommonMethods.showToast(getActivity(), "Successfully Canceled");
                    DMSApplication.ISCancelRequest = true;
                    requestedArchivedDetailList.clear();
                    mPendingApprovalHelper.doGetPendingApprovalData(1, true);
                }
            }
        }
    }


    private void showErrorDialog(String errorMessage, boolean isTimeout) {
        CommonMethods.showErrorDialog(errorMessage, mParentActivity, isTimeout, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
                mPendingApprovalHelper.doGetPendingApprovalData(1, true);
            }
        });
        noRecords.setVisibility(View.VISIBLE);
        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        swipeToRefresh.setRefreshing(false);
        showErrorDialog(errorMessage, false);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        swipeToRefresh.setRefreshing(false);
        showErrorDialog(serverErrorMessage, false);

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        swipeToRefresh.setRefreshing(false);
        showErrorDialog(serverErrorMessage, false);

    }

    @Override
    public void onTimeOutError(String mOldDataTag, String timeOutErrorMessage) {
        swipeToRefresh.setRefreshing(false);
        showErrorDialog(timeOutErrorMessage, true);


    }

}