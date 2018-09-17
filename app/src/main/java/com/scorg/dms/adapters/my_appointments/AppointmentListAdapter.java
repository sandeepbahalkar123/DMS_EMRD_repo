/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.scorg.dms.adapters.my_appointments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.customesViews.swipeable_recyclerview.SwipeRevealLayout;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scorg.dms.util.CommonMethods.toCamelCase;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.BOOKED_STATUS;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.CANCEL;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.COMPLETED;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.CONFIRM_STATUS;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.NO_SHOW;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.OTHER;


/**
 * Created by jeetal on 31/1/18.
 * <p>
 * MESSAGE ==> Explicitly disable long press,swipe and click listener. And do needful to make this work.
 */
public class AppointmentListAdapter
        extends RecyclerView.Adapter<AppointmentListAdapter.MyViewHolder> implements Filterable {
    private static final String TAG = "AppointmentListAdapter";
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private ArrayList<AppointmentPatientData> mAppointmentDataList;
    private ArrayList<AppointmentPatientData> mAppointmentDataListOriginal;


    public AppointmentListAdapter(Context context, ArrayList<AppointmentPatientData> waitingDataList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mAppointmentDataListOriginal = new ArrayList<>();
        this.mAppointmentDataListOriginal.addAll(waitingDataList);
        this.mAppointmentDataList = new ArrayList<>();
        this.mAppointmentDataList.addAll(waitingDataList);
        this.onItemClickListener = onItemClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private FrameLayout delete_layout;
        private FrameLayout front_layout;
        private CheckBox checkbox;
        private RelativeLayout swipe_layout;
        private RelativeLayout patientDetailsClickLinearLayout;
        private CustomTextView appointmentTime;
        private ImageView waitingIcon;
        private CustomTextView patientIdTextView;
        private CircularImageView patientImageView;
        private CustomTextView patientNameTextView;
        private CustomTextView patientAgeTextView;
        private CustomTextView patientGenderTextView;
        private CustomTextView opdTypeTextView;
        private CustomTextView patientPhoneNumber;
        private CustomTextView outstandingAmountTextView;
        private CustomTextView payableAmountTextView;
        private LinearLayout idAndDetailsLayout;
        private Button appointmentReschedule;
        private Button appointmentCancel;
        private Button appointmentComplete;
        private LinearLayout layoutAppointmentEpisode;

        MyViewHolder(View convertView) {
            super(convertView);
            idAndDetailsLayout = (LinearLayout) convertView.findViewById(R.id.idAndDetailsLayout);
            front_layout = (FrameLayout) convertView.findViewById(R.id.front_layout);
            delete_layout = (FrameLayout) convertView.findViewById(R.id.delete_layout);
            waitingIcon = (ImageView) convertView.findViewById(R.id.waitingIcon);

            swipe_layout = (RelativeLayout) convertView.findViewById(R.id.swipe_layout);
            checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            patientDetailsClickLinearLayout = (RelativeLayout) convertView.findViewById(R.id.patientDetailsClickLinearLayout);
            appointmentTime = (CustomTextView) convertView.findViewById(R.id.appointmentTime);
            patientIdTextView = (CustomTextView) convertView.findViewById(R.id.patientIdTextView);
            patientImageView = (CircularImageView) convertView.findViewById(R.id.patientImageView);
            patientNameTextView = (CustomTextView) convertView.findViewById(R.id.patientNameTextView);
            patientAgeTextView = (CustomTextView) convertView.findViewById(R.id.patientAgeTextView);
            patientGenderTextView = (CustomTextView) convertView.findViewById(R.id.patientGenderTextView);
            opdTypeTextView = (CustomTextView) convertView.findViewById(R.id.opdTypeTextView);
            patientPhoneNumber = (CustomTextView) convertView.findViewById(R.id.patientPhoneNumber);
            outstandingAmountTextView = (CustomTextView) convertView.findViewById(R.id.outstandingAmountTextView);
            payableAmountTextView = (CustomTextView) convertView.findViewById(R.id.payableAmountTextView);
            layoutAppointmentEpisode = (LinearLayout) convertView.findViewById(R.id.layoutAppointmentEpisode);
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mAppointmentDataList.get(position).getPatientId());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.my_appointment_child_item, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final AppointmentListAdapter.MyViewHolder holder, final int position) {

        AppointmentPatientData appointmentPatientDataObject = mAppointmentDataList.get(position);

        bindGroupItem(appointmentPatientDataObject, holder);
    }

    private void bindGroupItem(final AppointmentPatientData appointmentPatientDataObject, final AppointmentListAdapter.MyViewHolder holder) {

        String salutation = appointmentPatientDataObject.getSalutation();
        String patientName = toCamelCase(appointmentPatientDataObject.getPatientName());

        if (salutation != null) {
            patientName = salutation + " " + patientName;
        }

        //---- START: Setting of hospitalID or referecne ID, reference is IS high priority than hospitalID.-----
        String dataToShowInPatientID = String.valueOf(appointmentPatientDataObject.getPatientId());
        holder.patientIdTextView.setText(holder.patientIdTextView.getResources().getString(R.string.uhid) + " " + dataToShowInPatientID + "");
        //---- END------

        if (appointmentPatientDataObject.getSpannableString() != null) {
            //-----------------
            //Spannable condition for PatientName
            if (patientName.toLowerCase().contains(appointmentPatientDataObject.getSpannableString().toLowerCase())) {
                holder.patientNameTextView.setText(doCreateSpannableData(patientName, appointmentPatientDataObject.getSpannableString()));
            } else {
                holder.patientNameTextView.setText(patientName);
            }
            //----------------
            //Spannable condition for PatientPhoneNumber
            if (appointmentPatientDataObject.getContactNo() != null) {
                holder.patientPhoneNumber.setVisibility(View.VISIBLE);
                if (appointmentPatientDataObject.getContactNo().toLowerCase().contains(appointmentPatientDataObject.getSpannableString().toLowerCase())) {
                    holder.patientPhoneNumber.setText(doCreateSpannableData(appointmentPatientDataObject.getContactNo(), appointmentPatientDataObject.getSpannableString()));
                } else {
                    holder.patientPhoneNumber.setText(appointmentPatientDataObject.getContactNo());
                }
            } else {
                holder.patientPhoneNumber.setVisibility(View.VISIBLE);
                holder.patientPhoneNumber.setText("-");
            }

            //---------------
            //Spannable condition for PatientId
            if (dataToShowInPatientID.toLowerCase().contains(appointmentPatientDataObject.getSpannableString().toLowerCase())) {
                holder.patientIdTextView.setText(doCreateSpannableData(mContext.getString(R.string.uhid) + " " + dataToShowInPatientID, appointmentPatientDataObject.getSpannableString()));
            } else {
                holder.patientIdTextView.setText(mContext.getString(R.string.uhid) + " " + dataToShowInPatientID);
            }
            //---------------
        } else {
            holder.patientNameTextView.setText(patientName);
            holder.patientPhoneNumber.setText(appointmentPatientDataObject.getContactNo());
            holder.patientIdTextView.setText(mContext.getString(R.string.uhid) + " " + dataToShowInPatientID);
        }

        //-----------
        if (appointmentPatientDataObject.getAge() != null) {
            holder.patientAgeTextView.setVisibility(View.VISIBLE);
            holder.patientAgeTextView.setText(appointmentPatientDataObject.getAge() + " " + mContext.getString(R.string.years));
        } else {
            holder.patientAgeTextView.setVisibility(View.GONE);
        }
        //-----------
        if (appointmentPatientDataObject.getGender() != null) {
            holder.patientGenderTextView.setText(CommonMethods.toCamelCase(appointmentPatientDataObject.getGender()));
            holder.patientGenderTextView.setVisibility(View.VISIBLE);
        } else {
            holder.patientGenderTextView.setVisibility(View.GONE);
        }
        //-----------
        if (appointmentPatientDataObject.getAppointmentStatus().equalsIgnoreCase(BOOKED_STATUS)) {
            holder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.book_color));
            holder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + mContext.getString(R.string.booked));
        } else if (appointmentPatientDataObject.getAppointmentStatus().equals(COMPLETED)) {
            holder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + mContext.getString(R.string.capitalcompleted));
            holder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.complete_color));
            holder.waitingIcon.setVisibility(View.INVISIBLE);
        } else if (appointmentPatientDataObject.getAppointmentStatus().equalsIgnoreCase(CONFIRM_STATUS)) {
            holder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + appointmentPatientDataObject.getAppointmentStatus());
            holder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.confirm_color));
        } else if (appointmentPatientDataObject.getAppointmentStatus().equals(CANCEL)) {
            holder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + appointmentPatientDataObject.getAppointmentStatus());
            holder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.cancel_color));
            holder.waitingIcon.setVisibility(View.INVISIBLE);
        } else if (appointmentPatientDataObject.getAppointmentStatus().equals(NO_SHOW)) {
            holder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + appointmentPatientDataObject.getAppointmentStatus());
            holder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.no_show_color));
        } else if (appointmentPatientDataObject.getAppointmentStatus().equals(OTHER)) {
            holder.opdTypeTextView.setText(mContext.getString(R.string.opd_appointment) + " " + appointmentPatientDataObject.getAppointmentStatus());
            holder.opdTypeTextView.setTextColor(ContextCompat.getColor(mContext, R.color.other_color));
        }
        //--------------
        holder.outstandingAmountTextView.setText(mContext.getString(R.string.outstanding_amount) + " ");
        //--------
        String outstandingAmount = appointmentPatientDataObject.getOutstandingAmount();
        if ("0.00".equals(outstandingAmount) || "0.0".equals(outstandingAmount) || "0".equals(outstandingAmount)) {
            holder.payableAmountTextView.setText(" " + mContext.getString(R.string.nil));
            holder.payableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.rating_color));
        } else {
            holder.payableAmountTextView.setText(" Rs." + outstandingAmount + "/-");
            holder.payableAmountTextView.setTextColor(ContextCompat.getColor(mContext, R.color.Red));
        }
        //---------
        String appDate = appointmentPatientDataObject.getAppDate();
        if (appDate != null) {
            holder.appointmentTime.setVisibility(View.VISIBLE);
            holder.appointmentTime.setText(CommonMethods.formatDateTime(appDate, DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.TIME).toLowerCase());
        }
        //-------
        TextDrawable textDrawable = CommonMethods.getTextDrawable(mContext, appointmentPatientDataObject.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.skipMemoryCache(false);
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(mContext)
                .load(appointmentPatientDataObject.getPatientImageUrl())
                .apply(requestOptions).thumbnail(0.5f)
                .into(holder.patientImageView);

        ViewTreeObserver vto = holder.front_layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.front_layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = holder.delete_layout.getLayoutParams();
                layoutParams.height = holder.front_layout.getMeasuredHeight();
                layoutParams.width = holder.front_layout.getMeasuredWidth() / 2;
                holder.delete_layout.setLayoutParams(layoutParams);

            }
        });

        holder.layoutAppointmentEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResult searchResult =new SearchResult();
                searchResult.setPatientName(appointmentPatientDataObject.getPatientName());
                 searchResult.setPatientId(appointmentPatientDataObject.getPatientId());
                searchResult.setPatientAddress(appointmentPatientDataObject.getPatAddress());
                searchResult.setPatientImageURL(appointmentPatientDataObject.getPatientImageUrl());
                onItemClickListener.onClickedOfEpisodeListButton(searchResult);
            }
        });

        holder.idAndDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onClickOfPatientDetails(appointmentPatientDataObject);
            }
        });
        holder.patientPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactNo = appointmentPatientDataObject.getContactNo();
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
        return mAppointmentDataList.size();
    }

    // Sorting clicniclist by patientName , patientId , patientPhoneNo
    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                mAppointmentDataList.clear();

                if (charString.isEmpty()) {
                    for (AppointmentPatientData appointmentPatientDataObj : mAppointmentDataListOriginal) {
                        appointmentPatientDataObj.setSpannableString(null);
                        mAppointmentDataList.add(appointmentPatientDataObj);
                    }

                } else {
                    charString = charString.toLowerCase();
                    for (AppointmentPatientData appointmentPatientDataObj : mAppointmentDataListOriginal) {

                        if ((appointmentPatientDataObj.getPatientName().toLowerCase().contains(charString))
                                || (String.valueOf(appointmentPatientDataObj.getContactNo()).contains(charString))
                                || (String.valueOf(appointmentPatientDataObj.getPatientId().toLowerCase()).contains(charString))) {
                            //--------
                            appointmentPatientDataObj.setSpannableString(charString);
                            mAppointmentDataList.add(appointmentPatientDataObj);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mAppointmentDataList;
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

        void onClickOfPatientDetails(AppointmentPatientData patientListObject);

        void onPhoneNoClick(long patientPhone);

        void onClickedOfEpisodeListButton(SearchResult groupHeader);
    }
}
