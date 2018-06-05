package com.scorg.dms.ui.fragments.patient.my_patient;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.scorg.dms.adapters.my_appointments.BottomMenuAppointmentAdapter;
import com.scorg.dms.adapters.my_patients.MyPatientsAdapter;
import com.scorg.dms.bottom_menus.BottomMenu;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.doctor_location.DoctorLocationModel;
import com.scorg.dms.model.patient.doctor_patients.MyPatientBaseModel;
import com.scorg.dms.model.patient.doctor_patients.PatientList;
import com.scorg.dms.model.patient.doctor_patients.sync_resp.PatientUpdateDetail;
import com.scorg.dms.model.patient.template_sms.TemplateBaseModel;
import com.scorg.dms.model.patient.template_sms.TemplateList;
import com.scorg.dms.model.patient.template_sms.request_send_sms.PatientInfoList;
import com.scorg.dms.model.request_patients.RequestSearchPatients;
import com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list.AddToList;
import com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list.PatientAddToWaitingList;
import com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list.RequestToAddWaitingList;
import com.scorg.dms.model.waiting_list.response_add_to_waiting_list.AddToWaitingListBaseModel;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.ui.activities.my_patients.MyPatientsActivity;
import com.scorg.dms.ui.activities.my_patients.add_new_patient.AddNewPatientWebViewActivity;
import com.scorg.dms.ui.activities.my_patients.patient_history.PatientHistoryActivity;
import com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity;
import com.scorg.dms.ui.customesViews.EditTextWithDeleteButton;
import com.scorg.dms.ui.customesViews.drag_drop_recyclerview_helper.EndlessRecyclerViewScrollListener;
import com.scorg.dms.ui.fragments.book_appointment.CoachFragment;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.NetworkUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.scorg.dms.singleton.DMSApplication.getDoctorLocationModels;
import static com.scorg.dms.ui.activities.my_patients.add_new_patient.AddNewPatientWebViewActivity.ADD_PATIENT_REQUEST;
import static com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity.RESULT_CLOSE_ACTIVITY_WAITING_LIST;
import static com.scorg.dms.ui.fragments.patient.my_patient.SendSmsPatientActivity.RESULT_SEND_SMS;
import static com.scorg.dms.util.CommonMethods.toCamelCase;
import static com.scorg.dms.util.DMSConstants.LOCATION_ID;
import static com.scorg.dms.util.DMSConstants.SUCCESS;


/**
 * Created by jeetal on 31/1/18.
 */

public class MyPatientsFragment extends Fragment implements MyPatientsAdapter.OnDownArrowClicked, BottomMenuAppointmentAdapter.OnMenuBottomItemClickListener, HelperResponse {
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
    private MyPatientsAdapter mMyPatientsAdapter;
    private String[] mMenuNames = {"Select All", "Send SMS", "Waiting List"};
    private BottomMenuAppointmentAdapter mBottomMenuAppointmentAdapter;
    private String searchText = "";
    private ArrayList<DoctorLocationModel> mDoctorLocationModel = new ArrayList<>();
    private ArrayList<PatientAddToWaitingList> patientsListAddToWaitingLists;
    private ArrayList<PatientInfoList> patientInfoLists;

    private String mClinicName = "";
    //-------
    private String mClinicCity;
    private String mClinicArea;
    private int mClinicId;
    private int mLocationId;
    private RequestSearchPatients mRequestSearchPatients = new RequestSearchPatients();
    private String fromActivityLaunched = "";
    private boolean isFiltered = false;
    private AppDBHelper appDBHelper;

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

        String coachMarkStatus = DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.COACHMARK_ALL_PATIENT_DOWNLOAD, getActivity());
        if (!coachMarkStatus.equals(DMSConstants.YES)) {
            FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.coachmarkContainer, CoachFragment.newInstance());
            fragmentTransaction.addToBackStack("Coach");
            fragmentTransaction.commit();
        }
        init();
        return mRootView;

    }

    private void init() {

        appDBHelper = new AppDBHelper(getContext());

        if (getArguments().getString(DMSConstants.ACTIVITY_LAUNCHED_FROM) != null)
            fromActivityLaunched = getArguments().getString(DMSConstants.ACTIVITY_LAUNCHED_FROM);

        ArrayList<BottomMenu> mBottomMenuList = new ArrayList<>();
        mDoctorLocationModel = getDoctorLocationModels();
        mAppointmentHelper = new AppointmentHelper(getActivity(), this);
        for (String mMenuName : mMenuNames) {
            BottomMenu bottomMenu = new BottomMenu();
            bottomMenu.setMenuName(mMenuName);
            mBottomMenuList.add(bottomMenu);
        }

        recyclerView.setClipToPadding(false);
        // off recyclerView Animation
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        recyclerView.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.dp67));
        recyclerView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.divider));
        if (animator instanceof SimpleItemAnimator)
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);

        ArrayList<PatientList> patientLists = new ArrayList<>();
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayoutManager);

        mMyPatientsAdapter = new MyPatientsAdapter(getActivity(), patientLists, this, fromActivityLaunched.equals(DMSConstants.HOME_PAGE));
        recyclerView.setAdapter(mMyPatientsAdapter);

        nextPage(0, NetworkUtil.getConnectivityStatusBoolean(getContext()));

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                nextPage(page, NetworkUtil.getConnectivityStatusBoolean(getContext()));
            }
        });

        searchEditText.addTextChangedListener(new EditTextWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = s.toString();

                if (NetworkUtil.getConnectivityStatusBoolean(getContext())) {
                    if (searchText.length() >= 3) {
                        searchPatients(true);
                        isFiltered = true;
                    } else if (isFiltered) {
                        isFiltered = false;
                        searchText = "";
                        searchPatients(true);
                    }

                    if (s.toString().length() < 3)
                        mMyPatientsAdapter.getFilter().filter(s.toString());
                } else
                    searchPatients(false);
            }
        });
        if (fromActivityLaunched.equals(DMSConstants.HOME_PAGE)) {
            mBottomMenuAppointmentAdapter = new BottomMenuAppointmentAdapter(getContext(), this, mBottomMenuList, true, DMSConstants.NOT_FROM_COMPLETE_OPD);
            recyclerViewBottom.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerViewBottom.setAdapter(mBottomMenuAppointmentAdapter);
        } else {
            mBottomMenuAppointmentAdapter = new BottomMenuAppointmentAdapter(getContext(), this, mBottomMenuList, false, DMSConstants.NOT_FROM_COMPLETE_OPD);
            recyclerViewBottom.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            recyclerViewBottom.setAdapter(mBottomMenuAppointmentAdapter);
        }

        //--- Add new patient
        leftFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLongPressOpenBottomMenu(boolean isLongPressed, int groupPosition) {
        if (isLongPressed) {
            recyclerViewBottom.setVisibility(View.VISIBLE);
        } else {
            for (int index = 0; index < mMyPatientsAdapter.getGroupList().size(); index++) {
                mMyPatientsAdapter.getGroupList().get(index).setSelected(false);
            }
            mMyPatientsAdapter.notifyDataSetChanged();
            for (int i = 0; i < mBottomMenuAppointmentAdapter.getList().size(); i++) {
                mBottomMenuAppointmentAdapter.getList().get(i).setSelected(false);
            }
            mBottomMenuAppointmentAdapter.notifyDataSetChanged();
            recyclerViewBottom.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckUncheckRemoveSelectAllSelection(boolean ischecked, PatientList patientObject) {
        if (!ischecked) {
            ((MyPatientsActivity) getActivity()).selectedDoctorId.remove(patientObject.getHospitalPatId());

            for (int i = 0; i < mBottomMenuAppointmentAdapter.getList().size(); i++) {
                if (mBottomMenuAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.select_all))) {
                    mBottomMenuAppointmentAdapter.getList().get(i).setSelected(false);
                }
            }
            mBottomMenuAppointmentAdapter.notifyDataSetChanged();
        } else
            ((MyPatientsActivity) getActivity()).selectedDoctorId.add(patientObject.getHospitalPatId());
    }

    @Override
    public void onClickOfPatientDetails(PatientList patientListObject, String text, boolean isClickOnPatientDetailsRequired) {
        if (isClickOnPatientDetailsRequired) {

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
        } else {
            patientsListAddToWaitingLists = new ArrayList<>();
            for (int childIndex = 0; childIndex < mMyPatientsAdapter.getGroupList().size(); childIndex++) {
                PatientList patientList = mMyPatientsAdapter.getGroupList().get(childIndex);
                if (patientList.getHospitalPatId().equals(patientListObject.getHospitalPatId())) {
                    PatientAddToWaitingList patientInfoListObject = new PatientAddToWaitingList();
                    patientInfoListObject.setPatientName(patientList.getPatientName());
                    patientInfoListObject.setPatientId(String.valueOf(patientList.getPatientId()));
                    patientInfoListObject.setHospitalPatId(String.valueOf(patientList.getHospitalPatId()));
                    patientsListAddToWaitingLists.add(patientInfoListObject);
                }
            }

            if (!patientsListAddToWaitingLists.isEmpty()) {
                //if only one location then dont display dialog.
                if (mDoctorLocationModel.size() == 1) {
                    mLocationId = mDoctorLocationModel.get(0).getLocationId();
                    mClinicName = mDoctorLocationModel.get(0).getClinicName();
                    mClinicArea = mDoctorLocationModel.get(0).getArea();
                    mClinicCity = mDoctorLocationModel.get(0).getCity();
                    callWaitingListApi();
                } else {
                    showDialogToSelectLocation(mDoctorLocationModel, null);
                }
            }
        }
    }

    @Override
    public void onPhoneNoClick(String patientPhone) {
        MyPatientsActivity activity = (MyPatientsActivity) getActivity();
        activity.callPatient(patientPhone);
    }

    @Override
    public void onRecordFound(boolean isListEmpty) {
        if (isListEmpty)
            emptyListView.setVisibility(View.VISIBLE);
        else
            emptyListView.setVisibility(View.GONE);
    }


    public static MyPatientsFragment newInstance(Bundle bundle) {
        MyPatientsFragment myPatientsFragment = new MyPatientsFragment();
        myPatientsFragment.setArguments(bundle);
        return myPatientsFragment;
    }

    @Override
    public void setClickOnMenuItem(int position, BottomMenu bottomMenu) {
        if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.select_all))) {
            if (bottomMenu.isSelected()) {

                for (int index = 0; index < mMyPatientsAdapter.getGroupList().size(); index++) {
                    mMyPatientsAdapter.getGroupList().get(index).setSelected(true);
                    ((MyPatientsActivity) getActivity()).selectedDoctorId.add(mMyPatientsAdapter.getGroupList().get(index).getHospitalPatId());
                }
                mMyPatientsAdapter.notifyDataSetChanged();

            } else {
                for (int index = 0; index < mMyPatientsAdapter.getGroupList().size(); index++) {
                    mMyPatientsAdapter.getGroupList().get(index).setSelected(false);
                    ((MyPatientsActivity) getActivity()).selectedDoctorId.remove(mMyPatientsAdapter.getGroupList().get(index).getHospitalPatId());
                }
                mMyPatientsAdapter.notifyDataSetChanged();
            }

            //Send SMS
        } else if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.send_sms))) {
            patientInfoLists = new ArrayList<>();
            ArrayList<PatientList> mPatientListsOriginal = new ArrayList<>();
            for (int childIndex = 0; childIndex < mMyPatientsAdapter.getGroupList().size(); childIndex++) {
                PatientList patientList = mMyPatientsAdapter.getGroupList().get(childIndex);
                if (patientList.isSelected()) {
                    PatientInfoList patientInfoListObject = new PatientInfoList();
                    patientInfoListObject.setPatientName(patientList.getPatientName());
                    patientInfoListObject.setPatientId(patientList.getPatientId());
                    patientInfoListObject.setPatientPhone(patientList.getPatientPhone());
                    patientInfoListObject.setHospitalPatId(patientList.getHospitalPatId());
                    patientInfoLists.add(patientInfoListObject);
                    mPatientListsOriginal.add(patientList);
                }
            }


            if (!patientInfoLists.isEmpty()) {
                if (mDoctorLocationModel.size() == 1) {
                    mLocationId = mDoctorLocationModel.get(0).getLocationId();
                    mClinicName = mDoctorLocationModel.get(0).getClinicName();
                    mClinicArea = mDoctorLocationModel.get(0).getArea();
                    mClinicCity = mDoctorLocationModel.get(0).getCity();
                    mAppointmentHelper.doGetDoctorTemplate();
                } else {
                    showDialogForSmsLocationSelection(mDoctorLocationModel);
                }

                for (int i = 0; i < mBottomMenuAppointmentAdapter.getList().size(); i++) {
                    if (mBottomMenuAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.send_sms))) {
                        mBottomMenuAppointmentAdapter.getList().get(i).setSelected(false);
                    }
                }
                mBottomMenuAppointmentAdapter.notifyDataSetChanged();
            } else {
                CommonMethods.showToast(getActivity(), getString(R.string.please_select_patients));
                for (int i = 0; i < mBottomMenuAppointmentAdapter.getList().size(); i++) {
                    if (mBottomMenuAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.send_sms))) {
                        mBottomMenuAppointmentAdapter.getList().get(i).setSelected(false);
                    }
                }
                mBottomMenuAppointmentAdapter.notifyDataSetChanged();
            }

        } else if (bottomMenu.getMenuName().equalsIgnoreCase(getString(R.string.waiting_list))) {

            patientsListAddToWaitingLists = new ArrayList<>();
            for (int childIndex = 0; childIndex < mMyPatientsAdapter.getGroupList().size(); childIndex++) {
                PatientList patientList = mMyPatientsAdapter.getGroupList().get(childIndex);
                if (patientList.isSelected()) {
                    PatientAddToWaitingList patientInfoListObject = new PatientAddToWaitingList();
                    patientInfoListObject.setPatientName(patientList.getPatientName());
                    patientInfoListObject.setPatientId(String.valueOf(patientList.getPatientId()));
                    patientInfoListObject.setHospitalPatId(String.valueOf(patientList.getHospitalPatId()));
                    patientsListAddToWaitingLists.add(patientInfoListObject);
                }
            }

            if (!patientsListAddToWaitingLists.isEmpty()) {
                //if only one location is assigned to
                if (mDoctorLocationModel.size() == 1) {
                    mLocationId = mDoctorLocationModel.get(0).getLocationId();
                    mClinicName = mDoctorLocationModel.get(0).getClinicName();
                    mClinicArea = mDoctorLocationModel.get(0).getArea();
                    mClinicCity = mDoctorLocationModel.get(0).getCity();
                    callWaitingListApi();
                } else {
                    showDialogToSelectLocation(mDoctorLocationModel, null);
                }

                for (int i = 0; i < mBottomMenuAppointmentAdapter.getList().size(); i++) {
                    if (mBottomMenuAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.waiting_list))) {
                        mBottomMenuAppointmentAdapter.getList().get(i).setSelected(false);
                    }
                }
                mBottomMenuAppointmentAdapter.notifyDataSetChanged();
            } else {
                CommonMethods.showToast(getActivity(), getString(R.string.please_select_patients));
                for (int i = 0; i < mBottomMenuAppointmentAdapter.getList().size(); i++) {
                    if (mBottomMenuAppointmentAdapter.getList().get(i).getMenuName().equalsIgnoreCase(getString(R.string.waiting_list))) {
                        mBottomMenuAppointmentAdapter.getList().get(i).setSelected(false);
                    }
                }
                mBottomMenuAppointmentAdapter.notifyDataSetChanged();
            }
        }

    }

    private void showDialogForSmsLocationSelection(ArrayList<DoctorLocationModel> mDoctorLocationModel) {
        final Dialog dialog = new Dialog(getActivity());

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_location_waiting_list_layout);

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


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

    }

    private void showDialogToSelectLocation(ArrayList<DoctorLocationModel> mPatientListsOriginal, final String calledFrom) {

        final Dialog dialog = new Dialog(getActivity());
        final Bundle b = new Bundle();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_select_location_waiting_list_layout);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        if (!DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, getActivity()).equals("")) {
            mLocationId = Integer.parseInt(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, getActivity()));

            for (DoctorLocationModel doctorLocationModel : getDoctorLocationModels()) {
                if (doctorLocationModel.getLocationId().equals(mLocationId)) {
                    b.putInt(DMSConstants.CLINIC_ID, doctorLocationModel.getClinicId());
                    b.putInt(DMSConstants.CITY_ID, doctorLocationModel.getCityId());
                    b.putString(DMSConstants.LOCATION_ID, String.valueOf(doctorLocationModel.getLocationId()));
                    break;
                }
            }
        }
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        for (int index = 0; index < mPatientListsOriginal.size(); index++) {
            final DoctorLocationModel clinicList = mPatientListsOriginal.get(index);

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
                    mClinicCity = clinicList.getCity();
                    mClinicArea = clinicList.getArea();
                    CommonMethods.Log("clinicList", "" + clinicList.toString());
                }
            });
            radioGroup.addView(radioButton);
        }

        TextView okButton = (TextView) dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mLocationId != 0) {
                    if (getString(R.string.new_patients).equalsIgnoreCase(calledFrom)) {

                        for (DoctorLocationModel doctorLocationModel : getDoctorLocationModels()) {
                            if (doctorLocationModel.getLocationId().equals(mLocationId)) {
                                b.putInt(DMSConstants.CLINIC_ID, doctorLocationModel.getClinicId());
                                b.putInt(DMSConstants.CITY_ID, doctorLocationModel.getCityId());
                                b.putString(DMSConstants.CITY_NAME, doctorLocationModel.getCity());
                                b.putString(DMSConstants.LOCATION_ID, String.valueOf(doctorLocationModel.getLocationId()));
                                break;
                            }
                        }

                        Intent i = new Intent(getActivity(), AddNewPatientWebViewActivity.class);
                        //  Intent i = new Intent(getActivity(), AddNewPatientActivity.class);
                        i.putExtra(DMSConstants.PATIENT_DETAILS, b);
                        getActivity().startActivityForResult(i, ADD_PATIENT_REQUEST);

                        CommonMethods.Log("DOC_ID", "" + Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getActivity())));
                    } else {
                        callWaitingListApi();
                    }
                    dialog.cancel();
                } else
                    Toast.makeText(getActivity(), "Please select clinic location.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }


    private void callWaitingListApi() {
        DMSPreferencesManager.putString(DMSPreferencesManager.DMS_PREFERENCES_KEY.SELECTED_LOCATION_ID, String.valueOf(mLocationId), getActivity());
        for (int i = 0; i < mDoctorLocationModel.size(); i++) {
            if (mLocationId == mDoctorLocationModel.get(i).getLocationId()) {
                mClinicId = mDoctorLocationModel.get(i).getClinicId();
                mClinicName = mDoctorLocationModel.get(i).getClinicName();
                mClinicCity = mDoctorLocationModel.get(i).getCity();
                mClinicArea = mDoctorLocationModel.get(i).getArea();
            }
        }
        ArrayList<AddToList> addToWaitingArrayList = new ArrayList<>();
        AddToList addToList = new AddToList();
        addToList.setPatientAddToWaitingList(patientsListAddToWaitingLists);
        addToList.setLocationId(mLocationId);
        addToList.setLocationDetails(mClinicName + ", " + mClinicArea + ", " + mClinicCity);
        addToWaitingArrayList.add(addToList);
        RequestToAddWaitingList requestForWaitingListPatients = new RequestToAddWaitingList();
        requestForWaitingListPatients.setAddToList(addToWaitingArrayList);
        requestForWaitingListPatients.setTime(CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.HH_mm_ss));
        requestForWaitingListPatients.setDate(CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.YYYY_MM_DD));
        requestForWaitingListPatients.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getActivity())));
        mAppointmentHelper.doAddToWaitingListFromMyPatients(requestForWaitingListPatients);
    }


    public boolean callOnBackPressed() {
        return mMyPatientsAdapter != null && mMyPatientsAdapter.isLongPressed;
    }

    public void removeCheckBox() {
        recyclerViewBottom.setVisibility(View.GONE);
        mMyPatientsAdapter.setLongPressed(false);
        for (int index = 0; index < mMyPatientsAdapter.getGroupList().size(); index++) {
            mMyPatientsAdapter.getGroupList().get(index).setSelected(false);
        }
        mMyPatientsAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.rightFab, R.id.leftFab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rightFab:
                MyPatientsActivity activity = (MyPatientsActivity) getActivity();
                activity.openDrawer();
                break;
            case R.id.leftFab:
                if (mDoctorLocationModel.size() == 1) {
                    final Bundle b = new Bundle();
                    b.putInt(DMSConstants.CLINIC_ID, mDoctorLocationModel.get(0).getClinicId());
                    b.putInt(DMSConstants.CITY_ID, mDoctorLocationModel.get(0).getCityId());
                    b.putString(DMSConstants.CITY_NAME, mDoctorLocationModel.get(0).getCity());
                    b.putString(DMSConstants.LOCATION_ID, String.valueOf(mDoctorLocationModel.get(0).getLocationId()));
                    Intent i = new Intent(getActivity(), AddNewPatientWebViewActivity.class);
                    i.putExtra(DMSConstants.PATIENT_DETAILS, b);
                    getActivity().startActivityForResult(i, ADD_PATIENT_REQUEST);
                    CommonMethods.Log("DOC_ID", "" + Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getActivity())));
                } else {
                    showDialogToSelectLocation(mDoctorLocationModel, getString(R.string.new_patients));
                }
                break;
        }
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_GET_SEARCH_RESULT_MY_PATIENT)) {
            MyPatientBaseModel myAppointmentsBaseModel = (MyPatientBaseModel) customResponse;

            if (myAppointmentsBaseModel.getCommon().getStatusCode().equals(SUCCESS)) {
                ArrayList<PatientList> mLoadedPatientList = myAppointmentsBaseModel.getPatientDataModel().getPatientList();
                mMyPatientsAdapter.addAll(mLoadedPatientList, ((MyPatientsActivity) getActivity()).selectedDoctorId, searchText);

                if (!mMyPatientsAdapter.getGroupList().isEmpty()) {
                    emptyListView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                }

            }
        } else if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_ADD_TO_WAITING_LIST)) {
            AddToWaitingListBaseModel addToWaitingListBaseModel = (AddToWaitingListBaseModel) customResponse;
            if (DMSConstants.RESPONSE_OK.equalsIgnoreCase(addToWaitingListBaseModel.getCommon().getSuccess())) {
                if (addToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse().get(0).getStatusMessage().toLowerCase().contains(getString(R.string.patients_added_to_waiting_list).toLowerCase())) {
                    Intent intent = new Intent(getActivity(), WaitingMainListActivity.class);
                    intent.putExtra(LOCATION_ID, mLocationId);
                    startActivity(intent);
                    Toast.makeText(getActivity(), addToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse().get(0).getStatusMessage(), Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    getActivity().setResult(RESULT_CLOSE_ACTIVITY_WAITING_LIST);
                } else if (addToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse().get(0).getStatusMessage().toLowerCase().contains(getString(R.string.patient_limit_exceeded).toLowerCase())) {
                    Toast.makeText(getActivity(), addToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse().get(0).getStatusMessage(), Toast.LENGTH_LONG).show();

                } else if (addToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse().get(0).getStatusMessage().toLowerCase().contains(getString(R.string.already_exists_in_waiting_list).toLowerCase())) {
                    Toast.makeText(getActivity(), addToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse().get(0).getStatusMessage(), Toast.LENGTH_LONG).show();

                } else if (addToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse().get(0).getStatusMessage().toLowerCase().contains(getString(R.string.added_to_waiting_list).toLowerCase())) {
                    Intent intent = new Intent(getActivity(), WaitingMainListActivity.class);
                    intent.putExtra(LOCATION_ID, mLocationId);
                    startActivity(intent);
                    Toast.makeText(getActivity(), addToWaitingListBaseModel.getAddToWaitingModel().getAddToWaitingResponse().get(0).getStatusMessage(), Toast.LENGTH_LONG).show();
                    getActivity().finish();
                    getActivity().setResult(RESULT_CLOSE_ACTIVITY_WAITING_LIST);
                }
            }
        }
        if (mOldDataTag.equalsIgnoreCase(DMSConstants.TASK_GET_DOCTOR_SMS_TEMPLATE)) {
            TemplateBaseModel templateBaseModel = (TemplateBaseModel) customResponse;
            ArrayList<TemplateList> templateLists = templateBaseModel.getTemplateDataModel().getTemplateList();
            if (!templateLists.isEmpty()) {
                Intent intent = new Intent(getActivity(), TemplateListForMyPatients.class);
                intent.putExtra(LOCATION_ID, mLocationId);
                intent.putExtra(DMSConstants.CLINIC_ID, mClinicId);
                intent.putExtra(DMSConstants.CLINIC_NAME, mClinicName);
                intent.putParcelableArrayListExtra(DMSConstants.PATIENT_LIST, patientInfoLists);
                intent.putParcelableArrayListExtra(DMSConstants.TEMPLATE_LIST, templateLists);
                startActivity(intent);
            } else {

                Intent intent = new Intent(getActivity(), SendSmsPatientActivity.class);
                intent.putExtra(LOCATION_ID, mLocationId);
                intent.putExtra(DMSConstants.CLINIC_ID, mClinicId);
//                intent.putExtra(DMSConstants.TEMPLATE_OBJECT, templateList);
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

    public void nextPage(int pageNo, boolean isInternetAvailable) {
        boolean isAllPatientDownloaded = DMSPreferencesManager.getBoolean(DMSPreferencesManager.DMS_PREFERENCES_KEY.PATIENT_DOWNLOAD, getActivity());

        if (isInternetAvailable && !isAllPatientDownloaded) {
            mAppointmentHelper = new AppointmentHelper(getContext(), this);
            mRequestSearchPatients.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getContext())));
            mRequestSearchPatients.setSearchText(searchText);
            mRequestSearchPatients.setPageNo(pageNo);
            mAppointmentHelper.doGetSearchResult(mRequestSearchPatients, searchEditText.getText().toString().isEmpty());
        } else {
            ArrayList<PatientList> offlineAddedPatients = appDBHelper.getOfflineAddedPatients(mRequestSearchPatients, pageNo, searchEditText.getText().toString());
            if (!offlineAddedPatients.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyListView.setVisibility(View.GONE);
                mMyPatientsAdapter.addAll(offlineAddedPatients, ((MyPatientsActivity) getActivity()).selectedDoctorId, searchEditText.getText().toString());
                mMyPatientsAdapter.notifyDataSetChanged();
            } else {
                if (pageNo == 0) {
                    recyclerView.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void searchPatients(boolean isInternetAvailable) {
        mMyPatientsAdapter.clear();
        if (isInternetAvailable) {
            mRequestSearchPatients.setPageNo(0);
            mAppointmentHelper = new AppointmentHelper(getContext(), this);
            mRequestSearchPatients.setDocId(Integer.valueOf(DMSPreferencesManager.getString(DMSPreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getContext())));
            mRequestSearchPatients.setSearchText(searchText);
            mAppointmentHelper.doGetSearchResult(mRequestSearchPatients, searchEditText.getText().toString().isEmpty());
        } else {
            ArrayList<PatientList> offlineAddedPatients = appDBHelper.getOfflineAddedPatients(mRequestSearchPatients, 0, searchEditText.getText().toString());
            if (!offlineAddedPatients.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                emptyListView.setVisibility(View.GONE);
                mMyPatientsAdapter.addAll(offlineAddedPatients, ((MyPatientsActivity) getActivity()).selectedDoctorId, searchEditText.getText().toString());
                mMyPatientsAdapter.notifyDataSetChanged();
            } else {
                recyclerView.setVisibility(View.GONE);
                emptyListView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void apply(RequestSearchPatients mRequestSearchPatients, boolean isReset) {
        this.mRequestSearchPatients.setFilterParams(mRequestSearchPatients.getFilterParams());
        this.mRequestSearchPatients.setSortField(mRequestSearchPatients.getSortField());
        this.mRequestSearchPatients.setSortOrder(mRequestSearchPatients.getSortOrder());

        if (!isReset)
            searchPatients(NetworkUtil.getConnectivityStatusBoolean(getContext()));
    }

    public void updateList(ArrayList<PatientUpdateDetail> syncList) {
        if (mMyPatientsAdapter != null) {
            ArrayList<PatientList> groupList = mMyPatientsAdapter.getGroupList();
            int syncPatientCounter = 0;
            for (int i = 0; i < groupList.size(); i++) {
                PatientList orgGroupList = groupList.get(i);
                for (PatientUpdateDetail syncObj :
                        syncList) {
                    if (Integer.parseInt(syncObj.getMobilePatientId()) == orgGroupList.getPatientId()) {
                        orgGroupList.setPatientId(syncObj.getPatientId());
                        orgGroupList.setHospitalPatId(syncObj.getHospitalPatId());
                        mMyPatientsAdapter.notifyItemChanged(i);
                        syncPatientCounter++;
                    }
                }
                if (syncPatientCounter == syncList.size())
                    break;
            }
        }
    }
}
