package com.rescribe.doctor.ui.activities.my_appointments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.myappointments.AppointmentHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.my_appointments.AppointmentList;
import com.rescribe.doctor.model.my_appointments.ClinicList;
import com.rescribe.doctor.model.my_appointments.MyAppointmentsBaseModel;
import com.rescribe.doctor.model.my_appointments.MyAppointmentsDataModel;
import com.rescribe.doctor.model.my_appointments.PatientList;
import com.rescribe.doctor.model.my_appointments.StatusList;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.ui.fragments.my_appointments.DrawerForMyAppointment;
import com.rescribe.doctor.ui.fragments.my_appointments.MyAppointmentsFragment;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.doctor.ui.fragments.my_appointments.MyAppointmentsFragment.isLongPressed;
import static com.rescribe.doctor.util.RescribeConstants.APPOINTMENT_STATUS.BOOKED;
import static com.rescribe.doctor.util.RescribeConstants.APPOINTMENT_STATUS.CONFIRM;
import static com.rescribe.doctor.util.RescribeConstants.SUCCESS;

/**
 * Created by jeetal on 31/1/18.
 */
@RuntimePermissions
public class MyAppointmentsActivity extends AppCompatActivity implements HelperResponse, DrawerForMyAppointment.OnDrawerInteractionListener, DatePickerDialog.OnDateSetListener {
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.dateTextview)
    CustomTextView dateTextview;
    @BindView(R.id.viewContainer)
    FrameLayout viewContainer;
    @BindView(R.id.nav_view)
    FrameLayout navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    private Context mContext;
    private MyAppointmentsFragment mMyAppointmentsFragment;
    private AppointmentHelper mAppointmentHelper;
    private String month;
    private String mYear;
    private MyAppointmentsBaseModel myAppointmentsBaseMainModel;
    private String phoneNo;
    private String mDateSelectedByUser = "";
    public static final int CLOSE_APPOINTMENT_ACTIVITY_AFTER_BOOK_APPOINTMENT = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_patients_base_layout);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mContext = MyAppointmentsActivity.this;
        titleTextView.setText(getString(R.string.appointments));
        setDateInToolbar();
        //Call api for AppointmentData
        String date = CommonMethods.getCurrentDate(RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
        mAppointmentHelper = new AppointmentHelper(this, this);
        mAppointmentHelper.doGetAppointmentData(date);
    }

    public DrawerLayout getActivityDrawerLayout() {
        return drawerLayout;
    }

    private void setDateInToolbar() {
        //Set Date in Required Format i.e 13thJuly'18
        dateTextview.setVisibility(View.VISIBLE);

        month = CommonMethods.getCurrentDate("MM");
        mYear = CommonMethods.getCurrentDate("yyyy");
        String day = CommonMethods.getCurrentDate("dd");
        mDateSelectedByUser = CommonMethods.getCurrentDate(RescribeConstants.DATE_PATTERN.d_M_YYYY);
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
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_GET_APPOINTMENT_DATA)) {
            if (customResponse != null) {
                myAppointmentsBaseMainModel = (MyAppointmentsBaseModel) customResponse;
                if (myAppointmentsBaseMainModel.getCommon().getStatusCode().equals(SUCCESS)) {

                    MyAppointmentsDataModel myAppointmentsDM = new MyAppointmentsDataModel();
                    myAppointmentsDM.setAppointmentList(getBookedAndConfirmed(myAppointmentsBaseMainModel.getMyAppointmentsDataModel().getAppointmentList()));

                    mMyAppointmentsFragment = MyAppointmentsFragment.newInstance(myAppointmentsDM,mDateSelectedByUser);
                    getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mMyAppointmentsFragment).commit();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(RescribeConstants.APPOINTMENT_DATA, myAppointmentsBaseMainModel.getMyAppointmentsDataModel());
                    DrawerForMyAppointment mDrawerForMyAppointment = DrawerForMyAppointment.newInstance(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_view, mDrawerForMyAppointment).commit();
                }
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

        CommonMethods.showToast(mContext, errorMessage);
        emptyListView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(mContext, serverErrorMessage);
        emptyListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(mContext, serverErrorMessage);

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
                datePickerDialog.setMinDate(Calendar.getInstance());
                datePickerDialog.show(getSupportFragmentManager(), getResources().getString(R.string.select_date_text));

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            if (mMyAppointmentsFragment != null)
                if (isLongPressed) {
                    mMyAppointmentsFragment.removeCheckBox();
                } else {
                    super.onBackPressed();
                }
        }
    }

    @Override
    public void onApply(ArrayList<StatusList> statusLists, ArrayList<ClinicList> clinicLists, boolean drawerRequired) {
        drawerLayout.closeDrawers();

        if (statusLists.isEmpty() && clinicLists.isEmpty()) {
            MyAppointmentsDataModel myAppointmentsDM = new MyAppointmentsDataModel();
            myAppointmentsDM.setAppointmentList(getBookedAndConfirmed(myAppointmentsBaseMainModel.getMyAppointmentsDataModel().getAppointmentList()));
            mMyAppointmentsFragment.setFilteredData(myAppointmentsDM);
        } else {

            ArrayList<AppointmentList> mAppointmentLists = new ArrayList<>();
            ArrayList<AppointmentList> mFilterAppointmentList = new ArrayList<>();

            if (statusLists.isEmpty()) {
                mAppointmentLists.addAll(myAppointmentsBaseMainModel.getMyAppointmentsDataModel().getAppointmentList());
            } else {
                for (AppointmentList appointmentObject : myAppointmentsBaseMainModel.getMyAppointmentsDataModel().getAppointmentList()) {

                    ArrayList<PatientList> mPatientListArrayList = new ArrayList<>();
                    AppointmentList tempAppointmentListObject = null;
                    try {
                        tempAppointmentListObject = (AppointmentList) appointmentObject.clone();
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }

                    for (PatientList patientList : appointmentObject.getPatientList()) {
                        for (StatusList statusName : statusLists) {
                            if (statusName.getStatusId().equals(patientList.getAppointmentStatusId()))
                                mPatientListArrayList.add(patientList);
                        }
                    }

                    if (!mPatientListArrayList.isEmpty()) {
                        tempAppointmentListObject.setPatientList(mPatientListArrayList);
                        mAppointmentLists.add(tempAppointmentListObject);
                    }
                }
            }

            if (clinicLists.isEmpty()) {
                mFilterAppointmentList.addAll(mAppointmentLists);
            } else {
                for (AppointmentList appointmentObject : mAppointmentLists) {
                    for (ClinicList clinicList : clinicLists) {
                        if (clinicList.getLocationId().equals(appointmentObject.getLocationId()))
                            mFilterAppointmentList.add(appointmentObject);
                    }
                }
            }

            MyAppointmentsDataModel myAppointmentsDataModel = new MyAppointmentsDataModel();
            myAppointmentsDataModel.setAppointmentList(mFilterAppointmentList);
            myAppointmentsDataModel.setClinicList(myAppointmentsBaseMainModel.getMyAppointmentsDataModel().getClinicList());
            myAppointmentsDataModel.setStatusList(myAppointmentsBaseMainModel.getMyAppointmentsDataModel().getStatusList());

            mMyAppointmentsFragment.setFilteredData(myAppointmentsDataModel);
        }
    }

    private ArrayList<AppointmentList> getBookedAndConfirmed(ArrayList<AppointmentList> mAppointmentList) {

        ArrayList<AppointmentList> mAppointListForBookAndConfirm = new ArrayList<>();
        for (int i = 0; i < mAppointmentList.size(); i++) {
            ArrayList<PatientList> patientListToAddBookAndConfirmEntries = new ArrayList<>();

            AppointmentList tempAppointmentListObject = null;
            try {
                tempAppointmentListObject = (AppointmentList) mAppointmentList.get(i).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }

            ArrayList<PatientList> mPatientListsForBookAndCancel = tempAppointmentListObject.getPatientList();
            if (!mPatientListsForBookAndCancel.isEmpty()) {
                for (int j = 0; j < mPatientListsForBookAndCancel.size(); j++) {
                    if (mPatientListsForBookAndCancel.get(j).getAppointmentStatusId().equals(CONFIRM) || mPatientListsForBookAndCancel.get(j).getAppointmentStatusId().equals(BOOKED)) {
                        PatientList patientList = mPatientListsForBookAndCancel.get(j);
                        patientListToAddBookAndConfirmEntries.add(patientList);
                    }
                }
                if (!patientListToAddBookAndConfirmEntries.isEmpty()) {
                    tempAppointmentListObject.setPatientList(patientListToAddBookAndConfirmEntries);
                    mAppointListForBookAndConfirm.add(tempAppointmentListObject);
                }
            }
        }

        return mAppointListForBookAndConfirm;
    }

    @Override
    public void onReset(boolean drawerRequired) {

    }

    public void callPatient(String patientPhone) {
        phoneNo = patientPhone;
        MyAppointmentsActivityPermissionsDispatcher.doCallSupportWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        callSupport(phoneNo);
    }

    private void callSupport(String phoneNo) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        startActivity(callIntent);
    }


    public void onRequestPermssionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyAppointmentsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {

        int monthOfYearToShow = monthOfYear + 1;
        mDateSelectedByUser = dayOfMonth+"-"+monthOfYearToShow+"-"+year;
        dateTextview.setVisibility(View.VISIBLE);
        String timeToShow = CommonMethods.formatDateTime(dayOfMonth + "-" + monthOfYearToShow + "-" + year, RescribeConstants.DATE_PATTERN.MMM_YY,
                RescribeConstants.DATE_PATTERN.DD_MM_YYYY, RescribeConstants.DATE).toLowerCase();
        String[] timeToShowSpilt = timeToShow.split(",");
        month = timeToShowSpilt[0].substring(0, 1).toUpperCase() + timeToShowSpilt[0].substring(1);
        mYear = timeToShowSpilt.length == 2 ? timeToShowSpilt[1] : "";
        Date date = CommonMethods.convertStringToDate(dayOfMonth + "-" + monthOfYearToShow + "-" + year, RescribeConstants.DATE_PATTERN.DD_MM_YYYY);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> " + CommonMethods.getFormattedDate(monthOfYearToShow + " " + year, "MM yyyy", "MMM'' yy");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            dateTextview.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
        else
            dateTextview.setText(Html.fromHtml(toDisplay));

        mAppointmentHelper = new AppointmentHelper(this, this);
        isLongPressed = false;
        mAppointmentHelper.doGetAppointmentData(year + "-" + monthOfYearToShow + "-" + dayOfMonth);
    }
}
