package com.scorg.dms.adapters.add_records;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.model.my_records.SpinnerDoctor;
import com.scorg.dms.ui.customesViews.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpinnerAdapter extends ArrayAdapter<SpinnerDoctor> {

    Context mContext;
    private List<SpinnerDoctor> items;
    private ArrayList<SpinnerDoctor> mTempItems;
    private ArrayList<SpinnerDoctor> mSuggestions;
    private TextEnterListener mTextEnterListener;
    private ColorGenerator mColorGenerator;
    private String mDoctorName;

    public DoctorSpinnerAdapter(Context context, int resource, int textViewResourceId, ArrayList<SpinnerDoctor> items) {
        super(context, resource, textViewResourceId, items);
        this.mContext = context;
        this.items = items;
        mTempItems = new ArrayList<SpinnerDoctor>(items); // this makes the difference.
        mSuggestions = new ArrayList<SpinnerDoctor>();
        mColorGenerator = ColorGenerator.MATERIAL;

        try {
            this.mTextEnterListener = ((TextEnterListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement TextEnterListener.");
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.autocomplete_dropdown_layout, parent, false);
        }

        final SpinnerDoctor doctor_details = items.get(position);
        if (doctor_details != null) {
            TextView lblName = (TextView) view.findViewById(R.id.doctorName);
            CircularImageView doctorImage = (CircularImageView) view.findViewById(R.id.doctorImage);
            TextView doctorSpecialist = (TextView) view.findViewById(R.id.doctorSpecialist);
            TextView doctorAddress = (TextView) view.findViewById(R.id.doctorAddress);

            if(doctor_details.getDoctorName().contains("Dr. ")){
                mDoctorName = doctor_details.getDoctorName();
            }else{
                mDoctorName = "Dr. " + doctor_details.getDoctorName();
            }
            if (lblName != null)
                lblName.setText(mDoctorName);
            doctorSpecialist.setText(doctor_details.getSpecialization());
            doctorAddress.setText(doctor_details.getAddress());
            int color2 = mColorGenerator.getColor(doctor_details.getDoctorName());
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // width in px
                    .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + doctor_details.getDoctorName().charAt(0)).toUpperCase(), color2);


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.skipMemoryCache(true);
            requestOptions.error(drawable);
            requestOptions.placeholder(drawable);

            Glide.with(mContext)
                    .load(doctor_details.getDocImg())
                    .apply(requestOptions).thumbnail(0.5f)
                    .into(doctorImage);
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom mSuggestions we provide.
     */
    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((SpinnerDoctor) resultValue).getDoctorName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                mSuggestions.clear();
                for (SpinnerDoctor doctor_details : mTempItems) {
                    if (doctor_details.getDoctorName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        mSuggestions.add(doctor_details);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mSuggestions;
                filterResults.count = mSuggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<SpinnerDoctor> filterList = (ArrayList<SpinnerDoctor>) results.values;
            if (results.count > 0) {
                clear();
                for (SpinnerDoctor doctor_details : filterList) {
                    add(doctor_details);
                    notifyDataSetChanged();
                }
            }
            mTextEnterListener.onTextEnter(constraint != null && constraint.length() > 0);
        }
    };

    public SpinnerDoctor getDoctor(int position) {
        return mSuggestions.get(position);
    }

    public interface TextEnterListener {
        void onTextEnter(boolean isEntered);
    }
}