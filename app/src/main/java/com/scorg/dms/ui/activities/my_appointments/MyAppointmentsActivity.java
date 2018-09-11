package com.scorg.dms.ui.activities.my_appointments;

import android.Manifest;
import android.content.Context;
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
import com.scorg.dms.R;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.model.my_appointments.ClinicList;
import com.scorg.dms.model.my_appointments.MyAppointmentsBaseModel;
import com.scorg.dms.model.my_appointments.MyAppointmentsDataModel;
import com.scorg.dms.model.my_appointments.PatientList;
import com.scorg.dms.model.my_appointments.StatusList;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.fragments.my_appointments.MyAppointmentsFragment;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
public class MyAppointmentsActivity extends AppCompatActivity implements HelperResponse, DatePickerDialog.OnDateSetListener {
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
    private long mClickedPhoneNumber;
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
        String date = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH);

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
                    myAppointmentsDM.setAppointmentPatientData(getBookedAndConfirmed(myAppointmentsBaseMainModel.getMyAppointmentsDataModel().getAppointmentPatientData()));

                    mMyAppointmentsFragment = MyAppointmentsFragment.newInstance(myAppointmentsDM, mDateSelectedByUser);
                    getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mMyAppointmentsFragment).commit();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DMSConstants.APPOINTMENT_DATA, myAppointmentsBaseMainModel.getMyAppointmentsDataModel());
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
                //  datePickerDialog.setMinDate(Calendar.getInstance());
                datePickerDialog.show(getSupportFragmentManager(), getResources().getString(R.string.select_date_text));

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


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
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {

    }

    public void onRequestPermssionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, String year, String monthOfYear, String dayOfMonth) {

        int monthOfYearToShow = Integer.parseInt(monthOfYear) + 1;
        mDateSelectedByUser = dayOfMonth + "-" + monthOfYearToShow + "-" + year;
        dateTextview.setVisibility(View.VISIBLE);
        String timeToShow = CommonMethods.formatDateTime(dayOfMonth + "-" + monthOfYearToShow + "-" + year, DMSConstants.DATE_PATTERN.MMM_YY,
                DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE).toLowerCase();
        String[] timeToShowSpilt = timeToShow.split(",");
        month = timeToShowSpilt[0].substring(0, 1).toUpperCase() + timeToShowSpilt[0].substring(1);
        mYear = timeToShowSpilt.length == 2 ? timeToShowSpilt[1] : "";
        Date date = CommonMethods.convertStringToDate(dayOfMonth + "-" + monthOfYearToShow + "-" + year, DMSConstants.DATE_PATTERN.DD_MM_YYYY);
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
        //-----

        mAppointmentHelper.doGetAppointmentData(dayOfMonth + "/" + monthToSend + "/" + year);
    }
}
