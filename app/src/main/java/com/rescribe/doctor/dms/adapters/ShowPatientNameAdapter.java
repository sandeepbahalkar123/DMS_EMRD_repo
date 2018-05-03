package com.rescribe.doctor.dms.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.rescribe.doctor.R;
import com.rescribe.doctor.dms.model.responsemodel.patientnamelistresponsemodel.LstPatient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeetal on 18/5/17.
 */

public class ShowPatientNameAdapter extends ArrayAdapter<LstPatient> {

    Context context;
    int resource, textViewResourceId;
    List<LstPatient> items, tempItems, suggestions;

    public ShowPatientNameAdapter(Context context, int resource, int textViewResourceId, List<LstPatient> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<LstPatient>(items); // this makes the difference.
        suggestions = new ArrayList<LstPatient>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_spinner_layout, parent, false);
        }
        LstPatient names = items.get(position);
        if (names != null) {
            TextView lblName = (TextView) view.findViewById(R.id.custom_spinner_txt_view_Id);
            if (lblName != null)
                lblName.setText(names.getPatientName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((LstPatient) resultValue).getPatientName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (LstPatient names : tempItems) {
                    if (names.getPatientName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(names);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           try {
               List<LstPatient> filterList = (ArrayList<LstPatient>) results.values;
               if (results != null && results.count > 0) {
                   clear();
                   for (LstPatient names : filterList) {
                       add(names);
                       notifyDataSetChanged();
                   }
               }
           }
           catch (Exception e){
              e.printStackTrace();
           }

        }
    };
}



