package com.scorg.dms.adapters.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorg.dms.R;
import com.scorg.dms.model.login.ClinicList;
import com.scorg.dms.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ListViewHolder> {
    private final ArrayList<ClinicList> locationList;
    private final ItemListener itemListener;

    public interface ItemListener {
        void onClinicClick(ClinicList clinicList);
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.locationText)
        CustomTextView locationText;
        @BindView(R.id.clinicNameText)
        CustomTextView clinicNameText;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public LocationAdapter(ArrayList<ClinicList> locationList, Context mContext) {
        this.locationList = locationList;

        try {
            this.itemListener = ((LocationAdapter.ItemListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement LocationAdapter.ItemListener.");
        }
    }

    @Override
    public LocationAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_row_item, parent, false);

        return new LocationAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LocationAdapter.ListViewHolder holder, int position) {
        final ClinicList clinic = locationList.get(position);
        holder.locationText.setText(clinic.getClinicAddress());
        holder.clinicNameText.setText(clinic.getClinicName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClinicClick(clinic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}
