package com.scorg.dms.adapters.dms_adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel.LstPatient;
import com.scorg.dms.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeetal on 18/5/17.
 */

public class PatientSearchAutoCompleteTextViewAdapter extends ArrayAdapter<String> {

    Context context;
    int resource, textViewResourceId;
    List<String> items, tempItems;
    public static final String HARDCODED_STRING = "&&&&";

    public PatientSearchAutoCompleteTextViewAdapter(Context context, int resource, int textViewResourceId, List<String> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<String>(items); // this makes the difference.
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_spinner_layout, parent, false);
        }
        String names = items.get(position);
        if (names != null) {
            TextView lblName = (TextView) view.findViewById(R.id.custom_spinner_txt_view_Id);
            if (lblName != null) {

                String[] split = names.split(HARDCODED_STRING);
                String textToShow = "\"" + split[0] + "\" " + split[1].replace(HARDCODED_STRING, "");

                Spannable spanText = Spannable.Factory.getInstance().newSpannable(textToShow);
                spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.tagColor)), 0, (split[0].length() + 2), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lblName.setText(spanText);

                lblName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView temp = (TextView) v;
                        CommonMethods.Log("text", temp.getText().toString());
                    }
                });
            }

        }
        return view;
    }


}



