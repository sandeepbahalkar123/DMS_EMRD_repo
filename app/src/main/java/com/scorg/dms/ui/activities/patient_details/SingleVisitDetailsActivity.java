package com.scorg.dms.ui.activities.patient_details;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.scorg.dms.R;
import com.scorg.dms.adapters.patient_detail.SingleVisitAdapter;
import com.scorg.dms.helpers.patient_detail.PatientDetailHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.CommonBaseModelContainer;
import com.scorg.dms.model.case_details.PatientHistory;
import com.scorg.dms.model.case_details.Range;
import com.scorg.dms.model.case_details.VisitCommonData;
import com.scorg.dms.model.case_details.VisitData;
import com.scorg.dms.model.case_details.Vital;
import com.scorg.dms.ui.activities.add_records.SelectedRecordsActivity;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import net.gotev.uploadservice.UploadInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scorg.dms.adapters.patient_detail.SingleVisitAdapter.CHILD_TYPE_ATTACHMENTS;
import static com.scorg.dms.adapters.patient_detail.SingleVisitAdapter.CHILD_TYPE_VITALS;
import static com.scorg.dms.adapters.patient_detail.SingleVisitAdapter.TEXT_LIMIT;
import static com.scorg.dms.services.ChatBackUpService.STATUS;
import static com.scorg.dms.services.SyncOfflineRecords.DOC_UPLOAD;
import static com.scorg.dms.services.SyncOfflineRecords.UPLOAD_INFO;
import static com.scorg.dms.ui.fragments.patient.patient_history_fragment.PatientHistoryListFragmentContainer.SELECT_REQUEST_CODE;
import static com.scorg.dms.util.DMSConstants.FILE_STATUS.COMPLETED;
import static com.scorg.dms.util.DMSConstants.FILE_STATUS.FAILED;

/**
 * Created by jeetal on 14/6/17.
 */


public class SingleVisitDetailsActivity extends AppCompatActivity implements HelperResponse, SingleVisitAdapter.OnDeleteAttachments {

    private static final String PAIN_SCALE = "pain scale";
    @BindView(R.id.historyExpandableListView)
    ExpandableListView mHistoryExpandableListView;

    @BindView(R.id.emptyListView)
    RelativeLayout mNoRecordAvailable;

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
    @BindView(R.id.addRecordButton)
    Button addRecordButton;
    private int mLastExpandedPosition = -1;
    private SingleVisitAdapter mSingleVisitAdapter;
    private boolean isBpMin = false;
    private boolean isBpMax = false;
    private String month;
    private String mYear;
    private String mDateSelected;
    private String patientID;
    private String opdID;
    private String mHospitalPatId;
    private String mOpdTime;
    private PatientDetailHelper mSingleVisitDetailHelper;
    private boolean isAllAttachmentDeleted = false;
    private int mAptId ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_detail_activity);
        ButterKnife.bind(this);
        getBundleData();
        initialize();
    }

    private void getBundleData() {

        Intent intent = getIntent();
        userInfoTextView.setVisibility(View.VISIBLE);
        dateTextview.setVisibility(View.VISIBLE);
        if (intent.getExtras() != null) {
            patientID = intent.getStringExtra(DMSConstants.PATIENT_ID);
            mAptId = intent.getIntExtra(DMSConstants.APPOINTMENT_ID,0);
            opdID = intent.getStringExtra(DMSConstants.PATIENT_OPDID);
            mOpdTime = intent.getStringExtra(DMSConstants.OPD_TIME);
            mHospitalPatId = intent.getStringExtra(DMSConstants.PATIENT_HOS_PAT_ID);
            titleTextView.setText(intent.getStringExtra(DMSConstants.PATIENT_NAME));
            userInfoTextView.setText(intent.getStringExtra(DMSConstants.PATIENT_INFO));
            mDateSelected = intent.getStringExtra(DMSConstants.DATE);
            String timeToShow = CommonMethods.formatDateTime(mDateSelected, DMSConstants.DATE_PATTERN.MMM_YY,
                    DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.DATE).toLowerCase();
            String[] timeToShowSpilt = timeToShow.split(",");
            month = timeToShowSpilt[0].substring(0, 1).toUpperCase() + timeToShowSpilt[0].substring(1);
            mYear = timeToShowSpilt.length == 2 ? timeToShowSpilt[1] : "";
            Date date = CommonMethods.convertStringToDate(mDateSelected, DMSConstants.DATE_PATTERN.UTC_PATTERN);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String toDisplay = cal.get(Calendar.DAY_OF_MONTH) + "<sup>" + CommonMethods.getSuffixForNumber(cal.get(Calendar.DAY_OF_MONTH)) + "</sup> " + month + "'" + mYear;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                dateTextview.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
            } else {
                dateTextview.setText(Html.fromHtml(toDisplay));
            }
        }
    }

    private void initialize() {


        mSingleVisitDetailHelper = new PatientDetailHelper(this, this);
        mSingleVisitDetailHelper.doGetOneDayVisit(opdID, patientID);

        // title.setText(getString(R.string.visit_details));

        mHistoryExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

                // this is done because if single element in child list , groupPosition will not expand, it will expand on advice even if it has only one element ,vitals will also expand
                List<PatientHistory> listDataList = mSingleVisitAdapter.getListDataList();
                List<VisitCommonData> childObject = listDataList.get(groupPosition).getCommonData();

                if (childObject.size() == 1) {

                    boolean flag = true;
                    if (listDataList.get(groupPosition).getCaseDetailName().toLowerCase().contains(CHILD_TYPE_ATTACHMENTS) || listDataList.get(groupPosition).getCaseDetailName().toLowerCase().contains(CHILD_TYPE_VITALS))
                        flag = false;

                    if (flag) {
                        if (childObject.get(0).getName().length() <= TEXT_LIMIT)
                            mHistoryExpandableListView.collapseGroup(groupPosition);
                    }
                }

                collapseOther(groupPosition);
            }

            private void collapseOther(int groupPosition) {
                if (mLastExpandedPosition != -1 && mLastExpandedPosition != groupPosition)
                    mHistoryExpandableListView.collapseGroup(mLastExpandedPosition);
                mLastExpandedPosition = groupPosition;
            }
        });

        mHistoryExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()

        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition,
                                        int childPosition, long id) {

                mHistoryExpandableListView.collapseGroup(groupPosition);

                return false;
            }
        });

    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_DELETE_PATIENT_OPD_ATTCHMENTS)) {
            CommonBaseModelContainer common = (CommonBaseModelContainer) customResponse;
            if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(common.getCommonRespose().getSuccess())) {
                isAllAttachmentDeleted = mSingleVisitAdapter.removeSelectedAttachmentFromList();
                if (mSingleVisitAdapter.getListDataList().size() == 1) {
                    if (mSingleVisitAdapter.getListDataList().get(0).getCaseDetailName().equals(PAIN_SCALE)) {
                        mSingleVisitAdapter.getListDataList().remove(0);
                        mSingleVisitAdapter.notifyDataSetChanged();
                        mNoRecordAvailable.setVisibility(View.VISIBLE);
                    }
                } else {
                    mSingleVisitAdapter.notifyDataSetChanged();
                    mNoRecordAvailable.setVisibility(View.GONE);

                }
            }
            CommonMethods.showToast(this, common.getCommonRespose().getStatusMessage());
        } else {
            VisitData visitData = (VisitData) customResponse;
            if (visitData != null) {
                mHistoryExpandableListView.setVisibility(View.VISIBLE);
                mNoRecordAvailable.setVisibility(View.GONE);
            } else {
                mHistoryExpandableListView.setVisibility(View.GONE);
                mNoRecordAvailable.setVisibility(View.VISIBLE);
            }
            List<PatientHistory> patientHistoryList = visitData.getPatientHistory();
            List<Vital> vitalSortedList = new ArrayList<>();
            // Bpmin and Bpmax is clubed together as Bp in vitals
            for (int i = 0; i < patientHistoryList.size(); i++) {
                if (patientHistoryList.get(i).getVitals() != null) {
                    String pos = null;

                    List<Vital> vitalList = patientHistoryList.get(i).getVitals();
                    for (int j = 0; j < vitalList.size(); j++) {

                        Vital dataObject = vitalList.get(j);
                        if (dataObject.getUnitName().contains(getString(R.string.bp_max))) {
                            setBpMax(true);
                        }
                        if (dataObject.getUnitName().contains(getString(R.string.bp_min))) {
                            setBpMin(true);
                        }
                    }

                    for (int j = 0; j < vitalList.size(); j++) {
                        Vital dataObject = vitalList.get(j);
                        if (isBpMax() && isBpMin()) {
                            if (dataObject.getUnitName().contains(getString(R.string.bp_max)) || dataObject.getUnitName().contains(getString(R.string.bp_min))) {
                                Vital vital = new Vital();
                                if (pos == null) {
                                    vital.setUnitName(getString(R.string.bp) + " " + dataObject.getUnitValue());
                                    vital.setUnitValue(dataObject.getUnitValue());
                                    vital.setCategory(dataObject.getCategory());
                                    vital.setIcon(dataObject.getIcon());
                                    for (int k = 0; k < dataObject.getRanges().size(); k++) {
                                        dataObject.getRanges().get(k).setNameOfVital(getString(R.string.bp_max));
                                    }
                                    vital.setRanges(dataObject.getRanges());
                                    vital.setDisplayName(dataObject.getDisplayName());
                                    vitalSortedList.add(vital);
                                    pos = String.valueOf(j);
                                } else {
                                    Vital previousData = vitalSortedList.get(Integer.parseInt(pos));
                                    String unitValue = previousData.getUnitValue();
                                    String unitCategory = previousData.getCategory();
                                    unitCategory = unitCategory + getString(R.string.colon_sign) + dataObject.getCategory();
                                    unitValue = unitValue + "/" + dataObject.getUnitValue();
                                    previousData.setUnitName(getString(R.string.bp));
                                    previousData.setUnitValue(unitValue);
                                    previousData.setCategory(unitCategory);
                                    List<Range> ranges = previousData.getRanges();
                                    ranges.addAll(dataObject.getRanges());
                                    vitalSortedList.set(Integer.parseInt(pos), previousData);
                                }
                            } else {
                                Vital vital = new Vital();
                                vital.setUnitName(vitalList.get(j).getDisplayName());
                                vital.setUnitValue(vitalList.get(j).getUnitValue());
                                vital.setCategory(vitalList.get(j).getCategory());
                                vital.setRanges(vitalList.get(j).getRanges());
                                vital.setIcon(vitalList.get(j).getIcon());
                                vital.setDisplayName(vitalList.get(j).getDisplayName());
                                vitalSortedList.add(vital);
                            }

                        } else {
                            Vital vital;
                            if (dataObject.getUnitName().contains(getString(R.string.bp_max))) {
                                vital = vitalList.get(j);
                                vital.setUnitName("Systolic BP" + " " + vital.getUnitValue());
                                vital.setDisplayName("Systolic BP");
                                vitalSortedList.add(vital);
                            } else if (dataObject.getUnitName().contains(getString(R.string.bp_min))) {
                                vital = vitalList.get(j);
                                vital.setUnitName("Diastolic BP" + " " + vital.getUnitValue());
                                vital.setDisplayName("Diastolic BP");
                                vitalSortedList.add(vital);
                            } else {
                                vital = vitalList.get(j);
                                vital.setUnitName(vitalList.get(j).getDisplayName());
                                vitalSortedList.add(vital);
                            }
                        }
                    }
                    patientHistoryList.get(i).setVitals(vitalSortedList);
                }
            }


            mSingleVisitAdapter = new SingleVisitAdapter(this, patientHistoryList, this);
            mHistoryExpandableListView.setAdapter(mSingleVisitAdapter);
            addRecordButton.setVisibility(View.VISIBLE);


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
        mHistoryExpandableListView.setVisibility(View.GONE);
        mNoRecordAvailable.setVisibility(View.VISIBLE);
    }

    public boolean isBpMin() {
        return isBpMin;
    }

    public void setBpMin(boolean bpMin) {
        isBpMin = bpMin;
    }

    public boolean isBpMax() {
        return isBpMax;
    }

    public void setBpMax(boolean bpMax) {
        isBpMax = bpMax;
    }

    @OnClick({R.id.backImageView, R.id.userInfoTextView, R.id.addRecordButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backImageView:
                if (isAllAttachmentDeleted) {
                    Intent output = new Intent();
                    output.putExtra("SINGLE_PAGE_ADAPTER", isAllAttachmentDeleted);
                    setResult(RESULT_OK, output);
                }
                finish();
                break;
            case R.id.userInfoTextView:
                break;
            case R.id.addRecordButton:
                CommonMethods.getFormattedDate(mDateSelected, DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.DATE_PATTERN.DD_MM_YYYY);
                Intent intent = new Intent(this, SelectedRecordsActivity.class);
                intent.putExtra(DMSConstants.OPD_ID, opdID);
                intent.putExtra(DMSConstants.PATIENT_HOS_PAT_ID, mHospitalPatId);
                intent.putExtra(DMSConstants.LOCATION_ID, "0");
                intent.putExtra(DMSConstants.PATIENT_ID, patientID);
                intent.putExtra(DMSConstants.CLINIC_ID, "0");
                intent.putExtra(DMSConstants.APPOINTMENT_ID,mAptId);
                intent.putExtra(DMSConstants.PATIENT_NAME, titleTextView.getText().toString());
                intent.putExtra(DMSConstants.PATIENT_INFO, userInfoTextView.getText().toString());
                intent.putExtra(DMSConstants.VISIT_DATE, CommonMethods.getFormattedDate(mDateSelected, DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.DATE_PATTERN.DD_MM_YYYY));
                intent.putExtra(DMSConstants.OPD_TIME, mOpdTime);

                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (isAllAttachmentDeleted) {
            Intent output = new Intent();
            output.putExtra("SINGLE_PAGE_ADAPTER", isAllAttachmentDeleted);
            setResult(RESULT_OK, output);
        }
        super.onBackPressed();
    }

    @Override
    public void deleteAttachments(HashSet<VisitCommonData> list) {
        mSingleVisitDetailHelper.deleteSelectedAttachments(list, patientID);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(DOC_UPLOAD)) {
                    UploadInfo uploadInfo = intent.getParcelableExtra(UPLOAD_INFO);
                    int isFailed = intent.getIntExtra(STATUS, FAILED);
                    if (uploadInfo.getFilesLeft().isEmpty() && isFailed == COMPLETED)
                        initialize();
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DOC_UPLOAD);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_REQUEST_CODE)
                initialize();
        }
    }

}

