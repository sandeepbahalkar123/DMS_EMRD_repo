package com.scorg.dms.adapters.admitted_patient;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.model.admitted_patient.AdmittedPatientData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AdmittedPatientsListAdapter
        extends RecyclerView.Adapter<AdmittedPatientsListAdapter.MyViewHolder> implements Filterable {
    private static final String TAG = "AdmittedPatientsListAdapter";
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private ArrayList<AdmittedPatientData> admittedPatientDataList;
    private ArrayList<AdmittedPatientData> admittedPatientDataListOriginal;


    public AdmittedPatientsListAdapter(Context context, ArrayList<AdmittedPatientData> admittedPatientData, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.admittedPatientDataListOriginal = new ArrayList<>();
        this.admittedPatientDataListOriginal.addAll(admittedPatientData);
        this.admittedPatientDataList = new ArrayList<>();
        this.admittedPatientDataList.addAll(admittedPatientData);
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(admittedPatientDataList.get(position).getPatientId());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.admitted_patient_item, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final AdmittedPatientsListAdapter.MyViewHolder holder, final int position) {

        AdmittedPatientData admittedPatientDataObject = admittedPatientDataList.get(position);

        bindGroupItem(admittedPatientDataObject, holder);
    }

    @SuppressLint("CheckResult")
    private void bindGroupItem(final AdmittedPatientData admittedPatientDataObject, final AdmittedPatientsListAdapter.MyViewHolder holder) {

        holder.bluelineImageView.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        holder.callIcon.setColorFilter(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        //   holder.appointmentTime.setBackground(buttonBackground);
        holder.patientGenderTextView.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        holder.patientPhoneNumber.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        holder.separatorView.setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        holder.viewLine1.setBackgroundColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        holder.textBedNoHead.setTextColor(Color.parseColor(DMSApplication.COLOR_APPOINTMENT_TEXT));
        holder.textNewPatient.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        holder.patientIdTextView.setTextColor(Color.parseColor(DMSApplication.COLOR_APPOINTMENT_TEXT));
        holder.patientNameTextView.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));


        String salutation = admittedPatientDataObject.getSalutation();
        String patientName = admittedPatientDataObject.getPatientName();


        if (salutation != null) {
            patientName = salutation + " " + patientName;
        }


        holder.textBedNo.setText(admittedPatientDataObject.getBedNo());
        //---- START: Setting of hospitalID or referecne ID, reference is IS high priority than hospitalID.-----
        String dataToShowInPatientID = String.valueOf(admittedPatientDataObject.getPatientId());
        holder.patientIdTextView.setText(DMSApplication.LABEL_UHID + " " + dataToShowInPatientID + "");
        //---- END------

        if (admittedPatientDataObject.getSpannableString() != null) {
            //-----------------
            //Spannable condition for PatientName
            if (patientName.toLowerCase().contains(admittedPatientDataObject.getSpannableString().toLowerCase())) {
                holder.patientNameTextView.setText(doCreateSpannableData(patientName, admittedPatientDataObject.getSpannableString()));
            } else {
                holder.patientNameTextView.setText(patientName);
            }
            //----------------
            //Spannable condition for PatientPhoneNumber
            if (admittedPatientDataObject.getContactNo() != null) {
                holder.patientPhoneNumber.setVisibility(View.VISIBLE);
                if (admittedPatientDataObject.getContactNo().toLowerCase().contains(admittedPatientDataObject.getSpannableString().toLowerCase())) {
                    holder.patientPhoneNumber.setText(doCreateSpannableData(admittedPatientDataObject.getContactNo(), admittedPatientDataObject.getSpannableString()));
                } else {
                    holder.patientPhoneNumber.setText(admittedPatientDataObject.getContactNo());
                }
            } else {
                holder.patientPhoneNumber.setVisibility(View.VISIBLE);
                holder.patientPhoneNumber.setText("-");
            }

            //---------------
            //Spannable condition for PatientId
            if (dataToShowInPatientID.toLowerCase().contains(admittedPatientDataObject.getSpannableString().toLowerCase())) {
                holder.patientIdTextView.setText(doCreateSpannableData(DMSApplication.LABEL_UHID + " " + dataToShowInPatientID, admittedPatientDataObject.getSpannableString()));
            } else {
                holder.patientIdTextView.setText(DMSApplication.LABEL_UHID + " " + dataToShowInPatientID);
            }
            //---------------
        } else {
            holder.patientNameTextView.setText(patientName);
            holder.patientPhoneNumber.setText(admittedPatientDataObject.getContactNo());
            holder.patientIdTextView.setText(DMSApplication.LABEL_UHID + " " + dataToShowInPatientID);
        }

        //-----------
        if (admittedPatientDataObject.getAge() != null) {
            holder.patientAgeTextView.setVisibility(View.VISIBLE);
            holder.patientAgeTextView.setText(admittedPatientDataObject.getAge() + " " + mContext.getString(R.string.years));
        } else {
            holder.patientAgeTextView.setVisibility(View.GONE);
        }
        //-----------
        if (admittedPatientDataObject.getGender() != null) {
            holder.patientGenderTextView.setText(CommonMethods.toCamelCase(admittedPatientDataObject.getGender()));
            holder.patientGenderTextView.setVisibility(View.VISIBLE);
        } else {
            holder.patientGenderTextView.setVisibility(View.GONE);
        }
        //-----------

        //---------
        String appDate = admittedPatientDataObject.getAdmissionDate();
        if (appDate != null) {

            String day = CommonMethods.formatDateTime(appDate, "dd", DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.TIME);
            String monthYear = CommonMethods.formatDateTime(appDate, "MMM''yy", DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.TIME);
            String toDisplay = day + "<sup>" + CommonMethods.getSuffixForNumber(Integer.parseInt(day)) + "</sup> " + monthYear;

            Spanned dateToDisplay;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                dateToDisplay = Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY);
            else
                dateToDisplay = Html.fromHtml(toDisplay);


            holder.appointmentTime.setVisibility(View.VISIBLE);
            holder.appointmentTime.setText(dateToDisplay + " " + CommonMethods.formatDateTime(appDate, DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.TIME).toLowerCase());
        }
        //-------
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, admittedPatientDataObject.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(false);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(admittedPatientDataObject.getPatientImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.patientImageView);

        ViewTreeObserver vto = holder.front_layout.getViewTreeObserver();


        holder.layoutAppointmentEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (admittedPatientDataObject.isArchived()) {
                    SearchResult searchResult = new SearchResult();
                    searchResult.setPatientName(admittedPatientDataObject.getPatientName());
                    searchResult.setPatientId(admittedPatientDataObject.getPatientId());
                    searchResult.setPatientAddress(admittedPatientDataObject.getPatAddress());
                    searchResult.setPatientImageURL(admittedPatientDataObject.getPatientImageUrl());
                    onItemClickListener.onClickedOfEpisodeListButton(searchResult);
                } else {
                    CommonMethods.showErrorDialog(mContext.getString(R.string.patient_not_having_record), mContext, false, new ErrorDialogCallback() {
                        @Override
                        public void ok() {
                        }

                        @Override
                        public void retry() {
                        }
                    });
                }

            }
        });

        holder.idAndDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admittedPatientDataObject.isArchived()) {
                    onItemClickListener.onClickOfPatientDetails(admittedPatientDataObject);
                } else {
                    CommonMethods.showErrorDialog(mContext.getString(R.string.patient_not_having_record), mContext, false, new ErrorDialogCallback() {
                        @Override
                        public void ok() {
                        }

                        @Override
                        public void retry() {
                        }
                    });
                }

            }
        });
        holder.patientPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNo = admittedPatientDataObject.getContactNo();
                if (contactNo != null) {
                    try {
                        long i = Long.parseLong(contactNo);
                        onItemClickListener.onPhoneNoClick(i);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private SpannableString doCreateSpannableData(String inputData, String spannableString) {

        SpannableString spannableIdString = new SpannableString(inputData);
        Pattern pattern = Pattern.compile(spannableString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputData);

        while (matcher.find()) {
            spannableIdString.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(mContext, R.color.tagColor)),
                    matcher.start(), matcher.end(),//hightlight mSearchString
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableIdString;
    }

    @Override
    public int getItemCount() {
        return admittedPatientDataList.size();
    }

    // Sorting clicniclist by patientName , patientId , patientPhoneNo
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                admittedPatientDataList.clear();

                if (charString.isEmpty()) {
                    for (AdmittedPatientData appointmentPatientDataObj : admittedPatientDataListOriginal) {
                        appointmentPatientDataObj.setSpannableString(null);
                        admittedPatientDataList.add(appointmentPatientDataObj);
                    }

                } else {
                    charString = charString.toLowerCase();
                    for (AdmittedPatientData appointmentPatientDataObj : admittedPatientDataListOriginal) {

                        if ((appointmentPatientDataObj.getPatientName().toLowerCase().contains(charString))
                                || (String.valueOf(appointmentPatientDataObj.getContactNo()).contains(charString))
                                || (String.valueOf(appointmentPatientDataObj.getPatientId().toLowerCase()).contains(charString))) {
                            //--------
                            appointmentPatientDataObj.setSpannableString(charString);
                            admittedPatientDataList.add(appointmentPatientDataObj);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = admittedPatientDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }

    public interface OnItemClickListener {

        void onRecordFound(boolean isListEmpty);

        void onClickOfPatientDetails(AdmittedPatientData patientListObject);

        void onPhoneNoClick(long patientPhone);

        void onClickedOfEpisodeListButton(SearchResult groupHeader);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        private LinearLayout front_layout;
        private TextView appointmentTime;
        private ImageView bluelineImageView;
        private TextView patientIdTextView;
        private CircularImageView patientImageView;
        private TextView patientNameTextView;
        private TextView patientAgeTextView;
        private TextView patientGenderTextView;
        private TextView textBedNo;
        private TextView textBedNoHead;
        private TextView textNewPatient;
        private TextView patientPhoneNumber;


        private LinearLayout idAndDetailsLayout;
        private LinearLayout layoutAppointmentEpisode;
        private View viewLine1;
        private View separatorView;
        private ImageView callIcon;


        MyViewHolder(View convertView) {
            super(convertView);
            idAndDetailsLayout = (LinearLayout) convertView.findViewById(R.id.idAndDetailsLayout);
            front_layout = (LinearLayout) convertView.findViewById(R.id.front_layout);
            appointmentTime = (TextView) convertView.findViewById(R.id.appointmentTime);
            patientIdTextView = (TextView) convertView.findViewById(R.id.patientIdTextView);
            patientImageView = (CircularImageView) convertView.findViewById(R.id.patientImageView);
            patientNameTextView = (TextView) convertView.findViewById(R.id.patientNameTextView);
            patientAgeTextView = (TextView) convertView.findViewById(R.id.patientAgeTextView);
            patientGenderTextView = (TextView) convertView.findViewById(R.id.patientGenderTextView);
            textBedNo = (TextView) convertView.findViewById(R.id.textBedNo);
            textBedNoHead = (TextView) convertView.findViewById(R.id.textBedNoHead);
            textNewPatient = (TextView) convertView.findViewById(R.id.textNewPatient);
            patientPhoneNumber = (TextView) convertView.findViewById(R.id.patientPhoneNumber);
            layoutAppointmentEpisode = (LinearLayout) convertView.findViewById(R.id.layoutAppointmentEpisode);
            bluelineImageView = (ImageView) convertView.findViewById(R.id.bluelineImageView);
            callIcon = (ImageView) convertView.findViewById(R.id.callIcon);
            viewLine1 = (View) convertView.findViewById(R.id.viewLine1);
            separatorView = (View) convertView.findViewById(R.id.separatorView);

        }
    }
}
