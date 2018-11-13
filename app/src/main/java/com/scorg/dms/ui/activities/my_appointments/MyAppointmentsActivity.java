package com.scorg.dms.ui.activities.my_appointments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.scorg.dms.R;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.ViewRights;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.model.my_appointments.MyAppointmentsBaseModel;
import com.scorg.dms.model.my_appointments.MyAppointmentsDataModel;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.BaseActivity;
import com.scorg.dms.ui.fragments.my_appointments.ActiveAppointmentsFragment;
import com.scorg.dms.ui.fragments.my_appointments.AllAppointmentsFragment;
import com.scorg.dms.ui.fragments.my_appointments.MyAppointmentsFragment;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.scorg.dms.util.DMSConstants.SUCCESS;

/**
 * Created by jeetal on 31/1/18.
 */
@RuntimePermissions
public class MyAppointmentsActivity extends BaseActivity implements HelperResponse, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.userInfoTextView)
    TextView userInfoTextView;
    @BindView(R.id.dateTextview)
    TextView dateTextview;
//    @BindView(R.id.viewContainer)
//    FrameLayout viewContainer;
//    @BindView(R.id.nav_view)
//    FrameLayout navView;
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
//    @BindView(R.id.emptyListView)
//    RelativeLayout emptyListView;
//
//    @BindView(R.id.imgNoRecordFound)
//    ImageView imgNoRecordFound;


    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    private Context mContext;

    private AppointmentHelper mAppointmentHelper;

    private MyAppointmentsBaseModel myAppointmentsBaseMainModel;
    private long mClickedPhoneNumber;
    private String mDateSelectedByUser = "";

    public ViewRights viewRights;
    String[] mFragmentTitleList = new String[2];
    ActiveAppointmentsFragment activeAppointmentsFragment;
    AllAppointmentsFragment allAppointmentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting_base_layout);
        ButterKnife.bind(this);
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        findViewById(R.id.tabs).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        ButterKnife.bind(this);
        mFragmentTitleList[0] = getString(R.string.active_appointmets);
        mFragmentTitleList[1] = getString(R.string.all_appointments);
        initialize();
        setupViewPager(viewpager);
        tabs.setupWithViewPager(viewpager);


    }

    private void initialize() {
        mContext = MyAppointmentsActivity.this;
        titleTextView.setText(getString(R.string.appointments));
        setDateInToolbar();
        //Call api for AppointmentData
        mAppointmentHelper = new AppointmentHelper(this, this);
        String date = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.UTC_PATTERN);
        System.out.println(date);
        mAppointmentHelper.doGetAppointmentData(date);

        // imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        activeAppointmentsFragment = ActiveAppointmentsFragment.newInstance();
        allAppointmentsFragment = AllAppointmentsFragment.newInstance();
        adapter.addFragment(activeAppointmentsFragment, getString(R.string.active_appointmets));
        adapter.addFragment(allAppointmentsFragment, getString(R.string.all_appointments));
        viewPager.setAdapter(adapter);
    }

//    public DrawerLayout getActivityDrawerLayout() {
//        return drawerLayout;
//    }

    private void setDateInToolbar() {
        //Set Date in Required Format i.e 13thJuly'18
        dateTextview.setVisibility(View.VISIBLE);


        String day = CommonMethods.getCurrentDate("dd");
        mDateSelectedByUser = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.d_M_YYYY);
        String toDisplay = day + "<sup>" + CommonMethods.getSuffixForNumber(Integer.parseInt(day)) + "</sup> " + CommonMethods.getCurrentDate("MMM'' yy");

        Spanned dateToDisplay;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            dateToDisplay = Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY);
        else
            dateToDisplay = Html.fromHtml(toDisplay);

        dateTextview.setText(dateToDisplay);

    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_GET_APPOINTMENT_DATA)) {
            if (customResponse != null) {
                myAppointmentsBaseMainModel = (MyAppointmentsBaseModel) customResponse;
                if (myAppointmentsBaseMainModel.getCommon().getStatusCode().equals(SUCCESS)) {
                    MyAppointmentsDataModel myAppointmentsDM = myAppointmentsBaseMainModel.getMyAppointmentsDataModel();
                    viewRights = myAppointmentsDM.getViewRights();
                    myAppointmentsDM.setAppointmentPatientData(myAppointmentsBaseMainModel.getMyAppointmentsDataModel().getAppointmentPatientData());
                    activeAppointmentsFragment.setFilteredData(myAppointmentsDM);
                    allAppointmentsFragment.setFilteredData(myAppointmentsDM);

                }
            }
        }
    }

    private void showErrorDialog(String errorMessage, boolean isTimeout) {
        CommonMethods.showErrorDialog(errorMessage, mContext, isTimeout, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
                String date = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.UTC_PATTERN);
                System.out.println(date);
                mAppointmentHelper.doGetAppointmentData(date);
            }
        });
//        emptyListView.setVisibility(View.VISIBLE);
//        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        showErrorDialog(errorMessage, false);


    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        showErrorDialog(serverErrorMessage, false);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        showErrorDialog(serverErrorMessage, false);
    }

    @Override
    public void onTimeOutError(String mOldDataTag, String timeOutErrorMessage) {
        showErrorDialog(timeOutErrorMessage, true);

    }

    @OnClick({R.id.backImageView, R.id.dateTextview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPressed();
                break;
            case R.id.dateTextview:
                Calendar now = Calendar.getInstance();
                // As of version 2.3.0, `BottomSheetDatePickerDialog` is deprecated.
                DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setAccentColor(getResources().getColor(R.color.tagColor));
                //  datePickerDialog.setMinDate(Calendar.getInstance());
                datePickerDialog.show(getSupportFragmentManager(), getResources().getString(R.string.select_date_text));

                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
//            drawerLayout.closeDrawer(GravityCompat.END);
//        } else {
//            super.onBackPressed();
//        }
//    }


    private ArrayList<AppointmentPatientData> getBookedAndConfirmed(ArrayList<AppointmentPatientData> mAppointmentPatientData) {

        ArrayList<AppointmentPatientData> mAppointListForBookAndConfirm = new ArrayList<>();
        for (int i = 0; i < mAppointmentPatientData.size(); i++) {
            String appointmentStatus = mAppointmentPatientData.get(i).getAppointmentStatus();
            if (appointmentStatus.equalsIgnoreCase(DMSConstants.APPOINTMENT_STATUS.BOOKED_STATUS) || appointmentStatus.equalsIgnoreCase(DMSConstants.APPOINTMENT_STATUS.CONFIRM_STATUS)) {
                mAppointListForBookAndConfirm.add(mAppointmentPatientData.get(i));
            }
        }
        return mAppointListForBookAndConfirm;
    }


    public void callPatient(long patientPhone) {
        mClickedPhoneNumber = patientPhone;
        MyAppointmentsActivityPermissionsDispatcher.doCallSupportWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mClickedPhoneNumber));
        startActivity(callIntent);
    }


    public void onRequestPermssionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyAppointmentsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }


    @Override
    public void onDateSet(DatePickerDialog dialog, String year, String monthOfYear, String dayOfMonth) {

        int monthOfYearToShow = Integer.parseInt(monthOfYear) + 1;
        mDateSelectedByUser = dayOfMonth + "-" + monthOfYearToShow + "-" + year;
        Log.e("mDateSelectedByUser", "" + mDateSelectedByUser);
        dateTextview.setVisibility(View.VISIBLE);
        Date date = CommonMethods.convertStringToDate(dayOfMonth + "-" + monthOfYearToShow + "-" + year, DMSConstants.DATE_PATTERN.DD_MM_YYYY);
        Log.e("selected date", "" + date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> " + CommonMethods.getFormattedDate(monthOfYearToShow + " " + year, "MM yyyy", "MMM'' yy");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            dateTextview.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
        else
            dateTextview.setText(Html.fromHtml(toDisplay));

        mAppointmentHelper = new AppointmentHelper(this, this);

        //-----
        String monthToSend = "" + monthOfYearToShow;
        if (monthOfYearToShow <= 9) {
            monthToSend = "0" + monthToSend;
        }
        String dateToSend = CommonMethods.formatDateTime(dayOfMonth + "-" + monthToSend + "-" + year, DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE);

        Log.e("selected dateToSend", "" + dateToSend);

        //-----

        mAppointmentHelper.doGetAppointmentData(dateToSend);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
