package com.scorg.dms.adapters.dms_adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.PatientEpisodeFileData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.PatientFileData;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.util.CommonMethods;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by riteshpandhurkar on 24/2/17.
 */

public class PatientEpisodeRecycleViewListAdapter extends RecyclerView.Adapter<PatientEpisodeRecycleViewListAdapter.GroupViewHolder> {

    private static final String TAG = "PatientList";
    private Context _context;
    private OnEpisodeClickListener onPatientListener;

    private List<PatientEpisodeFileData> _originalListDataHeader = new ArrayList<>(); // header titles

    // @BindString(R.string.opd)
    private String opd;
    // @BindString(R.string.ipd)
    private String ipd;
    private String uhid;

    public PatientEpisodeRecycleViewListAdapter(Context context, List<PatientEpisodeFileData> searchResult) {
        this._context = context;
        addNewItems(searchResult);
        opd = _context.getString(R.string.opd);
        ipd = _context.getString(R.string.ipd);
        uhid = _context.getString(R.string.uhid);

        if (context instanceof OnEpisodeClickListener) {
            onPatientListener = (OnEpisodeClickListener) context;
        } else CommonMethods.Log(TAG, "Implement OnPatientListener in Activity");
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_content, parent, false);

        return new GroupViewHolder(itemView);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(GroupViewHolder childViewHolder, final int position) {

        final PatientEpisodeFileData childElement = _originalListDataHeader.get(position);

        //---
        if (opd.equalsIgnoreCase(childElement.getFileType())) {
            // Label

            childViewHolder.ipdAdmissionDate.setText(_context.getString(R.string.visit_date));

            // Label End

            childViewHolder.ipd.setText(opd);
            childViewHolder.ipdValue.setText(childElement.getFileTypeRefId());

            String s = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY_hh_mm, DMSConstants.DATE);
            childViewHolder.ipdAdmissionDateValue.setText(s);

        } else {

            // Label

            childViewHolder.ipdAdmissionDate.setText(_context.getString(R.string.admission_date));
            childViewHolder.ipdDischargeDate.setText(_context.getString(R.string.discharge_date));

            // Label End

            childViewHolder.ipd.setText(ipd);
            childViewHolder.ipdValue.setText(String.valueOf(childElement.getFileTypeRefId()));

            String date = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE);
            childViewHolder.ipdAdmissionDateValue.setText(date);

            date = CommonMethods.formatDateTime(childElement.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE);

            childViewHolder.ipdDischargeDateValue.setText(date);
        }

        if (CommonMethods.isTablet(_context))
            childViewHolder.ipdCheckBox.setVisibility(View.VISIBLE);
        else
            childViewHolder.ipdCheckBox.setVisibility(View.GONE);


        //--------------------

        childViewHolder.rowLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPatientListener.onEpisodeListItemClick(childElement);
            }
        });

      //  childViewHolder.childCardView.setBackgroundResource(R.drawable.round_background_and_square_side_view);
        childViewHolder.childItemCollapseButton.setVisibility(View.GONE);


        childViewHolder.childItemExpandCollapseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return _originalListDataHeader.size();
    }

    static class GroupViewHolder extends RecyclerView.ViewHolder {
        //---------

        @BindView(R.id.cardView)
        LinearLayout childCardView;
        @BindView(R.id.rowLay)
        LinearLayout rowLay;
        @BindView(R.id.ipd)
        TextView ipd;
        @BindView(R.id.ipdValue)
        TextView ipdValue;
        @BindView(R.id.ipdAdmissionDate)
        TextView ipdAdmissionDate;
        @BindView(R.id.ipdAdmissionDateValue)
        TextView ipdAdmissionDateValue;
        @BindView(R.id.ipdDischargeDate)
        TextView ipdDischargeDate;
        @BindView(R.id.ipdDischargeDateValue)
        TextView ipdDischargeDateValue;
        @BindView(R.id.ipdCheckBox)
        CheckBox ipdCheckBox;
        @BindView(R.id.childItemCollapseButton)
        LinearLayout childItemCollapseButton;
        @BindView(R.id.childItemExpandCollapseButton)
        AppCompatImageButton childItemExpandCollapseButton;

        //@BindView(R.id.divider)
        //View divider;

        GroupViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface OnEpisodeClickListener {
        // void onCompareDialogShow(PatientFileData patientFileData1, PatientFileData patientFileData2, String mCheckedBoxGroupName, String tempName, boolean b);

        void onEpisodeListItemClick(PatientEpisodeFileData groupHeader);

        void smoothScrollToPosition(int previousPosition);
    }

    public void removeAll() {
        this._originalListDataHeader.clear();
    }

    public void addNewItems(List<PatientEpisodeFileData> searchResult) {
        this._originalListDataHeader.addAll(searchResult);
    }

}