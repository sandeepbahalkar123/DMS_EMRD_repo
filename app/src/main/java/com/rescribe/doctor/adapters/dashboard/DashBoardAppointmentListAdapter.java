package com.rescribe.doctor.adapters.dashboard;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.doctor.R;
import com.rescribe.doctor.model.dashboard.AppointmentClinicList;
import com.rescribe.doctor.ui.customesViews.CustomTypefaceSpan;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 30/1/18.
 */

public class DashBoardAppointmentListAdapter extends RecyclerView.Adapter<DashBoardAppointmentListAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<AppointmentClinicList> mAppointmentClinicLists = new ArrayList<>();
    private String mStartTimeToShow;
    private String mStringFrom;
    private String mRequiredOpdOrOtString;

    public DashBoardAppointmentListAdapter(Context mContext, ArrayList<AppointmentClinicList> appointmentClinicList, String optOrOTRequired) {
        this.mContext = mContext;
        this.mAppointmentClinicLists = appointmentClinicList;
        this.mRequiredOpdOrOtString = optOrOTRequired;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_menu_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final AppointmentClinicList appointmentClinicList = mAppointmentClinicLists.get(position);
        if (appointmentClinicList.getAppointmentStartTime().equals("")) {
            mStringFrom = "";
            mStartTimeToShow = "";
        } else {
            mStringFrom = " From ";
            mStartTimeToShow = CommonMethods.formatDateTime(appointmentClinicList.getAppointmentStartTime(), RescribeConstants.DATE_PATTERN.hh_mm_a, RescribeConstants.DATE_PATTERN.HH_mm_ss, RescribeConstants.TIME);
        }
        //TODO : NEED TO IMPLEMENT
        if(mRequiredOpdOrOtString.equals(RescribeConstants.OT_AND_OPD)){

            String otCount = appointmentClinicList.getAppointmentOTCount() + " OT";
            String opdCount = appointmentClinicList.getAppointmentOpdCount() + " OPD, ";
            String clinicInfo = appointmentClinicList.getClinicName() + ", " + appointmentClinicList.getAreaName() + ", " + appointmentClinicList.getCityName();

            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto_bold.ttf");
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(clinicInfo + mStringFrom + mStartTimeToShow.toLowerCase() + " - " + opdCount + otCount);
            spannableStringBuilder.setSpan(new CustomTypefaceSpan("", font), clinicInfo.length() + +mStringFrom.length() + mStartTimeToShow.length() + 3, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.textViewName.setText(spannableStringBuilder);


        }else if(mRequiredOpdOrOtString.equals(RescribeConstants.OPD)){

            String opdCount = appointmentClinicList.getAppointmentOpdCount() + " OPD";
            String clinicInfo = appointmentClinicList.getClinicName() + ", " + appointmentClinicList.getAreaName() + ", " + appointmentClinicList.getCityName();

            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto_bold.ttf");
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(clinicInfo + mStringFrom + mStartTimeToShow.toLowerCase() + " - " + opdCount);
            spannableStringBuilder.setSpan(new CustomTypefaceSpan("", font), clinicInfo.length() + +mStringFrom.length() + mStartTimeToShow.length() + 3, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.textViewName.setText(spannableStringBuilder);


        }else if(mRequiredOpdOrOtString.equals(RescribeConstants.OT)){

            String otCount = appointmentClinicList.getAppointmentOTCount() + " OT";
            String clinicInfo = appointmentClinicList.getClinicName() + ", " + appointmentClinicList.getAreaName() + ", " + appointmentClinicList.getCityName();

            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto_bold.ttf");
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(clinicInfo + mStringFrom + mStartTimeToShow.toLowerCase() + " - " + otCount);
            spannableStringBuilder.setSpan(new CustomTypefaceSpan("", font), clinicInfo.length() + +mStringFrom.length() + mStartTimeToShow.length() + 3, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.textViewName.setText(spannableStringBuilder);

        }


    }

    @Override
    public int getItemCount() {
        return mAppointmentClinicLists.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewName)
        TextView textViewName;
        @BindView(R.id.adapter_divider_bottom)
        View adapterDividerBottom;
        @BindView(R.id.expandVisitDetailsLayout)
        LinearLayout expandVisitDetailsLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
