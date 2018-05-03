package com.rescribe.doctor.ui.fragments.patient.my_patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.my_patients.SmsRecepientPatientListAdapter;
import com.rescribe.doctor.helpers.myappointments.AppointmentHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.patient.template_sms.TemplateBaseModel;
import com.rescribe.doctor.model.patient.template_sms.TemplateList;
import com.rescribe.doctor.model.patient.template_sms.request_send_sms.ClinicListForSms;
import com.rescribe.doctor.model.patient.template_sms.request_send_sms.PatientInfoList;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by jeetal on 27/2/18.
 */

public class SendSmsPatientActivity extends AppCompatActivity implements SmsRecepientPatientListAdapter.OnCardViewClickListener, HelperResponse {

    public static final int RESULT_SEND_SMS = 2020;
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
    private Intent intent;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<PatientInfoList> patientLists = new ArrayList<>();
    private TemplateList mTemplateList = new TemplateList();
    private AppointmentHelper mAppointmentHelper;
    private boolean isSendEnabled;
    private int mlocationId;
    private int mClinicId;
    private ArrayList<ClinicListForSms> clinicListForSmsSend;
    private SmsRecepientPatientListAdapter mSmsRecepientPatientListAdapter;
    private String mClinicName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_sms_base_layout);
        ButterKnife.bind(this);
        initialize();
    }

    private void initialize() {
        mContext = SendSmsPatientActivity.this;
        intent = getIntent();
        titleTextView.setText(getString(R.string.send_sms));
        if (intent.getExtras() != null) {
            patientLists = intent.getParcelableArrayListExtra(RescribeConstants.PATIENT_LIST);
            mTemplateList = intent.getParcelableExtra(RescribeConstants.TEMPLATE_OBJECT);
            mlocationId = intent.getIntExtra(RescribeConstants.LOCATION_ID, 0);
            mClinicId = intent.getIntExtra(RescribeConstants.CLINIC_ID, 0);
            mClinicName = intent.getStringExtra(RescribeConstants.CLINIC_NAME);
        }
        recyclerView.setVisibility(View.VISIBLE);

        mSmsRecepientPatientListAdapter = new SmsRecepientPatientListAdapter(mContext, patientLists, this);

        ChipsLayoutManager spanLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setOrientation(ChipsLayoutManager.HORIZONTAL).setScrollingEnabled(true)
                .build();


        recyclerView.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelOffset(R.dimen.dp10),
                getResources().getDimensionPixelOffset(R.dimen.dp10)));
        recyclerView.setLayoutManager(spanLayoutManager);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 10);
        recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 10);
        recyclerView.setAdapter(mSmsRecepientPatientListAdapter);
        if(mTemplateList==null) {
            editTextSmsContent.setHint(getString(R.string.enter_sms_text_here));
        }else{
            editTextSmsContent.setText(mTemplateList.getTemplateContent());

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

    @OnClick({R.id.backImageView, R.id.titleTextView, R.id.sendSmsButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                finish();
                break;
            case R.id.titleTextView:
                break;
            case R.id.sendSmsButton:
                if (!editTextSmsContent.getText().toString().equals("") && mSmsRecepientPatientListAdapter.getSmsReciptentList().size() > 0) {
                    setSendEnabled(false);
                    if (!patientLists.isEmpty()) {
                        clinicListForSmsSend = new ArrayList<>();
                        ClinicListForSms clinicListForSms = new ClinicListForSms();
                        clinicListForSms.setPatientInfoList(patientLists);
                        clinicListForSms.setLocationId(mlocationId);
                        if (editTextSmsContent.getText().toString().contains("hospitalName")) {
                            String originalMsgContent = editTextSmsContent.getText().toString();
                            String clinicName = mClinicName;
                            String messageContent = originalMsgContent.replaceAll("hospitalName", clinicName);
                            clinicListForSms.setTemplateContent(messageContent);
                        } else {
                            clinicListForSms.setTemplateContent(editTextSmsContent.getText().toString());
                        }
                        clinicListForSms.setDocId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.DOC_ID, mContext)));
                        clinicListForSms.setClinicId(mClinicId);
                        clinicListForSmsSend.add(clinicListForSms);
                    }
                    mAppointmentHelper = new AppointmentHelper(mContext, this);
                    mAppointmentHelper.doRequestSendSMS(clinicListForSmsSend);
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
    public void onPhoneNumberClick(PatientInfoList patientList) {
        if (mSmsRecepientPatientListAdapter.getSmsReciptentList().size() == 0) {
            finish();
        } else {

        }
        ArrayList<PatientInfoList> patientInfoListsTempList = new ArrayList<>();
        for (PatientInfoList listForSms : patientLists) {

            if (listForSms.getPatientId().equals(patientList.getPatientId())) {
                patientInfoListsTempList.add(listForSms);
            }

            patientLists.removeAll(patientInfoListsTempList);
        }

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_REQUEST_SEND_SMS)) {
            TemplateBaseModel templateBaseModel = (TemplateBaseModel) customResponse;
            if (templateBaseModel != null)
                Toast.makeText(mContext, templateBaseModel.getCommon().getStatusMessage(), Toast.LENGTH_SHORT).show();
            setResult(RESULT_SEND_SMS);
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
