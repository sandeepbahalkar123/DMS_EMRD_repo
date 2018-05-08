package com.scorg.dms.ui.fragments.patient.patient_history_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorg.dms.R;
import com.scorg.dms.adapters.patient_history.CalenderDayOfMonthGridAdapter;
import com.scorg.dms.helpers.patient_detail.PatientDetailHelper;
import com.scorg.dms.model.login.Year;
import com.scorg.dms.model.patient.patient_history.DatesData;
import com.scorg.dms.model.patient.patient_history.PatientHistoryInfo;
import com.scorg.dms.ui.activities.patient_details.SingleVisitDetailsActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scorg.dms.ui.fragments.patient.patient_history_fragment.PatientHistoryListFragmentContainer.SELECT_REQUEST_CODE;


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
            mMonthName = arguments.getString(DMSConstants.MONTH);
            mYear = arguments.getString(DMSConstants.YEAR);
        }

        setGridViewAdapter();
        return mRootView;
    }


    public static PatientHistoryCalenderListFragment createNewFragment(Year dataString, Bundle b) {
        PatientHistoryCalenderListFragment fragment = new PatientHistoryCalenderListFragment();
        Bundle args = new Bundle();
        args.putString(DMSConstants.MONTH, dataString.getMonthName());
        args.putString(DMSConstants.YEAR, dataString.getYear());
        patientName = b.getString(DMSConstants.PATIENT_NAME);
        patientInfo = b.getString(DMSConstants.PATIENT_INFO);
        patientID = b.getString(DMSConstants.PATIENT_ID);
        mHospitalPatId = b.getString(DMSConstants.PATIENT_HOS_PAT_ID);
        mAptId = b.getInt(DMSConstants.APPOINTMENT_ID,0);
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
            Date m1Date = CommonMethods.convertStringToDate(m1.getVisitDate(), DMSConstants.DATE_PATTERN.UTC_PATTERN);
            Date m2Date = CommonMethods.convertStringToDate(m2.getVisitDate(), DMSConstants.DATE_PATTERN.UTC_PATTERN);
            return m2Date.compareTo(m1Date);
        }
    }


    @Override
    public void onClickOFLayout(String visitDate, String opdId, String opdTime) {
        Intent intent = new Intent(getActivity(), SingleVisitDetailsActivity.class);
        intent.putExtra(DMSConstants.PATIENT_OPDID, opdId);
        intent.putExtra(DMSConstants.PATIENT_ID, patientID);
        intent.putExtra(DMSConstants.PATIENT_NAME, patientName);
        intent.putExtra(DMSConstants.PATIENT_INFO, patientInfo);
        intent.putExtra(DMSConstants.PATIENT_HOS_PAT_ID, mHospitalPatId);
        intent.putExtra(DMSConstants.APPOINTMENT_ID,mAptId);
        intent.putExtra(DMSConstants.DATE, visitDate);
        intent.putExtra(DMSConstants.OPD_TIME, opdTime);
        getActivity().startActivityForResult(intent, SELECT_REQUEST_CODE);

    }

    // To find nique status from list, and set list in recycleview of parent fragment.


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}