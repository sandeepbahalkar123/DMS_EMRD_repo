package com.scorg.dms.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.DashboardAppointmentListAdapter;
import com.scorg.dms.helpers.dashboard.DashboardHelper;
import com.scorg.dms.helpers.login.LoginHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dashboard.DashboardBaseModel;
import com.scorg.dms.model.dashboard.DashboardDataModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.ui.activities.dashboard.SettingsActivity;
import com.scorg.dms.ui.activities.dashboard.SupportActivity;
import com.scorg.dms.ui.activities.dms_patient_list.PatientList;
import com.scorg.dms.ui.activities.my_appointments.MyAppointmentsActivity;
import com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.customesViews.SwitchButton;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.scorg.dms.util.DMSConstants.ACTIVE_STATUS;

/**
 * Created by jeetal on 28/6/17.
 */

@RuntimePermissions
public class HomePageActivity extends AppCompatActivity implements HelperResponse {

    private static final String TAG = "Home";

    //------------
    @BindView(R.id.totalPatientsCount)
    CustomTextView totalPatientsCount;
    @BindView(R.id.todayAppointmentsCount)
    CustomTextView todayAppointmentsCount;
    @BindView(R.id.waitingPatientCount)
    CustomTextView waitingPatientCount;
    @BindView(R.id.pendingApprovalCount)
    CustomTextView pendingApprovalCount;
    //------------

    @BindView(R.id.viewPagerDoctorItem)
    LinearLayout viewPagerDoctorItem;

    @BindView(R.id.welcomeTextView)
    CustomTextView welcomeTextView;

    @BindView(R.id.doctorNameTextView)
    CustomTextView doctorNameTextView;

    @BindView(R.id.aboutDoctorTextView)
    CustomTextView aboutDoctorTextView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layoutDrawerSetting)
    LinearLayout layoutDrawerSetting;

    @BindView(R.id.layoutDrawerSupport)
    LinearLayout layoutDrawerSupport;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.layoutDrawerIcon)
    LinearLayout layoutDrawerIcon;


    @BindView(R.id.nav_right_view)
    FrameLayout mRightNavigationView;

    ImageView menuImageView;
    CustomTextView appointmentTextView;
    CustomTextView viewTextView;

    ImageView menuImageWaitingList;
    CustomTextView menuNameTextView;
    ImageView dashboardArrowImageView;
    SwitchButton radioSwitch;
    @BindView(R.id.hostViewsLayout)
    LinearLayout hostViewsLayout;


    private Context mContext;
    private String docId;
    private LoginHelper loginHelper;
    private LinearLayout menuOptionLinearLayout;
    private DashboardHelper mDashboardHelper;
    private DashboardDataModel mDashboardDataModel;
    private ColorGenerator mColorGenerator;

    private DashboardAppointmentListAdapter mDashBoardAppointmentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_layout);
        ButterKnife.bind(this);
        mContext = HomePageActivity.this;

        mColorGenerator = ColorGenerator.MATERIAL;
        HomePageActivityPermissionsDispatcher.getPermissionWithCheck(HomePageActivity.this);
        docId = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, mContext);
        loginHelper = new LoginHelper(mContext, HomePageActivity.this);
        initialize();

        //drawerConfiguration();
    }


    private void initialize() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels / (CommonMethods.isTablet(mContext) ? 1.6 : 2));
        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        mRightNavigationView.setLayoutParams(layoutParams);
        mDashboardHelper = new DashboardHelper(this, this);
        String doctorNameToDisplay;
        if (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext).toLowerCase().contains("Dr.")) {
            doctorNameToDisplay = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext);
        } else {
            doctorNameToDisplay = "Dr. " + DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext);
        }

        doctorNameTextView.setText(doctorNameToDisplay);
        aboutDoctorTextView.setText("");// TODO: not getting from API right now

    }

    @Override
    protected void onResume() {
        if (mDashboardDataModel == null)
            mDashboardHelper.doGetDashboardResponse();

        super.onResume();
    }


    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void getPermission() {
        CommonMethods.Log(TAG, "asked permission");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        HomePageActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onBackPressed() {

        // closeDrawer();
        final Dialog dialog = new Dialog(mContext);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_exit);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);


        dialog.findViewById(R.id.button_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (DMSPreferencesManager.getBoolean(DMSPreferencesManager.DMS_PREFERENCES_KEY.isSkippedClicked, mContext)) {
                    DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SHOW_UPDATE_DIALOG_ON_SKIPPED, DMSConstants.YES, mContext);
                }
                finishAffinity();
            }
        });
        dialog.findViewById(R.id.button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        switch (mOldDataTag) {
            case DMSConstants.LOGOUT:
                //if user turns on radio button
                CommonMethods.Log(TAG, "logout");
                break;
            case ACTIVE_STATUS:
                CommonMethods.Log(ACTIVE_STATUS, "active");
                break;

            case DMSConstants.TASK_GET_DASHBOARD_RESPONSE:
                DashboardBaseModel mDashboardBaseModel = (DashboardBaseModel) customResponse;
                if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(mDashboardBaseModel.getCommon().getSuccess())) {
                    mDashboardDataModel = mDashboardBaseModel.getDashboardDataModel();
                    if (mDashboardDataModel != null) {
                       pendingApprovalCount.setText(mDashboardDataModel.getPendingApprovedCount());
                        totalPatientsCount.setText(mDashboardDataModel.getTotalPatientCount());
                        todayAppointmentsCount.setText(mDashboardDataModel.getAppointmentCount());
                       waitingPatientCount.setText(mDashboardDataModel.getWaitingCount());


                        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearlayoutManager);
                        mDashBoardAppointmentListAdapter = new DashboardAppointmentListAdapter(mContext, mDashboardDataModel.getAppointmentPatientDataList());
                        recyclerView.setAdapter(mDashBoardAppointmentListAdapter);

                        // setLayoutForAppointment(true, mDashboardDataModel.getAppointmentOpdOTAndOtherCountList());
                        // inflate waiting list layout
                        // setLayoutForWaitingList(mDashboardDataModel.getWaitingCount());

                        //setLayoutForMyPatients();
                    }
                }
                break;
        }

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        hostViewsLayout.removeAllViews();
        pendingApprovalCount.setText("0");
        totalPatientsCount.setText("0");
        todayAppointmentsCount.setText("0");
        waitingPatientCount.setText("0");

        Toast.makeText(mContext, errorMessage + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        hostViewsLayout.removeAllViews();
        pendingApprovalCount.setText("0");
        totalPatientsCount.setText("0");
        todayAppointmentsCount.setText("0");
        waitingPatientCount.setText("0");

        Toast.makeText(mContext, serverErrorMessage + "", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        Toast.makeText(mContext, serverErrorMessage + "", Toast.LENGTH_SHORT).show();

    }

    @OnClick({R.id.layoutDrawerIcon,R.id.totalPatientsCount, R.id.todayAppointmentsCount, R.id.waitingPatientCount, R.id.layoutDrawerSetting, R.id.layoutDrawerSupport})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.viewPagerDoctorItem:
                break;
            case R.id.todayAppointmentsCount:
                Intent myAppointmentsActivity = new Intent(this, MyAppointmentsActivity.class);
                startActivity(myAppointmentsActivity);
                break;
            case R.id.waitingPatientCount:
                Intent todayAppointmentsOrWaitingList = new Intent(this, WaitingMainListActivity.class);
                startActivity(todayAppointmentsOrWaitingList);
                break;
            case R.id.totalPatientsCount:
                Intent patientList = new Intent(this, PatientList.class);
                patientList.putExtra(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mDashboardDataModel.getFileTypes());
                startActivity(patientList);
                break;
            case R.id.layoutDrawerSetting:
                Intent intentDrawerSetting = new Intent(this, SettingsActivity.class);
                startActivity(intentDrawerSetting);
                break;
            case R.id.layoutDrawerSupport:
                Intent intentDrawerSupport = new Intent(this, SupportActivity.class);
                startActivity(intentDrawerSupport);
                break;
            case R.id.layoutDrawerIcon:
                mDrawer.openDrawer(GravityCompat.START);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SHOW_UPDATE_DIALOG_ON_SKIPPED, DMSConstants.YES, mContext);
        super.onDestroy();
    }

    //------Set dynamic layout for appointment,waiting and my_patient: START
/*

    private void setLayoutForAppointment(boolean isRecyclerViewRequired, ArrayList<DashboardDataModel.AppointmentOpdAndOtherCount> calendarTypeList) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View inflatedLayout = inflater.inflate(R.layout.waiting_todays_appointment_common_layout, null, false);
        hostViewsLayout.addView(inflatedLayout);
        recyclerView = (RecyclerView) inflatedLayout.findViewById(R.id.recyclerView);
        menuImageView = (ImageView) inflatedLayout.findViewById(R.id.menuImageView);
        appointmentTextView = (CustomTextView) inflatedLayout.findViewById(R.id.appointmentTextView);
        viewTextView = (CustomTextView) inflatedLayout.findViewById(R.id.viewTextView);
        menuImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.appointment));
        appointmentTextView.setText(getString(R.string.today_appointment).replace("\n", " "));
        viewTextView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        viewTextView.setText(getString(R.string.view));
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        // off recyclerView Animation

        if (calendarTypeList != null) {
            if (calendarTypeList.size() > 0) {
                RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
                if (animator instanceof SimpleItemAnimator)
                    ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
                if (isRecyclerViewRequired) {
                    mDashBoardAppointmentListAdapter = new DashBoardAppointmentListAdapter(mContext, calendarTypeList);
                    recyclerView.setAdapter(mDashBoardAppointmentListAdapter);
                } else {
                    CommonMethods.Log(TAG, "Dont show recyclerView");
                }
            }
        } else {
            RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator)
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            if (isRecyclerViewRequired) {
                mDashBoardAppointmentListAdapter = new DashBoardAppointmentListAdapter(mContext, calendarTypeList);
                recyclerView.setAdapter(mDashBoardAppointmentListAdapter);
            } else {
                CommonMethods.Log(TAG, "Dont show recyclerView");
            }

        }

        viewTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, MyAppointmentsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setLayoutForWaitingList(String waitingListCount) {
        LayoutInflater inflaterWaitingList = LayoutInflater.from(mContext);
        View inflatedLayoutWaitingList = inflaterWaitingList.inflate(R.layout.dashboard_menu_common_layout, null, false);
        hostViewsLayout.addView(inflatedLayoutWaitingList);
        menuOptionLinearLayout = (LinearLayout) inflatedLayoutWaitingList.findViewById(R.id.menuOptionLinearLayout);
        menuImageWaitingList = (ImageView) inflatedLayoutWaitingList.findViewById(R.id.menuImageView);
        menuNameTextView = (CustomTextView) inflatedLayoutWaitingList.findViewById(R.id.menuNameTextView);
        dashboardArrowImageView = (ImageView) inflatedLayoutWaitingList.findViewById(R.id.dashboardArrowImageView);
        radioSwitch = (SwitchButton) inflatedLayoutWaitingList.findViewById(R.id.radioSwitch);
        menuImageWaitingList.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.patientwaitinglist));
        menuNameTextView.setText(getString(R.string.waiting_list) + " - " + waitingListCount);
        dashboardArrowImageView.setVisibility(View.VISIBLE);
        menuOptionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WaitingMainListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setLayoutForMyPatients() {
        LayoutInflater inflaterMyPatients = LayoutInflater.from(mContext);
        View inflaterMyPatientsLayout = inflaterMyPatients.inflate(R.layout.dashboard_menu_common_layout, null, false);
        hostViewsLayout.addView(inflaterMyPatientsLayout);
        menuOptionLinearLayout = (LinearLayout) inflaterMyPatientsLayout.findViewById(R.id.menuOptionLinearLayout);
        menuImageWaitingList = (ImageView) inflaterMyPatientsLayout.findViewById(R.id.menuImageView);
        menuNameTextView = (CustomTextView) inflaterMyPatientsLayout.findViewById(R.id.menuNameTextView);
        dashboardArrowImageView = (ImageView) inflaterMyPatientsLayout.findViewById(R.id.dashboardArrowImageView);
        radioSwitch = (SwitchButton) inflaterMyPatientsLayout.findViewById(R.id.radioSwitch);
        menuImageWaitingList.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.patient));
        menuNameTextView.setText(getString(R.string.my_patients));
        menuOptionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PatientList.class);
                intent.putExtra(DMSConstants.ACTIVITY_LAUNCHED_FROM, DMSConstants.HOME_PAGE);
                intent.putExtra(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mDashboardDataModel.getFileTypes());
                startActivity(intent);
            }
        });
        dashboardArrowImageView.setVisibility(View.VISIBLE);
    }

*/

    //------Set dynamic layout for appointment,waiting and my_patient: END
}