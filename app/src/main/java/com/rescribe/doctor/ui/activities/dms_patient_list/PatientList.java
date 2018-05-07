package com.rescribe.doctor.ui.activities.dms_patient_list;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.dms_adapters.Custom_Spin_Adapter;
import com.rescribe.doctor.adapters.dms_adapters.PatientExpandableListAdapter;
import com.rescribe.doctor.adapters.dms_adapters.ShowPatientNameAdapter;
import com.rescribe.doctor.adapters.dms_adapters.TagAdapter;
import com.rescribe.doctor.helpers.patient_list.DMSPatientsHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.DatePickerDialogListener;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.rescribe.doctor.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationList;
import com.rescribe.doctor.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationListData;
import com.rescribe.doctor.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationListResponseModel;
import com.rescribe.doctor.model.dms_models.responsemodel.annotationlistresponsemodel.DocTypeList;
import com.rescribe.doctor.model.dms_models.responsemodel.patientnamelistresponsemodel.LstPatient;
import com.rescribe.doctor.model.dms_models.responsemodel.patientnamelistresponsemodel.PatientNameListData;
import com.rescribe.doctor.model.dms_models.responsemodel.patientnamelistresponsemodel.PatientNameListResponseModel;
import com.rescribe.doctor.model.dms_models.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.rescribe.doctor.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.rescribe.doctor.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
 import com.rescribe.doctor.dms.views.treeViewHolder.arrow_expand.ArrowExpandIconTreeItemHolder;
import com.rescribe.doctor.dms.views.treeViewHolder.arrow_expand.ArrowExpandSelectableHeaderHolder;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.ui.activities.SplashScreenActivity;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientList extends AppCompatActivity implements HelperResponse, View.OnClickListener, AdapterView.OnItemSelectedListener, PatientExpandableListAdapter.OnPatientListener, TreeNode.TreeNodeClickListener {

    private static final long ANIMATION_DURATION = 500; // in milliseconds
    SimpleDateFormat dfDate = new SimpleDateFormat(RescribeConstants.DATE_PATTERN.YYYY_MM_DD, Locale.US);

    @BindView(R.id.expandableListView)
    ExpandableListView mPatientListView;

    @BindView(R.id.openFilterRightDrawerFAB)
    FloatingActionButton mOpenFilterViewFAB;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    DrawerLayout mDrawer;
    NavigationView mLeftNavigationView;

    @BindView(R.id.nav_right_view)
    FrameLayout mRightNavigationView;

    @BindView(R.id.spinner_selectId)
    Spinner mSpinSelectedId;

    @BindView(R.id.spinner_admissionDate)
    Spinner mSpinnerAmissionDate;

    @BindView(R.id.et_uhid)
    EditText mUHIDEditText;

    @BindView(R.id.et_fromdate)
    EditText mFromDateEditText;

    @BindView(R.id.et_todate)
    EditText mToDateEditText;

    @BindView(R.id.et_searchPatientName)
    AutoCompleteTextView mSearchPatientNameEditText;

    @BindView(R.id.et_userEnteredAnnotation)
    EditText mAnnotationEditText;

    @BindView(R.id.et_search_annotation)
    EditText mSearchAnnotationEditText;

    @BindView(R.id.bt_clear_search_annotation)
    ImageView mClearSearchAnnotationButton;

    @BindView(R.id.bt_clear_patient_name)
    ImageView mClearPatientNameButton;

    @BindView(R.id.apply)
    TextView mApplySearchFilter;

    @BindView(R.id.reset)
    TextView mResetSearchFilter;

    @BindView(R.id.annotationTreeViewContainer)
    RelativeLayout mAnnotationTreeViewContainer;


    //----------------

    View mLeftHeaderView;
    private ImageView mUserImage;
    private TextView mUserName;
    private String mSelectedId;
    private String mSelectedFromDate;
    private String mAdmissionDate;
    private String[] mArrayId;
    private Context mContext;
    Date mFromDate;
    private RelativeLayout mFirstFileTypeProgressDialogLayout;
    private Custom_Spin_Adapter mCustomSpinAdapter;
    private DMSPatientsHelper mPatientsHelper;
    private TagAdapter mTagsAdapter;
    private List<LstPatient> mLstPatient;
    private RecyclerView mRecycleTag;
    private static Handler mAddedTagsEventHandler;
    private HashMap<String, Object> mAddedTagsForFiltering;
    private AndroidTreeView mAndroidTreeView;
    private AnnotationListData mAnnotationListData;
    private PatientNameListData mPatientNameListData;
    String patientName;
    ArrayList mPatientLists;
    private String TAG = this.getClass().getName();
    private boolean isCompareDialogCollapsed = true;
    private RelativeLayout mCompareDialogLayout;
    private TextView mCompareLabel;
    private ImageView mFileOneIcon;
    private TextView mFileOneType;
    private TextView mFileOneAdmissionDate;
    //    private TextView mFileOneDischargeDate;
    private ImageView mFileTwoIcon;
    private TextView mFileTwoType;
    private TextView mFileTwoAdmissionDate;
    //    private TextView mFileTwoDischargeDate;
    private Button mCompareButton;
    private PatientExpandableListAdapter patientExpandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_list_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);
        initialize();
    }


    private void initialize() {
        initializeVariables();
        bindView();
        doGetPatientList();
    }

    // intialize variables
    private void initializeVariables() {
        mContext = PatientList.this;
        mPatientsHelper = new DMSPatientsHelper(this, this);
        doGetPatientNameList();
        mAddedTagsForFiltering = new HashMap<String, Object>();
        mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, RescribeConstants.BLANK);
        mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.TO_DATE, RescribeConstants.BLANK);
        mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.FROM_DATE, RescribeConstants.BLANK);
        mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.FILE_TYPE, RescribeConstants.BLANK);
        mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.DATE_TYPE, RescribeConstants.BLANK);
        mAddedTagsForFiltering.put(RescribeConstants.ID, RescribeConstants.BLANK);
        mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, RescribeConstants.BLANK);
        //-------------
        mArrayId = getResources().getStringArray(R.array.ids);

        mAddedTagsEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                doGetPatientList();
            }
        };
        //-------------

        //------------
        mTagsAdapter = new TagAdapter(mContext, mAddedTagsForFiltering, mAddedTagsEventHandler);

    }

    private void doGetPatientNameList() {
        mPatientsHelper.doGetPatientNameList();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null) {
            if (mDrawer.isDrawerOpen(GravityCompat.END) && mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.END);
                mDrawer.closeDrawer(GravityCompat.START);
            } else if (mDrawer.isDrawerOpen(GravityCompat.END)) {
                mDrawer.closeDrawer(GravityCompat.END);
            } else if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else super.onBackPressed();
        } else
            super.onBackPressed();
    }

    // register all views
    private void bindView() {
        int width = (int) (getResources().getDisplayMetrics().widthPixels / (CommonMethods.isTablet(mContext) ? 1.6 : 1.2));

        //---------
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (mAnnotationListData == null) {
                    mPatientsHelper.doGetAllAnnotations();
                } else {
                    createAnnotationTreeStructure(mAnnotationListData, false);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        //---------
        mRecycleTag = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);
        mRecycleTag.setLayoutManager(layoutManager);

        mLeftNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mLeftHeaderView = mLeftNavigationView.getHeaderView(0);
        //---------------

        DrawerLayout.LayoutParams leftParams = (DrawerLayout.LayoutParams) mLeftNavigationView.getLayoutParams();
        leftParams.width = width;
        mLeftNavigationView.setLayoutParams(leftParams);
        //---------------
        mUserImage = (ImageView) mLeftHeaderView.findViewById(R.id.userImage);
        mUserName = (TextView) mLeftHeaderView.findViewById(R.id.userName);

        if (RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.USER_GENDER, mContext).equals("M")) {
            mUserImage.setBackground(getResources().getDrawable(R.drawable.image_male));
        } else {
            mUserImage.setBackground(getResources().getDrawable(R.drawable.image_female));
        }
        //---------

        // left navigation drawer clickListener
        mLeftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_logout) {
                    String mServerPath = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext);
                    String isValidConfig = RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, mContext);
                    RescribePreferencesManager.clearSharedPref(mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mServerPath, mContext);
                    RescribePreferencesManager.putString(RescribePreferencesManager.DMS_PREFERENCES_KEY.IS_VALID_IP_CONFIG, isValidConfig, mContext);
                    Intent intent = new Intent(PatientList.this, SplashScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    // Handle the camera action
                } else if (id == R.id.change_ip_address) {
                    CommonMethods.showDialog(RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.SERVER_PATH, mContext), getString(R.string.change_ip), mContext);

                }

                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        //---------
        mSpinSelectedId.setOnItemSelectedListener(this);
        mSpinnerAmissionDate.setOnItemSelectedListener(this);
        //---------------
        mOpenFilterViewFAB.setOnClickListener(this);
        mResetSearchFilter.setOnClickListener(this);
        mApplySearchFilter.setOnClickListener(this);
        mFromDateEditText.setOnClickListener(this);
        mToDateEditText.setOnClickListener(this);
        mClearSearchAnnotationButton.setOnClickListener(this);
        mClearSearchAnnotationButton.setVisibility(View.GONE);
        mClearPatientNameButton.setOnClickListener(this);
        mClearPatientNameButton.setBackground(getResources().getDrawable(R.mipmap.user));
        mUserName.setText(RescribePreferencesManager.getString(RescribeConstants.USERNAME, mContext));
        //--------
        // setting adapter for spinner in header view of right drawer
        mCustomSpinAdapter = new Custom_Spin_Adapter(this, mArrayId, getResources().getStringArray(R.array.select_id));
        mSpinSelectedId.setAdapter(mCustomSpinAdapter);
        //------
        onTextChanged();

        mCompareDialogLayout = (RelativeLayout) findViewById(R.id.compareDialog);
        mCompareLabel = (TextView) findViewById(R.id.compareLabel);
        mFileOneIcon = (ImageView) findViewById(R.id.fileOneIcon);
        mFileOneType = (TextView) findViewById(R.id.fileOneType);
        mFileOneAdmissionDate = (TextView) findViewById(R.id.fileOneAdmissionDate);
//        mFileOneDischargeDate = (TextView) findViewById(R.id.fileOneDischargeDate);
        mFileTwoIcon = (ImageView) findViewById(R.id.fileTwoIcon);
        mFileTwoType = (TextView) findViewById(R.id.fileTwoType);
        mFileTwoAdmissionDate = (TextView) findViewById(R.id.fileTwoAdmissionDate);
//        mFileTwoDischargeDate = (TextView) findViewById(R.id.fileTwoDischargeDate);
        mCompareButton = (Button) findViewById(R.id.compareButton);

        mCompareLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCompareDialogCollapsed)
                    expandCompareDialog();
                else collapseCompareDialog();
            }
        });

        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        mRightNavigationView.setLayoutParams(layoutParams);
        Log.e(TAG, "++++++++++++++++++++++");

        mAnnotationTreeViewContainer.addView(CommonMethods.loadView(R.layout.mydialog, this));
        mFirstFileTypeProgressDialogLayout = (RelativeLayout) mAnnotationTreeViewContainer.findViewById(R.id.progressBarContainerLayout);


    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag == RescribeConstants.TASK_PATIENT_LIST) {
            ShowSearchResultResponseModel showSearchResultResponseModel = (ShowSearchResultResponseModel) customResponse;
            List<SearchResult> searchResult = showSearchResultResponseModel.getSearchResultData().getSearchResult();

            patientExpandableListAdapter = new PatientExpandableListAdapter(this, searchResult);

            mPatientListView.setAdapter(patientExpandableListAdapter);
            mPatientListView.setGroupIndicator(null);
            mPatientListView.setChildIndicator(null);
            mPatientListView.setChildDivider(ContextCompat.getDrawable(this, R.color.transparent));
            mPatientListView.setDivider(ContextCompat.getDrawable(this, R.color.white));
            mPatientListView.setDividerHeight(2);

            //mPatientListView.setDividerHeight(2);
        } else if (mOldDataTag == RescribeConstants.TASK_ANNOTATIONS_LIST) {

            AnnotationListResponseModel annotationListResponseModel = (AnnotationListResponseModel) customResponse;
            mAnnotationListData = annotationListResponseModel.getAnnotationListData();
            createAnnotationTreeStructure(mAnnotationListData, false);
            mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        } else if (mOldDataTag == RescribeConstants.TASK_GET_PATIENT_NAME_LIST) {
            PatientNameListResponseModel patientNameListResponseModel = (PatientNameListResponseModel) customResponse;
            mPatientNameListData = patientNameListResponseModel.getData();
            mLstPatient = mPatientNameListData.getLstPatients();
            mPatientLists = new ArrayList();
            for (int i = 0; i < mLstPatient.size(); i++) {

                patientName = mLstPatient.get(i).getPatientName();
                mPatientLists.add(patientName);

            }
            ShowPatientNameAdapter adapter = new ShowPatientNameAdapter(this, R.layout.patient_filter_right_drawer, R.id.custom_spinner_txt_view_Id, mLstPatient);
            mSearchPatientNameEditText.setAdapter(adapter);
            Log.d(TAG, "" + mLstPatient);
        }

    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
    }

    /**
     * Return true if fields empty/validation failed, else false.
     *
     * @return
     */

    //onClick listener
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            //onclick on floating button
            case R.id.openFilterRightDrawerFAB:
                mDrawer.openDrawer(GravityCompat.END);
                if (mAnnotationListData == null) {
                    mFirstFileTypeProgressDialogLayout.setVisibility(View.VISIBLE);
                    mPatientsHelper.doGetAllAnnotations();
                } else {
                    createAnnotationTreeStructure(mAnnotationListData, false);
                }

                break;
            // on click of fromDate editext in right drawer
            case R.id.et_fromdate:


                new CommonMethods().datePickerDialog(this, new DatePickerDialogListener() {
                    @Override
                    public void getSelectedDate(String selectedTime) {
                        mSelectedFromDate = selectedTime;
                        mFromDateEditText.setText("" + selectedTime);
                        try {
                            mFromDate = dfDate.parse(mSelectedFromDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }, null, true, mFromDate);
                break;
            // on click of endDate ediText in right drawer
            case R.id.et_todate:
                new CommonMethods().datePickerDialog(this, new DatePickerDialogListener() {
                    @Override
                    public void getSelectedDate(String selectedTime) {
                        mToDateEditText.setText("" + selectedTime);

                    }
                }, null, false, mFromDate);
                break;
            //  on click of Reset in right drawer
            case R.id.reset:
                mAddedTagsForFiltering.clear();
                mUHIDEditText.setText(RescribeConstants.BLANK);
                mFromDateEditText.setText(RescribeConstants.BLANK);
                mToDateEditText.setText(RescribeConstants.BLANK);
                mSearchAnnotationEditText.setText(RescribeConstants.BLANK);
                mAnnotationEditText.setText(RescribeConstants.BLANK);
                mSearchPatientNameEditText.setText(RescribeConstants.BLANK);
                mSpinnerAmissionDate.setSelection(0);
                mSpinSelectedId.setSelection(0);
                // Reset Tree
                for (AnnotationList annotationList : mAnnotationListData.getAnnotationLists()) {
                    annotationList.setSelected(false);
                    for (DocTypeList docTypeList : annotationList.getDocTypeList())
                        docTypeList.setSelected(false);
                }

                createAnnotationTreeStructure(mAnnotationListData, false);
                // Reset Tree End
                try {
                    mFromDate = dfDate.parse("0000-00-00");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            //  on click of Apply in right drawer
            case R.id.apply:

                onCompareDialogShow(null, null, null, null, false);

                mAddedTagsForFiltering.clear();
                String fromDate = mFromDateEditText.getText().toString().trim();
                String toDate = mToDateEditText.getText().toString().trim();


                //*********adding field values in arrayList to generate tags in recycler view
                //we are adding refrence id and file type value in FILE_TYPE parameter
                //Reference id = UHID or OPD or IPD number *********//
                //--- FileType and enteredValue validation : START
                String enteredUHIDValue = mUHIDEditText.getText().toString().trim();
                if (mSelectedId.equalsIgnoreCase(getString(R.string.uhid)) && (enteredUHIDValue.length() == 0)) {
                    CommonMethods.showSnack(mContext, mUHIDEditText, getString(R.string.error_enter_uhid));
                    break;
                } else if ((enteredUHIDValue.length() != 0) && (mSelectedId.equalsIgnoreCase(getString(R.string.Select)))) {
                    CommonMethods.showSnack(mContext, mUHIDEditText, getString(R.string.Select) + " " + getString(R.string.ipd) + "/" + getString(R.string.opd) + "/" + getString(R.string.uhid));
                    break;
                } else {
                    if (!mSelectedId.equalsIgnoreCase(getString(R.string.Select))) {
                        mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mSelectedId + ":" + enteredUHIDValue);
                    }
                }
                //--- FileType and enteredValue validation : END

                if (!mAdmissionDate.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                    mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.DATE_TYPE, getString(R.string.date_type) + mAdmissionDate);
                }

                if (!fromDate.equalsIgnoreCase(RescribeConstants.BLANK)) {
                    mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.FROM_DATE, getString(R.string.from_date) + mFromDateEditText.getText().toString());
                }

                if (!toDate.equalsIgnoreCase(RescribeConstants.BLANK)) {
                    mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.TO_DATE, getString(R.string.to_date) + mToDateEditText.getText().toString());
                }

                if (mSearchPatientNameEditText.getText().toString().trim().length() != 0) {
                    mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, getString(R.string.patient_name) + mSearchPatientNameEditText.getText().toString());
                }

                if (mAnnotationEditText.getText().toString().trim().length() != 0) {
                    mAddedTagsForFiltering.put(RescribeConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, getString(R.string.enter_annotation) + mAnnotationEditText.getText().toString());
                }

                //----------
                ArrayList<Object> selectedAnnotations = getSelectedAnnotations();
                for (Object dataValue :
                        selectedAnnotations) {
                    //--- hashMap PatientNameListData : childName|id
                    if (dataValue instanceof DocTypeList) {
                        String dataValueString = getString(R.string.documenttype) + ((DocTypeList) dataValue).getTypeName() + "|" + String.valueOf(((DocTypeList) dataValue).getTypeId());
                        mAddedTagsForFiltering.put(dataValueString, dataValue);
                    } else if (dataValue instanceof AnnotationList) {
                        String dataValueString = getString(R.string.documenttype) + ((AnnotationList) dataValue).getCategoryName() + "|" + String.valueOf(((AnnotationList) dataValue).getCategoryId());
                        mAddedTagsForFiltering.put(dataValueString, dataValue);
                    }
                }

                mTagsAdapter = new TagAdapter(mContext, mAddedTagsForFiltering, mAddedTagsEventHandler);
                mRecycleTag.setAdapter(mTagsAdapter);
                mDrawer.closeDrawer(GravityCompat.END);
                doGetPatientList();


                break;

            case R.id.bt_clear_search_annotation:
                mSearchAnnotationEditText.setText("");
                break;
            case R.id.bt_clear_patient_name:
                mSearchPatientNameEditText.setText("");
                break;
        }

    }


    private void doGetPatientList() {

        ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();

        String selectedFileType = mTagsAdapter.getUpdatedTagValues(RescribeConstants.PATIENT_LIST_PARAMS.FILE_TYPE, "0");

        //THIS IS DONE BCAZ, FILETYPE contains (IPD/OPD/UHID:enteredID)
        String enteredID = mTagsAdapter.getUpdatedTagValues(RescribeConstants.PATIENT_LIST_PARAMS.FILE_TYPE, "1");

        showSearchResultRequestModel.setFileType(selectedFileType);

        if (getString(R.string.uhid).equalsIgnoreCase(selectedFileType)) {
            showSearchResultRequestModel.setPatientId(enteredID);
            showSearchResultRequestModel.setReferenceId(RescribeConstants.BLANK);
            showSearchResultRequestModel.setFileType(RescribeConstants.BLANK);
        } else {
            showSearchResultRequestModel.setPatientId(RescribeConstants.BLANK);
            showSearchResultRequestModel.setReferenceId(enteredID);
            if (selectedFileType.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                showSearchResultRequestModel.setFileType(RescribeConstants.BLANK);
            }
        }
        showSearchResultRequestModel.setDateType(mTagsAdapter.getUpdatedTagValues(RescribeConstants.PATIENT_LIST_PARAMS.DATE_TYPE, null));
        showSearchResultRequestModel.setFromDate(mTagsAdapter.getUpdatedTagValues(RescribeConstants.PATIENT_LIST_PARAMS.FROM_DATE, null));
        showSearchResultRequestModel.setToDate(mTagsAdapter.getUpdatedTagValues(RescribeConstants.PATIENT_LIST_PARAMS.TO_DATE, null));
        showSearchResultRequestModel.setPatientName(mTagsAdapter.getUpdatedTagValues(RescribeConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, null));
        showSearchResultRequestModel.setAnnotationText(mTagsAdapter.getUpdatedTagValues(RescribeConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, null));

        showSearchResultRequestModel.setDocTypeId(new String[]{mTagsAdapter.getUpdatedTagValues(getString(R.string.documenttype), null)});

        mPatientsHelper.doGetPatientList(showSearchResultRequestModel);
    }

    private void createAnnotationTreeStructure(AnnotationListData annotationListData, boolean isExpanded) {

        mAnnotationTreeViewContainer.removeAllViews();

        TreeNode root = TreeNode.root();
        int lstDocCategoryObjectLeftPadding = (int) (getResources().getDimension(R.dimen.dp30) / getResources().getDisplayMetrics().density);
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);
        int textColor = ContextCompat.getColor(this, R.color.black);

        List<AnnotationList> annotationLists = annotationListData.getAnnotationLists();

        for (int i = 0; i < annotationLists.size(); i++) {
            AnnotationList annotationCategoryObject = annotationLists.get(i);
            ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded);
            selectableHeaderHolder.setOnlyOneNodeExpanded(true);
            TreeNode folder1 = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, annotationCategoryObject.getCategoryName(), annotationCategoryObject, i))
                    .setViewHolder(selectableHeaderHolder);

            List<DocTypeList> docTypeList = annotationCategoryObject.getDocTypeList();

            for (int j = 0; j < docTypeList.size(); j++) {
                DocTypeList docTypeListObject = docTypeList.get(j);
                String dataToShow = docTypeListObject.getTypeName() + "|" + docTypeListObject.getTypeId();

                ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding);
                lstDocTypeChildSelectableHeaderHolder.setOnlyOneNodeExpanded(true);
                TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, docTypeListObject, i))
                        .setViewHolder(lstDocTypeChildSelectableHeaderHolder);

                folder1.addChildren(lstDocTypeChildFolder);
            }
            root.addChildren(folder1);
        }

        mAndroidTreeView = new AndroidTreeView(this, root);
        mAndroidTreeView.setDefaultAnimation(false);
        mAndroidTreeView.setUse2dScroll(true);
        mAndroidTreeView.setDefaultNodeClickListener(this);
        mAndroidTreeView.setUseAutoToggle(false);
        mAndroidTreeView.setSelectionModeEnabled(true);
        mAnnotationTreeViewContainer.addView(mAndroidTreeView.getView());
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.spinner_selectId) {
            int indexSselectedId = parent.getSelectedItemPosition();
            mArrayId = getResources().getStringArray(R.array.select_id);
            mSelectedId = mArrayId[indexSselectedId];
            if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mSpinnerAmissionDate.setEnabled(true);
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.ipd))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.ipd_array));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mUHIDEditText.setHint(getResources().getString(R.string.error_enter_ipd));
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.opd))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.opd_array));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mUHIDEditText.setHint(getResources().getString(R.string.error_enter_opd));
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.uhid))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mUHIDEditText.setHint(getResources().getString(R.string.error_enter_uhid));
            }
        } else if (parent.getId() == R.id.spinner_admissionDate) {
            int indexSselectedId = parent.getSelectedItemPosition();
            mArrayId = getResources().getStringArray(R.array.admission_date);
            mAdmissionDate = mArrayId[indexSselectedId];
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private ArrayList<Object> getSelectedAnnotations() {
        String parent = "";
        ArrayList<Object> annotationList = new ArrayList<>();
        if (mAndroidTreeView != null) {
            List<TreeNode> selected = mAndroidTreeView.getSelected();

            if (selected.size() > 0) {
                for (TreeNode data :
                        selected) {
                    if (data.getParent().isSelected()) {
                        annotationList.add(((ArrowExpandIconTreeItemHolder.IconTreeItem) data.getParent().getValue()).objectData);
                    } else {
                        //-- This is done for child only, no parent name will come in the list.
                        annotationList.add(((ArrowExpandIconTreeItemHolder.IconTreeItem) data.getValue()).objectData);
                    }
                }
            }
        }

        return annotationList;
    }


    protected void onTextChanged() {
        mSearchPatientNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = mSearchPatientNameEditText.getText().toString();
                if (enteredString.equals("")) {
                    mClearPatientNameButton.setBackground(getResources().getDrawable(R.mipmap.user));

                } else {
                    mClearPatientNameButton.setBackground(getResources().getDrawable(R.mipmap.crosswithcircle));

                }

            }
        });

        mSearchAnnotationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredString = mSearchAnnotationEditText.getText().toString();
                if (enteredString.equals("")) {
                    mClearSearchAnnotationButton.setVisibility(View.GONE);
                    createAnnotationTreeStructure(mAnnotationListData, false);
                } else {
                    mClearSearchAnnotationButton.setVisibility(View.VISIBLE);

                    CommonMethods.Log(TAG, "onSearchAnnotationEditor:" + enteredString);
                    AnnotationListData annotationListData = new AnnotationListData();
                    List<AnnotationList> annotationTempList = new ArrayList<>();
                    if (mAnnotationListData != null) {
                        List<AnnotationList> parentAnnotationList = mAnnotationListData.getAnnotationLists();
                        if (parentAnnotationList.size() > 0) {

                            for (AnnotationList tempParentObject : parentAnnotationList) {
                                //-------
                                List<DocTypeList> childDocTypeTemp = new ArrayList<DocTypeList>();
                                List<DocTypeList> childDocTypeList = tempParentObject.getDocTypeList();
                                for (DocTypeList tempDocTypeObject : childDocTypeList) {
                                    if (tempDocTypeObject.getTypeName().toLowerCase().startsWith(enteredString.toLowerCase())) {
                                        childDocTypeTemp.add(tempDocTypeObject);
                                    }
                                }

                                if (childDocTypeTemp.size() > 0) {
                                    AnnotationList annotationListTemp = new AnnotationList();
//                                annotationListTemp.setSelected(tempParentObject.getSelected());
                                    annotationListTemp.setCategoryId(tempParentObject.getCategoryId());
                                    annotationListTemp.setCategoryName(tempParentObject.getCategoryName());
                                    annotationListTemp.setDocTypeList(childDocTypeTemp);
                                    annotationTempList.add(annotationListTemp);
                                }
                                //------
                            }
                        }
                    }
                    annotationListData.setAnnotationLists(annotationTempList);
                    createAnnotationTreeStructure(annotationListData, false);
                }
            }
        });
    }

    @Override
    public void onCompareDialogShow(final PatientFileData patientFileData1, final PatientFileData patientFileData2, String mCheckedBoxGroupName, final String tempName, boolean isChecked) {

        if (patientFileData2 == null && patientFileData1 == null) {
            mFileOneAdmissionDate.setText("");
            mFileOneType.setText(getString(R.string.adddocument));
            mFileOneIcon.setImageResource(R.drawable.ic_unselected_document);
            mFileTwoAdmissionDate.setText("");
            mFileTwoType.setText(getString(R.string.adddocument));
            mFileTwoIcon.setImageResource(R.drawable.ic_unselected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.grey_700));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);

//            mFileOneDischargeDate.setVisibility(View.GONE);
            mFileOneAdmissionDate.setVisibility(View.GONE);

//            mFileTwoDischargeDate.setVisibility(View.GONE);
            mFileTwoAdmissionDate.setVisibility(View.GONE);

            if (!isCompareDialogCollapsed && !isChecked) {
                collapseCompareDialog();
            }
        } else if (patientFileData2 == null && patientFileData1 != null) {

            if (getString(R.string.opd).equals(patientFileData1.getFileType())) {
                String visitOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                mFileOneAdmissionDate.setText("V: " + visitOneDate);
//                mFileOneDischargeDate.setVisibility(View.GONE);
                mFileOneAdmissionDate.setVisibility(View.VISIBLE);
            } else if (getString(R.string.ipd).equals(patientFileData1.getFileType())) {
                String admissionOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                String dischargeOneDate = CommonMethods.formatDateTime(patientFileData1.getDischargeDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                mFileOneAdmissionDate.setText("A: " + admissionOneDate + " - D: " + dischargeOneDate);
//                mFileOneDischargeDate.setText("D: " + dischargeOneDate);
//                mFileOneDischargeDate.setVisibility(View.VISIBLE);
                mFileOneAdmissionDate.setVisibility(View.VISIBLE);
            }

            mFileOneType.setText(patientFileData1.getFileType() + ":" + patientFileData1.getReferenceId().toString());
            mFileOneIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.grey_700));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);
            if (isCompareDialogCollapsed)
                expandCompareDialog();
        } else if (patientFileData2 != null && patientFileData1 == null) {

            if (getString(R.string.opd).equals(patientFileData2.getFileType())) {
                String visitTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                mFileTwoAdmissionDate.setText("V: " + visitTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.GONE);
                mFileTwoAdmissionDate.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData2.getFileType())) {
                String admissionTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                String dischargeTwoDate = CommonMethods.formatDateTime(patientFileData2.getDischargeDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                mFileTwoAdmissionDate.setText("A: " + admissionTwoDate + " - D: " + dischargeTwoDate);
//                mFileTwoDischargeDate.setText("D: " + dischargeTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.VISIBLE);
                mFileTwoAdmissionDate.setVisibility(View.VISIBLE);
            }

            mFileTwoType.setText(patientFileData2.getFileType() + ":" + patientFileData2.getReferenceId().toString());
            mFileTwoIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.grey_700));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);
            if (isCompareDialogCollapsed)
                expandCompareDialog();
        } else {

            if (getString(R.string.opd).equals(patientFileData1.getFileType())) {
                String visitOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                mFileOneAdmissionDate.setText("V: " + visitOneDate);

//                mFileOneDischargeDate.setVisibility(View.GONE);
                mFileOneAdmissionDate.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData1.getFileType())) {
                String admissionOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                String dischargeOneDate = CommonMethods.formatDateTime(patientFileData1.getDischargeDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                mFileOneAdmissionDate.setText("A: " + admissionOneDate + " - D: " + dischargeOneDate);
//                mFileOneDischargeDate.setText("D: " + dischargeOneDate);

//                mFileOneDischargeDate.setVisibility(View.VISIBLE);
                mFileOneAdmissionDate.setVisibility(View.VISIBLE);
            }

            if (getString(R.string.opd).equals(patientFileData2.getFileType())) {
                String visitTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                mFileTwoAdmissionDate.setText("V: " + visitTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.GONE);
                mFileTwoAdmissionDate.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData2.getFileType())) {
                String admissionTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                String dischargeTwoDate = CommonMethods.formatDateTime(patientFileData2.getDischargeDate(), RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, RescribeConstants.DATE);
                mFileTwoAdmissionDate.setText("A: " + admissionTwoDate + " - D: " + dischargeTwoDate);
//                mFileTwoDischargeDate.setText("D: " + dischargeTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.VISIBLE);
                mFileTwoAdmissionDate.setVisibility(View.VISIBLE);
            }

            mFileOneType.setText(patientFileData1.getFileType() + ":" + patientFileData1.getReferenceId().toString());

            mFileTwoType.setText(patientFileData2.getFileType() + ":" + patientFileData2.getReferenceId().toString());

            mFileOneIcon.setImageResource(R.drawable.ic_selected_document);
            mFileTwoIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.Red));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_red_background));
            mCompareButton.setEnabled(true);
            mCompareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, FileTypeViewerActivity.class);
                    Bundle extra = new Bundle();
                    ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
                    dataToSend.add(patientFileData1);
                    dataToSend.add(patientFileData2);
                    SearchResult searchPatientInformation = patientExpandableListAdapter.searchPatientInfo(patientFileData1.getRespectiveParentPatientID());
                    extra.putSerializable(getString(R.string.compare), dataToSend);
                    extra.putString(RescribeConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
                    extra.putString(RescribeConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
                    extra.putString(RescribeConstants.ID, patientFileData1.getRespectiveParentPatientID());
                    extra.putString(RescribeConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, tempName);
                    intent.putExtra(RescribeConstants.DATA, extra);
                    startActivity(intent);
                }
            });
            if (isCompareDialogCollapsed && isChecked)
                expandCompareDialog();
        }
    }

    public void collapseCompareDialog() {
        isCompareDialogCollapsed = true;

        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) getResources().getDimension(R.dimen.compare_dialog), 0);
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCompareDialogLayout.getLayoutParams();
                params.height = CommonMethods.convertDpToPixel(value.intValue());
                mCompareDialogLayout.setLayoutParams(params);

            }
        });

        valueAnimator.start();

    }

    public void expandCompareDialog() {

        isCompareDialogCollapsed = false;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, (int) getResources().getDimension(R.dimen.compare_dialog));
        valueAnimator.setDuration(ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mCompareDialogLayout.getLayoutParams();
                params.height = CommonMethods.convertDpToPixel(value.intValue());
                mCompareDialogLayout.setLayoutParams(params);

            }
        });

        valueAnimator.start();
    }

    @Override
    public void onPatientListItemClick(PatientFileData childElement, String patientName) {
        Intent intent = new Intent(mContext, FileTypeViewerActivity.class);
        Bundle extra = new Bundle();
        ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
        dataToSend.add(childElement);
        SearchResult searchPatientInformation = patientExpandableListAdapter.searchPatientInfo(childElement.getRespectiveParentPatientID());
        extra.putSerializable(getString(R.string.compare), dataToSend);
        extra.putString(RescribeConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
        extra.putString(RescribeConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
        extra.putString(RescribeConstants.ID, childElement.getRespectiveParentPatientID());
        extra.putString(RescribeConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + patientName);
        intent.putExtra(RescribeConstants.DATA, extra);
        startActivity(intent);
    }

    @Override
    public void smoothScrollToPosition(int previousPosition) {
        mPatientListView.smoothScrollToPosition(previousPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        super.onConfigurationChanged(newConfig);
        ViewGroup.LayoutParams layoutParams = mRightNavigationView.getLayoutParams();
        layoutParams.width = width;
        mRightNavigationView.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(TreeNode node, Object value, View nodeView) {

        CheckBox nodeSelector = (CheckBox) nodeView.findViewById(R.id.node_selector);
        if (nodeSelector.isChecked()) {
            nodeSelector.setChecked(false);
        } else {
            nodeSelector.setChecked(true);
        }

        if (!node.isLeaf()) {
            for (TreeNode n : node.getChildren()) {
                mAndroidTreeView.selectNode(n, nodeSelector.isChecked());
            }
            for (DocTypeList docTypeList : ((AnnotationList) ((ArrowExpandIconTreeItemHolder.IconTreeItem) value).objectData).getDocTypeList()) {
                docTypeList.setSelected(nodeSelector.isChecked());
            }
        }

        DocTypeList docTypeListTemp = null;
        if (((ArrowExpandIconTreeItemHolder.IconTreeItem) value).objectData instanceof DocTypeList)
            docTypeListTemp = (DocTypeList) ((ArrowExpandIconTreeItemHolder.IconTreeItem) value).objectData;

        for (AnnotationList annotationCategoryObject : mAnnotationListData.getAnnotationLists()) {
            List<DocTypeList> docTypeList = annotationCategoryObject.getDocTypeList();
            if (docTypeListTemp != null)
                for (DocTypeList docTypeListObject : docTypeList) {
                    if (docTypeListObject.getTypeId().equals(docTypeListTemp.getTypeId())) {
                        docTypeListObject.setSelected(nodeSelector.isChecked());
                        node.setSelected(nodeSelector.isChecked());
                    }
                }
        }

    }
}
