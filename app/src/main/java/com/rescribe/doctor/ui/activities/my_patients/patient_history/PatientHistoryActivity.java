package com.rescribe.doctor.ui.activities.my_patients.patient_history;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rescribe.doctor.R;
import com.rescribe.doctor.ui.fragments.patient.patient_history_fragment.PatientHistoryListFragmentContainer;
import com.rescribe.doctor.util.RescribeConstants;

import net.gotev.uploadservice.UploadInfo;

import static com.rescribe.doctor.services.ChatBackUpService.STATUS;
import static com.rescribe.doctor.services.SyncOfflineRecords.DOC_UPLOAD;
import static com.rescribe.doctor.services.SyncOfflineRecords.UPLOAD_INFO;
import static com.rescribe.doctor.ui.fragments.patient.patient_history_fragment.PatientHistoryListFragmentContainer.SELECT_REQUEST_CODE;
import static com.rescribe.doctor.util.RescribeConstants.FILE_STATUS.COMPLETED;
import static com.rescribe.doctor.util.RescribeConstants.FILE_STATUS.FAILED;

/**
 * Created by jeetal on 31/7/17.
 */

public class PatientHistoryActivity extends AppCompatActivity {

    private PatientHistoryListFragmentContainer fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_history);
        addFragment();
    }

    private void addFragment() {
        fragment = new PatientHistoryListFragmentContainer();
        fragment.setArguments(getIntent().getBundleExtra(RescribeConstants.PATIENT_INFO));
        getSupportFragmentManager().beginTransaction().replace(R.id.viewContainer, fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_REQUEST_CODE)
                fragment.initialize();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DOC_UPLOAD);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals(DOC_UPLOAD)) {
                    UploadInfo uploadInfo = intent.getParcelableExtra(UPLOAD_INFO);
                    int isFailed = intent.getIntExtra(STATUS, FAILED);
                    if (uploadInfo.getFilesLeft().isEmpty() && isFailed == COMPLETED)
                        fragment.initialize();
                }
            }
        }
    };


}
