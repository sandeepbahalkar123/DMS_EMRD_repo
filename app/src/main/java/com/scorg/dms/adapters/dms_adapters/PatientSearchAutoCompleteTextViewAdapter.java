package com.scorg.dms.adapters.dms_adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.my_patient_filter.PatientFilter;
import com.scorg.dms.singleton.DMSApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeetal on 18/5/17.
 */

public class PatientSearchAutoCompleteTextViewAdapter extends ArrayAdapter<PatientFilter> {

    private Context context;
    private List<PatientFilter> items;
    public static final String HARDCODED_STRING = "in";
    private PatientSearchAutoCompleteTextViewAdapter.OnItemClickListener onItemClickListener;

    public PatientSearchAutoCompleteTextViewAdapter(Context context, int resource, int textViewResourceId, List<PatientFilter> items,OnItemClickListener onItemClickListener ) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.items = items;
        this.onItemClickListener = onItemClickListener;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_custom_spinner_layout, parent, false);
        }


        final String names = items.get(position).getSearchValue().toString().concat(" ").concat(items.get(position).getSearchType().toString());


        if (names != null) {

            final TextView lblName = (TextView) view.findViewById(R.id.custom_spinner_txt_view_Id);
            if (lblName != null) {

                String[] split = names.split(HARDCODED_STRING);
                String textToShow = "\"" + split[0] + "\" " + split[1];

                Spannable spanText = Spannable.Factory.getInstance().newSpannable(names);
               // spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.tagColor)), 0, (split[0].length() + 2), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spanText.setSpan(new ForegroundColorSpan(Color.parseColor(DMSApplication.COLOR_PRIMARY)), 0, (split[0].length()), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lblName.setText(spanText);


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onSearchAutoCompleteItemClicked(items.get(position));
                    }
                });
            }

        }
        return view;
    }


    public interface OnItemClickListener {

        void onSearchAutoCompleteItemClicked(PatientFilter patientFilter);
    }

}



