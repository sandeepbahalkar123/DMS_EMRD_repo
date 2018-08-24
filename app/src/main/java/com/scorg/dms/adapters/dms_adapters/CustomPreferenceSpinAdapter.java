package com.scorg.dms.adapters.dms_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.scorg.dms.R;

public class CustomPreferenceSpinAdapter extends BaseAdapter {
    Context mContext;
    String[] prefNameList;
    int[] prefImageList;


    public CustomPreferenceSpinAdapter(Context context, String[] prefNameList, int[] prefImageList) {
        this.mContext = context;
        this.prefNameList = prefNameList;
        this.prefImageList = prefImageList;
    }

    @Override
    public int getCount() {
        return prefNameList.length;
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
            view = layoutInflater.inflate(R.layout.layout_custom_spinner_preference, null);
        }

        TextView txtPrefName = (TextView) view.findViewById(R.id.textview_pref_name);
        ImageView imagePref = (ImageView) view.findViewById(R.id.image_pref);

        txtPrefName.setText(prefNameList[position]);
        imagePref.setImageResource(prefImageList[position]);

        return view;
    }

}