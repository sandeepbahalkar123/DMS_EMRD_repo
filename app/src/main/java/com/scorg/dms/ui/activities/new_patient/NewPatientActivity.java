package com.scorg.dms.ui.activities.new_patient;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.scorg.dms.R;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.new_patient.NewPatientBaseModel;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.fragments.new_patient.NewPatientFragment;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import java.util.HashSet;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 17/3/18.
 */
@RuntimePermissions
public class NewPatientActivity extends AppCompatActivity implements HelperResponse {
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
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    private Context mContext;
    public HashSet<Integer> selectedDoctorId = new HashSet<>();
    private String phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_opd_base_layout);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mContext = NewPatientActivity.this;
        titleTextView.setText(getString(R.string.today_new_patients));
        AppointmentHelper mAppointmentHelper = new AppointmentHelper(this, this);
        mAppointmentHelper.doGetNewPatientList();
        //  setUpNavigationDrawer();
    }



    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_GET_NEW_PATIENT_LIST)) {

            if (customResponse != null) {
                NewPatientBaseModel mNewPatientBaseModel = (NewPatientBaseModel) customResponse;
                Bundle bundle = new Bundle();
                bundle.putParcelable(DMSConstants.MY_PATIENTS_DATA, mNewPatientBaseModel);
                NewPatientFragment mNewPatientFragment = NewPatientFragment.newInstance(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, mNewPatientFragment).commit();
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

    @OnClick({R.id.backImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void callPatient(String patientPhone) {
        phoneNo = patientPhone;
        NewPatientActivityPermissionsDispatcher.doCallSupportWithCheck(this);
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
        NewPatientActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
