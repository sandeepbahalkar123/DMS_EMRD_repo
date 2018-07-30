package com.scorg.dms.adapters.book_appointment;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scorg.dms.R;
import com.scorg.dms.model.select_slot_book_appointment.TimeSlotsInfoList;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTimingsBookAppointmentDoctor extends RecyclerView.Adapter<ShowTimingsBookAppointmentDoctor.ListViewHolder> {

    private String mFormattedCurrentDateString;
    private String mSelectedDate;
    private Context mContext;
    private ArrayList<TimeSlotsInfoList.TimeSlotData> mDataList;
    public static String mSelectedTimeSlot;

    public static String mSelectedToTimeSlot;
    public static String mAptslotId;

    public ShowTimingsBookAppointmentDoctor(Context mContext, ArrayList<TimeSlotsInfoList.TimeSlotData> dataList, String mSelectedDate) {
        this.mDataList = dataList;
        this.mContext = mContext;
        this.mSelectedDate = mSelectedDate;
        mFormattedCurrentDateString = CommonMethods.formatDateTime(CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.DD_MM_YYYY), DMSConstants.DATE_PATTERN.YYYY_MM_DD, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE);

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_appointment_select_slot_childitem, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final TimeSlotsInfoList.TimeSlotData timeSlotData = mDataList.get(position);
        String fromTime = timeSlotData.getFromTime();

        //-----------
        String s = CommonMethods.formatDateTime(fromTime, DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.HH_mm_ss, DMSConstants.TIME);
        holder.showTime.setText(s);
        //-----------

        Date fromTimeDate = CommonMethods.convertStringToDate(mSelectedDate + " " + fromTime, DMSConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);
        Date currentDate = Calendar.getInstance().getTime();

        if (currentDate.getTime() > fromTimeDate.getTime())
            holder.showTime.setPaintFlags(holder.showTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mFormattedCurrentTimeString = CommonMethods.getCurrentTimeStamp(DMSConstants.DATE_PATTERN.HH_mm_ss);
                String s = "" + v.getTag();
                TimeSlotsInfoList.TimeSlotData tag1 = mDataList.get(Integer.parseInt(s));
                String fromTime = tag1.getFromTime();
                String toTime = tag1.getToTime();
                String slotId = tag1.getSlotId();

                Date fromTimeDate = CommonMethods.convertStringToDate(mSelectedDate + " " + fromTime, DMSConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);
                Date currentDate = CommonMethods.convertStringToDate(mFormattedCurrentDateString + " " + mFormattedCurrentTimeString, DMSConstants.DATE_PATTERN.YYYY_MM_DD_HH_mm_ss);

                if ((currentDate.getTime() > fromTimeDate.getTime()) && (mSelectedDate.equalsIgnoreCase(mFormattedCurrentDateString))) {
                    CommonMethods.showToast(mContext, mContext.getString(R.string.book_time_slot_err));
                    holder.showTime.setPaintFlags(holder.showTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    if (tag1.isAvailable()) {
                        fromTime = tag1.getFromTime();
                        if (fromTime.equalsIgnoreCase(mSelectedTimeSlot)) {
                            mSelectedTimeSlot = "";
                            mSelectedToTimeSlot = "";
                            mAptslotId = "";

                            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.color.white));
                            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));

                        } else {
                            mSelectedTimeSlot = fromTime;
                            mSelectedToTimeSlot = toTime;
                            mAptslotId = slotId;
                            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_round_rectangle));
                            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        });

        if (timeSlotData.getFromTime().equalsIgnoreCase(mSelectedTimeSlot)) {
            mSelectedTimeSlot = timeSlotData.getFromTime();
            mSelectedToTimeSlot = timeSlotData.getToTime();
            mAptslotId = timeSlotData.getSlotId();
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.green_round_rectangle));
            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.mainLayout.setBackground(ContextCompat.getDrawable(mContext, R.color.white));
            holder.showTime.setTextColor(ContextCompat.getColor(mContext, R.color.tagColor));
            if (!timeSlotData.isAvailable())
                holder.showTime.setPaintFlags(holder.showTime.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.view.setTag("" + position);

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.showTime)
        CustomTextView showTime;
        @BindView(R.id.mainLayout)
        LinearLayout mainLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }


    public static String getmSelectedToTimeSlot() {
        return mSelectedToTimeSlot;
    }

    public static String getSlotId() {
        return mAptslotId;
    }

    public static String getSelectedTimeSlot() {
        return mSelectedTimeSlot;
    }
}
