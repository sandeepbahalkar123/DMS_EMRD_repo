package com.scorg.dms.adapters.waiting_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.waiting_list.WaitingClinicList;

import java.util.ArrayList;

/**
 * Created by jeetal on 23/2/18.
 */

public class WaitingListSpinnerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<WaitingClinicList> mWaitingClinicLists;

    public WaitingListSpinnerAdapter(Context context, ArrayList<WaitingClinicList> mWaitingClinicLists) {
        this.mContext = context;
        this.mWaitingClinicLists = mWaitingClinicLists;
    }

    @Override
    public int getCount() {
        return mWaitingClinicLists.size();
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

        final WaitingClinicList waitingClinicListObject = mWaitingClinicLists.get(position);
        if (waitingClinicListObject != null) {
            TextView clinicNameTextView = (TextView) view.findViewById(R.id.clinicNameTextView);
            TextView clinicAddress = (TextView) view.findViewById(R.id.clinicAddress);
            clinicNameTextView.setText(waitingClinicListObject.getHosName() + " - ");
            clinicAddress.setText(waitingClinicListObject.getHosAddress1() + ", " + waitingClinicListObject.getHosAddress2());

        }
        return view;
    }

}
