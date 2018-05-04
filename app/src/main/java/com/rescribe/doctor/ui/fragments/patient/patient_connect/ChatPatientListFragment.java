package com.rescribe.doctor.ui.fragments.patient.patient_connect;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.patient_connect.ChatPatientListAdapter;
import com.rescribe.doctor.helpers.myappointments.AppointmentHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.patient.doctor_patients.MyPatientBaseModel;
import com.rescribe.doctor.model.patient.doctor_patients.PatientList;
import com.rescribe.doctor.model.patient.patient_connect.PatientData;
import com.rescribe.doctor.model.request_patients.RequestSearchPatients;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.ui.activities.ChatActivity;
import com.rescribe.doctor.ui.activities.book_appointment.SelectSlotToBookAppointmentBaseActivity;
import com.rescribe.doctor.ui.activities.my_patients.ShowMyPatientsListActivity;
import com.rescribe.doctor.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.doctor.ui.customesViews.drag_drop_recyclerview_helper.EndlessRecyclerViewScrollListener;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.rescribe.doctor.util.RescribeConstants.SUCCESS;

/**
 * Created by jeetal on 5/3/18.
 */

public class ChatPatientListFragment extends Fragment implements ChatPatientListAdapter.OnDownArrowClicked, HelperResponse {

    private AppointmentHelper mAppointmentHelper;
    @BindView(R.id.whiteUnderLine)
    ImageView whiteUnderLine;
    @BindView(R.id.historyExpandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.rightFab)
    FloatingActionButton rightFab;
    @BindView(R.id.leftFab)
    FloatingActionButton leftFab;
    @BindView(R.id.appointmentLayoutContainer)
    LinearLayout appointmentLayoutContainer;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerViewBottom)
    RecyclerView recyclerViewBottom;
    @BindView(R.id.searchEditText)
    EditTextWithDeleteButton searchEditText;
    private Unbinder unbinder;
    private ChatPatientListAdapter mMyPatientsAdapter;
    private String searchText = "";
    private boolean isFiltered = false;
    private RequestSearchPatients mRequestSearchPatients = new RequestSearchPatients();
    private String mFromWhichActivity;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.my_appointments_layout, container, false);
        //  hideSoftKeyboard();
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;

    }

    private void init() {

        mFromWhichActivity = getArguments().getString(RescribeConstants.ACTIVITY_LAUNCHED_FROM);

        mAppointmentHelper = new AppointmentHelper(getActivity(), this);
        recyclerView.setClipToPadding(false);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayoutManager);
        // off recyclerView Animation
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        recyclerView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.dp67));
        recyclerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.divider));
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        ArrayList<PatientList> patientLists = new ArrayList<>();
        mMyPatientsAdapter = new ChatPatientListAdapter(getActivity(), patientLists, this, mFromWhichActivity.equals(RescribeConstants.MY_APPOINTMENTS));
        recyclerView.setAdapter(mMyPatientsAdapter);

        nextPage(0);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                nextPage(page);
            }
        });


        searchEditText.setHint(getString(R.string.search_hint));
        searchEditText.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = s.toString();

                if (searchText.length() >= 3) {
                    searchPatients();
                    isFiltered = true;
                } else if (isFiltered) {
                    isFiltered = false;
                    searchText = "";
                    searchPatients();
                }
                if (s.toString().length() < 3) {
                    mMyPatientsAdapter.getFilter().filter(s.toString());
                }
            }

        });

    }

    @Override
    public void onRecordFound(boolean isListEmpty) {
        if (isListEmpty)
            emptyListView.setVisibility(View.VISIBLE);
        else
            emptyListView.setVisibility(View.GONE);
    }

    @Override
    public void onClickOfPatientDetails(PatientList patientListObject, String text) {
        if (mFromWhichActivity.equals(RescribeConstants.MY_APPOINTMENTS)) {
            Intent intent = new Intent(getActivity(), SelectSlotToBookAppointmentBaseActivity.class);
            intent.putExtra(RescribeConstants.PATIENT_INFO, patientListObject);
            intent.putExtra(RescribeConstants.PATIENT_DETAILS, text);
            intent.putExtra(RescribeConstants.IS_APPOINTMENT_TYPE_RESHEDULE, false);
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            getActivity().startActivity(intent);

        } else {
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            PatientData doctorConnectChatModel = new PatientData();
            doctorConnectChatModel.setId(patientListObject.getPatientId());
            doctorConnectChatModel.setImageUrl(patientListObject.getPatientImageUrl());
            doctorConnectChatModel.setPatientName(patientListObject.getPatientName());
            doctorConnectChatModel.setSalutation(patientListObject.getSalutation());
            intent.putExtra(RescribeConstants.PATIENT_DETAILS, text);
            intent.putExtra(RescribeConstants.PATIENT_INFO, doctorConnectChatModel);
            intent.putExtra(RescribeConstants.CLINIC_ID, patientListObject.getClinicId());
            intent.putExtra(RescribeConstants.PATIENT_HOS_PAT_ID, patientListObject.getHospitalPatId());
            intent.putExtra(RescribeConstants.IS_CALL_FROM_MY_PATIENTS, true);
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            getActivity().startActivityForResult(intent, Activity.RESULT_OK);
            getActivity().finish();
        }

    }

    @Override
    public void onPhoneNoClick(String patientPhone) {
        ShowMyPatientsListActivity activity = (ShowMyPatientsListActivity) getActivity();
        activity.callPatient(patientPhone);
    }


    public static ChatPatientListFragment newInstance(Bundle b) {
        ChatPatientListFragment fragment = new ChatPatientListFragment();
        fragment.setArguments(b);
        return fragment;
    }


    @OnClick({R.id.rightFab, R.id.leftFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightFab:
                ShowMyPatientsListActivity activity = (ShowMyPatientsListActivity) getActivity();
                activity.openDrawer();
                break;
            case R.id.leftFab:

                break;
        }
    }

    public void nextPage(int pageNo) {
        mAppointmentHelper = new AppointmentHelper(getContext(), this);
        mRequestSearchPatients.setDocId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getContext())));
        mRequestSearchPatients.setSearchText(searchText);
        mRequestSearchPatients.setPageNo(pageNo);
        mAppointmentHelper.doGetSearchResult(mRequestSearchPatients, false);
    }

    public void searchPatients() {
        mMyPatientsAdapter.clear();
        mRequestSearchPatients.setPageNo(0);
        mAppointmentHelper = new AppointmentHelper(getContext(), this);
        mRequestSearchPatients.setDocId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getContext())));
        mRequestSearchPatients.setSearchText(searchText);
        mAppointmentHelper.doGetSearchResult(mRequestSearchPatients, false);
    }

    public void apply(RequestSearchPatients mRequestSearchPatients, boolean isReset) {
        this.mRequestSearchPatients.setFilterParams(mRequestSearchPatients.getFilterParams());
        this.mRequestSearchPatients.setSortField(mRequestSearchPatients.getSortField());
        this.mRequestSearchPatients.setSortOrder(mRequestSearchPatients.getSortOrder());

        if (!isReset)
            searchPatients();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_GET_SEARCH_RESULT_MY_PATIENT)) {

            MyPatientBaseModel myAppointmentsBaseModel = (MyPatientBaseModel) customResponse;

            if (myAppointmentsBaseModel.getCommon().getStatusCode().equals(SUCCESS)) {

                ArrayList<PatientList> mLoadedPatientList = myAppointmentsBaseModel.getPatientDataModel().getPatientList();

                mMyPatientsAdapter.addAll(mLoadedPatientList, ((ShowMyPatientsListActivity) getActivity()).selectedDoctorId, searchText);

                if (!mMyPatientsAdapter.getGroupList().isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyListView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
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
