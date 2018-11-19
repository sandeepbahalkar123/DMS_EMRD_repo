package com.scorg.dms.ui.activities.dms_patient_list;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.PatientEpisodeRecycleViewListAdapter;
import com.scorg.dms.adapters.dms_adapters.PatientSearchAutoCompleteTextViewAdapter;
import com.scorg.dms.adapters.dms_adapters.RaiseRequestFileNameAdapter;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.ViewRights;
import com.scorg.dms.model.dms_models.requestmodel.archive.RaiseUnlockRequestModel;
import com.scorg.dms.model.dms_models.requestmodel.archive.UnlockRequestResponseBaseModel;
import com.scorg.dms.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.EpisodeResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.FileTypeList;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.PatientEpisodeFileData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.my_patient_filter.PatientFilter;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.activities.BaseActivity;
import com.scorg.dms.ui.customesViews.SearchTextViewWithDeleteButton;
import com.scorg.dms.ui.customesViews.drag_drop_recyclerview_helper.EndlessRecyclerViewScrollListener;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.scorg.dms.util.DMSConstants.BUNDLE;

public class PatientDetailsActivity extends BaseActivity implements HelperResponse, PatientEpisodeRecycleViewListAdapter.OnEpisodeClickListener, PatientSearchAutoCompleteTextViewAdapter.OnItemClickListener,SearchTextViewWithDeleteButton.OnClearButtonClickedInEditTextListener {

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

    @BindView(R.id.labelUHID)
    TextView labelUHID;

    @BindView(R.id.btnEpisodeRaiseRequest)
    TextView btnEpisodeRaiseRequest;


    ArrayList<PatientFilter> mAutoCompleteSearchBoxList = new ArrayList<>();
    private PatientSearchAutoCompleteTextViewAdapter mPatientSearchAutoCompleteTextViewAdapter;
    //----------

    private DMSPatientsHelper mPatientsHelper;
    private Context mContext;
    private SearchResult mReceivedPatientData;
    private PatientEpisodeRecycleViewListAdapter mPatientEpisodeRecycleViewListAdapter;
    private boolean mIsLoadMoreEpisode;
    public ViewRights mviewRights;
    EpisodeResponseModel showSearchResultResponseModel;

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
        mviewRights = (ViewRights) bundle.getSerializable(DMSConstants.VIEW_RIGHTS_DETAILS);
        init();
    }

    public void init() {
        GradientDrawable buttonBackgroundBtnEpisodeRaiseRequest = new GradientDrawable();
        buttonBackgroundBtnEpisodeRaiseRequest.setShape(GradientDrawable.RECTANGLE);
        buttonBackgroundBtnEpisodeRaiseRequest.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackgroundBtnEpisodeRaiseRequest.setCornerRadius(mContext.getResources().getDimension(R.dimen.dp5));

        imgNoRecordFound.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        mAutoCompleteSearchBox.setHint("Search " + DMSApplication.LABEL_REF_ID);
        btnEpisodeRaiseRequest.setBackground(buttonBackgroundBtnEpisodeRaiseRequest);
        //--------------
        labelUHID.setText(DMSApplication.LABEL_UHID);
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
        mPatientEpisodeRecycleViewListAdapter = new PatientEpisodeRecycleViewListAdapter(this, new ArrayList<PatientEpisodeFileData>(), mviewRights);
        listOfOPDTypes.setAdapter(mPatientEpisodeRecycleViewListAdapter);
        //--------
        doGetPatientEpisode("", 0);
        //----------
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listOfOPDTypes.setLayoutManager(linearlayoutManager);
        //---------
        listOfOPDTypes.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if (mIsLoadMoreEpisode) {
                    doGetPatientEpisode(mAutoCompleteSearchBox.getText().toString().trim(), page);
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
                    mAutoCompleteSearchBoxList.add(new PatientFilter(enteredText, getString(R.string.in).concat(DMSApplication.LABEL_REF_ID)));
                    mPatientSearchAutoCompleteTextViewAdapter = new PatientSearchAutoCompleteTextViewAdapter(PatientDetailsActivity.this, R.layout.patient_filter_right_drawer, R.id.custom_spinner_txt_view_Id, mAutoCompleteSearchBoxList, PatientDetailsActivity.this);
                    mAutoCompleteSearchBox.getEditText().setAdapter(mPatientSearchAutoCompleteTextViewAdapter);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAutoCompleteSearchBox.getEditText().showDropDown();
                        }
                    }, 200);

                } else {

                    // if (mPatientEpisodeRecycleViewListAdapter.getItemCount() == 0)
                    if (mAutoCompleteSearchBox.getEditText().length() == 0){
                        mPatientEpisodeRecycleViewListAdapter.removeAll();
                        doGetPatientEpisode("", 0);
                    }

                }

            }
        });
        //-----------------

        btnEpisodeRaiseRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRaiseRequestDialog(mReceivedPatientData);
            }
        });

    }

    private void showRaiseRequestDialog(final SearchResult mReceivedPatientData) {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_rasie_request_episode);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        float[] bottomLeftRadius = {0, 0, 0, 0, mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8), 0, 0};
        float[] bottomRightRadius = {0, 0, 0, 0, 0, 0, mContext.getResources().getDimension(R.dimen.dp8), mContext.getResources().getDimension(R.dimen.dp8)};


        GradientDrawable buttonLeftBackground = new GradientDrawable();
        buttonLeftBackground.setShape(GradientDrawable.RECTANGLE);
        buttonLeftBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonLeftBackground.setCornerRadii(bottomLeftRadius);

        GradientDrawable buttonRightBackground = new GradientDrawable();
        buttonRightBackground.setShape(GradientDrawable.RECTANGLE);
        buttonRightBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonRightBackground.setCornerRadii(bottomRightRadius);


        Button buttonCancel = dialog.findViewById(R.id.button_cancel);
        Button buttonOk = dialog.findViewById(R.id.button_ok);
        RecyclerView mRecycle = dialog.findViewById(R.id.recyclerViewFile);

        buttonOk.setBackground(buttonLeftBackground);
        buttonCancel.setBackground(buttonRightBackground);
        RaiseRequestFileNameAdapter requestFileNameAdapter = new RaiseRequestFileNameAdapter(showSearchResultResponseModel.getEpisodeDataList().getFileTypeList());
        mRecycle.setAdapter(requestFileNameAdapter);
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycle.setLayoutManager(linearlayoutManager);

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RaiseUnlockRequestModel unlockRequestModel = new RaiseUnlockRequestModel();
                ArrayList<String> arrayFileTypeIdList = new ArrayList<>();
                for (FileTypeList typeList : showSearchResultResponseModel.getEpisodeDataList().getFileTypeList()) {
                    if (typeList.isChecked()) {
                        arrayFileTypeIdList.add(String.valueOf(typeList.getFileTypeId()));
                        Log.e("typeId", String.valueOf(typeList.getFileTypeId()));
                    }
                }
                String[] stringArray = arrayFileTypeIdList.toArray(new String[arrayFileTypeIdList.size()]);
                unlockRequestModel.setRequestTypeId("6");
                unlockRequestModel.setFileTypeId(stringArray);
                unlockRequestModel.setPatId(mReceivedPatientData.getPatId());
                if (stringArray.length != 0) {
                    mPatientsHelper.raiseUnlockRequestArchivedFile(unlockRequestModel);
                } else {

                    CommonMethods.showToast(mContext, "Select File Type");
                }
                dialog.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void doGetPatientEpisode(String refId, int page) {
        ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();
        showSearchResultRequestModel.setPatientId(mReceivedPatientData.getPatientId());
        showSearchResultRequestModel.setReferenceId(refId.toUpperCase());
        showSearchResultRequestModel.setPageNumber("" + page);
        mPatientsHelper.doGetPatientEpisodeList(showSearchResultRequestModel);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag == DMSConstants.TASK_GET_EPISODE_LIST) {
            showSearchResultResponseModel = (EpisodeResponseModel) customResponse;
            if (!showSearchResultResponseModel.getCommon().getStatusCode().equals(DMSConstants.SUCCESS)) {
                CommonMethods.showToast(mContext, showSearchResultResponseModel.getCommon().getStatusMessage());
            } else {
                EpisodeResponseModel.EpisodeDataList episodeDataList = showSearchResultResponseModel.getEpisodeDataList();
                mIsLoadMoreEpisode = showSearchResultResponseModel.getEpisodeDataList().isPaggination();
                mAutoCompleteSearchBox.getEditText().dismissDropDown();
                if (episodeDataList != null) {
                    List<PatientEpisodeFileData> patientEpisodeFileDataList = episodeDataList.getPatientEpisodeFileDataList();

                    if (patientEpisodeFileDataList.size() != 0) {
                        mPatientEpisodeRecycleViewListAdapter.addNewItems(patientEpisodeFileDataList);
                        mPatientEpisodeRecycleViewListAdapter.notifyDataSetChanged();
                        emptyListView.setVisibility(View.GONE);
                    } else {
                        emptyListView.setVisibility(View.VISIBLE);
                    }

                } else {
                    emptyListView.setVisibility(View.VISIBLE);
                    finish();
                }
            }
        } else if (mOldDataTag == DMSConstants.TASK_RAISE_REQUEST_CONFIDENTIAL) {
            UnlockRequestResponseBaseModel unlockRequestResponseBaseMode = (UnlockRequestResponseBaseModel) customResponse;
            if (unlockRequestResponseBaseMode.getCommon().getStatusCode().equals(DMSConstants.SUCCESS)) {
                String msg = unlockRequestResponseBaseMode.getCommon().getStatusMessage();
                CommonMethods.showErrorDialog(msg, mContext, false, new ErrorDialogCallback() {
                    @Override
                    public void ok() {

                    }

                    @Override
                    public void retry() {

                    }
                });
            }
        }
    }

    @Override
    public void onParseError(String mOldDataTag, String errorMessage) {

        if (mPatientEpisodeRecycleViewListAdapter.getItemCount() == 0) {
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

        if (mPatientEpisodeRecycleViewListAdapter.getItemCount() == 0) {
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
        if (mPatientEpisodeRecycleViewListAdapter.getItemCount() == 0) {
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
        if (mPatientEpisodeRecycleViewListAdapter.getItemCount() == 0) {
            emptyListView.setVisibility(View.VISIBLE);
            CommonMethods.showErrorDialog(timeOutErrorMessage, mContext, true, new ErrorDialogCallback() {
                @Override
                public void ok() {
                }

                @Override
                public void retry() {
                    if (mPatientEpisodeRecycleViewListAdapter.getItemCount() == 0)
                        doGetPatientEpisode("", 0);
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
        extra.putString(DMSConstants.PAT_ID, String.valueOf(groupHeader.getPatId()));
        extra.putString(DMSConstants.PATIENT_LIST_PARAMS.PATIENT_NAME, "" + mReceivedPatientData.getPatientName());
        extra.putString(DMSConstants.RECORD_ID, String.valueOf(groupHeader.getRecordId()));
        intent.putExtra(DMSConstants.DATA, extra);
        startActivity(intent);
    }

    @Override
    public void showRaiseRequestBtn(boolean isShow) {

        if (isShow)
            btnEpisodeRaiseRequest.setVisibility(View.VISIBLE);
        else
            btnEpisodeRaiseRequest.setVisibility(View.GONE);
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
        doGetPatientEpisode(patientFilter.getSearchValue(), 0);
        mAutoCompleteSearchBox.getEditText().dismissDropDown();
        mAutoCompleteSearchBox.getEditText().setSelection(mAutoCompleteSearchBox.getText().length());

    }

    @Override
    public void onClearButtonClicked() {
        mPatientEpisodeRecycleViewListAdapter.removeAll();
        doGetPatientEpisode("", 0);
    }
}
