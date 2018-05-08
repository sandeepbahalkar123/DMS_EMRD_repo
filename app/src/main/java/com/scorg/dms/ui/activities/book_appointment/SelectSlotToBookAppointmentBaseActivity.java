package com.scorg.dms.ui.activities.book_appointment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.scorg.dms.R;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.patient.doctor_patients.PatientList;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.fragments.book_appointment.SelectSlotTimeToBookAppointmentFragment;
import com.scorg.dms.util.DMSConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectSlotToBookAppointmentBaseActivity extends AppCompatActivity implements HelperResponse {


    @BindView(R.id.viewContainer)
    FrameLayout viewContainer;
    @BindView(R.id.coachmarkContainer)
    FrameLayout coachmarkContainer;
    @BindView(R.id.backImageView)
    ImageView backImageView;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.userInfoTextView)
    CustomTextView userInfoTextView;
    @BindView(R.id.dateTextview)
    CustomTextView dateTextview;
    @BindView(R.id.year)
    Spinner year;
    @BindView(R.id.addImageView)
    ImageView addImageView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_slot_book_app_layout);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        titleTextView.setText(getString(R.string.book_appointment));
        Intent intent = getIntent();

        Bundle extras = intent.getExtras();
        if (extras == null) {
            extras = new Bundle();
        }

        if (intent.getExtras() != null) {
            PatientList patientListObject = intent.getParcelableExtra(DMSConstants.PATIENT_INFO);
           String patientDetail = intent.getStringExtra(DMSConstants.PATIENT_DETAILS);
           boolean isAppointmentTypeReschedule = intent.getBooleanExtra(DMSConstants.IS_APPOINTMENT_TYPE_RESHEDULE,false);
           extras.putParcelable(DMSConstants.PATIENT_INFO, patientListObject);
           extras.putString(DMSConstants.PATIENT_DETAILS,patientDetail);
           extras.putBoolean(DMSConstants.IS_APPOINTMENT_TYPE_RESHEDULE,isAppointmentTypeReschedule);
        }

        SelectSlotTimeToBookAppointmentFragment mSelectSlotTimeToBookAppointmentFragment = SelectSlotTimeToBookAppointmentFragment.newInstance(extras);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewContainer, mSelectSlotTimeToBookAppointmentFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {

    }

    @OnClick({R.id.backImageView, R.id.titleTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                onBackPressed();
                break;
            case R.id.titleTextView:
                break;
        }
    }
}
