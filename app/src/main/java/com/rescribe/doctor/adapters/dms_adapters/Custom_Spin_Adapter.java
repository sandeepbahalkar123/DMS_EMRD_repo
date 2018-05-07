package com.rescribe.doctor.adapters.dms_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rescribe.doctor.R;


/**
 * Created by root on 22/6/16.
 */
public class Custom_Spin_Adapter extends BaseAdapter {
    Context mContext;
    String[] spinIds;
    String[] choice;


    public Custom_Spin_Adapter(Context context, String[] ids, String[] spinner_data) {
        this.mContext = context;
        this.spinIds = ids;
        this.choice = spinner_data;
    }

    @Override
    public int getCount() {
        return choice.length;
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
            view = layoutInflater.inflate(R.layout.layout_custom_spinner_layout, null);
        }

        TextView txt_id = (TextView) view.findViewById(R.id.custom_spinner_txt_view_Id);
        TextView txt_data = (TextView) view.findViewById(R.id.custom_spinner_txt_view_txtField);

        txt_data.setText(choice[position]);
        //  txt_id.setText(ids[position]);
        txt_id.setVisibility(View.GONE);
        return view;
    }

}
