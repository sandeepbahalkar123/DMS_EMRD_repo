package com.scorg.dms.adapters.dms_adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.interfaces.ErrorDialogCallback;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;
import com.scorg.dms.singleton.DMSApplication;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.BOOKED_STATUS;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.CANCEL_STATUS;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.COMPLETED_STATUS;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.CONFIRM_STATUS;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.NO_SHOW;
import static com.scorg.dms.util.DMSConstants.APPOINTMENT_STATUS.OTHER;

/**
 * Created by riteshpandhurkar on 24/2/17.
 */

public class DashboardAppointmentListAdapter extends RecyclerView.Adapter<DashboardAppointmentListAdapter.GroupViewHolder> {

    private static final String TAG = "PatientListActivity";
    private final GradientDrawable buttonBackground;
    private Context _context;
    private DashboardAppointmentListAdapter.OnItemClickListener onItemClickListener;
    private List<AppointmentPatientData> _originalListDataHeader = new ArrayList<>(); // header titles

    private String uhid;

    public DashboardAppointmentListAdapter(Context context, List<AppointmentPatientData> searchResult, OnItemClickListener onItemClickListener) {
        this._context = context;
        addNewItems(searchResult);
        uhid = DMSApplication.LABEL_UHID;
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

        groupViewHolder.btnDone.setBackground(buttonBackground);
        groupViewHolder.userName.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));

        final AppointmentPatientData groupHeader = _originalListDataHeader.get(position);

        groupViewHolder.userName.setText(groupHeader.getPatientName().trim());
        groupViewHolder.patientId.setTextColor(Color.parseColor(DMSApplication.COLOR_APPOINTMENT_TEXT));
        groupViewHolder.patientId.setText(uhid + " - " + groupHeader.getPatientId().trim());

        //-------------
        TextDrawable textDrawable = CommonMethods.getTextDrawable(groupViewHolder.patientImageView.getContext(), groupHeader.getPatientName());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();
        requestOptions.placeholder(textDrawable);
        requestOptions.error(textDrawable);

        Glide.with(groupViewHolder.patientImageView.getContext())
                .load(groupHeader.getPatientImageUrl())
                .apply(requestOptions)
                .into(groupViewHolder.patientImageView);

        groupViewHolder.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("getPatientId", "--" + groupHeader.getPatientId());
                SearchResult searchResult = new SearchResult();
                searchResult.setPatientName(groupHeader.getPatientName());
                searchResult.setPatientId(groupHeader.getPatientId());
                searchResult.setPatientAddress(groupHeader.getPatAddress());
                searchResult.setPatientImageURL(groupHeader.getPatientImageUrl());
                onItemClickListener.onClickedOfEpisodeListButton(searchResult);
            }
        });

        String appDate = groupHeader.getAppDate();
        if (appDate != null) {
            groupViewHolder.appointmentTime.setVisibility(View.VISIBLE);
            groupViewHolder.appointmentTime.setText(CommonMethods.formatDateTime(appDate, DMSConstants.DATE_PATTERN.hh_mm_a, DMSConstants.DATE_PATTERN.UTC_PATTERN, DMSConstants.TIME).toLowerCase());
        } else {
            groupViewHolder.appointmentTime.setVisibility(View.INVISIBLE);
        }


        if (groupHeader.getAppointmentStatus().contains(BOOKED_STATUS)) {
            groupViewHolder.appointmentStatus.setTextColor(ContextCompat.getColor(_context, R.color.book_color));
            groupViewHolder.appointmentStatus.setText(_context.getString(R.string.booked));
        } else if (groupHeader.getAppointmentStatus().contains(COMPLETED_STATUS)) {
            groupViewHolder.appointmentStatus.setText(_context.getString(R.string.capitalcompleted));
            groupViewHolder.appointmentStatus.setTextColor(ContextCompat.getColor(_context, R.color.complete_color));
        } else if (groupHeader.getAppointmentStatus().contains(CONFIRM_STATUS)) {
            groupViewHolder.appointmentStatus.setText(groupHeader.getAppointmentStatus());
            groupViewHolder.appointmentStatus.setTextColor(ContextCompat.getColor(_context, R.color.confirm_color));
        } else if (groupHeader.getAppointmentStatus().contains(CANCEL_STATUS)) {
            groupViewHolder.appointmentStatus.setText(groupHeader.getAppointmentStatus());
            groupViewHolder.appointmentStatus.setTextColor(ContextCompat.getColor(_context, R.color.cancel_color));
        } else if (groupHeader.getAppointmentStatus().equals(NO_SHOW)) {
            groupViewHolder.appointmentStatus.setText(groupHeader.getAppointmentStatus());
            groupViewHolder.appointmentStatus.setTextColor(ContextCompat.getColor(_context, R.color.no_show_color));
        } else if (groupHeader.getAppointmentStatus().equals(OTHER)) {
            groupViewHolder.appointmentStatus.setText(groupHeader.getAppointmentStatus());
            groupViewHolder.appointmentStatus.setTextColor(ContextCompat.getColor(_context, R.color.other_color));
        }


        String consultationType = groupHeader.getConsultationType();
        if (!consultationType.equalsIgnoreCase("")) {
            groupViewHolder.appointmentConsultationType.setText(consultationType);
        }

        groupViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (groupHeader.isArchived()) {
                    onItemClickListener.onPatientListItemClick(groupHeader);
                } else {
                    CommonMethods.showErrorDialog(_context.getString(R.string.patient_not_having_record), _context, false, new ErrorDialogCallback() {
                        @Override
                        public void ok() {
                        }
                        @Override
                        public void retry() {

                        }
                    });
                }

            }
        });
        groupViewHolder.layoutAppointmentCode.setVisibility(View.VISIBLE);
//        if (groupHeader.getAppointmentCode()!=0) {
//            groupViewHolder.layoutAppointmentCode.setVisibility(View.VISIBLE);
//            groupViewHolder.patientAppointmentsCode.setText("" + groupHeader.getAppointmentCode());
//       }
        if(DMSApplication.APPOINTMENT_STATUS_URL.equalsIgnoreCase(""))
            groupViewHolder.btnDone.setVisibility(View.INVISIBLE);

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
        @BindView(R.id.btnDone)
        TextView btnDone;

        @BindView(R.id.appointmentStatus)
        TextView appointmentStatus;

        @BindView(R.id.appointmentConsultationType)
        TextView appointmentConsultationType;

        @BindView(R.id.appointmentTime)
        TextView appointmentTime;

        @BindView(R.id.patientAppointmentsCode)
        TextView patientAppointmentsCode;

        @BindView(R.id.cardView)
        LinearLayout cardView;

        @BindView(R.id.layoutAppointmentCode)
        LinearLayout layoutAppointmentCode;


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

        void onPatientListItemClick(AppointmentPatientData appointmentPatientData);

    }
}