package com.scorg.dms.adapters.book_appointment;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.scorg.dms.R;
import com.scorg.dms.model.select_slot_book_appointment.TimeSlotsInfoList;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SelectSlotToBookAppointmentAdapter extends BaseExpandableListAdapter {

    private final String mSelectedDate;
    private Context mContext;
    private ArrayList<TimeSlotsInfoList> expandableListTitle;


    public SelectSlotToBookAppointmentAdapter(Context context, ArrayList<TimeSlotsInfoList> expandableListTitle, String selectedDate) {
        this.mContext = context;
        this.expandableListTitle = expandableListTitle;
        this.mSelectedDate = selectedDate;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListTitle.get(listPosition).getTimeSlotList();
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ArrayList<TimeSlotsInfoList.TimeSlotData> timeSlotData = expandableListTitle.get(listPosition).getTimeSlotList();
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.select_slot_recycler_child_layout, null);
        }

        RecyclerView slotRecyclerView = (RecyclerView) convertView.findViewById(R.id.slotRecyclerView);

        ShowTimingsBookAppointmentDoctor mShowTimingsBookAppointmentDoctor = new ShowTimingsBookAppointmentDoctor(mContext, timeSlotData, mSelectedDate);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 4);
        slotRecyclerView.setLayoutManager(layoutManager);
        slotRecyclerView.setItemAnimator(new DefaultItemAnimator());
        slotRecyclerView.setAdapter(mShowTimingsBookAppointmentDoctor);
        slotRecyclerView.setNestedScrollingEnabled(false);

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        // THIS is done explicitly to manage child count in grid view.
        if (expandableListTitle.get(listPosition).getTimeSlotList().size() > 0) {
            return 1;
        }
        return 0;
        // return expandableListTitle.get(listPosition).getTimeSlotList().size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final TimeSlotsInfoList slotList = expandableListTitle.get(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.book_appointment_select_slot_groupitem, null);
        }
        TextView slotName = (TextView) convertView.findViewById(R.id.slotName);
        TextView slotTime = (TextView) convertView.findViewById(R.id.slotTime);
        ImageView slotImage = (ImageView) convertView.findViewById(R.id.slotImage);
        if (mContext.getString(R.string.morning).equalsIgnoreCase(slotList.getSlotName())) {
            slotImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.breakfast_normal));
        }
        if (mContext.getString(R.string.afternoon).equalsIgnoreCase(slotList.getSlotName())) {
            slotImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.lunch_highlighted));
        }
        if (mContext.getString(R.string.evening).equalsIgnoreCase(slotList.getSlotName())) {
            slotImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.snacks));
        }
        if (mContext.getString(R.string.night).equalsIgnoreCase(slotList.getSlotName())) {
            slotImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.night_dinner));
        }
        slotTime.setText(slotList.getSlotDescription());
        slotName.setText(slotList.getSlotName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }


    public String getSelectedTimeSlot() {
        return ShowTimingsBookAppointmentDoctor.getSelectedTimeSlot();
    }

    public String getToTimeSlot() {
        return ShowTimingsBookAppointmentDoctor.getmSelectedToTimeSlot();
    }
    public String getSlotId() {
        return ShowTimingsBookAppointmentDoctor.getSlotId();
    }

}
