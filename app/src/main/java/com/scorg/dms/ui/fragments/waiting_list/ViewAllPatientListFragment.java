package com.scorg.dms.ui.fragments.waiting_list;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.scorg.dms.R;
import com.scorg.dms.adapters.waiting_list.WaitingListAdapter;
import com.scorg.dms.adapters.waiting_list.WaitingListSpinnerAdapter;
import com.scorg.dms.model.waiting_list.WaitingListDataModel;
import com.scorg.dms.model.waiting_list.WaitingPatientData;
import com.scorg.dms.model.waiting_list.WaitingClinicList;
import com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by jeetal on 22/2/18.
 */
@RuntimePermissions
public class ViewAllPatientListFragment extends Fragment {

    @BindView(R.id.clinicListSpinner)
    Spinner clinicListSpinner;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.bulletImageView)
    CircularImageView bulletImageView;
    @BindView(R.id.clinicNameTextView)
    CustomTextView clinicNameTextView;
    @BindView(R.id.clinicAddress)
    CustomTextView clinicAddress;
    @BindView(R.id.hospitalDetailsLinearLayout)
    RelativeLayout hospitalDetailsLinearLayout;

    @BindView(R.id.noRecords)
    LinearLayout noRecords;

    private Unbinder unbinder;
    private ArrayList<WaitingClinicList> mWaitingClinicLists = new ArrayList<>();
    private ArrayList<WaitingPatientData> waitingPatientTempList;
    private String phoneNo;
    private WaitingMainListActivity mParentActivity;
    private WaitingListAdapter mWaitingListAdapter;

    public ViewAllPatientListFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mRootView = inflater.inflate(R.layout.waiting_content_layout, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        init();
        return mRootView;
    }

    private void init() {
        mParentActivity = (WaitingMainListActivity) getActivity();

        clinicListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (waitingPatientTempList != null) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.setClipToPadding(false);
                    setViewAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public static ViewAllPatientListFragment newInstance(Bundle bundle) {
        ViewAllPatientListFragment fragment = new ViewAllPatientListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NeedsPermission(Manifest.permission.CALL_PHONE)
    void doCallSupport() {
        callSupport(phoneNo);
    }

    private void callSupport(String phoneNo) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNo));
        startActivity(callIntent);
    }


    public void onRequestPermssionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ViewAllPatientListFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        WaitingListDataModel waitingListDataModel = mParentActivity.getWaitingListDataModel();
        waitingPatientTempList = waitingListDataModel.getWaitingPatientDataList();
        mWaitingClinicLists = waitingListDataModel.getWaitingClinicList();
        if (mWaitingClinicLists.size() > 1) {
            clinicListSpinner.setVisibility(View.VISIBLE);
            hospitalDetailsLinearLayout.setVisibility(View.GONE);
            WaitingListSpinnerAdapter mWaitingListSpinnerAdapter = new WaitingListSpinnerAdapter(getActivity(), mWaitingClinicLists);
            clinicListSpinner.setAdapter(mWaitingListSpinnerAdapter);
        }
        if (!mWaitingClinicLists.isEmpty()) {
            clinicListSpinner.setVisibility(View.GONE);
            hospitalDetailsLinearLayout.setVisibility(View.GONE);
            clinicNameTextView.setText(mWaitingClinicLists.get(0).getHosName() + " - ");
            clinicAddress.setText(mWaitingClinicLists.get(0).getHosAddress1());
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setClipToPadding(false);
            setViewAdapter();
        }
    }

    private void setViewAdapter() {

        if (waitingPatientTempList.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            noRecords.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            noRecords.setVisibility(View.GONE);
            mWaitingListAdapter = new WaitingListAdapter(this.getContext(), waitingPatientTempList);
            LinearLayoutManager linearlayoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(linearlayoutManager);
            mRecyclerView.setAdapter(mWaitingListAdapter);
        }
    }
}