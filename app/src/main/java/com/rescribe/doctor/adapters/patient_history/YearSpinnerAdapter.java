package com.rescribe.doctor.adapters.patient_history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.rescribe.doctor.R;

import java.util.ArrayList;


/**
 * Created by root on 22/6/16.
 */
public class YearSpinnerAdapter extends BaseAdapter {
    private final int mTextColor;
    Context mContext;
    ArrayList<String> mSelectedOption;

    public YearSpinnerAdapter(Context context, ArrayList<String> spinner_data, int textColor) {
        this.mContext = context;
        this.mSelectedOption = spinner_data;
        this.mTextColor = textColor;
    }

    @Override
    public int getCount() {
        return mSelectedOption.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            view = layoutInflater.inflate(R.layout.custom_spinner_layout, null);
        }

        TextView txt_id = (TextView) view.findViewById(R.id.custom_spinner_txt_view_Id);
        TextView txt_data = (TextView) view.findViewById(R.id.custom_spinner_txt_view_txtField);

        txt_data.setTextColor(mTextColor);
        txt_data.setText(mSelectedOption.get(position));
        txt_id.setVisibility(View.GONE);
        txt_data.setTag(mSelectedOption.get(position));
        txt_id.setTag(mSelectedOption.get(position));
        return view;
    }

}
