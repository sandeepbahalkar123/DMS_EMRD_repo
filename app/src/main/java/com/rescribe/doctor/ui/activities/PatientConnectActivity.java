package com.rescribe.doctor.ui.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.widget.Button;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rescribe.doctor.R;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.chat.MQTTMessage;
import com.rescribe.doctor.model.patient.patient_connect.PatientData;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.services.ChatBackUpService;
import com.rescribe.doctor.services.MQTTService;
import com.rescribe.doctor.ui.customesViews.CustomProgressDialog;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.doctor.ui.fragments.patient.patient_connect.PatientConnectChatFragment;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rescribe.doctor.preference.RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.BACK_UP;
import static com.rescribe.doctor.services.ChatBackUpService.CHAT_BACKUP;
import static com.rescribe.doctor.services.ChatBackUpService.RUNNING;
import static com.rescribe.doctor.services.ChatBackUpService.STATUS;
import static com.rescribe.doctor.services.MQTTService.MESSAGE_TOPIC;
import static com.rescribe.doctor.services.MQTTService.NOTIFY;
import static com.rescribe.doctor.services.MQTTService.TOPIC;
import static com.rescribe.doctor.util.RescribeConstants.ACTIVE_STATUS;


/**
 * Created by jeetal on 5/9/17.
 */

public class PatientConnectActivity extends AppCompatActivity implements HelperResponse {

    private final static String TAG = "DoctorConnect";
    public static final int PATIENT_CONNECT_REQUEST = 1111;

    private CustomProgressDialog customProgressDialog;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null) {
                if (intent.getAction().equals(NOTIFY)) {

                    String topic = intent.getStringExtra(MQTTService.TOPIC_KEY);
                    topic = topic == null ? "" : topic;

                    if (intent.getBooleanExtra(MQTTService.DELIVERED, false)) {

                        Log.d(TAG, "Delivery Complete");
                        Log.d(TAG + " MSG_ID", intent.getStringExtra(MQTTService.MESSAGE_ID));

                    } else if (topic.equals(TOPIC[MESSAGE_TOPIC])) {
                        // User message
                        CommonMethods.Log(TAG, "User message");
                        MQTTMessage message = intent.getParcelableExtra(MQTTService.MESSAGE);
                        mPatientConnectChatFragment.notifyCount(message);
                    }
                } else if (intent.getAction().equals(CHAT_BACKUP)) {
                    boolean isFailed = intent.getBooleanExtra(STATUS, false);
                    if (!isFailed) {
                        addFragment();
                        customProgressDialog.dismiss();
                    } else {
                        // You can retry here
                    }
                }
            }
        }
    };

    @BindView(R.id.backButton)
    ImageView mBackButton;
    @BindView(R.id.searchImageView)
    ImageView searchImageView;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.searchEditText)
    EditTextWithDeleteButton mSearchEditText;

    @BindView(R.id.toolbar)
    RelativeLayout toolbar;

    @BindView(R.id.container)
    FrameLayout container;

    public static final int PAID = 1;
    public static final int FREE = 0;
    //-----
    private PatientConnectChatFragment mPatientConnectChatFragment;
    private ArrayList<PatientData> mReceivedConnectedPatientDataList;
    private boolean isCanceled = true;
    //-----

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_connect);
        ButterKnife.bind(this);

        title.setText("" + getString(R.string.patient_connect));
        customProgressDialog = new CustomProgressDialog(this);
        customProgressDialog.setCancelable(true);
        customProgressDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (isCanceled)
                    onBackPressed();
            }
        });
        initialize();
    }


    private void initialize() {

        if (RUNNING)
            customProgressDialog.show();
        else {
            boolean isBackupTaken = RescribePreferencesManager.getBoolean(BACK_UP, this);

            if (!isBackupTaken) {

                final Dialog dialog = new Dialog(this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.chat_backup_prompt);
                dialog.setCancelable(false);

                Button button_skip = (Button) dialog.findViewById(R.id.button_skip);
                Button button_ok = (Button) dialog.findViewById(R.id.button_ok);

                button_skip.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        RescribePreferencesManager.putBoolean(BACK_UP, true, PatientConnectActivity.this);
                        dialog.dismiss();
                        addFragment();
                    }
                });

                button_ok.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent startIntentUpload = new Intent(PatientConnectActivity.this, ChatBackUpService.class);
                        startIntentUpload.setAction(RescribeConstants.STARTFOREGROUND_ACTION);
                        startService(startIntentUpload);
                        dialog.dismiss();
                        customProgressDialog.show();
                    }
                });
                dialog.show();

            } else {
                addFragment();
            }
        }
        //-----------
        mSearchEditText.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPatientConnectChatFragment.setOnClickOfSearchBar(s.toString());
            }
        });

        mSearchEditText.addClearTextButtonListener(new EditTextWithDeleteButton.OnClearButtonClickedInEditTextListener() {
            @Override
            public void onClearButtonClicked() {
                title.setVisibility(View.VISIBLE);
                mSearchEditText.setVisibility(View.GONE);
                searchImageView.setVisibility(View.VISIBLE);
                mSearchEditText.setText("");
            }
        });
        //-----------
    }

    private void addFragment() {
        isCanceled = false;
        mPatientConnectChatFragment = PatientConnectChatFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, mPatientConnectChatFragment);
        ft.commit();
    }


    public ArrayList<PatientData> getReceivedConnectedPatientDataList() {
        return mReceivedConnectedPatientDataList;
    }

    public void setReceivedConnectedPatientDataList(ArrayList<PatientData> mReceivedConnectedPatientDataList) {
        this.mReceivedConnectedPatientDataList = mReceivedConnectedPatientDataList;
    }


    // Recent

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NOTIFY);
        intentFilter.addAction(CHAT_BACKUP);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Activity.RESULT_OK == resultCode) {
            if (requestCode == PATIENT_CONNECT_REQUEST) {
                PatientData patientData = data.getParcelableExtra(RescribeConstants.CHAT_USERS);
                mPatientConnectChatFragment.addItem(patientData);
            }
        }
    }

    @OnClick({R.id.backButton, R.id.searchImageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backButton:
                if (mSearchEditText.getVisibility() == View.VISIBLE) {
                    title.setVisibility(View.VISIBLE);
                    mSearchEditText.setVisibility(View.GONE);
                    searchImageView.setVisibility(View.VISIBLE);
                    mSearchEditText.setText("");
                } else {
                    onBackPressed();
                }
                break;
            case R.id.searchImageView:
                title.setVisibility(View.GONE);
                mSearchEditText.setVisibility(View.VISIBLE);
                searchImageView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(ACTIVE_STATUS))
            CommonMethods.Log(ACTIVE_STATUS, "active");
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

}