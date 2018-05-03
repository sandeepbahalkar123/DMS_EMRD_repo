package com.rescribe.doctor.ui.activities.my_patients;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.my_patients.TemplateAdapter;
import com.rescribe.doctor.helpers.myappointments.AppointmentHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.my_appointments.AppointmentList;
import com.rescribe.doctor.model.my_appointments.ClinicList;
import com.rescribe.doctor.model.my_appointments.PatientList;
import com.rescribe.doctor.model.patient.template_sms.TemplateBaseModel;
import com.rescribe.doctor.model.patient.template_sms.TemplateList;
import com.rescribe.doctor.model.patient.template_sms.request_send_sms.ClinicListForSms;
import com.rescribe.doctor.model.patient.template_sms.request_send_sms.PatientInfoList;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.doctor.ui.activities.my_patients.SendSmsActivity.RESULT_SMS_SEND;

/**
 * Created by jeetal on 21/2/18.
 */

public class TemplateListActivity extends AppCompatActivity implements TemplateAdapter.OnCardViewClickListener {

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    private TemplateListActivity mContext;
    private AppointmentHelper mAppointmentHelper;
    private TemplateAdapter mTemplateAdapter;
    private Intent intent;
    private ArrayList<AppointmentList> appointmentList;
    private ArrayList<TemplateList> templateLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_base_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        mContext = TemplateListActivity.this;
        intent = getIntent();
        if (intent.getExtras() != null) {
            appointmentList = intent.getParcelableArrayListExtra(RescribeConstants.APPOINTMENT_LIST);
            templateLists = intent.getParcelableArrayListExtra(RescribeConstants.TEMPLATE_LIST);
        }

        titleTextView.setText(getString(R.string.template_list));
        if (!templateLists.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);
            mTemplateAdapter = new TemplateAdapter(mContext, templateLists, this);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            // off recyclerView Animation
            RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator)
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            recyclerView.setAdapter(mTemplateAdapter);
        } else {

        }

    }

    @Override
    public void onCardViewClick(TemplateList templateList) {
        ArrayList<PatientList> patientListsToShowOnSmsScreen = new ArrayList<>();
        ArrayList<ClinicListForSms> clinicListForSms = new ArrayList<>();
        for (AppointmentList appointmentList : appointmentList) {
            patientListsToShowOnSmsScreen.addAll(appointmentList.getPatientList());
        }

        for (AppointmentList appointmentList : appointmentList) {
            ClinicListForSms listForSms = new ClinicListForSms();
            ArrayList<PatientInfoList> patientInfoLists = new ArrayList<>();
            listForSms.setClinicId(appointmentList.getClinicId());
            listForSms.setClinicName(appointmentList.getClinicName());
            listForSms.setDocId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.DOC_ID, mContext)));
            listForSms.setLocationId(appointmentList.getLocationId());
            listForSms.setTemplateContent(templateList.getTemplateContent());
            for (PatientList patientList : appointmentList.getPatientList()) {
                PatientInfoList patientInfoList = new PatientInfoList();
                patientInfoList.setHospitalPatId(patientList.getHospitalPatId());
                patientInfoList.setPatientId(patientList.getPatientId());
                patientInfoList.setPatientPhone(patientList.getPatientPhone());
                patientInfoList.setPatientName(patientList.getPatientName());
                patientInfoLists.add(patientInfoList);
            }
            listForSms.setPatientInfoList(patientInfoLists);
            clinicListForSms.add(listForSms);

        }
        //  showSendSmsDialog(clinicListForSms);
        Intent intent = new Intent(this, SendSmsActivity.class);
        intent.putExtra(RescribeConstants.SMS_DETAIL_LIST, clinicListForSms);
        intent.putExtra(RescribeConstants.SMS_PATIENT_LIST_TO_SHOW, patientListsToShowOnSmsScreen);
        startActivityForResult(intent, RESULT_SMS_SEND);

    }

    @OnClick({R.id.backImageView, R.id.titleTextView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.titleTextView:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SMS_SEND) {
            finish();
        }
    }
}


