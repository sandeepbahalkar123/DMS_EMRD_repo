package com.scorg.dms.ui.activities.dms_patient_list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.scorg.dms.R;
import com.scorg.dms.helpers.patient_list.DMSPatientsHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.dms_models.requestmodel.showsearchresultrequestmodel.ShowSearchResultRequestModel;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.util.DMSConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scorg.dms.util.DMSConstants.BUNDLE;
import static com.scorg.dms.util.DMSConstants.PATIENT_DETAILS;

public class PatientDetailsActivity extends AppCompatActivity implements HelperResponse {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.listOfOPDTypes)
    RecyclerView listOfOPDTypes;

    private DMSPatientsHelper mPatientsHelper;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;

        Bundle bundle = getIntent().getBundleExtra(BUNDLE);
        SearchResult searchResult = (SearchResult) bundle.getSerializable(DMSConstants.PATIENT_DETAILS);

        mPatientsHelper = new DMSPatientsHelper(mContext, this);
        doGetPatientEpisode();
    }

    private void doGetPatientEpisode() {

        mPatientsHelper.doGetPatientEpisode();
    }

    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {

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
