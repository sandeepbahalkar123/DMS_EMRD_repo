package com.rescribe.doctor.ui.fragments.patient.patient_connect;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.patient_connect.PatientConnectAdapter;
import com.rescribe.doctor.adapters.patient_connect.SearchedMessagesAdapter;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.Common;
import com.rescribe.doctor.model.chat.MQTTMessage;
import com.rescribe.doctor.model.patient.patient_connect.ChatPatientConnectModel;
import com.rescribe.doctor.model.patient.patient_connect.PatientData;
import com.rescribe.doctor.ui.activities.my_patients.ShowMyPatientsListActivity;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.rescribe.doctor.services.MQTTService.DOCTOR;
import static com.rescribe.doctor.services.MQTTService.PATIENT;
import static com.rescribe.doctor.ui.activities.PatientConnectActivity.PATIENT_CONNECT_REQUEST;
import static com.rescribe.doctor.util.RescribeConstants.USER_STATUS.OFFLINE;

/**
 * Created by jeetal on 6/9/17.
 */

public class PatientConnectChatFragment extends Fragment implements HelperResponse, PatientConnectAdapter.FilterListener {

    @BindView(R.id.patientRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.searchRecyclerView)
    RecyclerView searchRecyclerView;
    @BindView(R.id.emptyListView)
    RelativeLayout mEmptyListView;
    Unbinder unbinder;
    @BindView(R.id.leftFab)
    FloatingActionButton f;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;

    private ArrayList<PatientData> mReceivedPatientDataList = new ArrayList<>();
    private PatientConnectAdapter mPatientConnectAdapter;
    private SearchedMessagesAdapter searchedMessagesAdapter;
    private ArrayList<MQTTMessage> mqttMessages = new ArrayList<>();
    private AppDBHelper appDBHelper;
    private String searchText = "";
    private String preSearchText = "pre";

    public static PatientConnectChatFragment newInstance() {
        PatientConnectChatFragment fragment = new PatientConnectChatFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PatientConnectChatFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.patient_connect_fragment, container, false);
        unbinder = ButterKnife.bind(this, mRootView);

        initialize();
        return mRootView;
    }

    private void initialize() {

        appDBHelper = new AppDBHelper(getContext());

//        PatientConnectHelper mPatientConnectHelper = new PatientConnectHelper(getActivity(), this);

        mRecyclerView.setNestedScrollingEnabled(false);
        searchRecyclerView.setNestedScrollingEnabled(false);

        getPatientList();

        mPatientConnectAdapter = new PatientConnectAdapter(getActivity(), mReceivedPatientDataList, this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setAdapter(mPatientConnectAdapter);

        searchedMessagesAdapter = new SearchedMessagesAdapter(getContext(), mqttMessages);
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        searchRecyclerView.addItemDecoration(dividerItemDecoration);
        LinearLayoutManager mLayoutM = new LinearLayoutManager(getActivity());
        searchRecyclerView.setLayoutManager(mLayoutM);
        searchRecyclerView.setAdapter(searchedMessagesAdapter);

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (searchText.equals(preSearchText)) {
                    CommonMethods.hideKeyboard(getActivity());
                }
                preSearchText = searchText;
            }
        });
    }

    private void getPatientList() {
        mReceivedPatientDataList.clear();
        Cursor cursor = appDBHelper.getChatUsers();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                PatientData patientData = new PatientData();
                patientData.setOnlineStatus(OFFLINE); // hardcoded
                patientData.setLastChatTime(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_TIME)));
                patientData.setLastChat(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG)));
//                    cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_ID)
                patientData.setId(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.USER2ID)));
//                    cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.USER1ID));

                String patientName = cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER)).equals(PATIENT) ? cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER_NAME)) : cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_NAME));
                patientData.setPatientName(patientName);

//                    cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SPECIALITY));
//                    cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_STATUS));

                String imageUrl = cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER)).equals(PATIENT) ? cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER_IMG_URL)) : cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_IMG_URL));
                patientData.setImageUrl(imageUrl);
//                    cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.FILE_URL));
//                    cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.FILE_TYPE));

                patientData.setSalutation(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SALUTATION)));
//                    cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_NAME));
//                    cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_IMG_URL));

//                    cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.UPLOAD_STATUS));
//                    cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.DOWNLOAD_STATUS));
//                    cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.READ_STATUS));

                mReceivedPatientDataList.add(patientData);
                cursor.moveToNext();
            }
        }

        cursor.close();
        appDBHelper.close();

        if (mReceivedPatientDataList.isEmpty()) {
            mEmptyListView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyListView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.GET_PATIENT_CHAT_LIST)) {
            ChatPatientConnectModel patientConnectBaseModel = (ChatPatientConnectModel) customResponse;
            ChatPatientConnectModel.PatientListData patientListData = patientConnectBaseModel.getPatientListData();
            if (patientListData.getPatientDataList().isEmpty()) {
                mEmptyListView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {

                AppDBHelper appDBHelper = new AppDBHelper(getContext());

                for (PatientData patientData : patientListData.getPatientDataList()) {
                    MQTTMessage mqttMessage = appDBHelper.getLastChatMessagesByPatientId(patientData.getId());
                    if (mqttMessage != null)
                        patientData.setLastChatTime(mqttMessage.getMsgTime());
                    mReceivedPatientDataList.add(patientData);
                }
                notifyDataChanged();
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        mEmptyListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        mEmptyListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        mEmptyListView.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    public void notifyDataChanged() {
        if (mPatientConnectAdapter != null)
            if (!mReceivedPatientDataList.isEmpty())
                mPatientConnectAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        notifyDataChanged();
    }

    public void notifyCount(MQTTMessage message) {
        boolean isThere = false;
        if (mReceivedPatientDataList != null && mPatientConnectAdapter != null) {
            for (int index = 0; index < mReceivedPatientDataList.size(); index++) {
                if (mReceivedPatientDataList.get(index).getId() == message.getPatId()) {
                    if (index != 0)
                        moveItemOnTop(message.getMsgTime(), index);
                    isThere = true;
                    break;
                }
            }

            if (!isThere) {
                PatientData patientData = new PatientData();
                patientData.setId(message.getPatId()); // Change
                patientData.setPatientName(message.getSenderName());
                patientData.setImageUrl(message.getSenderImgUrl());
                patientData.setUnreadMessages(1);
                patientData.setOnlineStatus(RescribeConstants.USER_STATUS.ONLINE);
                mReceivedPatientDataList.add(0, patientData);
            }

            if (mReceivedPatientDataList.isEmpty()) {
                mEmptyListView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mEmptyListView.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            mPatientConnectAdapter.notifyDataSetChanged();
        }
    }

    public void addItem(PatientData patientData) {

        boolean isThere = false;
        for (int index = 0; index < mReceivedPatientDataList.size(); index++) {
            if (mReceivedPatientDataList.get(index).getId().equals(patientData.getId())) {
                if (index != 0)
                    moveItemOnTop(patientData.getLastChatTime(), index);
                isThere = true;
                break;
            }
        }

        if (!isThere)
            mReceivedPatientDataList.add(0, patientData);

        if (mReceivedPatientDataList.isEmpty()) {
            mEmptyListView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            mEmptyListView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        mPatientConnectAdapter.notifyDataSetChanged();
    }

    private void moveItemOnTop(String time, int index) {
        PatientData patientData1 = mReceivedPatientDataList.remove(index);
        patientData1.setLastChatTime(time);
        mReceivedPatientDataList.add(0, patientData1);
    }

    public void setOnClickOfSearchBar(String searchText) {

        if (mPatientConnectAdapter != null)
            mPatientConnectAdapter.getFilter().filter(searchText);

        if (searchText.isEmpty()) {
            searchRecyclerView.setVisibility(View.GONE);
            if (mReceivedPatientDataList.isEmpty()) {
                mEmptyListView.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyListView.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void result(String searchText, ArrayList<PatientData> dataList) {
        searchRecyclerView.setVisibility(View.VISIBLE);
        this.searchText = searchText;

        mqttMessages.clear();
        Cursor cursor = appDBHelper.searchChatMessagesByChars(searchText);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    MQTTMessage mqttMessage = new MQTTMessage();

                    mqttMessage.setMsgId(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_ID)));
                    mqttMessage.setMsg(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG)));
                    mqttMessage.setMsgTime(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_TIME)));
                    mqttMessage.setPatId(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.USER2ID)));

                    mqttMessage.setSenderName(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER)).equals(PATIENT) ? cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER_NAME)) : cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_NAME)));
                    mqttMessage.setSenderImgUrl(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER)).equals(PATIENT) ? cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER_IMG_URL)) : cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_IMG_URL)));

                    mqttMessage.setMsgStatus(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.MSG_STATUS)));

                    mqttMessage.setSalutation(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SALUTATION)));

                    mqttMessage.setReceiverName(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER)).equals(DOCTOR) ? cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER_NAME)) : cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_NAME)));
                    mqttMessage.setReceiverImgUrl(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER)).equals(DOCTOR) ? cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER_IMG_URL)) : cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.RECEIVER_IMG_URL)));

                    mqttMessage.setSender(cursor.getString(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.SENDER)));

//                    mqttMessage.setDocId(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.USER1ID)));
                    mqttMessage.setSpecialization(null);
                    mqttMessage.setFileUrl("");
                    mqttMessage.setFileType("");
//                    mqttMessage.setUploadStatus(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.UPLOAD_STATUS)));
//                    mqttMessage.setDownloadStatus(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.DOWNLOAD_STATUS)));
//                    mqttMessage.setReadStatus(cursor.getInt(cursor.getColumnIndex(AppDBHelper.CHAT_MESSAGES.READ_STATUS)));

                    mqttMessages.add(mqttMessage);
                    cursor.moveToNext();
                }

                cursor.close();
            }

            appDBHelper.close();
        }

        if (!mqttMessages.isEmpty()) {
            mEmptyListView.setVisibility(View.GONE);
            searchedMessagesAdapter.setSearch(searchText);
        } else {
            if (dataList.isEmpty())
                mEmptyListView.setVisibility(View.VISIBLE);
            else mEmptyListView.setVisibility(View.GONE);

            searchRecyclerView.setVisibility(View.GONE);
        }

        searchedMessagesAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.searchRecyclerView, R.id.leftFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.searchRecyclerView:
                break;
            case R.id.leftFab:
                Intent intent = new Intent(getActivity(), ShowMyPatientsListActivity.class);
                intent.putExtra(RescribeConstants.ACTIVITY_LAUNCHED_FROM,RescribeConstants.PATIENT_CONNECT);
                getActivity().startActivityForResult(intent, PATIENT_CONNECT_REQUEST);
                break;
        }
    }
}
