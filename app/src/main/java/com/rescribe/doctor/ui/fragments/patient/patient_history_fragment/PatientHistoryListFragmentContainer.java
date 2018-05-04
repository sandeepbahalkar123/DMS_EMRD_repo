package com.rescribe.doctor.ui.fragments.patient.patient_history_fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.patient_history.OPDStatusShowAdapter;
import com.rescribe.doctor.adapters.patient_history.YearSpinnerAdapter;
import com.rescribe.doctor.helpers.patient_detail.PatientDetailHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.doctor_location.DoctorLocationModel;
import com.rescribe.doctor.model.login.Year;
import com.rescribe.doctor.model.patient.patient_history.PatientDetails;
import com.rescribe.doctor.model.patient.patient_history.PatientHistoryBaseModel;
import com.rescribe.doctor.model.patient.patient_history.PatientHistoryDataModel;
import com.rescribe.doctor.model.patient.patient_history.PatientHistoryInfo;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.singleton.RescribeApplication;
import com.rescribe.doctor.ui.activities.add_records.SelectedRecordsActivity;
import com.rescribe.doctor.ui.activities.my_patients.patient_history.PatientHistoryActivity;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.doctor.util.RescribeConstants.SALUTATION;
import static com.rescribe.doctor.util.RescribeConstants.SUCCESS;


public class PatientHistoryListFragmentContainer extends Fragment implements HelperResponse, DatePickerDialog.OnDateSetListener {

    public static final int SELECT_REQUEST_CODE = 111;
    @BindView(R.id.backImageView)
    ImageView mBackArrow;
    @BindView(R.id.tabFragment)
    TabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.year)
    Spinner mYearSpinnerView;
    @BindView(R.id.dateTextview)
    CustomTextView mYearSpinnerSingleItem;
    @BindView(R.id.noRecords)
    ImageView noRecords;
    @BindView(R.id.addRecordButton)
    Button mAddRecordButton;
    //----------
    private ArrayList<String> mYearList;
    private ArrayList<Year> mTimePeriodList;
    private Year mCurrentSelectedTimePeriodTab;
    private PatientDetailHelper mPatientDetailHelper;
    private ViewPagerAdapter mViewPagerAdapter;
    private HashSet<String> mGeneratedRequestForYearList;
    private Context mContext;
    private PatientHistoryActivity mParentActivity;
    private String mLocationId;
    private int mHospitalId;
    private String mPatientId;
    private String mHospitalPatId;
    private int mAptId;

    public PatientHistoryListFragmentContainer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mRootView = inflater.inflate(R.layout.patient_history_list_fragment_container, container, false);
        ButterKnife.bind(this, mRootView);

        mParentActivity = (PatientHistoryActivity) getActivity();
        mContext = getContext();
        initialize();
        return mRootView;
    }

    public void initialize() {

        mYearList = new ArrayList<>();
        mTimePeriodList = new ArrayList<>();

        mPatientId = getArguments().getString(RescribeConstants.PATIENT_ID);
        mHospitalId = getArguments().getInt(RescribeConstants.CLINIC_ID);

        if (getArguments().getString(RescribeConstants.PATIENT_NAME) != null) {
            titleTextView.setText(getArguments().getString(RescribeConstants.PATIENT_NAME));
            userInfoTextView.setVisibility(View.VISIBLE);
            userInfoTextView.setText(getArguments().getString(RescribeConstants.PATIENT_INFO));
            mHospitalPatId = getArguments().getString(RescribeConstants.PATIENT_HOS_PAT_ID);
            mAptId = getArguments().getInt(RescribeConstants.APPOINTMENT_ID);
        }

        YearSpinnerInteractionListener listener = new YearSpinnerInteractionListener();
        mYearSpinnerView.setOnTouchListener(listener);
        mYearSpinnerView.setOnItemSelectedListener(listener);
        mYearSpinnerView.setVisibility(View.GONE);
        mYearSpinnerSingleItem.setVisibility(View.GONE);
        //-------
        mPatientDetailHelper = new PatientDetailHelper(mContext, this);
        //-------
        mCurrentSelectedTimePeriodTab = new Year();
        mCurrentSelectedTimePeriodTab.setMonthName(new SimpleDateFormat("MMM", Locale.US).format(new Date()));
        mCurrentSelectedTimePeriodTab.setYear(new SimpleDateFormat("yyyy", Locale.US).format(new Date()));

        mGeneratedRequestForYearList = new HashSet<>();

        mPatientDetailHelper.doGetPatientHistory(mPatientId, mCurrentSelectedTimePeriodTab.getYear(), getArguments().getString(RescribeConstants.PATIENT_NAME) == null,getArguments().getString(RescribeConstants.PATIENT_HOS_PAT_ID));
    }

    @OnClick({R.id.backImageView, R.id.addRecordButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                mParentActivity.onBackPressed();
                break;
            case R.id.addRecordButton:
                Calendar now = Calendar.getInstance();
// As of version 2.3.0, `BottomSheetDatePickerDialog` is deprecated.
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setAccentColor(getResources().getColor(R.color.tagColor));
                datePickerDialog.setMaxDate(Calendar.getInstance());
                datePickerDialog.show(getActivity().getSupportFragmentManager(), getResources().getString(R.string.select_date_text));

                break;

        }
    }

    private void setupViewPager() {
        mViewPagerAdapter.mFragmentList.clear();
        mViewPagerAdapter.mFragmentTitleList.clear();
        for (Year data :
                mTimePeriodList) {
            Fragment fragment = PatientHistoryCalenderListFragment.createNewFragment(data, getArguments());
            mViewPagerAdapter.addFragment(fragment, data); // pass title here
        }
        mViewpager.setOffscreenPageLimit(0);
        mViewpager.setAdapter(mViewPagerAdapter);

        //------------
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //    datePickerDialog.show(getActivity().getSupportFragmentManager(), getResources().getString(R.string.select_date_text));

            }

            @Override
            public void onPageSelected(int position) {

                PatientHistoryCalenderListFragment item = (PatientHistoryCalenderListFragment) mViewPagerAdapter.getItem(position);
                Bundle arguments = item.getArguments();
                String month = arguments.getString(RescribeConstants.MONTH);
                String year = arguments.getString(RescribeConstants.YEAR);
                CommonMethods.Log("onPageSelected", month + " " + year);
                mCurrentSelectedTimePeriodTab.setMonthName(month);
                mCurrentSelectedTimePeriodTab.setYear(year);

                for (int i = 0; i < mYearList.size(); i++) {
                    if (mYearList.get(i).equalsIgnoreCase(year)) {
                        mYearSpinnerView.setSelection(i);
                        break;
                    }
                }
                //-------
                if (mYearList.size() == 1) {
                    mYearSpinnerSingleItem.setVisibility(View.VISIBLE);
                    mYearSpinnerView.setVisibility(View.GONE);
                    mYearSpinnerSingleItem.setText(mYearList.get(0));
                } else {
                    mYearSpinnerSingleItem.setVisibility(View.GONE);
                    mYearSpinnerView.setVisibility(View.VISIBLE);
                }
                //-------

                //-----THis condition calls API only once for that specific year.----
                if (!mGeneratedRequestForYearList.contains(year)) {
                    Map<String, Map<String, ArrayList<PatientHistoryInfo>>> yearWiseSortedPatientHistoryInfo = mPatientDetailHelper.getYearWiseSortedPatientHistoryInfo();
                    if (yearWiseSortedPatientHistoryInfo.get(year) == null) {
                        mGeneratedRequestForYearList.add(year);
                        mPatientDetailHelper.doGetPatientHistory(mPatientId, year, getArguments().getString(RescribeConstants.PATIENT_NAME) == null, getArguments().getString(RescribeConstants.PATIENT_HOS_PAT_ID));
                    }
                }
                //---------
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < mTimePeriodList.size(); i++) {
                    Year temp = mTimePeriodList.get(i);
                    if (temp.getYear().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getYear()) &&
                            temp.getMonthName().equalsIgnoreCase(mCurrentSelectedTimePeriodTab.getMonthName())) {
                        mViewpager.setCurrentItem(i);
                        break;
                    }

                    //    mViewpager.setCurrentItem(mTimePeriodList.size());
                }
            }
        }, 0);
        //---------
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        ArrayList<DoctorLocationModel> mDoctorLocationModel = RescribeApplication.getDoctorLocationModels();
        ArrayList<DoctorLocationModel> myDoctorLocations = getMyDoctorLocations(mDoctorLocationModel, mHospitalId);
        if (myDoctorLocations.size() == 1) {
            mLocationId = String.valueOf(myDoctorLocations.get(0).getLocationId());
            mHospitalId = myDoctorLocations.get(0).getClinicId();
            callAddRecordsActivity(mLocationId, mHospitalId, year, monthOfYear + 1, dayOfMonth);
        } else {
            showDialogToSelectLocation(getMyDoctorLocations(mDoctorLocationModel, mHospitalId), year, monthOfYear + 1, dayOfMonth);
        }
    }

    private ArrayList<DoctorLocationModel> getMyDoctorLocations(ArrayList<DoctorLocationModel> mDoctorLocationModel, int mClinicId) {
        ArrayList<DoctorLocationModel> mDoctorLocations = new ArrayList<>();
        for (DoctorLocationModel doctorLocationModel : mDoctorLocationModel) {
            if (mClinicId == 0) {
                mDoctorLocations.add(doctorLocationModel);
            } else if (doctorLocationModel.getClinicId() == mClinicId) {
                mDoctorLocations.add(doctorLocationModel);
            }
        }
        return mDoctorLocations;
    }

    private void showDialogToSelectLocation(ArrayList<DoctorLocationModel> mPatientListsOriginal, final int year, final int monthOfYear, final int dayOfMonth) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_location_waiting_list_layout);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (!RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, getActivity()).equals(""))
            mLocationId = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, getActivity());
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        for (int index = 0; index < mPatientListsOriginal.size(); index++) {
            final DoctorLocationModel clinicList = mPatientListsOriginal.get(index);

            RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.dialog_location_radio_item, null, false);

            if (mLocationId != null) {
                if (mLocationId.equals(String.valueOf(clinicList.getLocationId()))) {
                    mHospitalId = clinicList.getClinicId();
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }
            }
            radioButton.setText(clinicList.getClinicName() + ", " + clinicList.getAddress());
            radioButton.setId(CommonMethods.generateViewId());
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLocationId = String.valueOf(clinicList.getLocationId());
                    mHospitalId = clinicList.getClinicId();
                }
            });
            radioGroup.addView(radioButton);
        }

        TextView okButton = (TextView) dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLocationId != null) {
                    if (!mLocationId.equals("0")) {
                        callAddRecordsActivity(mLocationId, mHospitalId, year, monthOfYear, dayOfMonth);
                        dialog.cancel();
                    } else
                        Toast.makeText(getActivity(), "Please select clinic location.", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "Please select clinic location.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    private void callAddRecordsActivity(String mLocationId, int mHospitalId, int year, int monthOfYear, int dayOfMonth) {
        RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, String.valueOf(mLocationId), getActivity());
        Intent intent = new Intent(getActivity(), SelectedRecordsActivity.class);
        intent.putExtra(RescribeConstants.OPD_ID, "0");
        intent.putExtra(RescribeConstants.PATIENT_HOS_PAT_ID, mHospitalPatId);
        intent.putExtra(RescribeConstants.LOCATION_ID, mLocationId);
        intent.putExtra(RescribeConstants.APPOINTMENT_ID,mAptId);
        intent.putExtra(RescribeConstants.PATIENT_ID, mPatientId);
        intent.putExtra(RescribeConstants.CLINIC_ID, mHospitalId);
        intent.putExtra(RescribeConstants.PATIENT_NAME, titleTextView.getText().toString());
        intent.putExtra(RescribeConstants.PATIENT_INFO, userInfoTextView.getText().toString());
        intent.putExtra(RescribeConstants.VISIT_DATE, dayOfMonth + "-" + monthOfYear + "-" + year);
        intent.putExtra(RescribeConstants.OPD_TIME, "");

        getActivity().startActivityForResult(intent, SELECT_REQUEST_CODE);
    }


    //---------------
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<Year> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, Year title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position).getMonthName();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private class YearSpinnerInteractionListener implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

        boolean mYearSpinnerConfigChange = false;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mYearSpinnerConfigChange = true;
            return false;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (mYearSpinnerConfigChange) {
                // Your selection handling code here
                mYearSpinnerConfigChange = false;
                if (parent.getId() == R.id.year && !mYearSpinnerConfigChange) {
                    String selectedYear = mYearList.get(parent.getSelectedItemPosition());
                    for (int i = 0; i < mTimePeriodList.size(); i++) {
                        if (mTimePeriodList.get(i).getYear().equalsIgnoreCase("" + selectedYear)) {
                            mCurrentSelectedTimePeriodTab = mTimePeriodList.get(i);
                            mViewpager.setCurrentItem(i);
                            break;
                        }
                    }
                } else {
                    mYearSpinnerConfigChange = false;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    //---------------
    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        PatientHistoryBaseModel newBaseModel = (PatientHistoryBaseModel) customResponse;

        if (newBaseModel.getCommon().getStatusCode().equals(SUCCESS)) {

            PatientHistoryDataModel dataModel = newBaseModel.getPatientHistoryDataModel();

            if (dataModel.getPatientDetails() != null) {
                PatientDetails patientDetails = dataModel.getPatientDetails();

                String salutation = "";
                if (patientDetails.getSalutation() != 0)
                    salutation = SALUTATION[patientDetails.getSalutation() - 1];
                getArguments().putString(RescribeConstants.PATIENT_NAME, salutation + patientDetails.getPatientName());

                if (!patientDetails.getAge().equals("")) {
                    getArguments().putString(RescribeConstants.PATIENT_INFO, patientDetails.getAge() + " " + mContext.getString(R.string.years) + patientDetails.getGender());
                } else {
                    String getTodayDate = CommonMethods.getCurrentDate(RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                    String getBirthdayDate = patientDetails.getPatientDob();
                    if (!getBirthdayDate.equals("")) {
                        DateTime todayDateTime = CommonMethods.convertToDateTime(getTodayDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                        DateTime birthdayDateTime = CommonMethods.convertToDateTime(getBirthdayDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
                        getArguments().putString(RescribeConstants.PATIENT_INFO, CommonMethods.displayAgeAnalysis(todayDateTime, birthdayDateTime) + " " + mContext.getString(R.string.years) + patientDetails.getGender());
                    }
                }

                titleTextView.setText(getArguments().getString(RescribeConstants.PATIENT_NAME));
                userInfoTextView.setVisibility(View.VISIBLE);
                userInfoTextView.setText(getArguments().getString(RescribeConstants.PATIENT_INFO));
                mHospitalId = getArguments().getInt(RescribeConstants.CLINIC_ID);
                mHospitalPatId = getArguments().getString(RescribeConstants.PATIENT_HOS_PAT_ID);
                mAptId = getArguments().getInt(RescribeConstants.APPOINTMENT_ID);

            }


            mTimePeriodList = dataModel.getFormattedYearList();

            mViewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
            mTabLayout.setupWithViewPager(mViewpager);
            mYearList = dataModel.getUniqueYears();
            YearSpinnerAdapter mYearSpinnerAdapter = new YearSpinnerAdapter(mParentActivity, mYearList, ContextCompat.getColor(getActivity(), R.color.white));
            mYearSpinnerView.setAdapter(mYearSpinnerAdapter);

            if (dataModel.getYearsMonthsData().isEmpty()) {
                noRecords.setVisibility(View.VISIBLE);
                mYearSpinnerView.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.GONE);
                mViewpager.setVisibility(View.GONE);
            } else {
                mYearList = dataModel.getUniqueYears();
                mViewpager.setVisibility(View.VISIBLE);
                noRecords.setVisibility(View.GONE);
                mTabLayout.setVisibility(View.VISIBLE);
                if (mYearList.size() > 0) {
                    if (mYearList.size() == 1) {
                        mYearSpinnerView.setVisibility(View.GONE);
                        mYearSpinnerSingleItem.setVisibility(View.VISIBLE);
                        mYearSpinnerSingleItem.setText(mYearList.get(0));
                    } else {
                        mYearSpinnerView.setVisibility(View.VISIBLE);
                        mYearSpinnerSingleItem.setVisibility(View.GONE);
                    }
                }

                if (mTabLayout != null) {
                    if (mTabLayout.getTabCount() > 5) {
                        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
                    } else {
                        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
                        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
                    }
                }

                setupViewPager();
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

        if (mViewPagerAdapter != null) {
            setupViewPager();
        } else {
            noRecords.setVisibility(View.VISIBLE);
            mYearSpinnerView.setVisibility(View.GONE);
            mTabLayout.setVisibility(View.GONE);
        }

    }
    //---------------

    public PatientDetailHelper getParentPatientDetailHelper() {
        return mPatientDetailHelper;
    }

    public Button getAddRecordButton() {
        return mAddRecordButton;
    }


    public void setOPDStatusGridViewAdapter(ArrayList<String> list) {
        OPDStatusShowAdapter baseAdapter = new OPDStatusShowAdapter(getContext(), list);
        // mOpdStatusGridView.setAdapter(baseAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

