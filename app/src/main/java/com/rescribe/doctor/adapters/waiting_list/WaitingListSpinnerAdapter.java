package com.rescribe.doctor.adapters.waiting_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.rescribe.doctor.R;
import com.rescribe.doctor.model.waiting_list.WaitingclinicList;
import com.rescribe.doctor.ui.customesViews.CustomTextView;

import java.util.ArrayList;

/**
 * Created by jeetal on 23/2/18.
 */

public class WaitingListSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<WaitingclinicList> mWaitingclinicLists;

    public WaitingListSpinnerAdapter(Context context, ArrayList<WaitingclinicList> mWaitingclinicLists) {
        this.mContext = context;
        this.mWaitingclinicLists = mWaitingclinicLists;
    }

    @Override
    public int getCount() {
        return mWaitingclinicLists.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.spinner_row_item_layout, parent, false);
        }

        final WaitingclinicList waitingClinicListObject = mWaitingclinicLists.get(position);
        if (waitingClinicListObject != null) {
            CustomTextView clinicNameTextView = (CustomTextView) view.findViewById(R.id.clinicNameTextView);
            CustomTextView clinicAddress = (CustomTextView) view.findViewById(R.id.clinicAddress);
            clinicNameTextView.setText(waitingClinicListObject.getClinicName()+" - ");
            clinicAddress.setText(waitingClinicListObject.getArea() + ", " + waitingClinicListObject.getCity());

        }
        return view;
    }

}
