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
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rescribe.doctor.R;
import com.rescribe.doctor.model.chat.MQTTMessage;
import com.rescribe.doctor.ui.activities.ChatActivity;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.doctor.broadcast_receivers.ReplayBroadcastReceiver.MESSAGE_LIST;
import static com.rescribe.doctor.services.MQTTService.DOCTOR;
import static com.rescribe.doctor.services.MQTTService.PATIENT;
import static com.rescribe.doctor.services.MQTTService.REPLY_ACTION;
import static com.rescribe.doctor.ui.activities.ChatActivity.SEARCHED_TEXT;
import static com.rescribe.doctor.util.RescribeConstants.IS_CALL_FROM_MY_PATIENTS;
import static com.rescribe.doctor.util.RescribeConstants.MESSAGE_STATUS.PENDING;
import static com.rescribe.doctor.util.RescribeConstants.MESSAGE_STATUS.REACHED;
import static com.rescribe.doctor.util.RescribeConstants.MESSAGE_STATUS.SEEN;
import static com.rescribe.doctor.util.RescribeConstants.MESSAGE_STATUS.SENT;
import static com.rescribe.doctor.util.RescribeConstants.SALUTATION;


/**
 * Created by jeetal on 6/9/17.
 */

public class SearchedMessagesAdapter extends RecyclerView.Adapter<SearchedMessagesAdapter.ListViewHolder> {

    private final ColorGenerator mColorGenerator;
    private String searchString;
    private Context mContext;
    private ArrayList<MQTTMessage> mqttMessages;

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.headingText)
        CustomTextView headingText;
        @BindView(R.id.doctorName)
        CustomTextView doctorName;
        @BindView(R.id.timeText)
        TextView timeText;
        @BindView(R.id.messageText)
        TextView messageText;
        @BindView(R.id.imageOfDoctor)
        ImageView imageOfDoctor;
        @BindView(R.id.senderTickImageView)
        ImageView senderTickImageView;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public SearchedMessagesAdapter(Context mContext, ArrayList<MQTTMessage> mqttMessages) {
        this.mqttMessages = mqttMessages;
        this.mContext = mContext;
        mColorGenerator = ColorGenerator.MATERIAL;
    }

    public void setSearch(String searchString) {
        this.searchString = searchString;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connect_chats_search_row_item, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final MQTTMessage mqttMessage = mqttMessages.get(position);

        if (position == 0)
            holder.headingText.setVisibility(View.VISIBLE);
        else holder.headingText.setVisibility(View.GONE);

        String patientName = mqttMessage.getSenderName();

        String salutation = "";
        if (mqttMessage.getSalutation() != 0)
            salutation = SALUTATION[mqttMessage.getSalutation() - 1];
        else salutation = "";
        
        holder.doctorName.setText(salutation + CommonMethods.toCamelCase(patientName));//sandeep

        String timeT = CommonMethods.getDayFromDateTime(mqttMessage.getMsgTime(), RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE_PATTERN.DD_MM_YYYY_SLASH, RescribeConstants.DATE_PATTERN.hh_mm_a);
        holder.timeText.setText(timeT);

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

        String lowerCaseSearchString = searchString.toLowerCase();
        String loweCaseMsg = mqttMessage.getMsg().toLowerCase();

        int startIndex = loweCaseMsg.indexOf(lowerCaseSearchString);

        SpannableString spannableStringSearch = new SpannableString(mqttMessage.getMsg());
        spannableStringSearch.setSpan(new ForegroundColorSpan(
                        ContextCompat.getColor(mContext, R.color.tagColor)), startIndex
                , startIndex + searchString.length(),
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        holder.messageText.setText(spannableStringSearch);

        if (mqttMessage.getSender().equals(PATIENT))
            holder.senderTickImageView.setVisibility(View.GONE);
        else {
            switch (mqttMessage.getMsgStatus()) {
                case REACHED:
                    holder.senderTickImageView.setImageResource(R.drawable.ic_reached);
                    break;
                case SEEN:
                    holder.senderTickImageView.setImageResource(R.drawable.ic_seen);
                    break;
                case SENT:
                    holder.senderTickImageView.setImageResource(R.drawable.ic_sent);
                    break;
                case PENDING:
                    holder.senderTickImageView.setImageResource(R.drawable.ic_time);
                    break;
            }
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.setAction(REPLY_ACTION);
                intent.putExtra(MESSAGE_LIST, mqttMessage);
                intent.putExtra(SEARCHED_TEXT, searchString);
                intent.putExtra(IS_CALL_FROM_MY_PATIENTS, true);
                ((Activity) mContext).startActivityForResult(intent, Activity.RESULT_OK);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mqttMessages.size();
    }

}
