package com.rescribe.doctor.ui.fragments.patient.patient_history_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.patient_history.CalenderDayOfMonthGridAdapter;
import com.rescribe.doctor.helpers.patient_detail.PatientDetailHelper;
import com.rescribe.doctor.model.login.Year;
import com.rescribe.doctor.model.my_records.MyRecordInfoAndReports;
import com.rescribe.doctor.model.patient.patient_history.DatesData;
import com.rescribe.doctor.model.patient.patient_history.PatientHistoryInfo;
import com.rescribe.doctor.ui.activities.patient_details.SingleVisitDetailsActivity;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.doctor.ui.fragments.patient.patient_history_fragment.PatientHistoryListFragmentContainer.SELECT_REQUEST_CODE;


public class PatientHistoryCalenderListFragment extends Fragment implements CalenderDayOfMonthGridAdapter.OnDayClickListener {

    private static String patientName;
    private static String patientInfo;
    private static String mHospitalPatId;
    private static int mAptId;
    @BindView(R.id.calenderDays)
    RecyclerView mCalenderDays;
    private Context mContext;
    private String mMonthName;
    private String mYear;
    private ArrayList<PatientHistoryInfo> formattedDoctorList;
    private ArrayList<DatesData> mDateListForAdapter;
    private boolean mIsLongPressed;
    public CalenderDayOfMonthGridAdapter mCalenderDayOfMonthGridAdapter;
    private ArrayList<DatesData> mAdapterListToNotifyOnBackPress;
    private static String patientID;

    public PatientHistoryCalenderListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.patient_history_calender_view, container, false);
        ButterKnife.bind(this, mRootView);

        mContext = inflater.getContext();

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMonthName = arguments.getString(RescribeConstants.MONTH);
            mYear = arguments.getString(RescribeConstants.YEAR);
        }

        setGridViewAdapter();
        return mRootView;
    }


    public static PatientHistoryCalenderListFragment createNewFragment(Year dataString, Bundle b) {
        PatientHistoryCalenderListFragment fragment = new PatientHistoryCalenderListFragment();
        Bundle args = new Bundle();
        args.putString(RescribeConstants.MONTH, dataString.getMonthName());
        args.putString(RescribeConstants.YEAR, dataString.getYear());
        patientName = b.getString(RescribeConstants.PATIENT_NAME);
        patientInfo = b.getString(RescribeConstants.PATIENT_INFO);
        patientID = b.getString(RescribeConstants.PATIENT_ID);
        mHospitalPatId = b.getString(RescribeConstants.PATIENT_HOS_PAT_ID);
        mAptId = b.getInt(RescribeConstants.APPOINTMENT_ID,0);
        fragment.setArguments(args);
        return fragment;
    }


    private void setGridViewAdapter() {

        PatientHistoryListFragmentContainer parentFragment = (PatientHistoryListFragmentContainer) getParentFragment();

        PatientDetailHelper parentPatientDetailHelper = parentFragment.getParentPatientDetailHelper();
        if (parentPatientDetailHelper != null) {
            Map<String, Map<String, ArrayList<PatientHistoryInfo>>> yearWiseSortedPatientHistoryInfo = parentPatientDetailHelper.getYearWiseSortedPatientHistoryInfo();
            if (yearWiseSortedPatientHistoryInfo.size() != 0) {
                Map<String, ArrayList<PatientHistoryInfo>> monthArrayListHashMap = yearWiseSortedPatientHistoryInfo.get(mYear);
                if (monthArrayListHashMap != null) {

                    formattedDoctorList = monthArrayListHashMap.get(mMonthName);
                    if(formattedDoctorList!=null) {
                        Collections.sort(formattedDoctorList, new DateWiseComparator());

                        mCalenderDayOfMonthGridAdapter = new CalenderDayOfMonthGridAdapter(this.getContext(), formattedDoctorList, this);
                        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        mCalenderDays.setLayoutManager(linearlayoutManager);
                        mCalenderDays.setAdapter(mCalenderDayOfMonthGridAdapter);
                    }

                    //setOPDStatusGridViewAdapter(parentFragment, formattedDoctorList);
                }
            }
        }
    }

    private class DateWiseComparator implements Comparator<PatientHistoryInfo> {

        public int compare(PatientHistoryInfo m1, PatientHistoryInfo m2) {
            Date m1Date = CommonMethods.convertStringToDate(m1.getVisitDate(), RescribeConstants.DATE_PATTERN.UTC_PATTERN);
            Date m2Date = CommonMethods.convertStringToDate(m2.getVisitDate(), RescribeConstants.DATE_PATTERN.UTC_PATTERN);
            return m2Date.compareTo(m1Date);
        }
    }


    @Override
    public void onClickOFLayout(String visitDate, String opdId, String opdTime) {
        Intent intent = new Intent(getActivity(), SingleVisitDetailsActivity.class);
        intent.putExtra(RescribeConstants.PATIENT_OPDID, opdId);
        intent.putExtra(RescribeConstants.PATIENT_ID, patientID);
        intent.putExtra(RescribeConstants.PATIENT_NAME, patientName);
        intent.putExtra(RescribeConstants.PATIENT_INFO, patientInfo);
        intent.putExtra(RescribeConstants.PATIENT_HOS_PAT_ID, mHospitalPatId);
        intent.putExtra(RescribeConstants.APPOINTMENT_ID,mAptId);
        intent.putExtra(RescribeConstants.DATE, visitDate);
        intent.putExtra(RescribeConstants.OPD_TIME, opdTime);
        getActivity().startActivityForResult(intent, SELECT_REQUEST_CODE);

    }

    // To find nique status from list, and set list in recycleview of parent fragment.


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}