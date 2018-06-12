package com.scorg.dms.ui.fragments.waiting_list;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.h6ah4i.android.widget.advrecyclerview.animator.DraggableItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.scorg.dms.R;
import com.scorg.dms.adapters.waiting_list.WaitingListAdapter;
import com.scorg.dms.adapters.waiting_list.WaitingListSpinnerAdapter;
import com.scorg.dms.helpers.myappointments.AppointmentHelper;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.interfaces.HelperResponse;
import com.scorg.dms.model.patient.template_sms.TemplateBaseModel;
import com.scorg.dms.model.waiting_list.AbstractDataProvider;
import com.scorg.dms.model.waiting_list.Active;
import com.scorg.dms.model.waiting_list.PatientDataActiveWaitingListProvider;
import com.scorg.dms.model.waiting_list.WaitingListDataModel;
import com.scorg.dms.model.waiting_list.WaitingPatientData;
import com.scorg.dms.model.waiting_list.WaitingClinicList;
import com.scorg.dms.model.waiting_list.request_delete_waiting_list.RequestDeleteBaseModel;
import com.scorg.dms.model.waiting_list.request_drag_drop.RequestForDragAndDropBaseModel;
import com.scorg.dms.model.waiting_list.request_drag_drop.WaitingListSequence;
import com.scorg.dms.preference.DMSPreferencesManager;
import com.scorg.dms.ui.activities.waiting_list.WaitingMainListActivity;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.scorg.dms.util.DMSConstants.LOCATION_ID;

/**
 * Created by jeetal on 22/2/18.
 */
@RuntimePermissions
public class ActivePatientListFragment extends Fragment {

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

    public ActivePatientListFragment() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unbind
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
                    setAdapter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public static ActivePatientListFragment newInstance(Bundle bundle) {
        ActivePatientListFragment fragment = new ActivePatientListFragment();
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
        ActivePatientListFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
            setAdapter();
        }
    }

    private void setAdapter() {

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