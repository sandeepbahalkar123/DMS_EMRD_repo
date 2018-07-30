package com.scorg.dms.adapters.waiting_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scorg.dms.R;
import com.scorg.dms.model.waiting_list.response_add_to_waiting_list.AddToWaitingResponse;
import com.scorg.dms.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 16/3/18.
 */

public class ShowWaitingStatusAdapter extends RecyclerView.Adapter<ShowWaitingStatusAdapter.ListViewHolder> {

    private ArrayList<AddToWaitingResponse> mWaitingClinicList = new ArrayList<>();

    public ShowWaitingStatusAdapter(Context mContext, ArrayList<AddToWaitingResponse> mWaitingClinicList) {
        this.mWaitingClinicList = mWaitingClinicList;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_waiting_status_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {

        //TODO : NEED TO IMPLEMENT
        final AddToWaitingResponse waitingClinicList = mWaitingClinicList.get(position);
        holder.waitingStatusText.setText(waitingClinicList.getLocationDetails()+": "+waitingClinicList.getStatusMessage());
    }

    @Override
    public int getItemCount() {
        return mWaitingClinicList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bulletImageView)
        ImageView bulletImageView;
        @BindView(R.id.waitingStatusText)
        CustomTextView waitingStatusText;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
