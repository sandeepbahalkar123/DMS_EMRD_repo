package com.rescribe.doctor.ui.fragments.new_patient;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.my_appointments.BottomMenuForCompleteAppointmentAdapter;
import com.rescribe.doctor.adapters.new_patient.NewPatientAdapter;
import com.rescribe.doctor.bottom_menus.BottomMenu;
import com.rescribe.doctor.helpers.myappointments.AppointmentHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.new_patient.NewPatientBaseModel;
import com.rescribe.doctor.model.new_patient.NewPatientsDetail;
import com.rescribe.doctor.model.doctor_location.DoctorLocationModel;
import com.rescribe.doctor.model.patient.template_sms.TemplateBaseModel;
import com.rescribe.doctor.model.patient.template_sms.TemplateList;
import com.rescribe.doctor.model.patient.template_sms.request_send_sms.PatientInfoList;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.singleton.RescribeApplication;
import com.rescribe.doctor.ui.activities.my_patients.patient_history.PatientHistoryActivity;
import com.rescribe.doctor.ui.activities.new_patient.NewPatientActivity;
import com.rescribe.doctor.ui.customesViews.EditTextWithDeleteButton;
import com.rescribe.doctor.ui.fragments.patient.my_patient.SendSmsPatientActivity;
import com.rescribe.doctor.ui.fragments.patient.my_patient.TemplateListForMyPatients;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.rescribe.doctor.ui.fragments.patient.my_patient.SendSmsPatientActivity.RESULT_SEND_SMS;
import static com.rescribe.doctor.util.CommonMethods.toCamelCase;

/**
 * Created by jeetal on 17/3/18.
 */

public class NewPatientFragment extends Fragment implements NewPatientAdapter.OnDownArrowClicked, BottomMenuForCompleteAppointmentAdapter.OnMenuBottomItemClickListener, HelperResponse {
    private AppointmentHelper mAppointmentHelper;
    @BindView(R.id.whiteUnderLine)
    ImageView whiteUnderLine;
    @BindView(R.id.historyExpandableListView)
    ExpandableListView expandableListView;
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    @BindView(R.id.rightFab)
    FloatingActionButton rightFab;
    @BindView(R.id.leftFab)
    FloatingActionButton leftFab;
    @BindView(R.id.appointmentLayoutContainer)
    LinearLayout appointmentLayoutContainer;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.recyclerViewBottom)
    RecyclerView recyclerViewBottom;
    @BindView(R.id.searchEditText)
    EditTextWithDeleteButton searchEditText;
    private Unbinder unbinder;
    private NewPatientAdapter mCompletedOpdAdapter;
    private String[] mMenuNames = {"Select All", "Waiting List", "Send SMS"};
    private BottomMenuForCompleteAppointmentAdapter mBottomMenuForCompleteAppointmentAdapter;
    private ArrayList<DoctorLocationModel> mDoctorLocationModel = new ArrayList<>();
    private int mLocationId;
    private ArrayList<PatientInfoList> patientInfoLists;
    private int mClinicId;
    private String mClinicName = "";
    private ArrayList<NewPatientsDetail> mNewPatientsDetail;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.my_appointments_layout, container, false);
        //  hideSoftKeyboard();
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;

    }

    private void init() {
        rightFab.setVisibility(View.GONE);
        ArrayList<BottomMenu> mBottomMenuList = new ArrayList<>();
        mDoctorLocationModel = RescribeApplication.getDoctorLocationModels();
        mAppointmentHelper = new AppointmentHelper(getActivity(), this);
        for (String mMenuName : mMenuNames) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuName(mMenuName);
            mBottomMenuList.add(bottomMenu);
        }

        NewPatientBaseModel mNewPatientBaseModel = getArguments().getParcelable(RescribeConstants.MY_PATIENTS_DATA);
        mNewPatientsDetail = mNewPatientBaseModel.getNewPatientDataModel().getNewPatientsDetails();

        recyclerView.setClipToPadding(false);
        // off recyclerView Animation
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        recyclerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.divider));
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        initAdapter();

        searchEditText.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mCompletedOpdAdapter != null) {
                    mCompletedOpdAdapter.getFilter().filter(s);
                }
            }
        });

        mBottomMenuForCompleteAppointmentAdapter = new BottomMenuForCompleteAppointmentAdapter(getContext(), this, mBottomMenuList, true, RescribeConstants.COMPLETE_OPD);
        recyclerViewBottom.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewBottom.setAdapter(mBottomMenuForCompleteAppointmentAdapter);

    }

    private void initAdapter() {
        if (!mNewPatientsDetail.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);

            for (NewPatientsDetail patientList : mNewPatientsDetail) {
                patientList.setSelected(((NewPatientActivity) getActivity()).selectedDoctorId.contains(patientList.getHospitalPatId()));
            }

            boolean isLongPress = mCompletedOpdAdapter != null && mCompletedOpdAdapter.isLongPressed;

            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            mCompletedOpdAdapter = new NewPatientAdapter(getActivity(), mNewPatientsDetail, this);
            recyclerView.setAdapter(mCompletedOpdAdapter);
            mCompletedOpdAdapter.isLongPressed = isLongPress;

        } else {
            recyclerView.setVisibility(View.GONE);
            emptyListView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLongPressOpenBottomMenu(boolean isLongPressed, int groupPosition) {
        if (isLongPressed) {
            recyclerViewBottom.setVisibility(View.VISIBLE);
        } else {
            for (int index = 0; index < mCompletedOpdAdapter.getGroupList().size(); index++) {
                mCompletedOpdAdapter.getGroupList().get(index).setSelected(false);
            }
            mCompletedOpdAdapter.notifyDataSetChanged();
            for (int i = 0; i < mBottomMenuForCompleteAppointmentAdapter.getList().size(); i++) {
                mBottomMenuForCompleteAppointmentAdapter.getList().get(i).setSelected(false);
            }
            mBottomMenuForCompleteAppointmentAdapter.notifyDataSetChanged();
            recyclerViewBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRecordFound(boolean isListEmpty) {
        emptyListView.setVisibility(isListEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onCheckUncheckRemoveSelectAllSelection(boolean ischecked, NewPatientsDetail patientObject) {
        if (!ischecked) {
            ((NewPatientActivity) getActivity()).selectedDoctorId.remove(patientObject.getHospitalPatId());

            for (int i = 0; i < mBottomMenuForCompleteAppointmentAdapter.getList().size(); i++) {
                if (mBottomMenuForCompleteAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.select_all))) {
                    mBottomMenuForCompleteAppointmentAdapter.getList().get(i).setSelected(false);
                }
            }
            mBottomMenuForCompleteAppointmentAdapter.notifyDataSetChanged();
        } else
            ((NewPatientActivity) getActivity()).selectedDoctorId.add(patientObject.getHospitalPatId());
    }

    @Override
    public void onClickOfPatientDetails(NewPatientsDetail patientListObject, String text) {

        String patientName;
        if (patientListObject.getSalutation() != 0)
            patientName = RescribeConstants.SALUTATION[patientListObject.getSalutation() - 1] + toCamelCase(patientListObject.getPatientName());
        else patientName = toCamelCase(patientListObject.getPatientName());

        Bundle b = new Bundle();
        b.putString(RescribeConstants.PATIENT_NAME, patientName);
        b.putString(RescribeConstants.PATIENT_INFO, text);
        b.putInt(RescribeConstants.CLINIC_ID, patientListObject.getClinicId());
        b.putString(RescribeConstants.PATIENT_ID, String.valueOf(patientListObject.getPatientID()));
        b.putString(RescribeConstants.PATIENT_HOS_PAT_ID, String.valueOf(patientListObject.getHospitalPatId()));
        Intent intent = new Intent(getActivity(), PatientHistoryActivity.class);
        intent.putExtra(RescribeConstants.PATIENT_INFO, b);
        startActivity(intent);

    }

    @Override
    public void onPhoneNoClick(String patientPhone) {
        NewPatientActivity activity = (NewPatientActivity) getActivity();
        activity.callPatient(patientPhone);
    }


    public static NewPatientFragment newInstance(Bundle b) {
        NewPatientFragment fragment = new NewPatientFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void setClickOnMenuItem(int position, BottomMenu bottomMenu) {
        if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.select_all))) {
            if (bottomMenu.isSelected()) {

                for (int index = 0; index < mCompletedOpdAdapter.getGroupList().size(); index++) {
                    mCompletedOpdAdapter.getGroupList().get(index).setSelected(true);
                    ((NewPatientActivity) getActivity()).selectedDoctorId.add(mCompletedOpdAdapter.getGroupList().get(index).getHospitalPatId());
                }
                mCompletedOpdAdapter.notifyDataSetChanged();

            } else {
                for (int index = 0; index < mCompletedOpdAdapter.getGroupList().size(); index++) {
                    mCompletedOpdAdapter.getGroupList().get(index).setSelected(false);
                    ((NewPatientActivity) getActivity()).selectedDoctorId.remove(mCompletedOpdAdapter.getGroupList().get(index).getHospitalPatId());
                }
                mCompletedOpdAdapter.notifyDataSetChanged();
            }

            //Send SMS
        } else if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.send_sms))) {
            patientInfoLists = new ArrayList<>();
            ArrayList<NewPatientsDetail> mPatientListsOriginal = new ArrayList<>();
            for (int childIndex = 0; childIndex < mCompletedOpdAdapter.getGroupList().size(); childIndex++) {
                NewPatientsDetail patientList = mCompletedOpdAdapter.getGroupList().get(childIndex);
                if (patientList.isSelected()) {
                    PatientInfoList patientInfoListObject = new PatientInfoList();
                    patientInfoListObject.setPatientName(patientList.getPatientName());
                    patientInfoListObject.setPatientId(patientList.getPatientID());
                    patientInfoListObject.setPatientPhone(patientList.getPatientPhon());
                    patientInfoListObject.setHospitalPatId(patientList.getHospitalPatId());
                    patientInfoLists.add(patientInfoListObject);
                    mPatientListsOriginal.add(patientList);
                }
            }


            if (!patientInfoLists.isEmpty()) {
                showDialogForSmsLocationSelection(mDoctorLocationModel);

                for (int i = 0; i < mBottomMenuForCompleteAppointmentAdapter.getList().size(); i++) {
                    if (mBottomMenuForCompleteAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.send_sms))) {
                        mBottomMenuForCompleteAppointmentAdapter.getList().get(i).setSelected(false);
                    }
                }
                mBottomMenuForCompleteAppointmentAdapter.notifyDataSetChanged();
            } else {
                CommonMethods.showToast(getActivity(), getString(R.string.please_select_patients));
                for (int i = 0; i < mBottomMenuForCompleteAppointmentAdapter.getList().size(); i++) {
                    if (mBottomMenuForCompleteAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.send_sms))) {
                        mBottomMenuForCompleteAppointmentAdapter.getList().get(i).setSelected(false);
                    }
                }
                mBottomMenuForCompleteAppointmentAdapter.notifyDataSetChanged();
            }

        }
    }

    private void showDialogForSmsLocationSelection(ArrayList<DoctorLocationModel> mDoctorLocationModel) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_location_waiting_list_layout);
        dialog.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (!RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SELECTED_LOCATION_ID, getActivity()).equals(""))
            mLocationId = Integer.parseInt(RescribePreferencesManager.getString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SELECTED_LOCATION_ID, getActivity()));
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        for (int index = 0; index < mDoctorLocationModel.size(); index++) {
            final DoctorLocationModel clinicList = mDoctorLocationModel.get(index);

            final RadioButton radioButton = (RadioButton) inflater.inflate(R.layout.dialog_location_radio_item, null, false);
            if (mLocationId == clinicList.getLocationId()) {
                radioButton.setChecked(true);
            } else {
                radioButton.setChecked(false);
            }
            radioButton.setText(clinicList.getClinicName() + ", " + clinicList.getAddress());
            radioButton.setId(CommonMethods.generateViewId());
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mLocationId = clinicList.getLocationId();
                    mClinicId = clinicList.getClinicId();
                    mClinicName = clinicList.getClinicName();
                }
            });
            radioGroup.addView(radioButton);
        }

        TextView okButton = (TextView) dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLocationId != 0) {
                    RescribePreferencesManager.putString(RescribePreferencesManager.RESCRIBE_PREFERENCES_KEY.SELECTED_LOCATION_ID, String.valueOf(mLocationId), getActivity());
                    mAppointmentHelper.doGetDoctorTemplate();
                    dialog.cancel();
                } else {
                    Toast.makeText(getActivity(), "Please select clinic location.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }

    public boolean callOnBackPressed() {
        return mCompletedOpdAdapter != null && mCompletedOpdAdapter.isLongPressed;
    }

    public void removeCheckBox() {
        recyclerViewBottom.setVisibility(View.GONE);
        mCompletedOpdAdapter.setLongPressed(false);
        for (int index = 0; index < mCompletedOpdAdapter.getGroupList().size(); index++) {
            mCompletedOpdAdapter.getGroupList().get(index).setSelected(false);
        }
        mCompletedOpdAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.rightFab, R.id.leftFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightFab:

                break;
            case R.id.leftFab:

                break;
        }
    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(RescribeConstants.TASK_GET_DOCTOR_SMS_TEMPLATE)) {
            TemplateBaseModel templateBaseModel = (TemplateBaseModel) customResponse;
            ArrayList<TemplateList> templateLists = templateBaseModel.getTemplateDataModel().getTemplateList();
            if (!templateLists.isEmpty()) {
                Intent intent = new Intent(getActivity(), TemplateListForMyPatients.class);
                intent.putExtra(RescribeConstants.LOCATION_ID, mLocationId);
                intent.putExtra(RescribeConstants.CLINIC_ID, mClinicId);
                intent.putExtra(RescribeConstants.CLINIC_NAME, mClinicName);
                intent.putParcelableArrayListExtra(RescribeConstants.PATIENT_LIST, patientInfoLists);
                intent.putParcelableArrayListExtra(RescribeConstants.TEMPLATE_LIST, templateLists);
                startActivity(intent);
            } else {
                TemplateList templateList = null;
                Intent intent = new Intent(getActivity(), SendSmsPatientActivity.class);
                intent.putExtra(RescribeConstants.LOCATION_ID, mLocationId);
                intent.putExtra(RescribeConstants.CLINIC_ID, mClinicId);
                intent.putExtra(RescribeConstants.TEMPLATE_OBJECT, templateList);
                intent.putExtra(RescribeConstants.CLINIC_NAME, mClinicName);
                intent.putParcelableArrayListExtra(RescribeConstants.PATIENT_LIST, patientInfoLists);
                startActivityForResult(intent, RESULT_SEND_SMS);
            }
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

}
