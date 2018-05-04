package com.rescribe.doctor.ui.fragments.waiting_list;

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
import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.waiting_list.DraggableSwipeableViewAllWaitingListAdapter;
import com.rescribe.doctor.adapters.waiting_list.WaitingListSpinnerAdapter;
import com.rescribe.doctor.helpers.myappointments.AppointmentHelper;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.patient.template_sms.TemplateBaseModel;
import com.rescribe.doctor.model.waiting_list.AbstractDataProvider;
import com.rescribe.doctor.model.waiting_list.Active;
import com.rescribe.doctor.model.waiting_list.PatientDataProvider;
import com.rescribe.doctor.model.waiting_list.ViewAll;
import com.rescribe.doctor.model.waiting_list.WaitingPatientList;
import com.rescribe.doctor.model.waiting_list.WaitingclinicList;
import com.rescribe.doctor.model.waiting_list.request_delete_waiting_list.RequestDeleteBaseModel;
import com.rescribe.doctor.model.waiting_list.request_drag_drop.RequestForDragAndDropBaseModel;
import com.rescribe.doctor.model.waiting_list.request_drag_drop.WaitingListSequence;
import com.rescribe.doctor.preference.RescribePreferencesManager;
import com.rescribe.doctor.ui.customesViews.CircularImageView;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.ui.customesViews.drag_drop_recyclerview_helper.OnStartDragListener;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static com.rescribe.doctor.util.RescribeConstants.LOCATION_ID;

/**
 * Created by jeetal on 22/2/18.
 */
@RuntimePermissions
public class ViewAllPatientListFragment extends Fragment implements OnStartDragListener, HelperResponse {

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewDragDropManager recyclerViewDragDropManager;
    private RecyclerViewSwipeManager recyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager recyclerViewTouchActionGuardManager;

    @BindView(R.id.clinicListSpinner)
    Spinner clinicListSpinner;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
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
    private ArrayList<WaitingclinicList> waitingclinicLists = new ArrayList<>();
    private int adapterPos;
    private AppointmentHelper mAppointmentHelper;
    private int mLocationId;
    private DraggableSwipeableViewAllWaitingListAdapter myItemAdapter;
    private WaitingPatientList waitingPatientTempList;
    private String phoneNo;

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
        mAppointmentHelper = new AppointmentHelper(getActivity(), this);
        waitingclinicLists = getArguments().getParcelableArrayList(RescribeConstants.WAITING_LIST_INFO);
        if (waitingclinicLists != null) {
            if (waitingclinicLists.size() > 1) {
                clinicListSpinner.setVisibility(View.VISIBLE);
                hospitalDetailsLinearLayout.setVisibility(View.GONE);
                WaitingListSpinnerAdapter mWaitingListSpinnerAdapter = new WaitingListSpinnerAdapter(getActivity(), waitingclinicLists);
                clinicListSpinner.setAdapter(mWaitingListSpinnerAdapter);

            } else {
                mLocationId = waitingclinicLists.get(0).getLocationId();
                waitingPatientTempList = waitingclinicLists.get(0).getWaitingPatientList();
                clinicListSpinner.setVisibility(View.GONE);
                hospitalDetailsLinearLayout.setVisibility(View.VISIBLE);
                clinicNameTextView.setText(waitingclinicLists.get(0).getClinicName() + " - ");
                clinicAddress.setText(waitingclinicLists.get(0).getArea() + ", " + waitingclinicLists.get(0).getCity());
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setClipToPadding(false);
                setAdapter();
            }
        }
        clinicListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mLocationId = waitingclinicLists.get(i).getLocationId();
                waitingPatientTempList = waitingclinicLists.get(i).getWaitingPatientList();

                if (waitingPatientTempList != null) {

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setClipToPadding(false);
                    setAdapter();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        for (int index = 0; index < waitingclinicLists.size(); index++) {
            WaitingclinicList waitingclinicL = waitingclinicLists.get(index);
            if (waitingclinicL.getLocationId() == getArguments().getInt(LOCATION_ID)) {
                clinicListSpinner.setSelection(index);
                break;
            }
        }
    }


    private void setAdapter() {
        // New

        mLayoutManager = new LinearLayoutManager(getContext());

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        recyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        recyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        recyclerViewTouchActionGuardManager.setEnabled(true);

        // drag & drop manager
        recyclerViewDragDropManager = new RecyclerViewDragDropManager();
        recyclerViewDragDropManager.setDraggingItemShadowDrawable(
                (NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3));

        // swipe manager
        recyclerViewSwipeManager = new RecyclerViewSwipeManager();

        //adapter
        myItemAdapter = new DraggableSwipeableViewAllWaitingListAdapter(getDataProvider());
        myItemAdapter.setEventListener(new DraggableSwipeableViewAllWaitingListAdapter.EventListener() {

            @Override
            public void onDeleteClick(int position, ViewAll viewAll) {
                adapterPos = position;
                RequestDeleteBaseModel requestDeleteBaseModel = new RequestDeleteBaseModel();
                requestDeleteBaseModel.setDocId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getActivity())));
                requestDeleteBaseModel.setLocationId(mLocationId);
                requestDeleteBaseModel.setWaitingDate(CommonMethods.getCurrentDate(RescribeConstants.DATE_PATTERN.YYYY_MM_DD));
                requestDeleteBaseModel.setWaitingId(viewAll.getWaitingId());
                requestDeleteBaseModel.setWaitingSequence(viewAll.getWaitingSequence());
                mAppointmentHelper.doDeleteWaitingList(requestDeleteBaseModel);
            }

            @Override
            public void onItemPinned(int position) {
//                CommonMethods.showToast(getContext(), "Pinned " + position);
            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                onItemViewClick(v, pinned);
            }

            @Override
            public void onItemMoved(int fromPosition, int toPosition) {

                ViewAll removed = waitingPatientTempList.getViewAll().remove(fromPosition);
                waitingPatientTempList.getViewAll().add(toPosition, removed);

                RequestForDragAndDropBaseModel requestForDragAndDropBaseModel = new RequestForDragAndDropBaseModel();
                ArrayList<WaitingListSequence> waitingListSequences = new ArrayList<>();

                for (int i = 0; i < myItemAdapter.getAllItems().size(); i++) {
                    WaitingListSequence waitingListSequence = new WaitingListSequence();
                    waitingListSequence.setWaitingSequence(i + 1);
                    waitingListSequence.setWaitingId(String.valueOf(myItemAdapter.getAllItems().get(i).getWaitingId()));

                    waitingListSequences.add(waitingListSequence);
                }

                requestForDragAndDropBaseModel.setWaitingListSequence(waitingListSequences);
                mAppointmentHelper.doDargAndDropApi(requestForDragAndDropBaseModel);
            }

            @Override
            public void onPhoneClick(String patientPhone) {
                phoneNo = patientPhone;
                ViewAllPatientListFragmentPermissionsDispatcher.doCallSupportWithCheck(ViewAllPatientListFragment.this);

            }
        });

        mAdapter = myItemAdapter;
        mWrappedAdapter = recyclerViewDragDropManager.createWrappedAdapter(myItemAdapter);      // wrap for dragging
        mWrappedAdapter = recyclerViewSwipeManager.createWrappedAdapter(mWrappedAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new DraggableItemAnimator();

        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        recyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
       /* if (supportsViewElevation()) {
            // Lollipop or later has native drop shadow feature. ItemShadowDecorator is not required.
        } else {
            recyclerView.addItemDecoration(new ItemShadowDecorator((NinePatchDrawable) ContextCompat.getDrawable(getContext(), R.drawable.material_shadow_z3)));
        }
        recyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));
*/
        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop
        recyclerViewTouchActionGuardManager.attachRecyclerView(recyclerView);
        recyclerViewSwipeManager.attachRecyclerView(recyclerView);
        recyclerViewDragDropManager.attachRecyclerView(recyclerView);

        if (myItemAdapter.getItemCount() == 0)
            noRecords.setVisibility(View.VISIBLE);
        else noRecords.setVisibility(View.GONE);

    }

    public static ViewAllPatientListFragment newInstance(Bundle bundle) {
        ViewAllPatientListFragment fragment = new ViewAllPatientListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }

    @Override
    public void onDeleteViewAllLayoutClicked(int adapterPosition, ViewAll viewAll) {
        adapterPos = adapterPosition;
        RequestDeleteBaseModel requestDeleteBaseModel = new RequestDeleteBaseModel();
        requestDeleteBaseModel.setDocId(Integer.valueOf(RescribePreferencesManager.getString(RescribePreferencesManager.DMS_PREFERENCES_KEY.DOC_ID, getActivity())));
        requestDeleteBaseModel.setLocationId(mLocationId);
        requestDeleteBaseModel.setWaitingDate(CommonMethods.getCurrentDate(RescribeConstants.DATE_PATTERN.YYYY_MM_DD));
        requestDeleteBaseModel.setWaitingId(viewAll.getWaitingId());
        requestDeleteBaseModel.setWaitingSequence(viewAll.getWaitingSequence());
        mAppointmentHelper.doDeleteWaitingList(requestDeleteBaseModel);

    }

    @Override
    public void onDeleteActiveLayoutClicked(int adapterPosition, Active active) {

    }


    @Override
    public void onSuccess(String mOldDataTag, CustomResponse customResponse) {
        if (mOldDataTag.equals(RescribeConstants.TASK_DELETE_WAITING_LIST)) {
            TemplateBaseModel templateBaseModel = (TemplateBaseModel) customResponse;
            if (templateBaseModel.getCommon().isSuccess()) {
                Toast.makeText(getActivity(), templateBaseModel.getCommon().getStatusMessage() + "", Toast.LENGTH_SHORT).show();
                myItemAdapter.removeItem(adapterPos);
                waitingPatientTempList.getViewAll().remove(adapterPos);

                // remove from original

                if (myItemAdapter.getItemCount() == 0)
                    noRecords.setVisibility(View.VISIBLE);
                else noRecords.setVisibility(View.GONE);

            } else {
                Toast.makeText(getActivity(), templateBaseModel.getCommon().getStatusMessage() + "", Toast.LENGTH_SHORT).show();
            }
        } else if (mOldDataTag.equals(RescribeConstants.TASK_DARG_DROP)) {
            TemplateBaseModel templateBaseModel = (TemplateBaseModel) customResponse;
            if (templateBaseModel.getCommon().isSuccess()) {
                Toast.makeText(getActivity(), templateBaseModel.getCommon().getStatusMessage() + "", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), templateBaseModel.getCommon().getStatusMessage() + "", Toast.LENGTH_SHORT).show();
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
    public void onPause() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.cancelDrag();
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager.release();
            recyclerViewDragDropManager = null;
        }

        if (recyclerViewSwipeManager != null) {
            recyclerViewSwipeManager.release();
            recyclerViewSwipeManager = null;
        }

        if (recyclerViewTouchActionGuardManager != null) {
            recyclerViewTouchActionGuardManager.release();
            recyclerViewTouchActionGuardManager = null;
        }

        if (recyclerView != null) {
            recyclerView.setItemAnimator(null);
            recyclerView.setAdapter(null);
            recyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        super.onDestroyView();
    }

    private void onItemViewClick(View v, boolean pinned) {
//        int position = recyclerView.getChildAdapterPosition(v);
//        if (position != RecyclerView.NO_POSITION) {
//
//        }
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public AbstractDataProvider getDataProvider() {
        return new PatientDataProvider(waitingPatientTempList.getViewAll());
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void notifyItemInserted(int position) {
        mAdapter.notifyItemInserted(position);
        recyclerView.scrollToPosition(position);
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
}