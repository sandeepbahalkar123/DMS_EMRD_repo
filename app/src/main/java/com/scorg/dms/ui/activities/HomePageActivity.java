package com.scorg.dms.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.DashboardAppointmentListAdapter;
import com.scorg.dms.helpers.dashboard.DashboardHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dashboard.DashboardBaseModel;
import com.scorg.dms.model.dashboard.DashboardDataModel;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.admitted_patient_list.AdmittedPatientsActivity;
import com.scorg.dms.ui.activities.dashboard.SettingsActivity;
import com.scorg.dms.ui.activities.dashboard.SupportActivity;
import com.scorg.dms.ui.activities.dms_patient_list.FileTypeViewerActivity;
import com.scorg.dms.ui.activities.dms_patient_list.PatientListActivity;
import com.scorg.dms.ui.activities.my_appointments.MyAppointmentsActivity;
import com.scorg.dms.ui.activities.pending_approval_list.RequestedArchivedMainListActivity;
import com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 28/6/17.
 */

@RuntimePermissions
public class HomePageActivity extends BaseActivity implements HelperResponse, DashboardAppointmentListAdapter.OnItemClickListener {

    private static final String TAG = "Home";

    //------------
    @BindView(R.id.totalPatientsCount)
    ImageView totalPatientsCount;
    @BindView(R.id.todayAppointmentsCount)
    TextView todayAppointmentsCount;
    @BindView(R.id.waitingPatientCount)
    TextView waitingPatientCount;

    @BindView(R.id.pendingApprovalCount)
    TextView pendingApprovalCount;
    //------------

    @BindView(R.id.welcomeTextView)
    TextView welcomeTextView;

    @BindView(R.id.doctorNameTextView)
    TextView doctorNameTextView;

    @BindView(R.id.aboutDoctorTextView)
    TextView aboutDoctorTextView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.layoutDrawerSetting)
    LinearLayout layoutDrawerSetting;

    @BindView(R.id.layoutDrawerHome)
    LinearLayout layoutDrawerHome;

    @BindView(R.id.layoutDrawerSupport)
    LinearLayout layoutDrawerSupport;

    @BindView(R.id.settingText)
    TextView settingText;
    @BindView(R.id.homeText)
    TextView homeText;
    @BindView(R.id.supportText)
    TextView supportText;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;

    @BindView(R.id.layoutDrawerIcon)
    LinearLayout layoutDrawerIcon;

    @BindView(R.id.nav_right_view)
    FrameLayout mRightNavigationView;

    @BindView(R.id.txtDashboardHeader)
    TextView txtDashboardHeader;

    @BindView(R.id.textHeaderTodayAppointment)
    TextView textHeaderTodayAppointment;

    @BindView(R.id.layoutTodayAppointment)
    LinearLayout layoutTodayAppointment;

    @BindView(R.id.layoutWaitingPatient)
    LinearLayout layoutWaitingPatient;

    @BindView(R.id.layoutPendingApproval)
    LinearLayout layoutPendingApproval;

    @BindView(R.id.layoutAdmittedPatient)
    LinearLayout layoutAdmittedPatient;

    @BindView(R.id.admittedPatientCount)
    TextView admittedPatientCount;

    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;

    @BindView(R.id.hostViewsLayout)
    LinearLayout hostViewsLayout;

    @BindView(R.id.loginLogo)
    ImageView loginLogo;

    @BindView(R.id.actionBarLogo)
    ImageView actionBarLogo;

    @BindView(R.id.settingLogo)
    ImageView settingLogo;
    @BindView(R.id.homeLogo)
    ImageView homeLogo;
    @BindView(R.id.supportLogo)
    ImageView supportLogo;
    @BindView(R.id.patientLogo)
    ImageView patientLogo;
    @BindView(R.id.menuIcon)
    ImageView menuIcon;
    @BindView(R.id.menuIcon1)
    ImageView menuIcon1;

    @BindView(R.id.divider1)
    ImageView divider1;
    @BindView(R.id.divider2)
    ImageView divider2;

    @BindView(R.id.imgNoRecordFound)
    ImageView imgNoRecordFound;

    @BindView(R.id.layoutTopBackground)
    LinearLayout layoutTopBackground;

    @BindView(R.id.tabsActiveVieAll)
    TabLayout tabsActiveVieAll;

    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;
    ArrayList<AppointmentPatientData> appointmentActivePatientData;
    private Context mContext;
    private DashboardHelper mDashboardHelper;
    private DashboardDataModel mDashboardDataModel;
    private DashboardAppointmentListAdapter mDashBoardAppointmentListAdapter;

     GradientDrawable buttonBackgroundActiveSelected = new GradientDrawable();
     GradientDrawable buttonBackgroundViewAllSelected = new GradientDrawable();
     GradientDrawable buttonBackgroundActiveUnSelected = new GradientDrawable();
     GradientDrawable buttonBackgroundViewAllUnSelected = new GradientDrawable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_layout);
        ButterKnife.bind(this);

        mContext = HomePageActivity.this;
        HomePageActivityPermissionsDispatcher.getPermissionWithCheck(HomePageActivity.this);
        initialize();
        //drawerConfiguration();
    }

    private void initialize() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels / (CommonMethods.isTablet(mContext) ? 2.5 : 2));
        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mRightNavigationView.setLayoutParams(layoutParams);
        mDashboardHelper = new DashboardHelper(this, this);
        String doctorNameToDisplay;
        String hospitalNameToDisplay;
        if (DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.USER_NAME, mContext).toLowerCase().contains("Dr.")) {
            doctorNameToDisplay = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_NAME, mContext);
        } else {
            doctorNameToDisplay = "Dr. " + DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_NAME, mContext);
        }
        hospitalNameToDisplay = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.HOSPITAL_NAME, mContext);

        doctorNameTextView.setText(doctorNameToDisplay);
        aboutDoctorTextView.setText(hospitalNameToDisplay);
        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeToRefresh.setRefreshing(false);
                mDashboardHelper.doGetDashboardResponse();
            }
        });
        setupTabs();
    }

    private void setupTabs() {

        float[] bottomLeftRadius = {getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), 0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8)};
        float[] bottomRightRadius = {0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), 0, 0};

        buttonBackgroundActiveSelected.setShape(GradientDrawable.RECTANGLE);
        buttonBackgroundActiveSelected.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackgroundActiveSelected.setCornerRadii(bottomLeftRadius);

        buttonBackgroundViewAllSelected.setShape(GradientDrawable.RECTANGLE);
        buttonBackgroundViewAllSelected.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackgroundViewAllSelected.setCornerRadii(bottomRightRadius);

        buttonBackgroundActiveUnSelected.setShape(GradientDrawable.RECTANGLE);
        buttonBackgroundActiveUnSelected.setColor(getResources().getColor(R.color.white));
        buttonBackgroundActiveUnSelected.setStroke(1, Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackgroundActiveUnSelected.setCornerRadii(bottomLeftRadius);

        buttonBackgroundViewAllUnSelected.setShape(GradientDrawable.RECTANGLE);
        buttonBackgroundViewAllUnSelected.setColor(getResources().getColor(R.color.white));
        buttonBackgroundViewAllUnSelected.setStroke(1, Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackgroundViewAllUnSelected.setCornerRadii(bottomRightRadius);

        tabsActiveVieAll.addTab(tabsActiveVieAll.newTab());
        tabsActiveVieAll.addTab(tabsActiveVieAll.newTab());

        LinearLayout tabActive = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textActive = tabActive.findViewById(R.id.textTab);
        textActive.setText(getString(R.string.active_appointmets));
        textActive.setTextColor(getResources().getColor(R.color.white));
        tabActive.setBackground(buttonBackgroundActiveSelected);
        tabsActiveVieAll.getTabAt(0).setCustomView(tabActive);

        LinearLayout tabViewAll = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        TextView textViewAll = tabViewAll.findViewById(R.id.textTab);
        textViewAll.setText(getString(R.string.all));
        textViewAll.setTextColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        tabViewAll.setBackground(buttonBackgroundViewAllUnSelected);
        tabsActiveVieAll.getTabAt(1).setCustomView(tabViewAll);

        tabsActiveVieAll.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.getCustomView().setBackground(buttonBackgroundActiveSelected);
                    if (appointmentActivePatientData != null)
                        setAppointmentAdapter(appointmentActivePatientData);
                } else {
                    tab.getCustomView().setBackground(buttonBackgroundViewAllSelected);
                    if (mDashboardDataModel != null) {
                        if (mDashboardDataModel.getAppointmentPatientDataList() != null)
                            setAppointmentAdapter(mDashboardDataModel.getAppointmentPatientDataList());
                    }
                }
                TextView textView = tab.getCustomView().findViewById(R.id.textTab);
                textView.setTextColor(getResources().getColor(R.color.white));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tab.getCustomView().setBackground(buttonBackgroundActiveUnSelected);
                } else {
                    tab.getCustomView().setBackground(buttonBackgroundViewAllUnSelected);
                }
                TextView textView = tab.getCustomView().findViewById(R.id.textTab);
                textView.setTextColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onResume() {
        if (mDashboardDataModel == null || DMSApplication.ISCancelRequest) {
            mDashboardHelper.doGetDashboardResponse();
            DMSApplication.ISCancelRequest = false;
        }
        setAssetsFromServer();
        super.onResume();
    }

    private void setAssetsFromServer() {
        CommonMethods.setImageUrl(this, DMSConstants.Images.LOGO_SMALL, loginLogo, R.drawable.login_logo);
        CommonMethods.setImageUrl(this, DMSConstants.Images.IC_ACTIONBAR_LOGO, actionBarLogo, R.drawable.ic_launcher);

        layoutTopBackground.setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        settingLogo.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        homeLogo.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        supportLogo.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        patientLogo.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        menuIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        menuIcon1.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        divider1.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        divider2.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        txtDashboardHeader.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        settingText.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        homeText.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        supportText.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        textHeaderTodayAppointment.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        pendingApprovalCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        totalPatientsCount.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        todayAppointmentsCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        waitingPatientCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        admittedPatientCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        buttonBackgroundActiveSelected.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackgroundViewAllSelected.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackgroundActiveUnSelected.setStroke(1, Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackgroundViewAllUnSelected.setStroke(1, Color.parseColor(DMSApplication.COLOR_ACCENT));

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

        float[] bottomLeftRadius = {0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), 0, 0};
        float[] bottomRightRadius = {0, 0, 0, 0, 0, 0, getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8)};

        GradientDrawable buttonLeftBackground = new GradientDrawable();
        buttonLeftBackground.setShape(GradientDrawable.RECTANGLE);
        buttonLeftBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonLeftBackground.setCornerRadii(bottomLeftRadius);

        GradientDrawable buttonRightBackground = new GradientDrawable();
        buttonRightBackground.setShape(GradientDrawable.RECTANGLE);
        buttonRightBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonRightBackground.setCornerRadii(bottomRightRadius);

        Button buttonRight = dialog.findViewById(R.id.button_cancel);
        Button buttonLeft = dialog.findViewById(R.id.button_ok);
        ImageView dialogIcon = dialog.findViewById(R.id.dialogIcon);
        dialogIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        buttonLeft.setBackground(buttonLeftBackground);
        buttonRight.setBackground(buttonRightBackground);

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finishAffinity();
            }
        });
        buttonRight.setOnClickListener(new View.OnClickListener() {
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
            case DMSConstants.TASK_GET_DASHBOARD_RESPONSE:
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    Log.e("mDrawer", "open");
                    mDrawer.closeDrawer(GravityCompat.START);
                }
                DashboardBaseModel mDashboardBaseModel = (DashboardBaseModel) customResponse;
                if (!mDashboardBaseModel.getCommon().getStatusCode().equals(DMSConstants.SUCCESS)) {
                    CommonMethods.showToast(mContext, mDashboardBaseModel.getCommon().getStatusMessage());
                } else if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(mDashboardBaseModel.getCommon().getSuccess())) {

                    mDashboardDataModel = mDashboardBaseModel.getDashboardDataModel();
                    if (mDashboardDataModel != null) {
                        pendingApprovalCount.setText(mDashboardDataModel.getPendingApprovedCount());
                        todayAppointmentsCount.setText(mDashboardDataModel.getAppointmentCount());
                        // waitingPatientCount.setText(mDashboardDataModel.getWaitingCount());
                        admittedPatientCount.setText(mDashboardDataModel.getAdmittedPatientCount());

                        DMSPreferencesManager.putInt(DMSPreferencesManager.DMS_PREFERENCES_KEY.ARCHIVE_API_COUNT, mDashboardDataModel.getViewArchivedApiTakeCount(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COLOR_PRIMARY, mDashboardDataModel.getColorPrimary(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COLOR_DARK_PRIMARY, mDashboardDataModel.getColorPrimaryDark(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COLOR_ACCENT, mDashboardDataModel.getColorAccent(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COLOR_APPOINTMENT_TEXT, mDashboardDataModel.getAppointmentTextColor(), mContext);
                        DMSApplication.COLOR_PRIMARY = mDashboardDataModel.getColorPrimary();
                        DMSApplication.COLOR_DARK_PRIMARY = mDashboardDataModel.getColorPrimaryDark();
                        DMSApplication.COLOR_ACCENT = mDashboardDataModel.getColorAccent();
                        DMSApplication.COLOR_APPOINTMENT_TEXT = mDashboardDataModel.getAppointmentTextColor();
                        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LABEL_UHID, mDashboardDataModel.getLstLabelNameData().getLabelUHID(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LABEL_REF_ID, mDashboardDataModel.getLstLabelNameData().getLabelRefID(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.LABEL_DOCTOR_NAME, mDashboardDataModel.getLstLabelNameData().getLabelDoctorName(), mContext);
                        DMSApplication.LABEL_UHID = mDashboardDataModel.getLstLabelNameData().getLabelUHID();
                        DMSApplication.LABEL_REF_ID = mDashboardDataModel.getLstLabelNameData().getLabelRefID();
                        DMSApplication.LABEL_DOCTOR_NAME = mDashboardDataModel.getLstLabelNameData().getLabelDoctorName();
                        DMSApplication.APPOINTMENT_STATUS_URL = mDashboardDataModel.getAppointmentStatusUrl();
                        setAssetsFromServer();


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(Color.parseColor(DMSApplication.COLOR_DARK_PRIMARY));
                        }
                        appointmentActivePatientData = getBookedAppointment(mDashboardDataModel.getAppointmentPatientDataList());
                        if (tabsActiveVieAll.getVisibility() == View.INVISIBLE)
                            tabsActiveVieAll.setVisibility(View.VISIBLE);

                        if (tabsActiveVieAll.getSelectedTabPosition() == 0) {
                            setAppointmentAdapter(appointmentActivePatientData);
                            TextView textView = tabsActiveVieAll.getTabAt(1).getCustomView().findViewById(R.id.textTab);
                            textView.setTextColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
                        } else {
                            setAppointmentAdapter(mDashboardDataModel.getAppointmentPatientDataList());
                            TextView textView = tabsActiveVieAll.getTabAt(1).getCustomView().findViewById(R.id.textTab);
                            textView.setTextColor(getResources().getColor(R.color.white));
                        }

                        if (mDashboardDataModel.getAppointmentPatientDataList().size() <= 0) {
                            emptyListView.setVisibility(View.VISIBLE);
                            tabsActiveVieAll.setVisibility(View.INVISIBLE);
                        } else {
                            emptyListView.setVisibility(View.GONE);
                            tabsActiveVieAll.setVisibility(View.VISIBLE);
                        }
                    } else {
                        pendingApprovalCount.setText("0");
                        todayAppointmentsCount.setText("0");
                        // waitingPatientCount.setText("0");
                        admittedPatientCount.setText("0");
                        recyclerView.setVisibility(View.GONE);
                        emptyListView.setVisibility(View.VISIBLE);
                        tabsActiveVieAll.setVisibility(View.INVISIBLE);

                    }
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                    tabsActiveVieAll.setVisibility(View.INVISIBLE);
                }
                break;
        }

    }

    private void setAppointmentAdapter(ArrayList<AppointmentPatientData> appointmentPatientData) {
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayoutManager);
        mDashBoardAppointmentListAdapter = new DashboardAppointmentListAdapter(mContext, appointmentPatientData, this);
        recyclerView.setAdapter(mDashBoardAppointmentListAdapter);
    }

    private void setErrorContent() {
        emptyListView.setVisibility(View.VISIBLE);
        pendingApprovalCount.setText("0");
        todayAppointmentsCount.setText("0");
        // waitingPatientCount.setText("0");
        admittedPatientCount.setText("0");
        tabsActiveVieAll.setVisibility(View.INVISIBLE);
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            Log.e("mDrawer", "open");
            mDrawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        if (mDashboardDataModel == null)
            setErrorContent();
        CommonMethods.showErrorDialog(errorMessage, mContext, false, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
            }
        });

    }


    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        if (mDashboardDataModel == null)
            setErrorContent();
        CommonMethods.showErrorDialog(serverErrorMessage, mContext, false, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
            }
        });

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        if (mDashboardDataModel == null)
            setErrorContent();
        CommonMethods.showErrorDialog(serverErrorMessage, mContext, false, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
            }
        });

    }

    @Override
    public void onTimeOutError(String mOldDataTag, final String timeOutErrorMessage) {
        if (mDashboardDataModel == null)
            setErrorContent();
        CommonMethods.showErrorDialog(timeOutErrorMessage, mContext, true, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
                mDashboardHelper.doGetDashboardResponse();
            }
        });

    }

    @OnClick({R.id.layoutDrawerIcon, R.id.layoutTotalPatients, R.id.layoutTodayAppointment, R.id.layoutWaitingPatient, R.id.layoutDrawerSetting, R.id.layoutDrawerSupport, R.id.layoutPendingApproval, R.id.layoutDrawerHome, R.id.layoutAdmittedPatient})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutTodayAppointment:

                Intent myAppointmentsActivity = new Intent(this, MyAppointmentsActivity.class);
                startActivity(myAppointmentsActivity);
                break;
            case R.id.layoutAdmittedPatient:

                Intent admittedPatientsActivity = new Intent(this, AdmittedPatientsActivity.class);
                startActivity(admittedPatientsActivity);

                break;
            case R.id.layoutWaitingPatient:
                if (mDashboardDataModel != null) {
                    Intent todayAppointmentsOrWaitingList = new Intent(this, WaitingMainListActivity.class);
                    startActivity(todayAppointmentsOrWaitingList);
                }
                break;
            case R.id.layoutPendingApproval:

                Intent pendingApprovalList = new Intent(this, RequestedArchivedMainListActivity.class);
                startActivity(pendingApprovalList);

                break;
            case R.id.layoutTotalPatients:
                if (mDashboardDataModel != null) {
                    Intent patientList = new Intent(this, PatientListActivity.class);
                    patientList.putExtra(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mDashboardDataModel.getFileTypes());
                    startActivity(patientList);
                } else {
                    CommonMethods.showErrorDialog(getString(R.string.data_not_found), mContext, true, new ErrorDialogCallback() {
                        @Override
                        public void ok() {
                        }

                        @Override
                        public void retry() {
                            mDashboardHelper.doGetDashboardResponse();
                        }
                    });

                }
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
            case R.id.layoutDrawerHome:
                mDashboardHelper.doGetDashboardResponse();
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

    @Override
    public void onClickedOfEpisodeListButton(SearchResult groupHeader) {
//        Intent intent = new Intent(this, PatientDetailsActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(PATIENT_DETAILS, groupHeader);
//        intent.putExtra(DMSConstants.BUNDLE, bundle);
//        startActivity(intent);

    }

    @Override
    public void onPatientListItemClick(AppointmentPatientData appointmentPatientData) {
        Intent intent = new Intent(mContext, FileTypeViewerActivity.class);
        Bundle extra = new Bundle();
        extra.putString(DMSConstants.PATIENT_ADDRESS, appointmentPatientData.getPatAddress());
        extra.putString(DMSConstants.DOCTOR_NAME, "");
        extra.putString(DMSConstants.PATIENT_ID, appointmentPatientData.getPatientId());
        extra.putString(DMSConstants.PAT_ID, appointmentPatientData.getPatId());
        extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + appointmentPatientData.getPatientName());
        intent.putExtra(DMSConstants.DATA, extra);
        startActivity(intent);
    }


    private ArrayList<AppointmentPatientData> getBookedAppointment(ArrayList<AppointmentPatientData> mAppointmentPatientData) {

        ArrayList<AppointmentPatientData> mAppointListForBookAndConfirm = new ArrayList<>();
        for (int i = 0; i < mAppointmentPatientData.size(); i++) {
            String appointmentStatus = mAppointmentPatientData.get(i).getAppointmentStatus();
            if (!appointmentStatus.contains(DMSConstants.APPOINTMENT_STATUS.COMPLETED_STATUS) && !appointmentStatus.contains(DMSConstants.APPOINTMENT_STATUS.CANCEL_STATUS)) {
                mAppointListForBookAndConfirm.add(mAppointmentPatientData.get(i));
            }
        }
        return mAppointListForBookAndConfirm;
    }


}