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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.dashboard.SettingsActivity;
import com.scorg.dms.ui.activities.dashboard.SupportActivity;
import com.scorg.dms.ui.activities.dms_patient_list.PatientDetailsActivity;
import com.scorg.dms.ui.activities.dms_patient_list.PatientListActivity;
import com.scorg.dms.ui.activities.my_appointments.MyAppointmentsActivity;
import com.scorg.dms.ui.activities.pending_approval_list.RequestedArchivedMainListActivity;
import com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.scorg.dms.util.DMSConstants.PATIENT_DETAILS;

/**
 * Created by jeetal on 28/6/17.
 */

@RuntimePermissions
public class HomePageActivity extends BaseActivity implements HelperResponse, DashboardAppointmentListAdapter.OnItemClickListener {

    private static final String TAG = "Home";

    //------------
    @BindView(R.id.totalPatientsCount)
    TextView totalPatientsCount;
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

    ImageView menuImageView;
    TextView appointmentTextView;
    @BindView(R.id.viewTextView)
    TextView viewTextView;

    @BindView(R.id.textHeaderTodayAppointment)
    TextView textHeaderTodayAppointment;


    @BindView(R.id.layoutTotalPatients)
    RelativeLayout layoutTotalPatients;

    @BindView(R.id.layoutTodayAppointment)
    RelativeLayout layoutTodayAppointment;

    @BindView(R.id.layoutWaitingPatient)
    RelativeLayout layoutWaitingPatient;

    @BindView(R.id.layoutPendingApproval)
    RelativeLayout layoutPendingApproval;

    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;

    ImageView menuImageWaitingList;
    TextView menuNameTextView;
    ImageView dashboardArrowImageView;

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
        int width = (int) (getResources().getDisplayMetrics().widthPixels / (CommonMethods.isTablet(mContext) ? 2.5 : 2));
        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
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

    }

    @Override
    protected void onResume() {
        if (mDashboardDataModel == null)
            mDashboardHelper.doGetDashboardResponse();
        setAssetsFromServer();
        super.onResume();
    }

    private void setAssetsFromServer() {
        CommonMethods.setImageUrl(this, DMSConstants.Images.LOGO_SMALL, loginLogo, R.drawable.login_logo);
        CommonMethods.setImageUrl(this, DMSConstants.Images.IC_ACTIONBAR_LOGO, actionBarLogo, R.drawable.ic_launcher);

        settingLogo.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        homeLogo.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        supportLogo.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        patientLogo.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        settingText.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        homeText.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        supportText.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        viewTextView.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        textHeaderTodayAppointment.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        pendingApprovalCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        totalPatientsCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        todayAppointmentsCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        waitingPatientCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
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
                DashboardBaseModel mDashboardBaseModel = (DashboardBaseModel) customResponse;
                if (!mDashboardBaseModel.getCommon().getStatusCode().equals(DMSConstants.SUCCESS)) {
                    CommonMethods.showToast(mContext, mDashboardBaseModel.getCommon().getStatusMessage());
                } else if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(mDashboardBaseModel.getCommon().getSuccess())) {
                    mDashboardDataModel = mDashboardBaseModel.getDashboardDataModel();
                    if (mDashboardDataModel != null) {
                        pendingApprovalCount.setText(mDashboardDataModel.getPendingApprovedCount());
                        totalPatientsCount.setText(mDashboardDataModel.getTotalPatientCount());
                        todayAppointmentsCount.setText(mDashboardDataModel.getAppointmentCount());
                        waitingPatientCount.setText(mDashboardDataModel.getWaitingCount());


                        DMSPreferencesManager.putInt(DMSPreferencesManager.DMS_PREFERENCES_KEY.ARCHIVE_API_COUNT, mDashboardDataModel.getViewArchivedApiTakeCount(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COLOR_PRIMARY, mDashboardDataModel.getColorPrimary(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COLOR_DARK_PRIMARY, mDashboardDataModel.getColorPrimaryDark(), mContext);
                        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COLOR_ACCENT, mDashboardDataModel.getColorAccent(), mContext);
                        DMSApplication.COLOR_PRIMARY = mDashboardDataModel.getColorPrimary();
                        DMSApplication.COLOR_DARK_PRIMARY = mDashboardDataModel.getColorPrimaryDark();
                        DMSApplication.COLOR_ACCENT = mDashboardDataModel.getColorAccent();
                        viewTextView.setTextColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
                        textHeaderTodayAppointment.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
                        pendingApprovalCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
                        totalPatientsCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
                        todayAppointmentsCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
                        waitingPatientCount.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));

                        setAssetsFromServer();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            getWindow().setStatusBarColor(Color.parseColor(DMSApplication.COLOR_DARK_PRIMARY));
                        }
                        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearlayoutManager);
                        mDashBoardAppointmentListAdapter = new DashboardAppointmentListAdapter(mContext, mDashboardDataModel.getAppointmentPatientDataList(), this);
                        recyclerView.setAdapter(mDashBoardAppointmentListAdapter);
                        if (mDashboardDataModel.getAppointmentPatientDataList().size() <= 0)
                            emptyListView.setVisibility(View.VISIBLE);


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

    @OnClick({R.id.viewTextView, R.id.layoutDrawerIcon, R.id.layoutTotalPatients, R.id.layoutTodayAppointment, R.id.layoutWaitingPatient, R.id.layoutDrawerSetting, R.id.layoutDrawerSupport, R.id.layoutPendingApproval, R.id.layoutDrawerHome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutTodayAppointment:
                if (mDashboardDataModel != null && !mDashboardDataModel.getAppointmentCount().equalsIgnoreCase("0")) {
                    Intent myAppointmentsActivity = new Intent(this, MyAppointmentsActivity.class);
                    startActivity(myAppointmentsActivity);
                }
                break;
            case R.id.layoutWaitingPatient:
                if (mDashboardDataModel != null && !mDashboardDataModel.getWaitingCount().equalsIgnoreCase("0")) {
                    Intent todayAppointmentsOrWaitingList = new Intent(this, WaitingMainListActivity.class);
                    startActivity(todayAppointmentsOrWaitingList);
                }
                break;

            case R.id.layoutPendingApproval:
                if (mDashboardDataModel != null && !mDashboardDataModel.getPendingApprovedCount().equalsIgnoreCase("0")) {
                    Intent pendingApprovalList = new Intent(this, RequestedArchivedMainListActivity.class);
                    startActivity(pendingApprovalList);
                }
                break;
            case R.id.layoutTotalPatients:
                if (mDashboardDataModel != null && !mDashboardDataModel.getTotalPatientCount().equalsIgnoreCase("0")) {
                    Intent patientList = new Intent(this, PatientListActivity.class);
                    patientList.putExtra(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mDashboardDataModel.getFileTypes());
                    startActivity(patientList);
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
            case R.id.viewTextView:
                if (!mDashboardDataModel.getAppointmentCount().equalsIgnoreCase("0") && mDashboardDataModel != null) {
                    Intent myAppointmentsActivity = new Intent(this, MyAppointmentsActivity.class);
                    startActivity(myAppointmentsActivity);
                }
                break;
            case R.id.layoutDrawerHome:
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
        Intent intent = new Intent(this, PatientDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PATIENT_DETAILS, groupHeader);
        intent.putExtra(DMSConstants.BUNDLE, bundle);
        startActivity(intent);

    }

}