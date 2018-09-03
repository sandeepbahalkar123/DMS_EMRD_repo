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

package com.scorg.dms.adapters.pending_approvals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.scorg.dms.model.pending_approval_list.RequestedArchivedDetailList;
import com.scorg.dms.model.waiting_list.WaitingPatientData;
import com.scorg.dms.util.CommonMethods;

import java.util.ArrayList;


/**
 * Created by jeetal on 31/1/18.
 * <p>
 * MESSAGE ==> Explicitly disable long press,swipe and click listener. And do needful to make this work.
 */
public class RequestListAdapter
        extends RecyclerView.Adapter<RequestListAdapter.MyViewHolder> {
    private static final String TAG = "MyDSItemAdapter";
    private OnItemClickListener onItemClickListener;
    private Context mContext;
    private ArrayList<RequestedArchivedDetailList> mRequestedArchivedDetailLists;
    private boolean isPending;


    public RequestListAdapter(Context context, ArrayList<RequestedArchivedDetailList> requestedArchivedDetailLists, OnItemClickListener onItemClickListener, boolean isPending) {
        this.mContext = context;
        this.mRequestedArchivedDetailLists = requestedArchivedDetailLists;
        this.onItemClickListener = onItemClickListener;
        this.isPending=isPending;
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
        TextView textRequestId;
        TextView textCurrentStatus;
        LinearLayout mAppointmentDetailsLinearLayout;
        LinearLayout btn_cancel_request;
        TextView mAppointmentLabelTextView;
        TextView mAppointmentTimeTextView;
        TextView textRequester;
        View mSeparatorView;
        TextView textProcessBy;
        TextView textMyElapsedTime;
        TextView textCurrentStage;


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
            textRequestId = v.findViewById(R.id.textRequestId);
            textCurrentStatus = v.findViewById(R.id.textCurrentStatus);
            mAppointmentDetailsLinearLayout = v.findViewById(R.id.appointmentDetailsLinearLayout);
            mAppointmentLabelTextView = v.findViewById(R.id.appointmentLabelTextView);
            mAppointmentTimeTextView = v.findViewById(R.id.appointmentTimeTextView);
            textRequester = v.findViewById(R.id.textRequester);
            mSeparatorView = v.findViewById(R.id.separatorView);
            textProcessBy = v.findViewById(R.id.textProcessBy);
            textMyElapsedTime = v.findViewById(R.id.textMyElapsedTime);
            textCurrentStage = v.findViewById(R.id.textCurrentStage);
            btn_cancel_request = v.findViewById(R.id.btn_cancel_request);
        }

        @Override
        public View getSwipeableContainerView() {
            return mContainer;
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mRequestedArchivedDetailLists.get(position).getPatientID());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.list_item_requested_archive, parent, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final RequestedArchivedDetailList item = mRequestedArchivedDetailLists.get(position);

        //-------------
        String dataToShowInPatientID = String.valueOf(item.getPatientID());
        holder.mPatientIdTextView.setText(holder.mPatientIdTextView.getResources().getString(R.string.uhid) + " " + dataToShowInPatientID);
        //-------------
        String name = CommonMethods.toCamelCase(item.getPatientName());
        if (item.getSalutation() != null) {
            name = item.getSalutation() + " " + name;
        }
        holder.mPatientNameTextView.setText(name);


        holder.textMyElapsedTime.setText(item.getMyElapsedTime());
        //-------------
        holder.textCurrentStatus.setText(" " + CommonMethods.toCamelCase(item.getCurrentStatus()));
        holder.textProcessBy.setText(item.getStageChangeBy());
        holder.textRequester.setText(item.getRequesterName());
        holder.textRequestId.setText(""+item.getRequestID());
        holder.textCurrentStage.setText(item.getCurrentStage());

        if(isPending)
            holder.btn_cancel_request.setVisibility(View.VISIBLE);

        holder.btn_cancel_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResult searchResult =new SearchResult();
                onItemClickListener.onClickedOfEpisodeListButton(searchResult);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRequestedArchivedDetailLists.size();
    }


    public interface OnItemClickListener {
        public void onItemClick(WaitingPatientData clickItem);

        public void onPhoneNoClick(long phoneNumber);
        void onClickedOfEpisodeListButton(SearchResult groupHeader);
    }
}
