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
import com.rescribe.doctor.model.dashboard.WaitingClinicList;
import com.rescribe.doctor.ui.customesViews.CustomTypefaceSpan;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 7/3/18.
 */

public class DashBoardWaitingList extends RecyclerView.Adapter<DashBoardWaitingList.ListViewHolder> {

    private Context mContext;
    private ArrayList<WaitingClinicList> mWaitingClinicList = new ArrayList<>();
    public DashBoardWaitingList(Context mContext, ArrayList<WaitingClinicList> mWaitingClinicList) {
        this.mContext = mContext;
        this.mWaitingClinicList = mWaitingClinicList;
    }

    @Override
    public DashBoardWaitingList.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_menu_item_layout, parent, false);

        return new DashBoardWaitingList.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DashBoardWaitingList.ListViewHolder holder, int position) {

        //TODO : NEED TO IMPLEMENT
        final WaitingClinicList waitingClinicList = mWaitingClinicList.get(position);
        String waitingListCount = waitingClinicList.getPatientWaitingCount()+"";
        String clinicInfo = waitingClinicList.getClinicName()+", " +waitingClinicList.getAreaName()+", "+waitingClinicList.getCityName();

        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto_bold.ttf");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(clinicInfo+" - " + waitingListCount);
        spannableStringBuilder.setSpan(new CustomTypefaceSpan("", font), clinicInfo.length()+3+waitingListCount.length(),spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.textViewName.setText(spannableStringBuilder);


    }

    @Override
    public int getItemCount() {
        return mWaitingClinicList.size();
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
