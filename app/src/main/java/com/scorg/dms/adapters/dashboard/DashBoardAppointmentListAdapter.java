package com.scorg.dms.adapters.dashboard;

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

import com.scorg.dms.R;
import com.scorg.dms.model.dashboard.DashboardDataModel;
import com.scorg.dms.ui.customesViews.CustomTypefaceSpan;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 30/1/18.
 */

public class DashBoardAppointmentListAdapter extends RecyclerView.Adapter<DashBoardAppointmentListAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<DashboardDataModel.AppointmentOpdAndOtherCount> mAppointmentClinicLists = new ArrayList<>();

    public DashBoardAppointmentListAdapter(Context mContext, ArrayList<DashboardDataModel.AppointmentOpdAndOtherCount> appointmentOpdOTAndOtherCount) {
        this.mContext = mContext;
        this.mAppointmentClinicLists = appointmentOpdOTAndOtherCount;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_menu_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        DashboardDataModel.AppointmentOpdAndOtherCount appointmentOpdAndOtherCount = mAppointmentClinicLists.get(position);


        String otCount = appointmentOpdAndOtherCount.getAppointmentOpdOTCount();
        String clinicInfo = appointmentOpdAndOtherCount.getAppointmentOpdOTName();

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto_bold.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(clinicInfo + " - " + otCount);
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", font), clinicInfo.length() + 3, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.textViewName.setText(spannableStringBuilder);

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
