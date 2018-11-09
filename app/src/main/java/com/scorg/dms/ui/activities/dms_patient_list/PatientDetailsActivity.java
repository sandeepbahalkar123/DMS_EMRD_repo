package com.scorg.dms.ui.activities.dms_patient_list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.AutoCompleteTextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.PatientEpisodeRecycleViewListAdapter;
import com.scorg.dms.adapters.dms_adapters.PatientSearchAutoCompleteTextViewAdapter;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.EpisodeResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.PatientEpisodeFileData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.my_patient_filter.PatientFilter;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.BaseActivity;
import com.scorg.dms.ui.customesViews.SearchTextViewWithDeleteButton;
import com.scorg.dms.ui.customesViews.drag_drop_recyclerview_helper.EndlessRecyclerViewScrollListener;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;
import com.scorg.dms.util.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scorg.dms.util.DMSConstants.BUNDLE;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PatientDetailsActivity extends BaseActivity implements HelperResponse, PatientEpisodeRecycleViewListAdapter.OnEpisodeClickListener, PatientSearchAutoCompleteTextViewAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listOfOPDTypes)
    RecyclerView listOfOPDTypes;
    @BindView(R.id.patientName)
    TextView mPatientName;
    @BindView(R.id.uhidData)
    TextView mUHIDData;
    //-----------
    @BindView(R.id.autoCompleteSearchBox)
    SearchTextViewWithDeleteButton mAutoCompleteSearchBox;

    @BindView(R.id.emptyListView)
    RelativeLayout emptyListView;

    @BindView(R.id.imgNoRecordFound)
    ImageView imgNoRecordFound;

    @BindView(R.id.patientImageView)
    ImageView patientImageView;

    ArrayList<PatientFilter> mAutoCompleteSearchBoxList = new ArrayList<>();
    private PatientSearchAutoCompleteTextViewAdapter mPatientSearchAutoCompleteTextViewAdapter;
    //----------

    private DMSPatientsHelper mPatientsHelper;
    private Context mContext;
    private SearchResult mReceivedPatientData;
    private PatientEpisodeRecycleViewListAdapter mPatientEpisodeRecycleViewListAdapter;
    private boolean mIsLoadMoreEpisode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        findViewById(R.id.mainDataLayout).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        findViewById(R.id.layoutPatSearch).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle(getString(R.string.episode_details));
        mContext = this;
        findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        mReceivedPatientData = (SearchResult) bundle.getSerializable(DMSConstants.PATIENT_DETAILS);

        init();
    }

    public void init() {

//        GradientDrawable buttonBackground = new GradientDrawable();
//        buttonBackground.setShape(GradientDrawable.RECTANGLE);
//        buttonBackground.setColor(Color.WHITE);
//        buttonBackground.setCornerRadius(getResources().getDimension(R.dimen.dp8));
//        buttonBackground.setStroke(getResources().getDimensionPixelSize(R.dimen.dp2),Color.parseColor(DMSApplication.COLOR_PRIMARY));
//        mAutoCompleteSearchBox.setBackground(buttonBackground);
        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        //--------------

        mUHIDData.setText(mReceivedPatientData.getPatientId());


        mPatientName.setText(mReceivedPatientData.getPatientName());


        TextDrawable textDrawable = CommonMethods.getTextDrawable(patientImageView.getContext(), mReceivedPatientData.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.circleCrop();
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(patientImageView.getContext())
                .load(mReceivedPatientData.getPatientImageURL())
                .apply(requestOptions)
                .into(patientImageView);

        //--------------
        mPatientsHelper = new DMSPatientsHelper(mContext, this);
        //--------
        mPatientEpisodeRecycleViewListAdapter = new PatientEpisodeRecycleViewListAdapter(this, new ArrayList<PatientEpisodeFileData>());
        listOfOPDTypes.setAdapter(mPatientEpisodeRecycleViewListAdapter);
        //--------
        doGetPatientEpisode("",0);
        //----------
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listOfOPDTypes.setLayoutManager(linearlayoutManager);
        //---------
        listOfOPDTypes.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if (NetworkUtil.isInternetAvailable(mContext) && mIsLoadMoreEpisode) {
                    doGetPatientEpisode(mAutoCompleteSearchBox.getText().toString().trim(),page);
                }

            }
        });
        //------- search autocomplete--------
        mAutoCompleteSearchBox.getEditText().setThreshold(0);//start searching from 1 character

        mAutoCompleteSearchBox.addTextChangedListener(new SearchTextViewWithDeleteButton.TextChangedListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredText = s.toString().trim();
                mAutoCompleteSearchBoxList.clear();
                if (enteredText.length() != 0) {
                    mAutoCompleteSearchBoxList.add(new PatientFilter (enteredText , getString(R.string.in_ref_id)));
                    mPatientSearchAutoCompleteTextViewAdapter = new PatientSearchAutoCompleteTextViewAdapter(PatientDetailsActivity.this, R.layout.patient_filter_right_drawer, R.id.custom_spinner_txt_view_Id, mAutoCompleteSearchBoxList,PatientDetailsActivity.this);
                    mAutoCompleteSearchBox.getEditText().setAdapter(mPatientSearchAutoCompleteTextViewAdapter);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAutoCompleteSearchBox.getEditText().showDropDown();
                        }
                    }, 200);

                }else {
                    if (mPatientEpisodeRecycleViewListAdapter.getItemCount()==0)
                    doGetPatientEpisode("",0);
                }

            }
        });
        //-----------------
    }

    private void doGetPatientEpisode(String refId,int page) {
        ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();
        showSearchResultRequestModel.setPatientId(mReceivedPatientData.getPatientId());
        showSearchResultRequestModel.setReferenceId(refId);
        showSearchResultRequestModel.setPageNumber("" +page);
        mPatientsHelper.doGetPatientEpisodeList(showSearchResultRequestModel);
     }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag == DMSConstants.TASK_GET_EPISODE_LIST) {
            EpisodeResponseModel showSearchResultResponseModel = (EpisodeResponseModel) customResponse;
            if(!showSearchResultResponseModel.getCommon().getStatusCode().equals(DMSConstants.SUCCESS)){
                CommonMethods.showToast(mContext,showSearchResultResponseModel.getCommon().getStatusMessage());
            }
            else {
                EpisodeResponseModel.EpisodeDataList episodeDataList = showSearchResultResponseModel.getEpisodeDataList();
                mIsLoadMoreEpisode = showSearchResultResponseModel.getEpisodeDataList().isPaggination();
                mAutoCompleteSearchBox.getEditText().dismissDropDown();
                if (episodeDataList != null ) {
                    List<PatientEpisodeFileData> patientEpisodeFileDataList = episodeDataList.getPatientEpisodeFileDataList();

                    if (patientEpisodeFileDataList.size() != 0) {
                        mPatientEpisodeRecycleViewListAdapter.addNewItems(patientEpisodeFileDataList);
                        mPatientEpisodeRecycleViewListAdapter.notifyDataSetChanged();
                        emptyListView.setVisibility(View.GONE);
                    }
                    else {
                        emptyListView.setVisibility(View.VISIBLE);
                    }

                } else {
                    emptyListView.setVisibility(View.VISIBLE);
                    finish();
                }
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

        if (mPatientEpisodeRecycleViewListAdapter.getItemCount()==0){
            emptyListView.setVisibility(View.VISIBLE);
            CommonMethods.showErrorDialog(errorMessage, mContext, false, new ErrorDialogCallback() {
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
    public void onServerError(String mOldDataTag, String serverErrorMessage) {

        if (mPatientEpisodeRecycleViewListAdapter.getItemCount()==0){
            emptyListView.setVisibility(View.VISIBLE);
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

        if (mPatientEpisodeRecycleViewListAdapter.getItemCount()==0){
            emptyListView.setVisibility(View.VISIBLE);
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
    public void onTimeOutError(String mOldDataTag, String timeOutErrorMessage) {
        if (mPatientEpisodeRecycleViewListAdapter.getItemCount()==0){
            emptyListView.setVisibility(View.VISIBLE);
            CommonMethods.showErrorDialog(timeOutErrorMessage, mContext, true, new ErrorDialogCallback() {
                @Override
                public void ok() {
                }

                @Override
                public void retry() {
                    if (mPatientEpisodeRecycleViewListAdapter.getItemCount()==0)
                        doGetPatientEpisode("",0);
                }
            });

        }
    }

    @Override
    public void onEpisodeListItemClick(PatientEpisodeFileData groupHeader) {

        Intent intent = new Intent(mContext, FileTypeViewerActivity.class);
        Bundle extra = new Bundle();
        ArrayList<PatientEpisodeFileData> dataToSend = new ArrayList<PatientEpisodeFileData>();
        dataToSend.add(groupHeader);
        //  SearchResult searchPatientInformation = patientExpandableListAdapter.searchPatientInfo("" + groupHeader.getPatientId());
        //todo: filepath(pdf url is not getting in api)
        // extra.putSerializable(getString(R.string.compare), dataToSend);
        extra.putSerializable(getString(R.string.compare), dataToSend);
        extra.putString(DMSConstants.PATIENT_ADDRESS, mReceivedPatientData.getPatientAddress());
        extra.putString(DMSConstants.DOCTOR_NAME, groupHeader.getDoctorName());
        extra.putString(DMSConstants.PATIENT_ID, mReceivedPatientData.getPatientId());
        extra.putString(DMSConstants.PAT_ID, groupHeader.getPatId());
        extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + mReceivedPatientData.getPatientName());
        extra.putString(DMSConstants.RECORD_ID, String.valueOf(groupHeader.getRecordId()));
        intent.putExtra(DMSConstants.DATA, extra);
        startActivity(intent);
    }

    @Override
    public void smoothScrollToPosition(int previousPosition) {

    }

    @OnClick({})
    void onClicked(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSearchAutoCompleteItemClicked(PatientFilter patientFilter) {
        mPatientEpisodeRecycleViewListAdapter.removeAll();
        mAutoCompleteSearchBox.setText(patientFilter.getSearchValue());
        doGetPatientEpisode(patientFilter.getSearchValue(),0);
        mAutoCompleteSearchBox.getEditText().dismissDropDown();
        mAutoCompleteSearchBox.getEditText().setSelection(mAutoCompleteSearchBox.getText().length());

    }
}
