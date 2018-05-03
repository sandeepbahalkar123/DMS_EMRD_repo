package com.rescribe.doctor.adapters.patient_connect;

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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rescribe.doctor.R;
import com.rescribe.doctor.helpers.database.AppDBHelper;
import com.rescribe.doctor.model.patient.patient_connect.PatientData;
import com.rescribe.doctor.ui.activities.ChatActivity;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.doctor.ui.activities.PatientConnectActivity.PATIENT_CONNECT_REQUEST;
import static com.rescribe.doctor.util.RescribeConstants.IS_CALL_FROM_MY_PATIENTS;
import static com.rescribe.doctor.util.RescribeConstants.SALUTATION;


/**
 * Created by jeetal on 6/9/17.
 */

public class PatientConnectAdapter extends RecyclerView.Adapter<PatientConnectAdapter.ListViewHolder> implements Filterable {

    private final ArrayList<PatientData> mArrayList;
    private final ColorGenerator mColorGenerator;
    private final FilterListener filterListener;
    private Context mContext;
    private ArrayList<PatientData> dataList;
    private String searchString = "";

    private final AppDBHelper appDBHelper;
    private boolean isFromSearch = false;

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.headingText)
        CustomTextView headingText;

        @BindView(R.id.dateTimeText)
        CustomTextView dateTimeText;

        @BindView(R.id.doctorName)
        CustomTextView doctorName;

        @BindView(R.id.messageTextView)
        TextView messageTextView;

        @BindView(R.id.onlineStatusIcon)
        ImageView onlineStatusIcon;

        @BindView(R.id.imageOfDoctor)
        ImageView imageOfDoctor;

        @BindView(R.id.badgeText)
        TextView badgeText;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public PatientConnectAdapter(Context mContext, ArrayList<PatientData> appointmentsList, FilterListener filterListener) {

        appDBHelper = new AppDBHelper(mContext);
        this.filterListener = filterListener;
        this.dataList = appointmentsList;
        mArrayList = appointmentsList;
        this.mContext = mContext;
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.global_connect_chats_row_item, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final PatientData doctorConnectChatModel = dataList.get(position);

        if (isFromSearch) {
            holder.messageTextView.setVisibility(View.VISIBLE);
            if (position == 0)
                holder.headingText.setVisibility(View.VISIBLE);
            else holder.headingText.setVisibility(View.GONE);
            holder.messageTextView.setVisibility(View.GONE); // this is last message text view currently not using

        } else {
            holder.headingText.setVisibility(View.GONE);
            holder.messageTextView.setVisibility(View.GONE); // this is last message text view currently not using
        }

        if (doctorConnectChatModel.getLastChatTime() != null) {
            String timeT = CommonMethods.getDayFromDateTime(doctorConnectChatModel.getLastChatTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.hh_mm_a);
            holder.dateTimeText.setText(timeT);
        }

        String patientName = doctorConnectChatModel.getPatientName();
        int color2 = mColorGenerator.getColor(patientName);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40)))  // width in px
                .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                .endConfig()
                .buildRound(("" + patientName.charAt(0)).toUpperCase(), color2);
        if(doctorConnectChatModel.getImageUrl().equals("")) {
            patientName = patientName.replace("Dr. ", "");

            if (patientName != null && patientName.length() > 0) {

                holder.imageOfDoctor.setImageDrawable(drawable);
            }
        }else{
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.dontAnimate();
            requestOptions.override(100, 100);
            requestOptions.transform(new CircleCrop(holder.imageOfDoctor.getContext()));
            requestOptions.placeholder(drawable);
            Glide.with(holder.imageOfDoctor.getContext())
                    .load(doctorConnectChatModel.getImageUrl())
                    .apply(requestOptions).thumbnail(0.2f)
                    .into(holder.imageOfDoctor);
        }

        String salutation;
        if (doctorConnectChatModel.getSalutation() != 0)
            salutation = SALUTATION[doctorConnectChatModel.getSalutation() - 1];
        else salutation = "";

        if (searchString != null) {
            String lowerCaseSearchString = searchString.toLowerCase();
            String loweCaseMsg = salutation + doctorConnectChatModel.getPatientName().toLowerCase();
            int startIndex = loweCaseMsg.indexOf(lowerCaseSearchString);
            if (startIndex != -1) {
                SpannableString spannableStringSearch = new SpannableString(salutation + doctorConnectChatModel.getPatientName());
                spannableStringSearch.setSpan(new ForegroundColorSpan(
                                ContextCompat.getColor(mContext, R.color.tagColor)), startIndex
                        , startIndex + searchString.length(),
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.doctorName.setText(spannableStringSearch);
            } else
                holder.doctorName.setText(salutation + CommonMethods.toCamelCase(doctorConnectChatModel.getPatientName()));//sandeep
        } else
            holder.doctorName.setText(salutation + CommonMethods.toCamelCase(doctorConnectChatModel.getPatientName()));//sandeep

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(RescribeConstants.PATIENT_INFO, doctorConnectChatModel);
                intent.putExtra(IS_CALL_FROM_MY_PATIENTS, true);
                ((Activity) mContext).startActivityForResult(intent, PATIENT_CONNECT_REQUEST);
            }
        });

        doctorConnectChatModel.setUnreadMessages((int) appDBHelper.unreadChatMessageCountByPatientId(doctorConnectChatModel.getId()));
        if (doctorConnectChatModel.getUnreadMessages() > 0) {
            holder.badgeText.setText(String.valueOf(doctorConnectChatModel.getUnreadMessages()));
            holder.badgeText.setVisibility(View.VISIBLE);
        } else
            holder.badgeText.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                searchString = charString;
                if (charString.isEmpty()) {
                    isFromSearch = false;
                    dataList = mArrayList;
                } else {
                    isFromSearch = true;
                    ArrayList<PatientData> filteredList = new ArrayList<>();

                    for (PatientData doctorConnectModel : mArrayList) {
                        if (doctorConnectModel.getPatientName().toLowerCase().contains(charString.toLowerCase()))
                            filteredList.add(doctorConnectModel);
                    }

                    dataList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataList = (ArrayList<PatientData>) filterResults.values;
                filterListener.result(charSequence.toString(), dataList);
                notifyDataSetChanged();
            }
        };
    }

    public interface FilterListener {
        void result(String searchText, ArrayList<PatientData> dataList);
    }
}

