package com.rescribe.doctor.adapters.completed_opd;

import android.content.Context;
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
import com.rescribe.doctor.R;
import com.rescribe.doctor.model.completed_opd.CompletedOpd;
import com.rescribe.doctor.ui.customesViews.CircularImageView;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by jeetal on 17/3/18.
 */

public class CompletedOpdAdapter extends RecyclerView.Adapter<CompletedOpdAdapter.ListViewHolder> implements Filterable {

    private Context mContext;
    private ArrayList<CompletedOpd> mDataList;
    private ArrayList<CompletedOpd> mOriginalPatientList;
    public boolean isLongPressed;
    private CompletedOpdAdapter.OnDownArrowClicked mOnDownArrowClicked;


    public CompletedOpdAdapter(Context mContext, ArrayList<CompletedOpd> dataList, CompletedOpdAdapter.OnDownArrowClicked mOnDownArrowClicked) {
        this.mDataList = new ArrayList<>(dataList);
        this.mOriginalPatientList = new ArrayList<>(dataList);
        this.mContext = mContext;
        this.mOnDownArrowClicked = mOnDownArrowClicked;
    }

    @Override
    public CompletedOpdAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_appointments_child_item, parent, false);

        return new CompletedOpdAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CompletedOpdAdapter.ListViewHolder holder, final int position) {
        final CompletedOpd patientObject = mDataList.get(position);
        holder.opdTypeTextView.setVisibility(View.VISIBLE);
        holder.patientClinicAddress.setVisibility(View.VISIBLE);
        if (patientObject.getOpdFollowUpStatus().equals(0)) {
            holder.opdTypeTextView.setText(mContext.getString(R.string.consultation));
        } else {
            holder.opdTypeTextView.setText(mContext.getString(R.string.follow_up));
        }
        String patientName;
        if (patientObject.getSalutation() != 0)
            patientName = RescribeConstants.SALUTATION[patientObject.getSalutation() - 1] + CommonMethods.toCamelCase(patientObject.getPatientName());
        else patientName = CommonMethods.toCamelCase(patientObject.getPatientName());
        holder.chatImageView.setVisibility(View.GONE);

        //---- START: Setting of hospitalID or referecne ID, reference is IS high priority than hospitalID.-----
        String dataToShowInPatientID = String.valueOf(patientObject.getReferenceId());

        if (dataToShowInPatientID == null || RescribeConstants.BLANK.equalsIgnoreCase(dataToShowInPatientID)) {
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

            if (patientObject.getPatientPhon().toLowerCase().contains(patientObject.getSpannableString().toLowerCase())) {
                SpannableString spannablePhoneString = new SpannableString(patientObject.getPatientPhon());
                Pattern pattern = Pattern.compile(patientObject.getSpannableString(), Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(patientObject.getPatientPhon());
                while (matcher.find()) {
                    spannablePhoneString.setSpan(new ForegroundColorSpan(
                                    ContextCompat.getColor(mContext, R.color.tagColor)),
                            matcher.start(), matcher.end(),//hightlight mSearchString
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                holder.patientPhoneNumber.setText(spannablePhoneString);
            } else {
                holder.patientPhoneNumber.setText(patientObject.getPatientPhon());
            }
            //TODO:
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
        } else {
            holder.patientNameTextView.setText(patientName);
            holder.patientPhoneNumber.setText(patientObject.getPatientPhon());
            holder.patientIdTextView.setText(mContext.getString(R.string.id) + " " + dataToShowInPatientID);
        }

        if (patientObject.getAge().equals("") && !patientObject.getPatientDob().equals("")) {
            holder.patientAgeTextView.setVisibility(View.VISIBLE);
            String getTodayDate = CommonMethods.getCurrentDate(RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            String getBirthdayDate = patientObject.getPatientDob();
            DateTime todayDateTime = CommonMethods.convertToDateTime(getTodayDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            DateTime birthdayDateTime = CommonMethods.convertToDateTime(getBirthdayDate, RescribeConstants.DATE_PATTERN.YYYY_MM_DD);
            holder.patientAgeTextView.setText(CommonMethods.displayAgeAnalysis(todayDateTime, birthdayDateTime) + " " + mContext.getString(R.string.years));
        } else if (!patientObject.getAge().equals("")) {
            holder.patientAgeTextView.setVisibility(View.VISIBLE);
            holder.patientAgeTextView.setText(patientObject.getAge() + " " + mContext.getString(R.string.years));
        } else {
            holder.patientAgeTextView.setVisibility(View.GONE);
        }

        holder.patientGenderTextView.setText(CommonMethods.toCamelCase(patientObject.getPatientGender()));
        holder.outstandingAmountTextView.setText(mContext.getString(R.string.outstanding_amount) + " ");
        if (patientObject.getOutstandingAmount().equals("0.00") || patientObject.getOutstandingAmount().equals("0.0") || patientObject.getOutstandingAmount().equals("0")) {
            holder.payableAmountTextView.setText(" " + mContext.getString(R.string.nil));
            holder.payableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.rating_color));

        } else {
            holder.payableAmountTextView.setText(" Rs." + patientObject.getOutstandingAmount() + "/-");
            holder.payableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.Red));

        }

        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, patientObject.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(true);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(patientObject.getProfilePhoto())
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

        //Long press event disabled by sandeep
//        if (isLongPressed)
//            holder.checkbox.setVisibility(View.VISIBLE);
//        else holder.checkbox.setVisibility(View.GONE);

        //Click event disabled by sandeep
//        holder.patientDetailsClickLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnDownArrowClicked.onClickOfPatientDetails(patientObject, holder.patientAgeTextView.getText().toString() + holder.patientGenderTextView.getText().toString());
//            }
//        });


        holder.patientPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onPhoneNoClick(patientObject.getPatientPhon());
            }
        });
        holder.patientDetailsClickLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnDownArrowClicked.onClickOfPatientDetails(patientObject,holder.patientAgeTextView.getText().toString()+holder.patientGenderTextView.getText().toString());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public ArrayList<CompletedOpd> getGroupList() {
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

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                ArrayList<CompletedOpd> mListToShowAfterFilter;
                ArrayList<CompletedOpd> mTempPatientListToIterate = new ArrayList<>(mOriginalPatientList);

                if (charString.isEmpty()) {
                    mListToShowAfterFilter = new ArrayList<>();
                    for (CompletedOpd patientListObject : mTempPatientListToIterate) {
                        //--------
                        patientListObject.setSpannableString(null);
                        mListToShowAfterFilter.add(patientListObject);

                    }
                } else {
                    mListToShowAfterFilter = new ArrayList<>();
                    for (CompletedOpd patientListObject : mTempPatientListToIterate) {

                        if (patientListObject.getPatientName().toLowerCase().contains(charString.toLowerCase())
                                || patientListObject.getPatientPhon().contains(charString)
                                || String.valueOf(patientListObject.getHospitalPatId()).contains(charString)
                                || String.valueOf(patientListObject.getReferenceId().toLowerCase()).contains(charString.toLowerCase())) {
                            //--------
                            patientListObject.setSpannableString(charString);
                            mListToShowAfterFilter.add(patientListObject);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mListToShowAfterFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataList.clear();
                mDataList.addAll((ArrayList<CompletedOpd>) filterResults.values);

                if (mDataList.isEmpty()) {
                    mOnDownArrowClicked.onRecordFound(true);
                } else mOnDownArrowClicked.onRecordFound(false);
                notifyDataSetChanged();
            }
        };

    }

    public interface OnDownArrowClicked {

        void onLongPressOpenBottomMenu(boolean isLongPressed, int groupPosition);

        void onRecordFound(boolean isListEmpty);

        void onCheckUncheckRemoveSelectAllSelection(boolean ischecked, CompletedOpd patientObject);

        void onClickOfPatientDetails(CompletedOpd patientListObject, String text);

        void onPhoneNoClick(String patientPhone);

    }


}