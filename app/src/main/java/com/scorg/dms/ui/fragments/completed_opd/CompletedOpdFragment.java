package com.scorg.dms.ui.fragments.completed_opd;

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

import com.scorg.dms.R;
import com.scorg.dms.adapters.completed_opd.CompletedOpdAdapter;
import com.scorg.dms.adapters.my_appointments.BottomMenuForCompleteAppointmentAdapter;
import com.scorg.dms.bottom_menus.BottomMenu;
import com.scorg.dms.model.patient.doctor_patients.PatientList;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.completed_opd.CompletedOpd;
import com.scorg.dms.model.completed_opd.CompletedOpdBaseModel;
import com.scorg.dms.model.doctor_location.DoctorLocationModel;
import com.scorg.dms.model.patient.template_sms.TemplateBaseModel;
import com.scorg.dms.model.patient.template_sms.TemplateList;
import com.scorg.dms.model.patient.template_sms.request_send_sms.PatientInfoList;
import com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list.AddToList;
import com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list.PatientAddToWaitingList;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.completed_opd.CompletedOpdActivity;
import com.scorg.dms.ui.activities.my_patients.patient_history.PatientHistoryActivity;
import com.scorg.dms.ui.customesViews.EditTextWithDeleteButton;
import com.scorg.dms.ui.fragments.patient.my_patient.SendSmsPatientActivity;
import com.scorg.dms.ui.fragments.patient.my_patient.TemplateListForMyPatients;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.scorg.dms.ui.fragments.patient.my_patient.SendSmsPatientActivity.RESULT_SEND_SMS;
import static com.scorg.dms.util.CommonMethods.toCamelCase;

/**
 * Created by jeetal on 17/3/18.
 */

public class CompletedOpdFragment extends Fragment implements CompletedOpdAdapter.OnDownArrowClicked, BottomMenuForCompleteAppointmentAdapter.OnMenuBottomItemClickListener, HelperResponse {
    private static Bundle args;
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
    private CompletedOpdAdapter mCompletedOpdAdapter;
    private String[] mMenuNames = {"Select All", "Waiting List", "Send SMS"};
    private BottomMenuForCompleteAppointmentAdapter mBottomMenuForCompleteAppointmentAdapter;
    public static final int PAGE_START = 0;
    private String searchText = "";
    private ArrayList<DoctorLocationModel> mDoctorLocationModel = new ArrayList<>();
    private ArrayList<PatientList> mPatientListsOriginal;
    private int mLocationId;
    private ArrayList<PatientAddToWaitingList> patientsListAddToWaitingLists;
    private ArrayList<PatientInfoList> patientInfoLists;
    private int mClinicId;
    private String mClinicName = "";
    private boolean isFiltered = false;
    private ArrayList<CompletedOpd> mCompletedOpdArrayList;
    private ArrayList<AddToList> addToWaitingArrayList;
    private String mClinicCity;
    private String mClinicArea;

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
        mDoctorLocationModel = DMSApplication.getDoctorLocationModels();
        mAppointmentHelper = new AppointmentHelper(getActivity(), this);
        for (String mMenuName : mMenuNames) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuName(mMenuName);
            mBottomMenuList.add(bottomMenu);
        }
        CompletedOpdBaseModel mmCompletedOpdBaseModel = args.getParcelable(DMSConstants.MY_PATIENTS_DATA);
        mCompletedOpdArrayList = mmCompletedOpdBaseModel.getCompletedOpdModel().getCompletedOpd();

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
                    searchText = s.toString();
                    mCompletedOpdAdapter.getFilter().filter(s);
                }


            }
        });

        mBottomMenuForCompleteAppointmentAdapter = new BottomMenuForCompleteAppointmentAdapter(getContext(), this, mBottomMenuList, true, DMSConstants.COMPLETE_OPD);
        recyclerViewBottom.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewBottom.setAdapter(mBottomMenuForCompleteAppointmentAdapter);

    }

    private void initAdapter() {
        if (mCompletedOpdArrayList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyListView.setVisibility(View.GONE);

            for (CompletedOpd patientList : mCompletedOpdArrayList) {
                patientList.setSelected(((CompletedOpdActivity) getActivity()).selectedDoctorId.contains(patientList.getHospitalPatId()));
            }

            boolean isLongPress = mCompletedOpdAdapter != null && mCompletedOpdAdapter.isLongPressed;

            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearlayoutManager);
            mCompletedOpdAdapter = new CompletedOpdAdapter(getActivity(), mCompletedOpdArrayList, this);
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
    public void onCheckUncheckRemoveSelectAllSelection(boolean ischecked, CompletedOpd patientObject) {
        if (!ischecked) {
            ((CompletedOpdActivity) getActivity()).selectedDoctorId.remove(patientObject.getHospitalPatId());

            for (int i = 0; i < mBottomMenuForCompleteAppointmentAdapter.getList().size(); i++) {
                if (mBottomMenuForCompleteAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.select_all))) {
                    mBottomMenuForCompleteAppointmentAdapter.getList().get(i).setSelected(false);
                }
            }
            mBottomMenuForCompleteAppointmentAdapter.notifyDataSetChanged();
        } else
            ((CompletedOpdActivity) getActivity()).selectedDoctorId.add(patientObject.getHospitalPatId());
    }

    @Override
    public void onClickOfPatientDetails(CompletedOpd patientListObject, String text) {
        String patientName;
        if (patientListObject.getSalutation() != 0)
            patientName = DMSConstants.SALUTATION[patientListObject.getSalutation() - 1] + toCamelCase(patientListObject.getPatientName());
        else patientName = toCamelCase(patientListObject.getPatientName());

        Bundle b = new Bundle();
        b.putString(DMSConstants.PATIENT_NAME, patientName);
        b.putString(DMSConstants.PATIENT_INFO, text);
        b.putInt(DMSConstants.CLINIC_ID, patientListObject.getClinicId());
        b.putString(DMSConstants.PATIENT_ID, String.valueOf(patientListObject.getPatientId()));
        b.putString(DMSConstants.PATIENT_HOS_PAT_ID, String.valueOf(patientListObject.getHospitalPatId()));
        Intent intent = new Intent(getActivity(), PatientHistoryActivity.class);
        intent.putExtra(DMSConstants.PATIENT_INFO, b);
        startActivity(intent);

    }

    @Override
    public void onPhoneNoClick(String patientPhone) {
        CompletedOpdActivity activity = (CompletedOpdActivity) getActivity();
        activity.callPatient(patientPhone);
    }


    public static CompletedOpdFragment newInstance(Bundle b) {
        CompletedOpdFragment fragment = new CompletedOpdFragment();
        args = b;
        if (args == null) {
            args = new Bundle();
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setClickOnMenuItem(int position, BottomMenu bottomMenu) {
        if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.select_all))) {
            if (bottomMenu.isSelected()) {

                for (int index = 0; index < mCompletedOpdAdapter.getGroupList().size(); index++) {
                    mCompletedOpdAdapter.getGroupList().get(index).setSelected(true);
                    ((CompletedOpdActivity) getActivity()).selectedDoctorId.add(mCompletedOpdAdapter.getGroupList().get(index).getHospitalPatId());
                }
                mCompletedOpdAdapter.notifyDataSetChanged();

            } else {
                for (int index = 0; index < mCompletedOpdAdapter.getGroupList().size(); index++) {
                    mCompletedOpdAdapter.getGroupList().get(index).setSelected(false);
                    ((CompletedOpdActivity) getActivity()).selectedDoctorId.remove(mCompletedOpdAdapter.getGroupList().get(index).getHospitalPatId());
                }
                mCompletedOpdAdapter.notifyDataSetChanged();
            }

            //Send SMS
        } else if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.send_sms))) {
            patientInfoLists = new ArrayList<>();
            ArrayList<CompletedOpd> mPatientListsOriginal = new ArrayList<>();
            for (int childIndex = 0; childIndex < mCompletedOpdAdapter.getGroupList().size(); childIndex++) {
                CompletedOpd patientList = mCompletedOpdAdapter.getGroupList().get(childIndex);
                if (patientList.isSelected()) {
                    PatientInfoList patientInfoListObject = new PatientInfoList();
                    patientInfoListObject.setPatientName(patientList.getPatientName());
                    patientInfoListObject.setPatientId(patientList.getPatientId());
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
        if (!DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, getActivity()).equals(""))
            mLocationId = Integer.parseInt(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, getActivity()));
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
                    DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, String.valueOf(mLocationId), getActivity());
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
        if (mCompletedOpdAdapter != null) {
            return mCompletedOpdAdapter.isLongPressed;
        } else {
            return false;
        }
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
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_GET_DOCTOR_SMS_TEMPLATE)) {
            TemplateBaseModel templateBaseModel = (TemplateBaseModel) customResponse;
            ArrayList<TemplateList> templateLists = templateBaseModel.getTemplateDataModel().getTemplateList();
            if (!templateLists.isEmpty()) {
                Intent intent = new Intent(getActivity(), TemplateListForMyPatients.class);
                intent.putExtra(DMSConstants.LOCATION_ID, mLocationId);
                intent.putExtra(DMSConstants.CLINIC_ID, mClinicId);
                intent.putExtra(DMSConstants.CLINIC_NAME, mClinicName);
                intent.putParcelableArrayListExtra(DMSConstants.PATIENT_LIST, patientInfoLists);
                intent.putParcelableArrayListExtra(DMSConstants.TEMPLATE_LIST, templateLists);
                startActivity(intent);
            } else {
                TemplateList templateList = null;
                Intent intent = new Intent(getActivity(), SendSmsPatientActivity.class);
                intent.putExtra(DMSConstants.LOCATION_ID, mLocationId);
                intent.putExtra(DMSConstants.CLINIC_ID, mClinicId);
                intent.putExtra(DMSConstants.TEMPLATE_OBJECT, templateList);
                intent.putExtra(DMSConstants.CLINIC_NAME, mClinicName);
                intent.putParcelableArrayListExtra(DMSConstants.PATIENT_LIST, patientInfoLists);
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
