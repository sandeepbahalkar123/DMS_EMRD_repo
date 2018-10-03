package com.scorg.dms.ui.fragments.admitted_patient;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scorg.dms.R;
import com.scorg.dms.adapters.admitted_patient.AdmittedPatientsListAdapter;
import com.scorg.dms.adapters.my_appointments.AppointmentListAdapter;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.model.admitted_patient.AdmittedPatientData;
import com.scorg.dms.model.admitted_patient.AdmittedPatientDataModel;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.model.my_appointments.MyAppointmentsDataModel;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.admitted_patient_list.AdmittedPatientsActivity;
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

import static com.scorg.dms.util.DMSConstants.ADMITTED_PATIENT_DATA;
import static com.scorg.dms.util.DMSConstants.PATIENT_DETAILS;


/**
 * Created by jeetal on 31/1/18.
 */

public class AdmittedPatientsFragment extends Fragment implements AdmittedPatientsListAdapter.OnItemClickListener {


    @BindView(R.id.searchEditText)
    EditTextWithDeleteButton searchEditText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.rightFab)
    FloatingActionButton rightFab;

    @BindView(R.id.imgNoRecordFound)
    ImageView imgNoRecordFound;
    Unbinder unbinder;
    private AdmittedPatientsListAdapter admittedPatientsListAdapter;
    private AppointmentHelper mAppointmentHelper;

    private ArrayList<AppointmentPatientData> mAppointmentPatientData;
    private String mUserSelectedDate;
    private DMSPatientsHelper mPatientsHelper;

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
        searchEditText.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (admittedPatientsListAdapter != null) {
                    admittedPatientsListAdapter.getFilter().filter(s);
                }
            }
        });

        mUserSelectedDate = getArguments().getString(DMSConstants.DATE);
        AdmittedPatientDataModel myAppointmentsDataModel = getArguments().getParcelable(ADMITTED_PATIENT_DATA);
        setFilteredData(myAppointmentsDataModel);
    }

    public static AdmittedPatientsFragment newInstance(AdmittedPatientDataModel admittedPatientDataModel, String mDateSelectedByUser) {
        AdmittedPatientsFragment myAppointmentsFragment = new AdmittedPatientsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DMSConstants.DATE, mDateSelectedByUser);
        bundle.putParcelable(ADMITTED_PATIENT_DATA, admittedPatientDataModel);
        myAppointmentsFragment.setArguments(bundle);
        return myAppointmentsFragment;
    }

    @Override
    public void onPhoneNoClick(long patientPhone) {
        AdmittedPatientsActivity activity = (AdmittedPatientsActivity) getActivity();
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
    public void onClickOfPatientDetails(AdmittedPatientData patientListObject) {
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
                activity.getActivityDrawerLayout().openDrawer(GravityCompat.END);
                break;
        }
    }


    public void setFilteredData(AdmittedPatientDataModel myAppointmentsDataModel) {

        if (!myAppointmentsDataModel.getAdmittedPatientData().isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);

            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            //list is sorted for Booked and Confirmed Status appointments
            admittedPatientsListAdapter = new AdmittedPatientsListAdapter(getActivity(), myAppointmentsDataModel.getAdmittedPatientData(), this);
            recyclerView.setAdapter(admittedPatientsListAdapter);

        } else {
            recyclerView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
            imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        }

    }


    @Override
    public void onClickedOfEpisodeListButton(SearchResult groupHeader) {

        Intent intent = new Intent(getActivity(), PatientDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PATIENT_DETAILS, groupHeader);
        intent.putExtra(DMSConstants.BUNDLE, bundle);
        startActivity(intent);

    }
}