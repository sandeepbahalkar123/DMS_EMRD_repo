package com.scorg.dms.ui.fragments.patient.my_patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.scorg.dms.R;
import com.scorg.dms.adapters.my_patients.TemplateAdapter;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.model.patient.template_sms.TemplateList;
import com.scorg.dms.model.patient.template_sms.request_send_sms.PatientInfoList;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scorg.dms.ui.fragments.patient.my_patient.SendSmsPatientActivity.RESULT_SEND_SMS;

/**
 * Created by jeetal on 27/2/18.
 */

public class TemplateListForMyPatients extends AppCompatActivity implements TemplateAdapter.OnCardViewClickListener {

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
    private TemplateListForMyPatients mContext;
    private AppointmentHelper mAppointmentHelper;
    private TemplateAdapter mTemplateAdapter;
    private Intent intent;
    private ArrayList<PatientInfoList> patientLists = new ArrayList<>();
    private int mlocationId;
    private int mClinicId;
    private String mClinicName = "";
    private ArrayList<TemplateList> mTemplateLists;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_base_layout);
        ButterKnife.bind(this);
        initialize();

    }

    private void initialize() {
        mContext = TemplateListForMyPatients.this;
        intent = getIntent();
        if (intent.getExtras() != null) {
            patientLists = intent.getParcelableArrayListExtra(DMSConstants.PATIENT_LIST);
            mlocationId = intent.getIntExtra(DMSConstants.LOCATION_ID, 0);
            mClinicId = intent.getIntExtra(DMSConstants.CLINIC_ID, 0);
            mClinicName = intent.getStringExtra(DMSConstants.CLINIC_NAME);
            mTemplateLists = intent.getParcelableArrayListExtra(DMSConstants.TEMPLATE_LIST);
        }

        titleTextView.setText(getString(R.string.template_list));
        if (!mTemplateLists.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);
            mTemplateAdapter = new TemplateAdapter(mContext, mTemplateLists, this);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            // off recyclerView Animation
            RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
            if (animator instanceof SimpleItemAnimator)
                ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
            recyclerView.setAdapter(mTemplateAdapter);

        }

    }
    @Override
    public void onCardViewClick(TemplateList templateList) {

        Intent intent = new Intent(this,SendSmsPatientActivity.class);
        intent.putExtra(DMSConstants.LOCATION_ID, mlocationId);
        intent.putExtra(DMSConstants.CLINIC_ID, mClinicId);
        intent.putExtra(DMSConstants.TEMPLATE_OBJECT,templateList);
        intent.putExtra(DMSConstants.CLINIC_NAME,mClinicName);
        intent.putParcelableArrayListExtra(DMSConstants.PATIENT_LIST, patientLists);
        startActivityForResult(intent,RESULT_SEND_SMS);

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
        if (requestCode == RESULT_SEND_SMS) {
            finish();
        }
    }
}
