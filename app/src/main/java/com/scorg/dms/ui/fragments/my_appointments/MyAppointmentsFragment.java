package com.scorg.dms.ui.fragments.my_appointments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scorg.dms.R;
import com.scorg.dms.adapters.my_appointments.AppointmentListAdapter;
import com.scorg.dms.adapters.my_appointments.BottomMenuAppointmentAdapter;
import com.scorg.dms.adapters.waiting_list.ShowWaitingStatusAdapter;
import com.scorg.dms.bottom_menus.BottomMenu;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResultData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.model.my_appointments.MyAppointmentsDataModel;
import com.scorg.dms.model.my_appointments.PatientList;
import com.scorg.dms.model.my_appointments.request_cancel_or_complete_appointment.RequestAppointmentCancelModel;
import com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list.AddToList;
import com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list.RequestToAddWaitingList;
import com.scorg.dms.model.waiting_list.response_add_to_waiting_list.AddToWaitingListBaseModel;
import com.scorg.dms.model.waiting_list.response_add_to_waiting_list.AddToWaitingResponse;
import com.scorg.dms.ui.activities.dms_patient_list.FileTypeViewerActivity;
import com.scorg.dms.ui.activities.my_appointments.MyAppointmentsActivity;
import com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity;
import com.scorg.dms.ui.customesViews.EditTextWithDeleteButton;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity.RESULT_CLOSE_ACTIVITY_WAITING_LIST;
import static com.scorg.dms.util.CommonMethods.toCamelCase;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_DATA;


/**
 * Created by jeetal on 31/1/18.
 */

public class MyAppointmentsFragment extends Fragment implements AppointmentListAdapter.OnItemClickListener, BottomMenuAppointmentAdapter.OnMenuBottomItemClickListener, HelperResponse {


    @BindView(R.id.searchEditText)
    EditTextWithDeleteButton searchEditText;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.rightFab)
    FloatingActionButton rightFab;
    @BindView(R.id.leftFab)
    FloatingActionButton leftFab;

    Unbinder unbinder;
    private AppointmentListAdapter mAppointmentListAdapter;
    private AppointmentHelper mAppointmentHelper;

    private ArrayList<AppointmentPatientData> mAppointmentPatientData;
    private String mUserSelectedDate;
    private DMSPatientsHelper mPatientsHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.my_appointments_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        mAppointmentHelper = new AppointmentHelper(getActivity(), this);

        searchEditText.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mAppointmentListAdapter != null) {
                    mAppointmentListAdapter.getFilter().filter(s);
                }
            }
        });

        mUserSelectedDate = getArguments().getString(DMSConstants.DATE);
        MyAppointmentsDataModel myAppointmentsDataModel = getArguments().getParcelable(APPOINTMENT_DATA);
        setFilteredData(myAppointmentsDataModel);
    }

    public static MyAppointmentsFragment newInstance(MyAppointmentsDataModel myAppointmentsDataModel, String mDateSelectedByUser) {
        MyAppointmentsFragment myAppointmentsFragment = new MyAppointmentsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DMSConstants.DATE, mDateSelectedByUser);
        bundle.putParcelable(APPOINTMENT_DATA, myAppointmentsDataModel);
        myAppointmentsFragment.setArguments(bundle);
        return myAppointmentsFragment;
    }

    @Override
    public void onPhoneNoClick(String patientPhone) {
        MyAppointmentsActivity activity = (MyAppointmentsActivity) getActivity();
        activity.callPatient(patientPhone);
    }

    @Override
    public void onRecordFound(boolean isListEmpty) {
        if (isListEmpty) {
            emptyListView.setVisibility(View.VISIBLE);
        } else {
            emptyListView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickOfPatientDetails(AppointmentPatientData patientListObject, int clinicId, String text) {
        ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();
        // TODO: hardcoed for now, As patientList And WaitingList API patientID not sync from server
        showSearchResultRequestModel.setPatientId("07535277");
        // showSearchResultRequestModel.setPatientId(patientListObject.getPatientId());

        mPatientsHelper.doGetPatientList(showSearchResultRequestModel);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rightFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightFab:
                MyAppointmentsActivity activity = (MyAppointmentsActivity) getActivity();
                activity.getActivityDrawerLayout().openDrawer(GravityCompat.END);
                break;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_GET_APPOINTMENT_DATA)) {
            AddToWaitingListBaseModel mAddToWaitingListBaseModel = (AddToWaitingListBaseModel) customResponse;
            if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(mAddToWaitingListBaseModel.getCommon().getSuccess())) {
                showDialogForWaitingStatus(mAddToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse());
            }
        } else if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_PATIENT_LIST)) {
            ShowSearchResultResponseModel showSearchResultResponseModel = (ShowSearchResultResponseModel) customResponse;
            SearchResultData searchResultData = showSearchResultResponseModel.getSearchResultData();

            if (searchResultData != null) {
                List<SearchResult> searchResultList = searchResultData.getSearchResult();
                if (!searchResultList.isEmpty()) {
                    SearchResult searchPatientInformation = searchResultList.get(0);
                    List<PatientFileData> patientFileDataList = searchPatientInformation.getPatientFileData();
                    if (patientFileDataList != null) {
                        if (!patientFileDataList.isEmpty()) {
                            PatientFileData childElement = patientFileDataList.get(0);
                            Intent intent = new Intent(getActivity(), FileTypeViewerActivity.class);
                            Bundle extra = new Bundle();
                            ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
                            dataToSend.add(childElement);
                            extra.putSerializable(getString(R.string.compare), dataToSend);
                            extra.putString(DMSConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
                            extra.putString(DMSConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
                            extra.putString(DMSConstants.ID, childElement.getRespectiveParentPatientID());
                            extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + searchPatientInformation.getPatientName());
                            intent.putExtra(DMSConstants.DATA, extra);
                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    private void showDialogForWaitingStatus(ArrayList<AddToWaitingResponse> addToWaitingResponse) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.waiting_status_list_dialog);
        dialog.setCancelable(true);

        RecyclerView recyclerViewBottom = (RecyclerView) dialog.findViewById(R.id.recyclerViewBottom);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerViewBottom.setLayoutManager(linearlayoutManager);
        ShowWaitingStatusAdapter mShowWaitingStatusAdapter = new ShowWaitingStatusAdapter(getActivity(), addToWaitingResponse);
        recyclerViewBottom.setAdapter(mShowWaitingStatusAdapter);
        TextView okButton = (TextView) dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), WaitingMainListActivity.class);
                startActivity(intent);
                getActivity().finish();
                getActivity().setResult(RESULT_CLOSE_ACTIVITY_WAITING_LIST);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        CommonMethods.showToast(getActivity(), errorMessage);

    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        CommonMethods.showToast(getActivity(), serverErrorMessage);
    }

    public void setFilteredData(MyAppointmentsDataModel myAppointmentsDataModel) {

        if (!myAppointmentsDataModel.getAppointmentPatientData().isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);

            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            //list is sorted for Booked and Confirmed Status appointments
            mAppointmentListAdapter = new AppointmentListAdapter(getActivity(), myAppointmentsDataModel.getAppointmentPatientData());
            recyclerView.setAdapter(mAppointmentListAdapter);

        } else {
            recyclerView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setClickOnMenuItem(int position, BottomMenu bottomMenu) {

    }
}