package com.scorg.dms.adapters.patient_connect;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
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
import com.scorg.dms.R;
import com.scorg.dms.model.patient.patient_connect.PatientData;
import com.scorg.dms.ui.activities.ChatActivity;
import com.scorg.dms.ui.activities.PatientConnectActivity;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.ui.fragments.patient.patient_connect.PatientSearchFragment;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scorg.dms.ui.activities.PatientConnectActivity.PATIENT_CONNECT_REQUEST;


/**
 * Created by jeetal on 6/9/17.
 */

public class PatientConnectChatAdapter extends RecyclerView.Adapter<PatientConnectChatAdapter.ListViewHolder> implements Filterable {

    private final ArrayList<PatientData> mArrayList;
    private final ColorGenerator mColorGenerator;
    private Fragment mCalledParentFragment;
    private Context mContext;
    private ArrayList<PatientData> dataList;
    String searchString = "";


    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.doctorType)
        TextView doctorType;
        @BindView(R.id.messageTextView)
        TextView onlineStatusTextView;

        @BindView(R.id.onlineStatusIcon)
        ImageView onlineStatusIcon;

        @BindView(R.id.imageOfDoctor)
        ImageView imageOfDoctor;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public PatientConnectChatAdapter(Context mContext, ArrayList<PatientData> appointmentsList, Fragment parentFragment) {
        this.dataList = appointmentsList;
        this.mCalledParentFragment = parentFragment;
        mArrayList = appointmentsList;
        this.mContext = mContext;
        //--------------
        mColorGenerator = ColorGenerator.MATERIAL;
        //-----------------
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

        //-----------

        holder.onlineStatusIcon.setVisibility(View.GONE);

       /* if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(ONLINE)) {
            holder.onlineStatusIcon.setVisibility(View.VISIBLE);
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.green_light));
        } else if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(IDLE)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.range_yellow));
        } else if (doctorConnectChatModel.getOnlineStatus().equalsIgnoreCase(OFFLINE)) {
            holder.onlineStatusTextView.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500));
        }*/
        //-----------

/*
        holder.onlineStatusTextView.setText(doctorConnectChatModel.getOnlineStatus());
*/

        holder.doctorType.setVisibility(View.GONE);

        //---------
        String patientName = doctorConnectChatModel.getPatientName();
        patientName = patientName.replace("Dr. ", "");
        if (patientName != null && patientName.length() > 0) {
            int color2 = mColorGenerator.getColor(patientName);
            TextDrawable drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(Math.round(mContext.getResources().getDimension(R.dimen.dp40)))  // width in px
                    .height(Math.round(mContext.getResources().getDimension(R.dimen.dp40))) // height in px
                    .endConfig()
                    .buildRound(("" + patientName.charAt(0)).toUpperCase(), color2);
            holder.imageOfDoctor.setImageDrawable(drawable);
        }
        //---------
        SpannableString spannableStringSearch = null;

        if ((searchString != null) && (!searchString.isEmpty())) {
            spannableStringSearch = new SpannableString(doctorConnectChatModel.getPatientName());
            spannableStringSearch.setSpan(new ForegroundColorSpan(
                            ContextCompat.getColor(mContext, R.color.tagColor)),
                    0, searchString.length(),
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (spannableStringSearch != null) {
            holder.doctorName.setText(spannableStringSearch);
        } else {
            holder.doctorName.setText(doctorConnectChatModel.getPatientName());
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(DMSConstants.PATIENT_INFO, doctorConnectChatModel);
                ((PatientConnectActivity) mContext).startActivityForResult(intent, PATIENT_CONNECT_REQUEST);
            }
        });
        //---------

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

                    dataList = mArrayList;
                } else {

                    ArrayList<PatientData> filteredList = new ArrayList<>();

                    for (PatientData doctorConnectModel : mArrayList) {

                        if (doctorConnectModel.getPatientName().toLowerCase().startsWith(charString.toLowerCase())) {

                            filteredList.add(doctorConnectModel);
                        }
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
                if (dataList.size() == 0) {
                    PatientSearchFragment temp = (PatientSearchFragment) mCalledParentFragment;
                    temp.isDataListViewVisible(false);
                } else {
                    PatientSearchFragment temp = (PatientSearchFragment) mCalledParentFragment;
                    temp.isDataListViewVisible(true);
                }
                notifyDataSetChanged();
            }
        };
    }

}

