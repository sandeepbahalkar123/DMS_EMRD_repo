package com.scorg.dms.ui.fragments.waiting_list;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.adapters.waiting_list.WaitingListAdapter;
import com.scorg.dms.adapters.waiting_list.WaitingListSpinnerAdapter;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResultData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.model.waiting_list.WaitingClinicList;
import com.scorg.dms.model.waiting_list.WaitingListDataModel;
import com.scorg.dms.model.waiting_list.WaitingPatientData;
import com.scorg.dms.ui.activities.dms_patient_list.FileTypeViewerActivity;
import com.scorg.dms.ui.activities.dms_patient_list.PatientDetailsActivity;
import com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity;
import com.scorg.dms.ui.customesViews.CircularImageView;
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
public class ViewAllPatientListFragment extends Fragment implements WaitingListAdapter.OnItemClickListener, HelperResponse {

    @BindView(R.id.clinicListSpinner)
    Spinner clinicListSpinner;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
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

    private Unbinder unbinder;
    private ArrayList<WaitingClinicList> mWaitingClinicLists = new ArrayList<>();
    private ArrayList<WaitingPatientData> waitingPatientTempList;
    private String phoneNo;
    private WaitingMainListActivity mParentActivity;
    private WaitingListAdapter mWaitingListAdapter;
    private DMSPatientsHelper mPatientsHelper;
    private long mClickedPhoneNumber;

    public ViewAllPatientListFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        mParentActivity = (WaitingMainListActivity) getActivity();
        mPatientsHelper = new DMSPatientsHelper(this.getContext(), this);

        clinicListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (waitingPatientTempList != null) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.setClipToPadding(false);
                    setViewAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public static ViewAllPatientListFragment newInstance(Bundle bundle) {
        ViewAllPatientListFragment fragment = new ViewAllPatientListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        WaitingListDataModel waitingListDataModel = mParentActivity.getWaitingListDataModel();
        waitingPatientTempList = waitingListDataModel.getWaitingPatientDataList();
        mWaitingClinicLists = waitingListDataModel.getWaitingClinicList();
        if (mWaitingClinicLists.size() > 1) {
            clinicListSpinner.setVisibility(View.VISIBLE);
            hospitalDetailsLinearLayout.setVisibility(View.GONE);
            WaitingListSpinnerAdapter mWaitingListSpinnerAdapter = new WaitingListSpinnerAdapter(getActivity(), mWaitingClinicLists);
            clinicListSpinner.setAdapter(mWaitingListSpinnerAdapter);
        }
        if (!mWaitingClinicLists.isEmpty()) {
            clinicListSpinner.setVisibility(View.GONE);
            hospitalDetailsLinearLayout.setVisibility(View.GONE);
            clinicNameTextView.setText(mWaitingClinicLists.get(0).getHosName() + " - ");
            clinicAddress.setText(mWaitingClinicLists.get(0).getHosAddress1());
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setClipToPadding(false);
            setViewAdapter();
        }
    }

    private void setViewAdapter() {

        if (waitingPatientTempList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            noRecords.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noRecords.setVisibility(View.GONE);
            mWaitingListAdapter = new WaitingListAdapter(this.getContext(), waitingPatientTempList, this);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearlayoutManager);
            mRecyclerView.setAdapter(mWaitingListAdapter);
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
        mClickedPhoneNumber =phoneNumber;
        ViewAllPatientListFragmentPermissionsDispatcher.doCallSupportWithCheck(this);
    }

    @Override
    public void onClickedOfEpisodeListButton(SearchResult groupHeader) {
        Intent intent = new Intent(getActivity(), PatientDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PATIENT_DETAILS, groupHeader);
        intent.putExtra(DMSConstants.BUNDLE, bundle);
        startActivity(intent);
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