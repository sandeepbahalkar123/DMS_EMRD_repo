package com.scorg.dms.ui.activities.dms_patient_list;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.philliphsu.bottomsheetpickers.date.DatePickerDialog;
import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.Custom_Spin_Adapter;
import com.scorg.dms.adapters.dms_adapters.PatientRecycleViewListAdapter;
import com.scorg.dms.adapters.dms_adapters.PatientSearchAutoCompleteTextViewAdapter;
import com.scorg.dms.adapters.dms_adapters.ShowPatientNameAdapter;
import com.scorg.dms.adapters.dms_adapters.TagAdapter;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.ViewRights;
import com.scorg.dms.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationList;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationListData;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.AnnotationListResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel.DocTypeList;
import com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel.LstPatient;
import com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel.PatientNameListData;
import com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel.PatientNameListResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
import com.scorg.dms.model.my_patient_filter.PatientFilter;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.BaseActivity;
import com.scorg.dms.ui.customesViews.SearchTextViewWithDeleteButton;
import com.scorg.dms.ui.customesViews.drag_drop_recyclerview_helper.EndlessRecyclerViewScrollListener;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandIconTreeItemHolder;
import com.scorg.dms.ui.customesViews.treeViewHolder.arrow_expand.ArrowExpandSelectableHeaderHolder;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
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

import static com.scorg.dms.util.DMSConstants.PATIENT_DETAILS;
import static com.scorg.dms.util.DMSConstants.VIEW_RIGHTS_DETAILS;

public class PatientListActivity extends BaseActivity implements HelperResponse, View.OnClickListener, AdapterView.OnItemSelectedListener, PatientRecycleViewListAdapter.OnPatientListener, TreeNode.TreeNodeClickListener, PatientSearchAutoCompleteTextViewAdapter.OnItemClickListener {

    private static final long ANIMATION_DURATION = 500; // in milliseconds
    private static Handler mAddedTagsEventHandler;
    SimpleDateFormat dfDate = new SimpleDateFormat(DMSConstants.DATE_PATTERN.YYYY_MM_DD, Locale.US);

    @BindView(R.id.expandableListView)
    RecyclerView mPatientListView;
    @BindView(R.id.swipeToRefresh)
    SwipeRefreshLayout swipeToRefresh;

    @BindView(R.id.openFilterRightDrawerFAB)
    FloatingActionButton mOpenFilterViewFAB;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    DrawerLayout mDrawer;
    @BindView(R.id.nav_right_view)
    FrameLayout mRightNavigationView;
    @BindView(R.id.spinner_selectId)
    Spinner mSpinSelectedId;
    //--------
    @BindView(R.id.autocompleteSearchBox)
    SearchTextViewWithDeleteButton mAutoCompleteSearchBox;
    //--------
    ArrayList<PatientFilter> mAutoCompleteSearchBoxList = new ArrayList<>();
    @BindView(R.id.spinner_admissionDate)
    Spinner mSpinnerAmissionDate;
    @BindView(R.id.et_uhid)
    AutoCompleteTextView mUHIDEditText;
    @BindView(R.id.et_fromdate)
    EditText mFromDateEditText;
    @BindView(R.id.et_todate)
    EditText mToDateEditText;
    @BindView(R.id.et_searchPatientName)
    AutoCompleteTextView mSearchPatientNameEditText;
    @BindView(R.id.et_userEnteredAnnotation)
    AutoCompleteTextView mAnnotationEditText;
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
    @BindView(R.id.imgNoRecordFound)
    ImageView imgNoRecordFound;
    @BindView(R.id.imgNoRecordFoundDrawer)
    ImageView imgNoRecordFoundDrawer;

    @BindView(R.id.imageFromDate)
    ImageView imageFromDate;
    @BindView(R.id.imageToDate)
    ImageView imageToDate;

    @BindView(R.id.imageAnnotation)
    ImageView imageAnnotation;
    //----------------
    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;
    Date mFromDate;
    String patientName;
    ArrayList mPatientLists;
    private String mSelectedId;
    private String mSelectedFromDate;
    private String mAdmissionDate;
    private String[] mArrayId;
    private Context mContext;
    private RelativeLayout mFirstFileTypeProgressDialogLayout;
    private Custom_Spin_Adapter mCustomSpinAdapter;
    private DMSPatientsHelper mPatientsHelper;
    private TagAdapter mTagsAdapter;
    private List<LstPatient> mLstPatient;
    private RecyclerView mRecycleTag;
    private HashMap<String, Object> mAddedTagsForFiltering;
    private AndroidTreeView mAndroidTreeView;
    private AnnotationListData mAnnotationListData;
    private PatientNameListData mPatientNameListData;
    private String TAG = this.getClass().getName();
    private boolean isCompareDialogCollapsed = true;
    private RelativeLayout mCompareDialogLayout;

    //    private TextView mFileTwoDischargeDate;
    private PatientRecycleViewListAdapter patientExpandableListAdapter;

    //---------
    private int currentPage = 0;
    private String[] mFileTypeStringArrayExtra;
    private PatientSearchAutoCompleteTextViewAdapter mPatientSearchAutoCompleteTextViewAdapter;
    private String priv = "";
    private boolean mIsLoadMorePatients;
    public ViewRights viewRights;
    private String old = "";

    //---------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_list_activity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ButterKnife.bind(this);

        //----------
        setSupportActionBar(mToolbar);
        ActionBar mSupportActionBar = getSupportActionBar();
        mSupportActionBar.setTitle(getString(R.string.my_patients));
        mSupportActionBar.setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        findViewById(R.id.layoutPatSearch).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        //----------
        initialize();
    }

    private void initialize() {
        initializeVariables();
        bindView();
        doGetPatientList();
        mOpenFilterViewFAB.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPatientListView.setLayoutManager(linearlayoutManager);
        imageFromDate.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        imageToDate.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mClearPatientNameButton.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        imageAnnotation.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mClearSearchAnnotationButton.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mAutoCompleteSearchBox.setHint(getString(R.string.search_label) + DMSApplication.LABEL_UHID + getString(R.string.coma_space_seperator) + DMSApplication.LABEL_REF_ID + getString(R.string.label_patient_name));
        mPatientListView.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //  NetworkUtil.isInternetAvailable(mContext) &&
                if (mIsLoadMorePatients) {
                    currentPage = currentPage + 1;
                    doGetPatientList();
                }
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAutoCompleteSearchBox.setText("");
                currentPage = 0;
               // doGetPatientList();
            }
        });
    }

    // intialize variables
    @SuppressLint("HandlerLeak")
    private void initializeVariables() {

        //--------
        patientExpandableListAdapter = new PatientRecycleViewListAdapter(this, new ArrayList<SearchResult>());
        mPatientListView.setAdapter(patientExpandableListAdapter);
        //--------

        mContext = PatientListActivity.this;
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
                mIsLoadMorePatients = true;
                currentPage = 0;
                doGetPatientList();
            }
        };
        //-------------

        //------------
        mTagsAdapter = new TagAdapter(mContext, mAddedTagsForFiltering, mAddedTagsEventHandler);

        //------- search autocomplete--------
        // mAutoCompleteSearchBox.setThreshold(0);//start searching from 1 character


        mAutoCompleteSearchBox.addTextChangedListener(new SearchTextViewWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final String enteredText = s.toString().trim();
                mAutoCompleteSearchBoxList.clear();
                if (enteredText.length() != 0) {
                    mAutoCompleteSearchBoxList.add(new PatientFilter(enteredText, getString(R.string.in).concat(DMSApplication.LABEL_UHID)));
                    mAutoCompleteSearchBoxList.add(new PatientFilter(enteredText, getString(R.string.in_patient_name)));
                    mAutoCompleteSearchBoxList.add(new PatientFilter(enteredText, getString(R.string.in).concat(DMSApplication.LABEL_REF_ID)));
                    mAutoCompleteSearchBoxList.add(new PatientFilter(enteredText, getString(R.string.in_all)));

                    mPatientSearchAutoCompleteTextViewAdapter = new PatientSearchAutoCompleteTextViewAdapter(PatientListActivity.this, R.layout.patient_filter_right_drawer, R.id.custom_spinner_txt_view_Id, mAutoCompleteSearchBoxList, PatientListActivity.this);
                    mAutoCompleteSearchBox.getEditText().setAdapter(mPatientSearchAutoCompleteTextViewAdapter);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                                mAutoCompleteSearchBox.getEditText().showDropDown();
                                Log.i("SHOWDROPDOWN", "shown");
                        }
                    }, 200);

                } else {
                    if (mAutoCompleteSearchBox.getEditText().length() == 0)
                        patientExpandableListAdapter.removeAll();
                    currentPage = 0;
                    doGetPatientList();
                }
                old = enteredText;
            }
        });
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
        mDrawer = findViewById(R.id.drawer_layout);
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                if (mAnnotationListData == null) {
//                    mPatientsHelper.doGetAllAnnotations();
//                } else {
//                    createAnnotationTreeStructure(mAnnotationListData, false);
//                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        //---------
        mRecycleTag = findViewById(R.id.recycler_view);
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
        mClearPatientNameButton.setImageDrawable(getResources().getDrawable(R.mipmap.user));
        //--------
        // setting adapter for spinner in header view of right drawer
        mFileTypeStringArrayExtra = getIntent().getStringArrayExtra(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE);
        if (mFileTypeStringArrayExtra == null) {
            mFileTypeStringArrayExtra = getResources().getStringArray(R.array.select_id);
        } else {
            //TODO, hacked bcaz API not sending UHID, remove else block once received from API.
            String[] stringArrayExtraTemp = new String[mFileTypeStringArrayExtra.length + 1];
            System.arraycopy(mFileTypeStringArrayExtra, 0, stringArrayExtraTemp, 0, mFileTypeStringArrayExtra.length);
            stringArrayExtraTemp[mFileTypeStringArrayExtra.length] = DMSApplication.LABEL_UHID;
            //------
            // ******THIS IS DONE TO ADD SELECT in SPINNER.
            String[] newTemp = new String[stringArrayExtraTemp.length + 1];
            newTemp[0] = "Select";
            System.arraycopy(stringArrayExtraTemp, 0, newTemp, 1, stringArrayExtraTemp.length);
            //------
            mFileTypeStringArrayExtra = newTemp;
        }
        mCustomSpinAdapter = new Custom_Spin_Adapter(this, mArrayId, mFileTypeStringArrayExtra);
        mSpinSelectedId.setAdapter(mCustomSpinAdapter);
        //------
        onTextChanged();

        mCompareDialogLayout = findViewById(R.id.compareDialog);
        TextView mCompareLabel = findViewById(R.id.compareLabel);
        Button mCompareButton = findViewById(R.id.compareButton);

        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setShape(GradientDrawable.RECTANGLE);
        buttonBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackground.setCornerRadius(getResources().getDimension(R.dimen.dp8));
        mCompareButton.setBackground(buttonBackground);

        float[] topRadius = {getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), getResources().getDimension(R.dimen.dp8), 0, 0, 0, 0};
        GradientDrawable topBackground = new GradientDrawable();
        topBackground.setShape(GradientDrawable.RECTANGLE);
        topBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        topBackground.setCornerRadii(topRadius);
        mCompareLabel.setBackground(topBackground);

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

//        mAnnotationTreeViewContainer.getLayoutParams().width = width;
//        mAnnotationTreeViewContainer.requestLayout();

        Log.e(TAG, "++++++++++++++++++++++");

        mAnnotationTreeViewContainer.addView(CommonMethods.loadView(R.layout.mydialog, this));
        mFirstFileTypeProgressDialogLayout = mAnnotationTreeViewContainer.findViewById(R.id.progressBarContainerLayout);

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                mSearchPatientNameEditText.dismissDropDown();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        swipeToRefresh.setRefreshing(false);
        switch (mOldDataTag) {
            case DMSConstants.TASK_PATIENT_LIST:
                ShowSearchResultResponseModel showSearchResultResponseModel = (ShowSearchResultResponseModel) customResponse;
                List<SearchResult> searchResult = showSearchResultResponseModel.getSearchResultData().getSearchResult();
                viewRights = showSearchResultResponseModel.getSearchResultData().getViewRights();
                mIsLoadMorePatients = showSearchResultResponseModel.getSearchResultData().isPaggination();

           //     mAutoCompleteSearchBox.getEditText().dismissDropDown();

                if (currentPage == 0)
                    patientExpandableListAdapter.removeAll();
                patientExpandableListAdapter.addNewItems(searchResult);
                patientExpandableListAdapter.notifyDataSetChanged();
                mSearchPatientNameEditText.dismissDropDown();

                Log.e("searchResult", "---" + searchResult.size());
                if (searchResult.size() <= 0) {
                    mPatientListView.setVisibility(View.GONE);
                    // mRecycleTag.setVisibility(View.GONE);
                    emptyListView.setVisibility(View.VISIBLE);
                    imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
                } else {
                    mPatientListView.setVisibility(View.VISIBLE);
                    //   mRecycleTag.setVisibility(View.VISIBLE);
                    emptyListView.setVisibility(View.GONE);
                }
                //mPatientListView.setDividerHeight(2);
                break;
            case DMSConstants.TASK_ANNOTATIONS_LIST:
                AnnotationListResponseModel annotationListResponseModel = (AnnotationListResponseModel) customResponse;
                mAnnotationListData = annotationListResponseModel.getAnnotationListData();
                if (mAnnotationListData != null) {
                    createAnnotationTreeStructure(mAnnotationListData, false);
                    mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
                    if (imgNoRecordFoundDrawer.getVisibility() == View.VISIBLE)
                        imgNoRecordFoundDrawer.setVisibility(View.GONE);
                } else {
                    if (imgNoRecordFoundDrawer.getVisibility() != View.VISIBLE)
                        imgNoRecordFoundDrawer.setVisibility(View.VISIBLE);
                }
                break;
            case DMSConstants.TASK_GET_PATIENT_NAME_LIST:
                PatientNameListResponseModel patientNameListResponseModel = (PatientNameListResponseModel) customResponse;
                mPatientNameListData = patientNameListResponseModel.getData();
                mLstPatient = mPatientNameListData.getLstPatients();
                mPatientLists = new ArrayList();
                for (int i = 0; i < mLstPatient.size(); i++) {
                    patientName = mLstPatient.get(i).getPatientName();
                    mPatientLists.add(patientName);
                }
                ShowPatientNameAdapter mShowPatientNameAdapter = new ShowPatientNameAdapter(this, R.layout.patient_filter_right_drawer, R.id.custom_spinner_txt_view_Id, mLstPatient);
                mSearchPatientNameEditText.setAdapter(mShowPatientNameAdapter);
                mSearchPatientNameEditText.showDropDown();
                Log.d(TAG, "" + mLstPatient);
                break;
        }

    }


    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        setErrorView(mOldDataTag);
        CommonMethods.showErrorDialog(errorMessage, mContext, false, new ErrorDialogCallback() {
            @Override
            public void ok() {
            }

            @Override
            public void retry() {
            }
        });
    }

    private void setErrorView(String mOldDataTag) {
        swipeToRefresh.setRefreshing(false);
        if (mOldDataTag.equals(DMSConstants.TASK_PATIENT_LIST)) {
            if (patientExpandableListAdapter.getItemCount() == 0) {
                mPatientListView.setVisibility(View.GONE);
                //  mRecycleTag.setVisibility(View.GONE);
                emptyListView.setVisibility(View.VISIBLE);
                imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
            }
        } else if (mOldDataTag.equals(DMSConstants.TASK_ANNOTATIONS_LIST)) {
            imgNoRecordFoundDrawer.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
            imgNoRecordFoundDrawer.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onServerError(String mOldDataTag, String serverErrorMessage) {
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        setErrorView(mOldDataTag);
        if (mOldDataTag.equals(DMSConstants.TASK_PATIENT_LIST)) {

            if (patientExpandableListAdapter.getItemCount() == 0) {
                CommonMethods.showErrorDialog(serverErrorMessage, mContext, false, new ErrorDialogCallback() {
                    @Override
                    public void ok() {
                    }

                    @Override
                    public void retry() {
                    }
                });
            }
        } else if (mOldDataTag.equals(DMSConstants.TASK_ANNOTATIONS_LIST)) {
            CommonMethods.showErrorDialog(serverErrorMessage, mContext, false, new ErrorDialogCallback() {
                @Override
                public void ok() {
                }

                @Override
                public void retry() {
                }
            });
        }

    }

    @Override
    public void onNoConnectionError(String mOldDataTag, String serverErrorMessage) {
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        setErrorView(mOldDataTag);
        if (mOldDataTag.equals(DMSConstants.TASK_PATIENT_LIST)) {

            if (patientExpandableListAdapter.getItemCount() == 0) {
                CommonMethods.showErrorDialog(serverErrorMessage, mContext, false, new ErrorDialogCallback() {
                    @Override
                    public void ok() {
                    }

                    @Override
                    public void retry() {
                    }
                });
            }
        } else if (mOldDataTag.equals(DMSConstants.TASK_ANNOTATIONS_LIST)) {
            CommonMethods.showErrorDialog(serverErrorMessage, mContext, false, new ErrorDialogCallback() {
                @Override
                public void ok() {
                }

                @Override
                public void retry() {
                }
            });
        }

    }

    @Override
    public void onTimeOutError(final String mOldDataTag, String timeOutErrorMessage) {
        mFirstFileTypeProgressDialogLayout.setVisibility(View.GONE);
        setErrorView(mOldDataTag);
        if (mOldDataTag.equals(DMSConstants.TASK_PATIENT_LIST)) {

            if (patientExpandableListAdapter.getItemCount() == 0) {
                CommonMethods.showErrorDialog(timeOutErrorMessage, mContext, true, new ErrorDialogCallback() {
                    @Override
                    public void ok() {

                    }

                    @Override
                    public void retry() {
                        if (mOldDataTag.equals(DMSConstants.TASK_PATIENT_LIST)) {
                            if (patientExpandableListAdapter.getItemCount() == 0) {
                                //currentPage = 0;
                                doGetPatientList();
                            }
                        }


                    }
                });

            }
        } else if (mOldDataTag.equals(DMSConstants.TASK_ANNOTATIONS_LIST)) {
            CommonMethods.showErrorDialog(timeOutErrorMessage, mContext, false, new ErrorDialogCallback() {
                @Override
                public void ok() {
                }

                @Override
                public void retry() {
                }
            });
        }
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
                break;
            // on click of endDate ediText in right drawer
            case R.id.et_todate:
                doConfigDatePickerDialog(getString(R.string.to), mFromDate);
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

                //onCompareDialogShow(null, null, null, null, false);

                String fromDate = mFromDateEditText.getText().toString().trim();
                String toDate = mToDateEditText.getText().toString().trim();
                patientExpandableListAdapter.removeAll();
                mSearchPatientNameEditText.dismissDropDown();
                //*********adding field values in arrayList to generate tags in recycler view
                //we are adding refrence id and file type value in FILE_TYPE parameter
                //Reference id = UHID or OPD or IPD number *********//
                //--- FileType and enteredValue validation : START
                String enteredUHIDValue = mUHIDEditText.getText().toString().trim();
                Log.e("enteredUHIDValue", enteredUHIDValue);
                if (mSelectedId.equalsIgnoreCase(DMSApplication.LABEL_UHID) && (enteredUHIDValue.length() == 0)) {
                    CommonMethods.showSnack(mContext, mUHIDEditText, getString(R.string.error_enter_uhid));
                    break;
                } else if ((enteredUHIDValue.length() != 0) && (mSelectedId.equalsIgnoreCase(getString(R.string.Select)))) {
                    CommonMethods.showSnack(mContext, mUHIDEditText, getString(R.string.Select) + " " + getString(R.string.ipd) + "/" + getString(R.string.opd) + "/" + DMSApplication.LABEL_UHID);
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

                Log.e("selectedAnnotations--", "" + selectedAnnotations.size());

//                if (selectedAnnotations.size() <= 0) {
//                    mAddedTagsForFiltering.clear();
//                }
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
                mIsLoadMorePatients = true;
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

        if (DMSApplication.LABEL_UHID.equalsIgnoreCase(selectedFileType)) {
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

        //------------
        String enteredSearchBoxValue = mAutoCompleteSearchBox.getText().toString().trim();
        //------------
        if (enteredSearchBoxValue.length() != 0) {
            if (enteredSearchBoxValue.toLowerCase().endsWith(getString(R.string.in).concat(DMSApplication.LABEL_UHID).toLowerCase())) {
                String[] split = enteredSearchBoxValue.split(getString(R.string.in).concat(DMSApplication.LABEL_UHID));
                showSearchResultRequestModel.setPatientId(split[0].toUpperCase());
                showSearchResultRequestModel.setReferenceId(DMSConstants.BLANK);
                showSearchResultRequestModel.setFileType(DMSConstants.BLANK);
            } else if (enteredSearchBoxValue.toLowerCase().endsWith(getString(R.string.in).concat(DMSApplication.LABEL_REF_ID).toLowerCase())) {
                showSearchResultRequestModel.setPatientId(DMSConstants.BLANK);
                showSearchResultRequestModel.setReferenceId(enteredSearchBoxValue.toUpperCase());
            } else if (enteredSearchBoxValue.toLowerCase().endsWith(getString(R.string.in_patient_name).toLowerCase())) {
                showSearchResultRequestModel.setPatientId(DMSConstants.BLANK);
                showSearchResultRequestModel.setPatientName(enteredSearchBoxValue.toUpperCase());
            } else if (enteredSearchBoxValue.toLowerCase().endsWith(getString(R.string.in_all).toLowerCase())) {
                showSearchResultRequestModel.setCommonSearch("" + mAutoCompleteSearchBox.getText().toString().toUpperCase());
            }
        }
        Log.e("pageno",""+currentPage);
        mPatientsHelper.doGetPatientList(showSearchResultRequestModel);
    }


    private void doGetPatientListFilter(PatientFilter patientFilter) {


        ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();

        String selectedFileType = mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, "0");

        //THIS IS DONE BCAZ, FILETYPE contains (IPD/OPD/UHID:enteredID)
        String enteredID = mTagsAdapter.getUpdatedTagValues(DMSConstants.PATIENT_LIST_PARAMS.FILE_TYPE, "1");

        showSearchResultRequestModel.setFileType(selectedFileType);

        if (DMSApplication.LABEL_UHID.equalsIgnoreCase(selectedFileType)) {
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

        //------------
        String enteredSearchBoxValue = patientFilter.getSearchType();
        //------------
        if (enteredSearchBoxValue.length() != 0) {
            if (enteredSearchBoxValue.toLowerCase().equals(getString(R.string.in).concat(DMSApplication.LABEL_UHID).toLowerCase())) {
                showSearchResultRequestModel.setPatientId(patientFilter.getSearchValue());
                showSearchResultRequestModel.setReferenceId(DMSConstants.BLANK);
                showSearchResultRequestModel.setFileType(DMSConstants.BLANK);
            } else if (enteredSearchBoxValue.toLowerCase().equals(getString(R.string.in).concat(DMSApplication.LABEL_REF_ID).toLowerCase())) {
                showSearchResultRequestModel.setPatientId(DMSConstants.BLANK);
                showSearchResultRequestModel.setReferenceId(patientFilter.getSearchValue().toUpperCase());
            } else if (enteredSearchBoxValue.toLowerCase().equals(getString(R.string.in_patient_name).toLowerCase())) {
                showSearchResultRequestModel.setPatientId(DMSConstants.BLANK);
                showSearchResultRequestModel.setPatientName(patientFilter.getSearchValue().toUpperCase());
            } else if (enteredSearchBoxValue.toLowerCase().equals(getString(R.string.in_all).toLowerCase())) {
                showSearchResultRequestModel.setCommonSearch(patientFilter.getSearchValue().toUpperCase());
            }
        }

     //   mAutoCompleteSearchBox.getEditText().dismissDropDown();

        mPatientsHelper.doGetPatientList(showSearchResultRequestModel);
    }

    private void createAnnotationTreeStructure(AnnotationListData annotationListData, boolean isExpanded) {

        mAnnotationTreeViewContainer.removeAllViews();

        TreeNode root = TreeNode.root();
        int lstDocTypeChildLeftPadding = (int) (getResources().getDimension(R.dimen.dp50) / getResources().getDisplayMetrics().density);

        boolean isChecked;
        if (annotationListData != null) {
            List<AnnotationList> annotationLists = annotationListData.getAnnotationLists();

            if (annotationLists != null) {
                for (int i = 0; i < annotationLists.size(); i++) {
                    AnnotationList annotationCategoryObject = annotationLists.get(i);
                    isChecked = annotationCategoryObject.getSelected();
                    ArrowExpandSelectableHeaderHolder selectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, true, 0, isChecked);
                    selectableHeaderHolder.setOnlyOneNodeExpanded(true);

                    TreeNode folder1 = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, annotationCategoryObject.getCategoryName(), annotationCategoryObject, i))
                            .setViewHolder(selectableHeaderHolder);

                    List<DocTypeList> docTypeList = annotationCategoryObject.getDocTypeList();

                    for (int j = 0; j < docTypeList.size(); j++) {
                        DocTypeList docTypeListObject = docTypeList.get(j);
                        String dataToShow = docTypeListObject.getTypeName() + "|" + docTypeListObject.getTypeId();
                        isChecked = annotationCategoryObject.getSelected();
                        ArrowExpandSelectableHeaderHolder lstDocTypeChildSelectableHeaderHolder = new ArrowExpandSelectableHeaderHolder(this, isExpanded, lstDocTypeChildLeftPadding, true, 0, isChecked);
                        lstDocTypeChildSelectableHeaderHolder.setOnlyOneNodeExpanded(true);
                        TreeNode lstDocTypeChildFolder = new TreeNode(new ArrowExpandIconTreeItemHolder.IconTreeItem(R.string.ic_shopping_cart, dataToShow, docTypeListObject, i))
                                .setViewHolder(lstDocTypeChildSelectableHeaderHolder);
                        folder1.addChildren(lstDocTypeChildFolder);
                    }
                    root.addChildren(folder1);
                }
            }

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

            Log.e("mSelectedId", mSelectedId);

            if (!(getResources().getString(R.string.Select)).equalsIgnoreCase(mSelectedId)) {
                mUHIDEditText.setHint(getResources().getString(R.string.enter) + mSelectedId);
            }

            if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
                mSpinnerAmissionDate.setEnabled(true);
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.ipd))) {
                mUHIDEditText.setHint(getResources().getString(R.string.enter) + "ID");
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.ipd_array));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.opd))) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.opd_array));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
            } else if (mSelectedId.equalsIgnoreCase(DMSApplication.LABEL_UHID)) {
                mSpinnerAmissionDate.setEnabled(true);
                mCustomSpinAdapter = new Custom_Spin_Adapter(mContext, mArrayId, getResources().getStringArray(R.array.admission_date));
                mSpinnerAmissionDate.setAdapter(mCustomSpinAdapter);
            }
        } else if (parent.getId() == R.id.spinner_admissionDate) {
            int indexSselectedId = parent.getSelectedItemPosition();
            if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.Select))) {
                mArrayId = getResources().getStringArray(R.array.admission_date);
                mAdmissionDate = mArrayId[indexSselectedId];
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.ipd))) {
                mArrayId = getResources().getStringArray(R.array.ipd_array);
                mAdmissionDate = mArrayId[indexSselectedId];
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.opd))) {
                mArrayId = getResources().getStringArray(R.array.opd_array);
                mAdmissionDate = mArrayId[indexSselectedId];
            } else if (mSelectedId.equalsIgnoreCase(getResources().getString(R.string.admission_date))) {
                mArrayId = getResources().getStringArray(R.array.admission_date);
                mAdmissionDate = mArrayId[indexSselectedId];
            }
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
                if (mDrawer.isDrawerOpen(GravityCompat.END)) {
                    String enteredString = mSearchPatientNameEditText.getText().toString();
                    if (!enteredString.equals(priv)) {
                        if (enteredString.equals("")) {
                            mClearPatientNameButton.setImageDrawable(getResources().getDrawable(R.mipmap.user));
                        } else {
                            mClearPatientNameButton.setImageDrawable(getResources().getDrawable(R.mipmap.crosswithcircle));
                        }
                        if (enteredString.trim().length() >= 3) {
                            mPatientsHelper.doGetPatientNameList(s.toString());
                        }
                    }

                    priv = enteredString;
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

                                //Before filtered only on DocTypeList, AnnotationList filtered added now 20-jun-2018
                                if (tempParentObject.getCategoryName().toLowerCase().startsWith(enteredString.toLowerCase())) {
                                    annotationTempList.add(tempParentObject);
                                } else {
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
                                        // annotationListTemp.setSelected(tempParentObject.getSelected());
                                        annotationListTemp.setCategoryId(tempParentObject.getCategoryId());
                                        annotationListTemp.setCategoryName(tempParentObject.getCategoryName());
                                        annotationListTemp.setDocTypeList(childDocTypeTemp);
                                        annotationTempList.add(annotationListTemp);
                                    }
                                    //------
                                }
                            }
                        }
                    }
                    annotationListData.setAnnotationLists(annotationTempList);
                    createAnnotationTreeStructure(annotationListData, false);
                }
            }
        });
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
    public void onPatientListItemClick(SearchResult groupHeader) {

        Intent intent = new Intent(mContext, FileTypeViewerActivity.class);
        Bundle extra = new Bundle();
        //ArrayList<PatientFileData> dataToSend = new ArrayList<PatientFileData>();
        //dataToSend.add(childElement);
        //  SearchResult searchPatientInformation = patientExpandableListAdapter.searchPatientInfo("" + groupHeader.getPatientId());
        //todo: filepath(pdf url is not getting in api)
        // extra.putSerializable(getString(R.string.compare), dataToSend);
        // extra.putSerializable(getString(R.string.compare), new ArrayList<PatientFileData>());

        extra.putString(DMSConstants.PATIENT_ADDRESS, groupHeader.getPatientAddress());
        extra.putString(DMSConstants.DOCTOR_NAME, groupHeader.getDoctorName());
        extra.putString(DMSConstants.PATIENT_ID, groupHeader.getPatientId());
        extra.putString(DMSConstants.PAT_ID, groupHeader.getPatId());

        extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + groupHeader.getPatientName());
        intent.putExtra(DMSConstants.DATA, extra);
        startActivity(intent);
    }

    @Override
    public void smoothScrollToPosition(int previousPosition) {
        mPatientListView.smoothScrollToPosition(previousPosition);
    }

    @Override
    public void onClickedOfEpisodeListButton(SearchResult groupHeader) {

        Intent intent = new Intent(this, PatientDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PATIENT_DETAILS, groupHeader);
        bundle.putSerializable(VIEW_RIGHTS_DETAILS, viewRights);
        intent.putExtra(DMSConstants.BUNDLE, bundle);
        startActivity(intent);

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
        CheckBox nodeSelector = nodeView.findViewById(R.id.node_selector);

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
        if (((ArrowExpandIconTreeItemHolder.IconTreeItem) value).objectData instanceof DocTypeList) {

            Log.e("docTypeList", "true");
            docTypeListTemp = (DocTypeList) ((ArrowExpandIconTreeItemHolder.IconTreeItem) value).objectData;
        }

        AnnotationList annotationListTemp = null;
        if (((ArrowExpandIconTreeItemHolder.IconTreeItem) value).objectData instanceof AnnotationList) {
            Log.e("AnnotationList", "true");
            annotationListTemp = (AnnotationList) ((ArrowExpandIconTreeItemHolder.IconTreeItem) value).objectData;
        }
        for (AnnotationList annotationCategoryObject : mAnnotationListData.getAnnotationLists()) {

            if (annotationListTemp != null) {
                Log.e("annotationListTemp", "" + annotationListTemp.getCategoryName());
                Log.e("annotationCategory", "" + annotationCategoryObject.getCategoryName());

                if (annotationCategoryObject.getCategoryId().equals(annotationListTemp.getCategoryId())) {
                    Log.e("annotationCategory if", "same");
                    annotationCategoryObject.setSelected(nodeSelector.isChecked());
                    node.setSelected(nodeSelector.isChecked());
                }
            }

            List<DocTypeList> docTypeList = annotationCategoryObject.getDocTypeList();
            if (docTypeListTemp != null) {
                Log.e("docTypeListTemp", "" + docTypeListTemp.getTypeName());
                for (DocTypeList docTypeListObject : docTypeList) {
                    if (docTypeListObject.getTypeId().equals(docTypeListTemp.getTypeId())) {
                        docTypeListObject.setSelected(nodeSelector.isChecked());
                        node.setSelected(nodeSelector.isChecked());
                    }
                }
            }
        }

    }


    private void doConfigDatePickerDialog(final String callFrom, Date mFromDate) {
        //---------
        Calendar selectedTimeSlotDateCal = Calendar.getInstance();
        DatePickerDialog mDatePickerDialog = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog dialog, String year, String monthOfYear, String dayOfMonth) {

                        int month = Integer.parseInt(monthOfYear) + 1;
                        if (month <= 9) {
                            monthOfYear = "0" + month;
                        } else
                            monthOfYear = String.valueOf(month);
                        String selectedTime = year + "-" + monthOfYear + "-" + dayOfMonth;
                        Log.e("selectedTime", "" + selectedTime);
                        if (getString(R.string.from).equalsIgnoreCase(callFrom)) {

                            mSelectedFromDate = selectedTime;
                            mFromDateEditText.setText("" + selectedTime);
                            try {
                                PatientListActivity.this.mFromDate = dfDate.parse(mSelectedFromDate);
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

    @Override
    public void onSearchAutoCompleteItemClicked(PatientFilter patientFilter) {
        mAutoCompleteSearchBox.getEditText().dismissDropDown();
        mAutoCompleteSearchBox.getEditText().setSelection(mAutoCompleteSearchBox.getText().length());

        patientExpandableListAdapter.removeAll();
        doGetPatientListFilter(patientFilter);
        mAutoCompleteSearchBox.getEditText().setSelection(mAutoCompleteSearchBox.getText().length());

    }
}
