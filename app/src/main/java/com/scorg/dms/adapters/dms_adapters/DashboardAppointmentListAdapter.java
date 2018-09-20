package com.scorg.dms.adapters.dms_adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.util.CommonMethods;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by riteshpandhurkar on 24/2/17.
 */

public class DashboardAppointmentListAdapter extends RecyclerView.Adapter<DashboardAppointmentListAdapter.GroupViewHolder> {

    private static final String TAG = "PatientList";
    private final GradientDrawable buttonBackground;
    private Context _context;
    private DashboardAppointmentListAdapter.OnItemClickListener onItemClickListener;
    private List<AppointmentPatientData> _originalListDataHeader = new ArrayList<>(); // header titles

    // @BindString(R.string.opd)
    private String opd;
    // @BindString(R.string.ipd)
    private String ipd;
    private String uhid;

    public DashboardAppointmentListAdapter(Context context, List<AppointmentPatientData> searchResult,OnItemClickListener onItemClickListener) {
        this._context = context;
        addNewItems(searchResult);
        opd = _context.getString(R.string.opd);
        ipd = _context.getString(R.string.ipd);
        uhid = _context.getString(R.string.uhid);
        this.onItemClickListener = onItemClickListener;

        buttonBackground = new GradientDrawable();
        buttonBackground.setShape(GradientDrawable.RECTANGLE);
        buttonBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackground.setCornerRadius(_context.getResources().getDimension(R.dimen.dp5));
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dashboard_appointment_list, parent, false);

        return new GroupViewHolder(itemView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(GroupViewHolder groupViewHolder, final int position) {

        groupViewHolder.episodeList.setBackground(buttonBackground);
        groupViewHolder.userName.setTextColor(Color.parseColor(DMSApplication.COLOR_ACCENT));

        final AppointmentPatientData groupHeader = _originalListDataHeader.get(position);

        groupViewHolder.userName.setText(groupHeader.getPatientName().trim());
        groupViewHolder.patientId.setText(uhid + " - " + groupHeader.getPatientId().trim());

        //-------------
        TextDrawable textDrawable = CommonMethods.getTextDrawable(groupViewHolder.patientImageView.getContext(), groupHeader.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        //  requestOptions.override(groupViewHolder.patientImageView.getResources().getDimensionPixelSize(R.dimen.dp67));
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(groupViewHolder.patientImageView.getContext())
                .load(groupHeader.getPatientImageUrl())
                .apply(requestOptions)
                .into(groupViewHolder.patientImageView);

        groupViewHolder.episodeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("getPatientId","--"+groupHeader.getPatientId());
                SearchResult searchResult =new SearchResult();
                searchResult.setPatientName(groupHeader.getPatientName());
                searchResult.setPatientId(groupHeader.getPatientId());
                searchResult.setPatientAddress(groupHeader.getPatAddress());
                searchResult.setPatientImageURL(groupHeader.getPatientImageUrl());
                onItemClickListener.onClickedOfEpisodeListButton(searchResult);
            }
        });

    }

    @Override
    public int getItemCount() {
        return _originalListDataHeader.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.patientId)
        TextView patientId;
        @BindView(R.id.patientImageView)
        ImageView patientImageView;
        @BindView(R.id.episodeList)
        TextView episodeList;

        GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public void addNewItems(List<AppointmentPatientData> searchResult) {
        this._originalListDataHeader.addAll(searchResult);
    }


    public interface OnItemClickListener {

        void onClickedOfEpisodeListButton(SearchResult groupHeader);
    }
}