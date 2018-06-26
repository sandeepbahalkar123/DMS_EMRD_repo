package com.scorg.dms.ui.activities.dms_patient_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.scorg.dms.R;
import com.scorg.dms.adapters.dms_adapters.PatientEpisodeRecycleViewListAdapter;
import com.scorg.dms.adapters.dms_adapters.PatientRecycleViewListAdapter;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.Common;
import com.scorg.dms.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.EpisodeResponseModel;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.PatientEpisodeFileData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.ShowSearchResultResponseModel;
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

public class PatientDetailsActivity extends AppCompatActivity implements HelperResponse, PatientEpisodeRecycleViewListAdapter.OnEpisodeClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listOfOPDTypes)
    RecyclerView listOfOPDTypes;

    private DMSPatientsHelper mPatientsHelper;
    private Context mContext;
    private SearchResult mReceivedPatinetData;
    private PatientEpisodeRecycleViewListAdapter mPatientEpisodeRecycleViewListAdapter;
    private int currentPage = 0;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        mReceivedPatinetData = (SearchResult) bundle.getSerializable(DMSConstants.PATIENT_DETAILS);

        init();
    }

    private void init() {
        mPatientsHelper = new DMSPatientsHelper(mContext, this);
        //--------
        mPatientEpisodeRecycleViewListAdapter = new PatientEpisodeRecycleViewListAdapter(this, new ArrayList<PatientEpisodeFileData>());
        listOfOPDTypes.setAdapter(mPatientEpisodeRecycleViewListAdapter);
        //--------
        doGetPatientEpisode();
        //----------
        LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listOfOPDTypes.setLayoutManager(linearlayoutManager);
        //---------
        listOfOPDTypes.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearlayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                if (NetworkUtil.isInternetAvailable(mContext)) {
                    currentPage = currentPage + 1;
                    doGetPatientEpisode();
                    loading = true;
                }
            }
        });
    }

    private void doGetPatientEpisode() {
        ShowSearchResultRequestModel showSearchResultRequestModel = new ShowSearchResultRequestModel();
        showSearchResultRequestModel.setPatientId(mReceivedPatinetData.getPatientId());
        showSearchResultRequestModel.setPageNumber("" + currentPage);
        mPatientsHelper.doGetPatientEpisodeList(showSearchResultRequestModel);
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag == DMSConstants.TASK_GET_EPISODE_LIST) {
            EpisodeResponseModel showSearchResultResponseModel = (EpisodeResponseModel) customResponse;
            EpisodeResponseModel.EpisodeDataList episodeDataList = showSearchResultResponseModel.getEpisodeDataList();

            if (episodeDataList != null) {
                List<PatientEpisodeFileData> patientEpisodeFileDataList = episodeDataList.getPatientEpisodeFileDataList();
                mPatientEpisodeRecycleViewListAdapter.addNewItems(patientEpisodeFileDataList);
                mPatientEpisodeRecycleViewListAdapter.notifyDataSetChanged();
            } else {
                CommonMethods.showToast(this, "No data found");
                finish();
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

    @Override
    public void onEpisodeListItemClick(PatientEpisodeFileData groupHeader) {

    }

    @Override
    public void smoothScrollToPosition(int previousPosition) {

    }

    @OnClick({})
    void onClicked(View v) {
        switch (v.getId()) {

        }
    }
}
