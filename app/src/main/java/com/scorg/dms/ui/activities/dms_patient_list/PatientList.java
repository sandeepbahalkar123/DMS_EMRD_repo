package com.scorg.dms.ui.activities.dms_patient_list;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
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
import android.widget.AbsListView;
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

import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.Custom_Spin_Adapter;
import com.scorg.dms.adapters.dms_adapters.PatientExpandableListAdapter;
import com.scorg.dms.adapters.dms_adapters.ShowPatientNameAdapter;
import com.scorg.dms.adapters.dms_adapters.TagAdapter;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationList;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationListData;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationListResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.DocTypeList;
import com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel.LstPatient;
import com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel.PatientNameListData;
import com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel.PatientNameListResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandIconTreeItemHolder;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandSelectableHeaderHolder;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.NetworkUtil;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PatientList extends AppCompatActivity implements HelperResponse, View.OnClickListener, AdapterView.OnItemSelectedListener, PatientExpandableListAdapter.OnPatientListener, TreeNode.TreeNodeClickListener {

    private static final long ANIMATION_DURATION = 500; // in milliseconds
    SimpleDateFormat dfDate = new SimpleDateFormat(DMSConstants.DATE_PATTERN.YYYY_MM_DD, Locale.US);

    @BindView(R.id.expandableListView)
    ExpandableListView mPatientListView;

    @BindView(R.id.openFilterRightDrawerFAB)
    FloatingActionButton mOpenFilterViewFAB;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    DrawerLayout mDrawer;

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
    private TextView mFileOnePatientID;
    private TextView mFileTwoPatientID;

    //    private TextView mFileOneDischargeDate;
    private ImageView mFileTwoIcon;
    private TextView mFileTwoType;
    private TextView mFileTwoAdmissionDate;
    //    private TextView mFileTwoDischargeDate;
    private Button mCompareButton;
    private PatientExpandableListAdapter patientExpandableListAdapter;
    private ActionBar mSupportActionBar;
    private DatePickerDialog mDatePickerDialog;
    private ShowPatientNameAdapter mShowPatientNameAdapter;

    //---------
    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
    private String[] mFileTypeStringArrayExtra;
    //---------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_list_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);

        //----------
        setSupportActionBar(mToolbar);
        mSupportActionBar = getSupportActionBar();
        mSupportActionBar.setTitle(getString(R.string.my_patients));
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
        //----------
        initialize();
    }


    private void initialize() {
        initializeVariables();
        bindView();
        doGetPatientList();

        mPatientListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (NetworkUtil.isInternetAvailable(mContext)) {


                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                            currentPage++;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                        // I load the next page of gigs using a background task,
                        // but you can call any function here.
                        currentPage = currentPage + 1;
                        doGetPatientList();
                        loading = true;
                    }
                }
            }
        });
    }

    // intialize variables
    private void initializeVariables() {

        //--------
        patientExpandableListAdapter = new PatientExpandableListAdapter(this, new ArrayList<SearchResult>());
        mPatientListView.setAdapter(patientExpandableListAdapter);
        //--------

        mContext = PatientList.this;
        mPatientsHelper = new DMSPatientsHelper(this, this);
        //doGetPatientNameList();
        mAddedTagsForFiltering = new HashMap<String, Object>();
        mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, DMSConstants.BLANK);
        mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.TO_DATE, DMSConstants.BLANK);
        mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.FROM_DATE, DMSConstants.BLANK);
        mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, DMSConstants.BLANK);
        mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.DATE_TYPE, DMSConstants.BLANK);
        mAddedTagsForFiltering.put(DMSConstants.ID, DMSConstants.BLANK);
        mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, DMSConstants.BLANK);
        //-------------
        mArrayId = getResources().getStringArray(R.array.ids);

        mAddedTagsEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                patientExpandableListAdapter.removeAll();
                currentPage = 0;
                doGetPatientList();
            }
        };
        //-------------

        //------------
        mTagsAdapter = new TagAdapter(mContext, mAddedTagsForFiltering, mAddedTagsEventHandler);

    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null) {
            if (mDrawer.isDrawerOpen(GravityCompat.END) && mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.END);
                // mDrawer.closeDrawer(GravityCompat.START);
            } else if (mDrawer.isDrawerOpen(GravityCompat.END)) {
                mDrawer.closeDrawer(GravityCompat.END);
            } else if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                mDrawer.closeDrawer(GravityCompat.START);
            } else super.onBackPressed();
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
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

        //---------
        mRecycleTag = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
        layoutManager.setReverseLayout(true);
        mRecycleTag.setLayoutManager(layoutManager);


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
        //--------
        // setting adapter for spinner in header view of right drawer
        mFileTypeStringArrayExtra = getIntent().getStringArrayExtra(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE);
        if (mFileTypeStringArrayExtra == null) {
            mFileTypeStringArrayExtra = getResources().getStringArray(R.array.select_id);
        } else {
            //TODO, hacked bcaz API not sending UHID, remove else block once received from API.
            String[] stringArrayExtraTemp = new String[mFileTypeStringArrayExtra.length + 1];
            System.arraycopy(mFileTypeStringArrayExtra, 0, stringArrayExtraTemp, 0, mFileTypeStringArrayExtra.length);
            stringArrayExtraTemp[mFileTypeStringArrayExtra.length] = getString(R.string.uhid);
            mFileTypeStringArrayExtra = stringArrayExtraTemp;
        }
        mCustomSpinAdapter = new Custom_Spin_Adapter(this, mArrayId, mFileTypeStringArrayExtra);
        mSpinSelectedId.setAdapter(mCustomSpinAdapter);
        //------
        onTextChanged();

        mCompareDialogLayout = (RelativeLayout) findViewById(R.id.compareDialog);
        mCompareLabel = (TextView) findViewById(R.id.compareLabel);
        mFileOneIcon = (ImageView) findViewById(R.id.fileOneIcon);
        mFileOneType = (TextView) findViewById(R.id.fileOneType);
        mFileOneAdmissionDate = (TextView) findViewById(R.id.fileOneAdmissionDate);
        mFileOnePatientID = (TextView) findViewById(R.id.fileOnePatientID);
        mFileTwoIcon = (ImageView) findViewById(R.id.fileTwoIcon);
        mFileTwoType = (TextView) findViewById(R.id.fileTwoType);
        mFileTwoAdmissionDate = (TextView) findViewById(R.id.fileTwoAdmissionDate);
        mFileTwoPatientID = (TextView) findViewById(R.id.fileTwoPatientID);
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
        if (mOldDataTag == DMSConstants.TASK_PATIENT_LIST) {
            ShowSearchResultResponseModel showSearchResultResponseModel = (ShowSearchResultResponseModel) customResponse;
            List<SearchResult> searchResult = showSearchResultResponseModel.getSearchResultData().getSearchResult();

            patientExpandableListAdapter.addNewItems(searchResult);

            mPatientListView.setGroupIndicator(null);
            mPatientListView.setChildIndicator(null);
            mPatientListView.setChildDivider(ContextCompat.getDrawable(this, R.color.transparent));
            mPatientListView.setDivider(ContextCompat.getDrawable(this, R.color.white));

            //mPatientListView.setDividerHeight(2);
        } else if (mOldDataTag == DMSConstants.TASK_ANNOTATIONS_LIST) {

            AnnotationListResponseModel annotationListResponseModel = (AnnotationListResponseModel) customResponse;
            mAnnotationListData = annotationListResponseModel.getAnnotationListData();
            createAnnotationTreeStructure(mAnnotationListData, false);
            mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        } else if (mOldDataTag == DMSConstants.TASK_GET_PATIENT_NAME_LIST) {
            PatientNameListResponseModel patientNameListResponseModel = (PatientNameListResponseModel) customResponse;
            mPatientNameListData = patientNameListResponseModel.getData();
            mLstPatient = mPatientNameListData.getLstPatients();
            mPatientLists = new ArrayList();
            for (int i = 0; i < mLstPatient.size(); i++) {
                patientName = mLstPatient.get(i).getPatientName();
                mPatientLists.add(patientName);
            }

            mShowPatientNameAdapter = new ShowPatientNameAdapter(this, R.layout.patient_filter_right_drawer, R.id.custom_spinner_txt_view_Id, mLstPatient);
            mSearchPatientNameEditText.setAdapter(mShowPatientNameAdapter);
            mSearchPatientNameEditText.showDropDown();

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
                doConfigDatePickerDialog(getString(R.string.from), mFromDate);
/*
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
                }, null, true, mFromDate);*/
                break;
            // on click of endDate ediText in right drawer
            case R.id.et_todate:
                doConfigDatePickerDialog(getString(R.string.to), mFromDate);
               /* new CommonMethods().datePickerDialog(this, new DatePickerDialogListener() {
                    @Override
                    public void getSelectedDate(String selectedTime) {
                        mToDateEditText.setText("" + selectedTime);

                    }
                }, null, false, mFromDate);*/
                break;
            //  on click of Reset in right drawer
            case R.id.reset:
                mAddedTagsForFiltering.clear();
                mUHIDEditText.setText(DMSConstants.BLANK);
                mFromDateEditText.setText(DMSConstants.BLANK);
                mToDateEditText.setText(DMSConstants.BLANK);
                mSearchAnnotationEditText.setText(DMSConstants.BLANK);
                mAnnotationEditText.setText(DMSConstants.BLANK);
                mSearchPatientNameEditText.setText(DMSConstants.BLANK);
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

                String fromDate = mFromDateEditText.getText().toString().trim();
                String toDate = mToDateEditText.getText().toString().trim();
                patientExpandableListAdapter.removeAll();

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
                        mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, mSelectedId + ":" + enteredUHIDValue);
                    }
                }
                //--- FileType and enteredValue validation : END

                if (!mAdmissionDate.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                    mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.DATE_TYPE, getString(R.string.date_type) + mAdmissionDate);
                }

                if (!fromDate.equalsIgnoreCase(DMSConstants.BLANK)) {
                    mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.FROM_DATE, getString(R.string.from_date) + mFromDateEditText.getText().toString());
                }

                if (!toDate.equalsIgnoreCase(DMSConstants.BLANK)) {
                    mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.TO_DATE, getString(R.string.to_date) + mToDateEditText.getText().toString());
                }

                if (mSearchPatientNameEditText.getText().toString().trim().length() != 0) {
                    mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, getString(R.string.patient_name) + mSearchPatientNameEditText.getText().toString());
                }

                if (mAnnotationEditText.getText().toString().trim().length() != 0) {
                    mAddedTagsForFiltering.put(DMSConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, getString(R.string.enter_annotation) + mAnnotationEditText.getText().toString());
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
                currentPage = 0;
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

        String selectedFileType = mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, "0");

        //THIS IS DONE BCAZ, FILETYPE contains (IPD/OPD/UHID:enteredID)
        String enteredID = mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, "1");

        showSearchResultRequestModel.setFileType(selectedFileType);

        if (getString(R.string.uhid).equalsIgnoreCase(selectedFileType)) {
            showSearchResultRequestModel.setPatientId(enteredID);
            showSearchResultRequestModel.setReferenceId(DMSConstants.BLANK);
            showSearchResultRequestModel.setFileType(DMSConstants.BLANK);
        } else {
            showSearchResultRequestModel.setPatientId(DMSConstants.BLANK);
            showSearchResultRequestModel.setReferenceId(enteredID);
            if (selectedFileType.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                showSearchResultRequestModel.setFileType(DMSConstants.BLANK);
            }
        }
        showSearchResultRequestModel.setDateType(mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.DATE_TYPE, null));
        showSearchResultRequestModel.setFromDate(mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.FROM_DATE, null));
        showSearchResultRequestModel.setToDate(mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.TO_DATE, null));
        showSearchResultRequestModel.setPatientName(mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, null));
        showSearchResultRequestModel.setAnnotationText(mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.ANNOTATION_TEXT, null));
        showSearchResultRequestModel.setPageNumber("" + currentPage);

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
            mSelectedId = mFileTypeStringArrayExtra[indexSselectedId];

            if (!(getResources().getString(R.string.Select)).equalsIgnoreCase(mSelectedId)) {
                mUHIDEditText.setHint(getResources().getString(R.string.enter) + mSelectedId);
            }

            if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mSpinnerAmissionDate.setEnabled(true);
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.ipd))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.ipd_array));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.opd))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.opd_array));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.uhid))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
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
                if (enteredString.trim().length() >= 3) {
                    mPatientsHelper.doGetPatientNameList(s.toString());
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
            mFileOnePatientID.setText("");
            mFileOneType.setText(getString(R.string.adddocument));
            mFileOneIcon.setImageResource(R.drawable.ic_unselected_document);
            mFileTwoAdmissionDate.setText("");
            mFileTwoType.setText(getString(R.string.adddocument));
            mFileTwoIcon.setImageResource(R.drawable.ic_unselected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.white));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);

//            mFileOneDischargeDate.setVisibility(View.GONE);
            mFileOnePatientID.setVisibility(View.GONE);

//            mFileTwoDischargeDate.setVisibility(View.GONE);
            mFileTwoAdmissionDate.setVisibility(View.GONE);

            if (!isCompareDialogCollapsed && !isChecked) {
                collapseCompareDialog();
            }
        } else if (patientFileData2 == null && patientFileData1 != null) {

            if (getString(R.string.opd).equals(patientFileData1.getFileType())) {
                String visitOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                mFileOnePatientID.setText("" + patientFileData1.getRespectiveParentPatientID());
//                mFileOneDischargeDate.setVisibility(View.GONE);
                mFileOnePatientID.setVisibility(View.VISIBLE);
            } else if (getString(R.string.ipd).equals(patientFileData1.getFileType())) {
                String admissionOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                String dischargeOneDate = CommonMethods.formatDateTime(patientFileData1.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                // mFileOneAdmissionDate.setText("A: " + admissionOneDate + " - D: " + dischargeOneDate);
                mFileOnePatientID.setText(getString(R.string.patient_id) + patientFileData1.getRespectiveParentPatientID());
//                mFileOneDischargeDate.setText("D: " + dischargeOneDate);
//                mFileOneDischargeDate.setVisibility(View.VISIBLE);
                mFileOnePatientID.setVisibility(View.VISIBLE);
            }

            mFileOneType.setText(patientFileData1.getFileType() + ":" + patientFileData1.getReferenceId().toString());
            mFileOneIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.white));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);
            if (isCompareDialogCollapsed)
                expandCompareDialog();
        } else if (patientFileData2 != null && patientFileData1 == null) {

            if (getString(R.string.opd).equals(patientFileData2.getFileType())) {
                String visitTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                mFileTwoPatientID.setText("" + patientFileData2.getRespectiveParentPatientID());

//                mFileTwoDischargeDate.setVisibility(View.GONE);
                mFileTwoPatientID.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData2.getFileType())) {
                String admissionTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                String dischargeTwoDate = CommonMethods.formatDateTime(patientFileData2.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                // mFileTwoAdmissionDate.setText("A: " + admissionTwoDate + " - D: " + dischargeTwoDate);
                mFileTwoPatientID.setText(getString(R.string.patient_id) + patientFileData2.getRespectiveParentPatientID());
//                mFileTwoDischargeDate.setText("D: " + dischargeTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.VISIBLE);
                mFileTwoPatientID.setVisibility(View.VISIBLE);
            }

            mFileTwoType.setText(patientFileData2.getFileType() + ":" + patientFileData2.getReferenceId().toString());
            mFileTwoIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.white));
            mCompareButton.setBackground(getResources().getDrawable(R.drawable.compare_button_grey_background));
            mCompareButton.setEnabled(false);
            if (isCompareDialogCollapsed)
                expandCompareDialog();
        } else {

            if (getString(R.string.opd).equals(patientFileData1.getFileType())) {
                String visitOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                mFileOnePatientID.setText("" + patientFileData1.getRespectiveParentPatientID());

//                mFileOneDischargeDate.setVisibility(View.GONE);
                mFileOnePatientID.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData1.getFileType())) {
                String admissionOneDate = CommonMethods.formatDateTime(patientFileData1.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                String dischargeOneDate = CommonMethods.formatDateTime(patientFileData1.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                //mFileOneAdmissionDate.setText("A: " + admissionOneDate + " - D: " + dischargeOneDate);
                mFileOnePatientID.setText(getString(R.string.patient_id) + patientFileData1.getRespectiveParentPatientID());
//                mFileOneDischargeDate.setText("D: " + dischargeOneDate);

//                mFileOneDischargeDate.setVisibility(View.VISIBLE);
                mFileOnePatientID.setVisibility(View.VISIBLE);
            }

            if (getString(R.string.opd).equals(patientFileData2.getFileType())) {
                String visitTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                mFileTwoPatientID.setText(patientFileData2.getRespectiveParentPatientID());

//                mFileTwoDischargeDate.setVisibility(View.GONE);
                mFileTwoPatientID.setVisibility(View.VISIBLE);

            } else if (getString(R.string.ipd).equals(patientFileData2.getFileType())) {
                String admissionTwoDate = CommonMethods.formatDateTime(patientFileData2.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                String dischargeTwoDate = CommonMethods.formatDateTime(patientFileData2.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
                //mFileTwoAdmissionDate.setText("A: " + admissionTwoDate + " - D: " + dischargeTwoDate);
                mFileTwoPatientID.setText(getString(R.string.patient_id) + patientFileData2.getRespectiveParentPatientID());
//                mFileTwoDischargeDate.setText("D: " + dischargeTwoDate);

//                mFileTwoDischargeDate.setVisibility(View.VISIBLE);
                mFileTwoPatientID.setVisibility(View.VISIBLE);
            }

            mFileOneType.setText(patientFileData1.getFileType() + ":" + patientFileData1.getReferenceId().toString());

            mFileTwoType.setText(patientFileData2.getFileType() + ":" + patientFileData2.getReferenceId().toString());

            mFileOneIcon.setImageResource(R.drawable.ic_selected_document);
            mFileTwoIcon.setImageResource(R.drawable.ic_selected_document);
            mCompareButton.setTextColor(getResources().getColor(R.color.white));
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
                    extra.putString(DMSConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
                    extra.putString(DMSConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
                    extra.putString(DMSConstants.ID, patientFileData1.getRespectiveParentPatientID());
                    extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, tempName);
                    intent.putExtra(DMSConstants.DATA, extra);
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
        extra.putString(DMSConstants.PATIENT_ADDRESS, searchPatientInformation.getPatientAddress());
        extra.putString(DMSConstants.DOCTOR_NAME, searchPatientInformation.getDoctorName());
        extra.putString(DMSConstants.ID, childElement.getRespectiveParentPatientID());
        extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + patientName);
        intent.putExtra(DMSConstants.DATA, extra);
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


    private void doConfigDatePickerDialog(final String callFrom, Date mFromDate) {

        //---------
        Calendar selectedTimeSlotDateCal = Calendar.getInstance();
        mDatePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog dialog, String year, String monthOfYear, String dayOfMonth) {

                        int month = Integer.parseInt(monthOfYear) + 1;
                        if (month <= 9) {
                            monthOfYear = "0" + month;
                        }
                        String selectedTime = year + "-" + monthOfYear + "-" + dayOfMonth;
                        if (getString(R.string.from).equalsIgnoreCase(callFrom)) {

                            mSelectedFromDate = selectedTime;
                            mFromDateEditText.setText("" + selectedTime);
                            try {
                                PatientList.this.mFromDate = dfDate.parse(mSelectedFromDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else if (getString(R.string.to).equalsIgnoreCase(callFrom)) {
                            mToDateEditText.setText("" + selectedTime);
                        }
                    }
                },
                selectedTimeSlotDateCal.get(Calendar.YEAR),
                selectedTimeSlotDateCal.get(Calendar.MONTH),
                selectedTimeSlotDateCal.get(Calendar.DAY_OF_MONTH));
        //---------

        mDatePickerDialog.setAccentColor(getResources().getColor(R.color.tagColor));
        mDatePickerDialog.show(getSupportFragmentManager(), getResources().getString(R.string.select_date_text));
        //-------------
        if (getString(R.string.from).equalsIgnoreCase(callFrom)) {
            Calendar now = Calendar.getInstance();
            mDatePickerDialog.setMaxDate(now);

        } else if (getString(R.string.to).equalsIgnoreCase(callFrom)) {
            if (mFromDate != null) {
                mDatePickerDialog.setMaxDate(Calendar.getInstance());
                Calendar now = Calendar.getInstance();
                now.setTime(mFromDate);
                mDatePickerDialog.setMinDate(now);
            } else {
                mDatePickerDialog.setMaxDate(Calendar.getInstance());
            }
        }
        //-------------

    }

}
