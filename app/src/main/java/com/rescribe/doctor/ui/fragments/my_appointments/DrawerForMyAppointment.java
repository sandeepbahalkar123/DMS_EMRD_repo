package com.rescribe.doctor.ui.fragments.my_appointments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rescribe.doctor.R;
import com.rescribe.doctor.adapters.drawer_adapters.DrawerAppointmentSelectStatusAdapter;
import com.rescribe.doctor.adapters.drawer_adapters.DrawerAppointmetClinicNameAdapter;
import com.rescribe.doctor.adapters.drawer_adapters.SortByPriceFilterAdapter;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.interfaces.HelperResponse;
import com.rescribe.doctor.model.my_appointments.ClinicList;
import com.rescribe.doctor.model.my_appointments.FilterSortByHighLowList;
import com.rescribe.doctor.model.my_appointments.MyAppointmentsDataModel;
import com.rescribe.doctor.model.my_appointments.StatusList;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by jeetal on 12/2/18.
 */

public class DrawerForMyAppointment extends Fragment implements HelperResponse, SortByPriceFilterAdapter.onSortByAmountMenuClicked, DrawerAppointmentSelectStatusAdapter.OnClickOfFilterComponents, DrawerAppointmetClinicNameAdapter.OnClickOfFilterClinic {

    @BindView(R.id.applyButton)
    Button applyButton;
    @BindView(R.id.titleTextView)
    CustomTextView titleTextView;
    @BindView(R.id.resetButton)
    Button resetButton;
    @BindView(R.id.genderHeaderView)
    LinearLayout genderHeaderView;
    @BindView(R.id.statusNameRecyclerView)
    RecyclerView statusNameRecyclerView;
    @BindView(R.id.clinicFeesHeaderView)
    LinearLayout clinicFeesHeaderView;
    @BindView(R.id.clinicNameRecyclerView)
    RecyclerView clinicNameRecyclerView;

    @BindView(R.id.selectStatus)
    CustomTextView selectStatus;
    @BindView(R.id.selectClinic)
    CustomTextView selectClinic;
    private Unbinder unbinder;
    private DrawerAppointmentSelectStatusAdapter mDrawerAppointmentSelectStatusAdapter;
    private DrawerAppointmetClinicNameAdapter mDrawerAppointmetClinicNameAdapter;
    private OnDrawerInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.appointment_sorting_filter, container, false);
        unbinder = ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {

        MyAppointmentsDataModel mMyAppointmentsDataModel = getArguments().getParcelable(RescribeConstants.APPOINTMENT_DATA);

        SpannableString selectStatusString = new SpannableString("Select Status");
        selectStatusString.setSpan(new UnderlineSpan(), 0, selectStatusString.length(), 0);
        selectStatus.setText(selectStatusString);
        SpannableString selectClinicString = new SpannableString("Select Clinic");
        selectClinicString.setSpan(new UnderlineSpan(), 0, selectClinicString.length(), 0);
        selectClinic.setText(selectClinicString);
        //select status recyelerview
        mDrawerAppointmentSelectStatusAdapter = new DrawerAppointmentSelectStatusAdapter(getActivity(), mMyAppointmentsDataModel.getStatusList(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        statusNameRecyclerView.setLayoutManager(layoutManager);
        statusNameRecyclerView.setHasFixedSize(true);
        statusNameRecyclerView.setNestedScrollingEnabled(false);
        statusNameRecyclerView.setAdapter(mDrawerAppointmentSelectStatusAdapter);

        // clinic names recyclerview
        mDrawerAppointmetClinicNameAdapter = new DrawerAppointmetClinicNameAdapter(getActivity(), mMyAppointmentsDataModel.getClinicList(), this);
        LinearLayoutManager layoutManagerClinicList = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        clinicNameRecyclerView.setLayoutManager(layoutManagerClinicList);
        clinicNameRecyclerView.setHasFixedSize(true);
        clinicNameRecyclerView.setNestedScrollingEnabled(false);
        clinicNameRecyclerView.setAdapter(mDrawerAppointmetClinicNameAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
    }


    public static DrawerForMyAppointment newInstance(Bundle b) {
        DrawerForMyAppointment fragment = new DrawerForMyAppointment();
        fragment.setArguments(b);
        return fragment;
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

    @OnClick({R.id.applyButton, R.id.resetButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.applyButton:
                ArrayList<StatusList> mStatuslistOfFilter = new ArrayList<>();
                for (StatusList statusList : mDrawerAppointmentSelectStatusAdapter.getAdapterStatusList()) {
                    if (statusList.isSelected()) {
                        mStatuslistOfFilter.add(statusList);
                    }
                }

                ArrayList<ClinicList> mCliniclistOfFilter = new ArrayList<>();
                for (ClinicList clinicObj : mDrawerAppointmetClinicNameAdapter.getAdapterClinicList()) {
                    if (clinicObj.isSelected()) {
                        mCliniclistOfFilter.add(clinicObj);
                    }
                }

                mListener.onApply(mStatuslistOfFilter, mCliniclistOfFilter, true);

                break;
            case R.id.resetButton:
                initialize();
                for (int i = 0; i < mDrawerAppointmetClinicNameAdapter.getAdapterClinicList().size(); i++) {
                    mDrawerAppointmetClinicNameAdapter.getAdapterClinicList().get(i).setSelected(false);
                }
                mDrawerAppointmetClinicNameAdapter.notifyDataSetChanged();
                for (int i = 0; i < mDrawerAppointmentSelectStatusAdapter.getAdapterStatusList().size(); i++) {
                    mDrawerAppointmentSelectStatusAdapter.getAdapterStatusList().get(i).setSelected(false);
                }
                mDrawerAppointmentSelectStatusAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onClickOfSortMenu(FilterSortByHighLowList filterSortByHighLowObject, int groupPosition) {
    }

    @Override
    public void onClickofSelectStatus(ArrayList<StatusList> mStatusLists) {

    }

    @Override
    public void onClickofClinic(ArrayList<ClinicList> mClinicList) {

    }

    public interface OnDrawerInteractionListener {
        void onApply(ArrayList<StatusList> statusLists, ArrayList<ClinicList> clinicLists, boolean drawerRequired);
        void onReset(boolean drawerRequired);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDrawerInteractionListener) {
            mListener = (OnDrawerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDrawerInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
