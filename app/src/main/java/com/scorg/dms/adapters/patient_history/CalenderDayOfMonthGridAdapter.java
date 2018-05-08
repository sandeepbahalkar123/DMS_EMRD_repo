package com.scorg.dms.adapters.patient_history;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.patient.patient_history.PatientHistoryInfo;
import com.scorg.dms.ui.customesViews.CircularImageView;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by riteshpandhurkar on 6/2/18.
 */

public class CalenderDayOfMonthGridAdapter extends RecyclerView.Adapter<CalenderDayOfMonthGridAdapter.ListViewHolder> {

    private final OnDayClickListener mListener;

    private Context mContext;
    private ArrayList<PatientHistoryInfo> mDays;
    public boolean longPressed;
    private SimpleDateFormat mDateFormat;

    public CalenderDayOfMonthGridAdapter(Context mContext, ArrayList<PatientHistoryInfo> days, OnDayClickListener listener) {
        this.mDays = days;
        this.mContext = mContext;
        mDateFormat = new SimpleDateFormat(DMSConstants.DATE_PATTERN.DD_MM_YYYY, Locale.US);
        this.mListener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_list_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final PatientHistoryInfo patientHistoryInfoObject = mDays.get(position);

        Date date = CommonMethods.convertStringToDate(patientHistoryInfoObject.getVisitDate(), DMSConstants.DATE_PATTERN.YYYY_MM_DD);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //----
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String toDisplay = day + "<sup>" + CommonMethods.getSuffixForNumber(day) + "</sup>";
        //------
        if (patientHistoryInfoObject.getVisitDate().equalsIgnoreCase(mDateFormat.format(new Date()))) {
            toDisplay = toDisplay + "\n" + mContext.getString(R.string.just_now);
        }
        //------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.date.setText(Html.fromHtml(toDisplay, Html.FROM_HTML_MODE_LEGACY));
        } else {
            holder.date.setText(Html.fromHtml(toDisplay));
        }

        if (position % 2 == 0) {
            holder.parentDataContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bg_white_color));
            holder.sideBarView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tagColor));
        } else {
            holder.parentDataContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.bg_grey_color));
            holder.sideBarView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.statusbar));
        }

        String opdLabel = patientHistoryInfoObject.getOpdLabel();
        String opdLabelToShow = opdLabel.substring(0, 1).toUpperCase() + opdLabel.substring(1);
        holder.doctorName.setText(opdLabelToShow);
        holder.doctorAddress.setText(CommonMethods.stripExtension(patientHistoryInfoObject.getOpdValue()));
        holder.time.setText(CommonMethods.formatDateTime(patientHistoryInfoObject.getOpdTime(), DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.HH_mm_ss, DMSConstants.TIME).toLowerCase());
        SpannableString patientID = new SpannableString("VIEW MORE");
        patientID.setSpan(new UnderlineSpan(), 0, patientID.length(), 0);
        holder.viewMore.setText(patientID);

        if (position == 0) {
            holder.circularBulletMainElement.setVisibility(View.VISIBLE);
            holder.circularBulletChildElement.setVisibility(View.GONE);
            holder.upperLine.setVisibility(View.INVISIBLE);
            holder.circularBulletMainElement.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.blue_dot));
        } else {
            holder.circularBulletMainElement.setVisibility(View.GONE);
            holder.circularBulletChildElement.setVisibility(View.VISIBLE);
            holder.upperLine.setVisibility(View.VISIBLE);
            holder.circularBulletChildElement.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.dark_blue_circle));
        }

        holder.viewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickOFLayout(patientHistoryInfoObject.getVisitDate(), String.valueOf(patientHistoryInfoObject.getOpdId()), patientHistoryInfoObject.getOpdTime());
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDays.size();
    }

    public ArrayList<PatientHistoryInfo> getAdapterList() {
        return mDays;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sideBarView)
        TextView sideBarView;
        @BindView(R.id.date)
        CustomTextView date;
        @BindView(R.id.upperLine)
        TextView upperLine;
        @BindView(R.id.circularBulletMainElement)
        ImageView circularBulletMainElement;
        @BindView(R.id.circularBulletChildElement)
        ImageView circularBulletChildElement;
        @BindView(R.id.lowerLine)
        TextView lowerLine;
        @BindView(R.id.docProfileImage)
        CircularImageView docProfileImage;
        @BindView(R.id.thumbnail)
        LinearLayout thumbnail;
        @BindView(R.id.doctorType)
        CustomTextView doctorType;
        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorAddress)
        CustomTextView doctorAddress;
        @BindView(R.id.patientOpdInfoLayout)
        LinearLayout patientOpdInfoLayout;
        @BindView(R.id.time)
        CustomTextView time;
        @BindView(R.id.viewMore)
        CustomTextView viewMore;
        @BindView(R.id.parentDataContainer)
        LinearLayout parentDataContainer;
        @BindView(R.id.clickOnDoctorVisitLinearLayout)
        LinearLayout clickOnDoctorVisitLinearLayout;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnDayClickListener {
        public void onClickOFLayout(String visitDate, String opdId, String opdTime);
    }
}