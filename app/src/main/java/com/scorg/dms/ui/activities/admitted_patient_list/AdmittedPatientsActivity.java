package com.scorg.dms.ui.activities.admitted_patient_list;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.scorg.dms.R;
import com.scorg.dms.helpers.admittedpatient.AdmittedPatientHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.admitted_patient.AdmittedPatientBaseModel;
import com.scorg.dms.model.admitted_patient.AdmittedPatientDataModel;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.BaseActivity;
import com.scorg.dms.ui.fragments.admitted_patient.AdmittedPatientsFragment;
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

@RuntimePermissions
public class AdmittedPatientsActivity extends BaseActivity implements HelperResponse, DatePickerDialog.OnDateSetListener {


    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.userInfoTextView)
    TextView userInfoTextView;
    @BindView(R.id.dateTextview)
    TextView dateTextview;
    @BindView(R.id.viewContainer)
    FrameLayout viewContainer;
    @BindView(R.id.nav_view)
    FrameLayout navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;

    @BindView(R.id.imgNoRecordFound)
    ImageView imgNoRecordFound;

    private Context mContext;
    private AdmittedPatientsFragment mAdmittedPatientsFragment;
    private AdmittedPatientHelper admittedPatientHelper;
    private String month;
    private String mYear;
    private AdmittedPatientBaseModel admittedPatientBaseModel;
    private long mClickedPhoneNumber;
    private String mDateSelectedByUser = "";
    public static final int CLOSE_APPOINTMENT_ACTIVITY_AFTER_BOOK_APPOINTMENT = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_patients_base_layout);
        ButterKnife.bind(this);
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        initialize();
    }

    private void initialize() {
        mContext = AdmittedPatientsActivity.this;
        titleTextView.setText(getString(R.string.admitted_patient));
        setDateInToolbar();
        //Call api for AppointmentData
        String date = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.UTC_PATTERN);
        System.out.println(date);
        admittedPatientHelper = new AdmittedPatientHelper(this, this);
        admittedPatientHelper.doGetAdmittedData(date);

        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
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
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_ADMITTED_PATIENT_DATA)) {
            if (customResponse != null) {
                admittedPatientBaseModel = (AdmittedPatientBaseModel) customResponse;
                if (admittedPatientBaseModel.getCommon().getStatusCode().equals(SUCCESS)) {

                    AdmittedPatientDataModel patientDataModel = admittedPatientBaseModel.getAdmittedPatientDataModel();
                    patientDataModel.setAdmittedPatientData(admittedPatientBaseModel.getAdmittedPatientDataModel().getAdmittedPatientData());

                    mAdmittedPatientsFragment = AdmittedPatientsFragment.newInstance(patientDataModel, mDateSelectedByUser,admittedPatientBaseModel.getAdmittedPatientDataModel().getViewRights());
                    getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mAdmittedPatientsFragment).commit();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable(DMSConstants.ADMITTED_PATIENT_DATA, admittedPatientBaseModel.getAdmittedPatientDataModel());
                    bundle.putSerializable(DMSConstants.VIEW_RIGHTS_DETAILS, admittedPatientBaseModel.getAdmittedPatientDataModel().getViewRights());
                    if (emptyListView.getVisibility()==View.VISIBLE){
                        emptyListView.setVisibility(View.GONE);
                        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
                    }

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
                admittedPatientHelper.doGetAdmittedData(date);

            }
        });
        emptyListView.setVisibility(View.VISIBLE);
        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

       showErrorDialog(errorMessage,false);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        showErrorDialog(serverErrorMessage,false);


    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        showErrorDialog(serverErrorMessage,false);

    }

    @Override
    public void onTimeOutError(String mOldDataTag, String timeOutErrorMessage) {
        showErrorDialog(timeOutErrorMessage,true);


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
        AdmittedPatientsActivityPermissionsDispatcher.doCallSupportWithCheck(this);

    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + mClickedPhoneNumber));
        startActivity(callIntent);
    }


    public void onRequestPermssionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AdmittedPatientsActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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

        admittedPatientHelper = new AdmittedPatientHelper(this, this);

        //-----
        String monthToSend = "" + monthOfYearToShow;
        if (monthOfYearToShow <= 9) {
            monthToSend = "0" + monthToSend;
        }
        String dateToSend = CommonMethods.formatDateTime(dayOfMonth + "-" + monthToSend + "-" + year, DMSConstants.DATE_PATTERN.UTC_PATTERN,DMSConstants.DATE_PATTERN.DD_MM_YYYY,DMSConstants.DATE);

        Log.e("selected dateToSend",""+dateToSend);
        //-----

        admittedPatientHelper.doGetAdmittedData(dateToSend);
    }
}


