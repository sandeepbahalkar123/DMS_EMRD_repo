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

package com.scorg.dms.adapters.waiting_list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableSwipeableItemViewHolder;
import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.waiting_list.WaitingPatientData;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;


/**
 * Created by jeetal on 31/1/18.
 * <p>
 * MESSAGE ==> Explicitly disable long press,swipe and click listener. And do needful to make this work.
 */
public class WaitingListAdapter
        extends RecyclerView.Adapter<WaitingListAdapter.MyViewHolder> {
    private static final String TAG = "MyDSItemAdapter";
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private ArrayList<WaitingPatientData> mWaitingDataList;


    public WaitingListAdapter(Context context, ArrayList<WaitingPatientData> waitingDataList, OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.mWaitingDataList = waitingDataList;
        this.onItemClickListener = onItemClickListener;
    }

    public static class MyViewHolder extends AbstractDraggableSwipeableItemViewHolder {
        FrameLayout mContainer;
        ImageView mDragHandle;

        LinearLayout mBehindViews;
        ImageButton deleteButton;

        LinearLayout idAndDetailsLayout;
        ImageView mBluelineImageView;
        TextView mPatientIdTextView;
        TextView mAppointmentTime;
        ImageView mPatientImageView;
        TextView mPatientNameTextView;
        LinearLayout mPatientDetailsLinearLayout;
        TextView mStatusTextView;
        TextView mTypeStatus;
        LinearLayout mAppointmentDetailsLinearLayout;
        LinearLayout layoutWaitingEpisode;
        TextView mAppointmentLabelTextView;
        TextView mAppointmentTimeTextView;
        TextView mPatientPhoneNumber;
        View mSeparatorView;
        TextView mTokenLabelTextView;
        TextView mTokenNumber;


        MyViewHolder(View v) {
            super(v);
            mContainer = v.findViewById(R.id.container);
            mDragHandle = v.findViewById(R.id.dragHandle);

            mBehindViews = v.findViewById(R.id.behind_views);
            deleteButton = v.findViewById(R.id.deleteButton);

            idAndDetailsLayout = v.findViewById(R.id.idAndDetailsLayout);
            mBluelineImageView = v.findViewById(R.id.bluelineImageView);
            mPatientIdTextView = v.findViewById(R.id.patientIdTextView);
            mAppointmentTime = v.findViewById(R.id.appointmentTime);
            mPatientImageView = v.findViewById(R.id.patientImageView);
            mPatientNameTextView = v.findViewById(R.id.patientNameTextView);
            mPatientDetailsLinearLayout = v.findViewById(R.id.patientDetailsLinearLayout);
            mStatusTextView = v.findViewById(R.id.statusTextView);
            mTypeStatus = v.findViewById(R.id.typeStatus);
            mAppointmentDetailsLinearLayout = v.findViewById(R.id.appointmentDetailsLinearLayout);
            mAppointmentLabelTextView = v.findViewById(R.id.appointmentLabelTextView);
            mAppointmentTimeTextView = v.findViewById(R.id.appointmentTimeTextView);
            mPatientPhoneNumber = v.findViewById(R.id.patientPhoneNumber);
            mSeparatorView = v.findViewById(R.id.separatorView);
            mTokenLabelTextView = v.findViewById(R.id.tokenLabelTextView);
            mTokenNumber = v.findViewById(R.id.tokenNumber);
            layoutWaitingEpisode = v.findViewById(R.id.layoutWaitingEpisode);
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mWaitingDataList.get(position).getPatientId());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_draggable, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final WaitingPatientData item = mWaitingDataList.get(position);

        //-------------
        String dataToShowInPatientID = String.valueOf(item.getPatientId());
        holder.mPatientIdTextView.setText(holder.mPatientIdTextView.getResources().getString(R.string.uhid) + " " + dataToShowInPatientID);
        //-------------
        String name = CommonMethods.toCamelCase(item.getPatientName());
        if (item.getSalutation() != null) {
            name = item.getSalutation() + " " + name;
        }
        holder.mPatientNameTextView.setText(name);
        //-------------
        if (!item.getAppDate().equals("")) {
            holder.mAppointmentTime.setVisibility(View.VISIBLE);
            String waitingTime = CommonMethods.formatDateTime(item.getAppDate().split("T")[1], DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.HH_mm_ss, DMSConstants.TIME).toLowerCase();
            holder.mAppointmentTime.setText(holder.mPatientIdTextView.getResources().getString(R.string.in_time) + " - " + waitingTime);
        } else {
            holder.mAppointmentTime.setVisibility(View.INVISIBLE);
        }
        //-------------
        if (!item.getAppDate().equals("")) {
            holder.mAppointmentTimeTextView.setVisibility(View.VISIBLE);
            holder.mAppointmentLabelTextView.setVisibility(View.VISIBLE);
            String appointmentScheduleTime = CommonMethods.formatDateTime(item.getAppDate().split("T")[1], DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.HH_mm_ss, DMSConstants.TIME).toLowerCase();
            holder.mAppointmentTimeTextView.setText(appointmentScheduleTime);
        } else {
            holder.mAppointmentTimeTextView.setVisibility(View.INVISIBLE);
            holder.mAppointmentLabelTextView.setVisibility(View.INVISIBLE);
        }
        //-------------
        holder.mPatientPhoneNumber.setText(item.getContactNo());
        //-------------
        holder.mTokenNumber.setVisibility(View.GONE);
        //-------------
        holder.mTypeStatus.setText(" " + item.getWaitingStatus());
        //-------------
        TextDrawable textDrawable = CommonMethods.getTextDrawable(holder.mPatientImageView.getContext(), item.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.override(holder.mPatientImageView.getResources().getDimensionPixelSize(R.dimen.dp67));
        requestOptions.circleCrop();
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(holder.mPatientImageView.getContext())
                .load(item.getPatientImageUrl())
                .apply(requestOptions)
                .into(holder.mPatientImageView);
        //-------------
        ViewTreeObserver vto = holder.mContainer.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = holder.mBehindViews.getLayoutParams();
                layoutParams.height = holder.mContainer.getMeasuredHeight();
                layoutParams.width = holder.mContainer.getMeasuredWidth();
                holder.mBehindViews.setLayoutParams(layoutParams);
            }
        });

        holder.idAndDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitingPatientData item = mWaitingDataList.get(position);

                onItemClickListener.onItemClick(item);
            }
        });
        holder.mPatientPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitingPatientData item = mWaitingDataList.get(position);
                String contactNo = item.getContactNo();
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


        holder.layoutWaitingEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResult searchResult =new SearchResult();
                searchResult.setPatientName(item.getPatientName());
                searchResult.setPatientId(item.getPatientId());
                searchResult.setPatientAddress(item.getPatAddress());
                searchResult.setPatientImageURL(item.getPatientImageUrl());
                onItemClickListener.onClickedOfEpisodeListButton(searchResult);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mWaitingDataList.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(WaitingPatientData clickItem);

        public void onPhoneNoClick(long phoneNumber);
        void onClickedOfEpisodeListButton(SearchResult groupHeader);
    }
}
