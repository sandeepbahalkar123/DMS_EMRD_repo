package com.rescribe.doctor.ui.activities.my_patients;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.my_patients.SmsRecepientListAdapter;
import com.rescribe.doctor.adapters.my_patients.TemplateAdapter;
import com.rescribe.doctor.helpers.myappointments.AppointmentHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.my_appointments.ClinicList;
import com.rescribe.doctor.model.my_appointments.PatientList;
import com.rescribe.doctor.model.patient.template_sms.TemplateBaseModel;
import com.rescribe.doctor.model.patient.template_sms.TemplateList;
import com.rescribe.doctor.model.patient.template_sms.request_send_sms.ClinicListForSms;
import com.rescribe.doctor.model.patient.template_sms.request_send_sms.PatientInfoList;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jeetal on 22/2/18.
 */

public class SendSmsActivity extends AppCompatActivity implements SmsRecepientListAdapter.OnCardViewClickListener, HelperResponse {

    public static final int RESULT_SMS_SEND = 1010;
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
    @BindView(R.id.toTextView)
    CustomTextView toTextView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.editTextSmsContent)
    EditText editTextSmsContent;
    @BindView(R.id.sendSmsButton)
    ImageView sendSmsButton;
    private Context mContext;
    private ArrayList<ClinicListForSms> clinicSmsInfoList = new ArrayList<>();
    private SmsRecepientListAdapter mSmsRecepientListAdapter;
    private ArrayList<PatientList> patientLists = new ArrayList<>();
    private boolean isSendEnabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_sms_base_layout);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mContext = SendSmsActivity.this;
        Intent intent = getIntent();
        titleTextView.setText(getString(R.string.send_sms));
        if (intent.getExtras() != null) {
            clinicSmsInfoList = intent.getParcelableArrayListExtra(RescribeConstants.SMS_DETAIL_LIST);
            patientLists = intent.getParcelableArrayListExtra(RescribeConstants.SMS_PATIENT_LIST_TO_SHOW);
        }
        recyclerView.setVisibility(View.VISIBLE);

        mSmsRecepientListAdapter = new SmsRecepientListAdapter(mContext, patientLists, this);

        ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setOrientation(ChipsLayoutManager.HORIZONTAL).setScrollingEnabled(true)
                .build();

        recyclerView.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.dp10),
                getResources().getDimensionPixelOffset(R.dimen.dp10)));
        recyclerView.setLayoutManager(spanLayoutManager);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 10);
        recyclerView.setAdapter(mSmsRecepientListAdapter);
        if (clinicSmsInfoList.get(0).getTemplateContent().equals("")) {
            editTextSmsContent.setHint(getString(R.string.enter_sms_text_here));
        } else {
            editTextSmsContent.setText(clinicSmsInfoList.get(0).getTemplateContent());
        }

        editTextSmsContent.setScroller(new Scroller(getApplicationContext()));
        editTextSmsContent.setVerticalScrollBarEnabled(true);
        editTextSmsContent.setMinLines(1);
        editTextSmsContent.setMaxLines(4);
        editTextSmsContent.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().equals("")) {
                    setSendEnabled(true);
                    sendSmsButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.send_button));
                } else {
                    setSendEnabled(false);
                    sendSmsButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.sendbutton_grey));
                }
            }
        });

    }

    @OnClick({R.id.backImageView, R.id.sendSmsButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.sendSmsButton:
                if (!editTextSmsContent.getText().toString().equals("") && mSmsRecepientListAdapter.getSmsReciptentList().size() > 0) {
                    setSendEnabled(false);
                    AppointmentHelper mAppointmentHelper = new AppointmentHelper(mContext, this);
                    for (ClinicListForSms listForSms : clinicSmsInfoList) {
                        if (editTextSmsContent.getText().toString().contains("hospitalName")) {
                            String originalMsgContent = editTextSmsContent.getText().toString();
                            String clinicName = listForSms.getClinicName();
                            String messageContent = originalMsgContent.replaceAll("hospitalName", clinicName);
                            listForSms.setTemplateContent(messageContent);
                        } else {
                            listForSms.setTemplateContent(editTextSmsContent.getText().toString());
                        }
                    }
                    mAppointmentHelper.doRequestSendSMS(clinicSmsInfoList);
                } else {
                    sendSmsButton.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.sendbutton_grey));
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public void onPhoneNumberClick(PatientList patientList) {
        if (mSmsRecepientListAdapter.getSmsReciptentList().size() == 0) {
            finish();
        } else {

        }
        for (ClinicListForSms listForSms : clinicSmsInfoList) {

            ArrayList<PatientInfoList> mClinicListsTemp = new ArrayList<>();
            for (PatientInfoList patientInfoListObject : listForSms.getPatientInfoList()) {
                if (patientInfoListObject.getPatientId().equals(patientList.getPatientId())) {
                    mClinicListsTemp.add(patientInfoListObject);
                }
            }
            listForSms.getPatientInfoList().removeAll(mClinicListsTemp);

        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_REQUEST_SEND_SMS)) {
            TemplateBaseModel templateBaseModel = (TemplateBaseModel) customResponse;
            if (templateBaseModel != null)
                Toast.makeText(mContext, templateBaseModel.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
            setResult(RESULT_SMS_SEND);
            finish();

        }
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

    public boolean isSendEnabled() {
        return isSendEnabled;
    }

    public void setSendEnabled(boolean sendEnabled) {
        isSendEnabled = sendEnabled;
    }
}
