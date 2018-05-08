package com.scorg.dms.adapters.my_patients;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.helpers.database.AppDBHelper;
import com.scorg.dms.model.patient.doctor_patients.PatientList;
import com.scorg.dms.model.patient.patient_connect.PatientData;
import com.scorg.dms.ui.activities.ChatActivity;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scorg.dms.util.CommonMethods.toCamelCase;
import static com.scorg.dms.util.DMSConstants.CLINIC_ID;
import static com.scorg.dms.util.DMSConstants.PATIENT_HOS_PAT_ID;

/**
 * Created by jeetal on 31/1/18.
 */

public class MyPatientsAdapter extends RecyclerView.Adapter<MyPatientsAdapter.ListViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<PatientList> mDataList = new ArrayList<>();
    public boolean isLongPressed;
    private OnDownArrowClicked mOnDownArrowClicked;
    private boolean isClickOnPatientDetailsRequired;
    private ArrayList<PatientList> mListToShowAfterFilter;
    private AppDBHelper appDBHelper;

    public MyPatientsAdapter(Context mContext, ArrayList<PatientList> dataList, OnDownArrowClicked mOnDownArrowClicked, boolean isClickOnPatientDetailsRequired) {
        this.mListToShowAfterFilter = dataList;
        mDataList.addAll(dataList);
        removeDuplicateElements();
        this.mContext = mContext;
        appDBHelper = new AppDBHelper(mContext);
        this.mOnDownArrowClicked = mOnDownArrowClicked;
        this.isClickOnPatientDetailsRequired = isClickOnPatientDetailsRequired;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_appointments_child_item, parent, false);

        return new ListViewHolder(itemView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        final PatientList patientObject = mDataList.get(position);
        holder.opdTypeTextView.setVisibility(View.GONE);
        holder.patientClinicAddress.setVisibility(View.VISIBLE);
        String area = patientObject.getPatientArea().isEmpty() ? "" : (patientObject.getPatientCity().isEmpty() ? patientObject.getPatientArea() : patientObject.getPatientArea() + ", ");
        holder.patientClinicAddress.setText(CommonMethods.toCamelCase(area + patientObject.getPatientCity()));
        String patientName = "";

        if (patientObject.getSalutation() != 0)
            patientName = DMSConstants.SALUTATION[patientObject.getSalutation() - 1] + toCamelCase(patientObject.getPatientName());
        else patientName = toCamelCase(patientObject.getPatientName());


        //---- START: Setting of hospitalID or referecne ID, reference is IS high priority than hospitalID.-----
        String dataToShowInPatientID = String.valueOf(patientObject.getReferenceID());

        if (dataToShowInPatientID == null || DMSConstants.BLANK.equalsIgnoreCase(dataToShowInPatientID)) {
            dataToShowInPatientID = String.valueOf(patientObject.getHospitalPatId());
        }
        //---- END------

        if (patientObject.getSpannableString() != null) {
            //Spannable condition for PatientName
            if (patientObject.getPatientName().toLowerCase().contains(patientObject.getSpannableString().toLowerCase())) {
                SpannableString spannableString = new SpannableString(patientName);
                Pattern pattern = Pattern.compile(patientObject.getSpannableString(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(patientName);
                while (matcher.find()) {
                    spannableString.setSpan(new ForegroundColorSpan(
                                    ContextCompat.getColor(mContext, R.color.tagColor)),
                            matcher.start(), matcher.end(),//hightlight mSearchString
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.patientNameTextView.setText(spannableString);
            } else {
                holder.patientNameTextView.setText(patientName);
            }
            //Spannable condition for PatientPhoneNomber

            if (patientObject.getPatientPhone().toLowerCase().contains(patientObject.getSpannableString().toLowerCase())) {
                SpannableString spannablePhoneString = new SpannableString(patientObject.getPatientPhone());
                Pattern pattern = Pattern.compile(patientObject.getSpannableString(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(patientObject.getPatientPhone());
                while (matcher.find()) {
                    spannablePhoneString.setSpan(new ForegroundColorSpan(
                                    ContextCompat.getColor(mContext, R.color.tagColor)),
                            matcher.start(), matcher.end(),//hightlight mSearchString
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.patientPhoneNumber.setText(spannablePhoneString);
            } else {
                holder.patientPhoneNumber.setText(patientObject.getPatientPhone());
            }
            //Spannable condition for PatientId


            if (dataToShowInPatientID.toLowerCase().contains(patientObject.getSpannableString().toLowerCase())) {

                SpannableString spannableIdString = new SpannableString(mContext.getString(R.string.id) + " " + dataToShowInPatientID);
                Pattern pattern = Pattern.compile(patientObject.getSpannableString(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(mContext.getString(R.string.id) + " " + dataToShowInPatientID);

                while (matcher.find()) {
                    spannableIdString.setSpan(new ForegroundColorSpan(
                                    ContextCompat.getColor(mContext, R.color.tagColor)),
                            matcher.start(), matcher.end(),//hightlight mSearchString
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.patientIdTextView.setText(spannableIdString);
            } else {
                holder.patientIdTextView.setText(mContext.getString(R.string.id) + " " + dataToShowInPatientID);
            }
            //--- END: -------------------------------
        } else {
            holder.patientNameTextView.setText(patientName);
            holder.patientPhoneNumber.setText(patientObject.getPatientPhone());
            holder.patientIdTextView.setText(mContext.getString(R.string.id) + " " + dataToShowInPatientID);
        }
        //------------
        String gender = CommonMethods.toCamelCase(patientObject.getGender());
        holder.patientGenderTextView.setText(gender);
        //------------
        //-- DOnt show - when gender is empty
        String yearsString = mContext.getString(R.string.years);
        String finalStringForAgeNGender = "";
        if (gender != null && DMSConstants.BLANK.equalsIgnoreCase(gender)) {
            yearsString = yearsString.substring(0, (yearsString.length() - 2));
        }
        if (patientObject.getAge() != null && !DMSConstants.BLANK.equalsIgnoreCase(patientObject.getAge())) {
            holder.patientAgeTextView.setVisibility(View.VISIBLE);

            finalStringForAgeNGender = patientObject.getAge() + " " + yearsString;
            holder.patientAgeTextView.setText(finalStringForAgeNGender);

        } else if (patientObject.getDateOfBirth() != null && !DMSConstants.BLANK.equalsIgnoreCase(patientObject.getDateOfBirth())) {
            holder.patientAgeTextView.setVisibility(View.VISIBLE);
            String getTodayDate = CommonMethods.getCurrentDate(DMSConstants.DATE_PATTERN.YYYY_MM_DD);
            String getBirthdayDate = patientObject.getDateOfBirth();
            DateTime todayDateTime = CommonMethods.convertToDateTime(getTodayDate, DMSConstants.DATE_PATTERN.YYYY_MM_DD);
            DateTime birthdayDateTime = CommonMethods.convertToDateTime(getBirthdayDate, DMSConstants.DATE_PATTERN.YYYY_MM_DD);

            finalStringForAgeNGender = CommonMethods.displayAgeAnalysis(todayDateTime, birthdayDateTime) + " " + yearsString;
            holder.patientAgeTextView.setText(finalStringForAgeNGender);
        } else {
            holder.patientAgeTextView.setVisibility(View.GONE);
        }


        //------------

        holder.outstandingAmountTextView.setText(mContext.getString(R.string.outstanding_amount) + " ");
        if (patientObject.getOutStandingAmount().equals("0.00") || patientObject.getOutStandingAmount().equals("0.0") || patientObject.getOutStandingAmount().equals("0")) {
            holder.payableAmountTextView.setText(" " + mContext.getString(R.string.nil));
            holder.payableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.rating_color));

        } else {
            holder.payableAmountTextView.setText(" Rs." + patientObject.getOutStandingAmount() + "/-");
            holder.payableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.Red));

        }
        holder.chatImageView.setVisibility(View.VISIBLE);
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, patientObject.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(patientObject.getPatientImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.patientImageView);
        holder.checkbox.setChecked(patientObject.isSelected());

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patientObject.setSelected(holder.checkbox.isChecked());
                mOnDownArrowClicked.onCheckUncheckRemoveSelectAllSelection(holder.checkbox.isChecked(), patientObject);
            }
        });
        if (isLongPressed)
            holder.checkbox.setVisibility(View.VISIBLE);
        else holder.checkbox.setVisibility(View.GONE);

        holder.patientDetailsClickLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPressed = !isLongPressed;
                mOnDownArrowClicked.onLongPressOpenBottomMenu(isLongPressed, position);
                notifyDataSetChanged();
                return false;
            }
        });
        holder.patientDetailsClickLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //THis is done, bcz not getting proper data from age n gender view.
                String patientInfo = "";
                if (!patientObject.getAge().isEmpty() && !patientObject.getGender().isEmpty())
                    patientInfo = patientObject.getAge() + " yrs - " + patientObject.getGender();
                else if (!patientObject.getAge().isEmpty())
                    patientInfo = patientObject.getAge() + " yrs";
                else if (!patientObject.getGender().isEmpty())
                    patientInfo = patientObject.getGender();


                mOnDownArrowClicked.onClickOfPatientDetails(patientObject, patientInfo, isClickOnPatientDetailsRequired);
            }
        });

        holder.chatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                PatientData doctorConnectChatModel = new PatientData();
                doctorConnectChatModel.setId(patientObject.getPatientId());
                doctorConnectChatModel.setImageUrl(patientObject.getPatientImageUrl());
                doctorConnectChatModel.setPatientName(patientObject.getPatientName());
                doctorConnectChatModel.setSalutation(patientObject.getSalutation());
                intent.putExtra(CLINIC_ID, patientObject.getClinicId());
                intent.putExtra(PATIENT_HOS_PAT_ID, patientObject.getHospitalPatId());
                intent.putExtra(DMSConstants.PATIENT_INFO, doctorConnectChatModel);
                ((Activity) mContext).startActivityForResult(intent, Activity.RESULT_OK);
            }
        });
        holder.patientPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onPhoneNoClick(patientObject.getPatientPhone());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public ArrayList<PatientList> getGroupList() {
        return mDataList;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bluelineImageView)
        ImageView bluelineImageView;
        @BindView(R.id.patientIdTextView)
        CustomTextView patientIdTextView;
        @BindView(R.id.appointmentTime)
        CustomTextView appointmentTime;
        @BindView(R.id.chatImageView)
        ImageView chatImageView;
        @BindView(R.id.patientImageView)
        CircularImageView patientImageView;
        @BindView(R.id.patientNameTextView)
        CustomTextView patientNameTextView;
        @BindView(R.id.patientAgeTextView)
        CustomTextView patientAgeTextView;
        @BindView(R.id.patientGenderTextView)
        CustomTextView patientGenderTextView;
        @BindView(R.id.patientDetailsLinearLayout)
        LinearLayout patientDetailsLinearLayout;
        @BindView(R.id.opdTypeTextView)
        CustomTextView opdTypeTextView;
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.patientPhoneNumber)
        CustomTextView patientPhoneNumber;
        @BindView(R.id.separatorView)
        View separatorView;
        @BindView(R.id.outstandingAmountTextView)
        CustomTextView outstandingAmountTextView;
        @BindView(R.id.payableAmountTextView)
        CustomTextView payableAmountTextView;
        @BindView(R.id.cardView)
        LinearLayout cardView;
        @BindView(R.id.patientClinicAddress)
        CustomTextView patientClinicAddress;
        @BindView(R.id.patientDetailsClickLinearLayout)
        RelativeLayout patientDetailsClickLinearLayout;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public boolean isLongPressed() {
        return isLongPressed;
    }

    public void setLongPressed(boolean longPressed) {
        isLongPressed = longPressed;
    }

    public interface OnDownArrowClicked {

        void onLongPressOpenBottomMenu(boolean isLongPressed, int groupPosition);

        void onCheckUncheckRemoveSelectAllSelection(boolean ischecked, PatientList patientObject);

        void onClickOfPatientDetails(PatientList patientListObject, String text, boolean isClickOnPatientDetailsRequired);

        void onPhoneNoClick(String patientPhone);

        void onRecordFound(boolean isListEmpty);
    }


    public void add(PatientList mc) {
        mDataList.add(mc);
        mListToShowAfterFilter.add(mc);
        removeDuplicateElements();
        notifyItemInserted(mDataList.size() - 1);
    }


    // this is added to remove duplicate patients from list based on patientID.
    private void removeDuplicateElements() {
        Map<Integer, PatientList> map = new LinkedHashMap<>();
        for (PatientList ays : mDataList) {
            map.put(ays.getHospitalPatId(), ays);
        }

        mDataList.clear();
        mDataList.addAll(map.values());
        mListToShowAfterFilter.clear();
        mListToShowAfterFilter.addAll(map.values());
    }

    public void addAll(ArrayList<PatientList> mcList, HashSet<Integer> selectedDoctorId, String searchText) {

        for (PatientList mc : mcList) {
            mc.setSpannableString(searchText);
            mc.setSelected(selectedDoctorId.contains(mc.getHospitalPatId()));
            add(mc);
            // add patient in sqlite while pagination.

            if (mc.isOfflinePatientSynced()) {
                mc.setOfflinePatientSynced(true);
            } else {
                mc.setOfflinePatientSynced(false);
            }

            // mc.setReferenceID("");
            appDBHelper.addNewPatient(mc);
        }

        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString().toLowerCase();
                mDataList.clear();

                if (charString.isEmpty()) {
                    for (PatientList patList : mListToShowAfterFilter) {
                        patList.setSpannableString("");
                        mDataList.add(patList);
                    }
                } else {
                    for (PatientList patientListObject : mListToShowAfterFilter) {
                        if (patientListObject.getPatientName().toLowerCase().contains(charString)
                                || patientListObject.getPatientPhone().contains(charString)
                                || String.valueOf(patientListObject.getPatientId()).contains(charString)) {

                            patientListObject.setSpannableString(charString);
                            mDataList.add(patientListObject);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataList = (ArrayList<PatientList>) filterResults.values;
                if (mDataList.isEmpty())
                    mOnDownArrowClicked.onRecordFound(true);
                else
                    mOnDownArrowClicked.onRecordFound(false);

                notifyDataSetChanged();
            }
        };
    }
}