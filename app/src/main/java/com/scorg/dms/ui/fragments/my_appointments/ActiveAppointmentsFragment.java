package com.scorg.dms.ui.fragments.my_appointments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scorg.dms.R;
import com.scorg.dms.adapters.my_appointments.AppointmentListAdapter;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.model.dms_models.ViewRights;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.model.my_appointments.MyAppointmentsDataModel;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.dms_patient_list.FileTypeViewerActivity;
import com.scorg.dms.ui.activities.dms_patient_list.PatientDetailsActivity;
import com.scorg.dms.ui.activities.my_appointments.MyAppointmentsActivity;
import com.scorg.dms.ui.customesViews.EditTextWithDeleteButton;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.scorg.dms.util.DMSConstants.PATIENT_DETAILS;
import static com.scorg.dms.util.DMSConstants.VIEW_RIGHTS_DETAILS;

public class ActiveAppointmentsFragment extends Fragment implements AppointmentListAdapter.OnItemClickListener {

    @BindView(R.id.searchEditText)
    EditTextWithDeleteButton searchEditText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipeToRefresh)
    public SwipeRefreshLayout swipeToRefresh;

    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.rightFab)
    FloatingActionButton rightFab;

    @BindView(R.id.imgNoRecordFound)
    ImageView imgNoRecordFound;
    Unbinder unbinder;
    ViewRights viewRights;
    private AppointmentListAdapter mAppointmentListAdapter;
    private OnFragmentInteraction onFragmentInteraction;

    public static ActiveAppointmentsFragment newInstance() {
        return new ActiveAppointmentsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.my_appointments_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        mRootView.findViewById(R.id.layoutPatSearch).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        init();
        return mRootView;
    }

    private void init() {
        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        searchEditText.setHint(getString(R.string.search_label)+DMSApplication.LABEL_UHID+ getString(R.string.label_patient_name));
        searchEditText.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAppointmentListAdapter != null) {
                    mAppointmentListAdapter.getFilter().filter(s);
                }
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onFragmentInteraction.pullRefresh();
                searchEditText.setText("");
            }
        });
    }

    @Override
    public void onPhoneNoClick(long patientPhone) {
        MyAppointmentsActivity activity = (MyAppointmentsActivity) getActivity();
        activity.callPatient(patientPhone);
    }

    @Override
    public void onRecordFound(boolean isListEmpty) {
        if (isListEmpty) {
            emptyListView.setVisibility(View.VISIBLE);
            imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        } else {
            emptyListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickOfPatientDetails(AppointmentPatientData patientListObject) {
        // ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();
        // TODO: hardcoed for now, As patientList And WaitingList API patientID not sync from server
        //showSearchResultRequestModel.setPatientId("07535277");
        // showSearchResultRequestModel.setPatientId(patientListObject.getPatientId());

//        mPatientsHelper.doGetPatientList(showSearchResultRequestModel);

        Intent intent = new Intent(getActivity(), FileTypeViewerActivity.class);
        Bundle extra = new Bundle();
        //ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
        //dataToSend.add(childElement);
        //  SearchResult searchPatientInformation = patientExpandableListAdapter.searchPatientInfo("" + groupHeader.getPatientId());
        //todo: filepath(pdf url is not getting in api)
        // extra.putSerializable(getString(R.string.compare), dataToSend);
        // extra.putSerializable(getString(R.string.compare), new ArrayList<PatientFileData>());

        extra.putString(DMSConstants.PATIENT_ADDRESS, patientListObject.getPatAddress());
        extra.putString(DMSConstants.DOCTOR_NAME, "");
        extra.putString(DMSConstants.PATIENT_ID, patientListObject.getPatientId());
        extra.putString(DMSConstants.PAT_ID, patientListObject.getPatId());
        extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + patientListObject.getPatientName());
        intent.putExtra(DMSConstants.DATA, extra);
        startActivity(intent);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rightFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightFab:
                MyAppointmentsActivity activity = (MyAppointmentsActivity) getActivity();
                //activity.getActivityDrawerLayout().openDrawer(GravityCompat.END);
                break;
        }
    }


    public void setFilteredData(MyAppointmentsDataModel myAppointmentsDataModel) {
        swipeToRefresh.setRefreshing(false);
        viewRights = myAppointmentsDataModel.getViewRights();
        if (!myAppointmentsDataModel.getAppointmentPatientData().isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);

            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            //list is sorted for Booked and Confirmed Status appointments
            ArrayList<AppointmentPatientData> activeAppointmentPatientData = sortByActiveAppointmets(myAppointmentsDataModel.getAppointmentPatientData());

            mAppointmentListAdapter = new AppointmentListAdapter(getActivity(), activeAppointmentPatientData, this);
            recyclerView.setAdapter(mAppointmentListAdapter);

        } else {
            recyclerView.setVisibility(View.GONE);
            if (emptyListView.getVisibility() != View.VISIBLE)
                emptyListView.setVisibility(View.VISIBLE);
            imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        }

    }

    private ArrayList<AppointmentPatientData> sortByActiveAppointmets(ArrayList<AppointmentPatientData> appointmentPatientData) {
        ArrayList<AppointmentPatientData> filterList = new ArrayList<>();
        for (AppointmentPatientData patientData : appointmentPatientData) {

            if (!patientData.getAppointmentStatus().contains(DMSConstants.APPOINTMENT_STATUS.COMPLETED_STATUS) && !patientData.getAppointmentStatus().contains(DMSConstants.APPOINTMENT_STATUS.CANCEL_STATUS)) {
                filterList.add(patientData);
            }

        }
        return filterList;
    }

    @Override
    public void onClickedOfEpisodeListButton(SearchResult groupHeader) {
        Intent intent = new Intent(getActivity(), PatientDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PATIENT_DETAILS, groupHeader);
        bundle.putSerializable(VIEW_RIGHTS_DETAILS, viewRights);
        intent.putExtra(DMSConstants.BUNDLE, bundle);
        startActivity(intent);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteraction) {
            onFragmentInteraction = (OnFragmentInteraction) context;
        }
    }

    public interface OnFragmentInteraction {
        void pullRefresh();
    }
}
