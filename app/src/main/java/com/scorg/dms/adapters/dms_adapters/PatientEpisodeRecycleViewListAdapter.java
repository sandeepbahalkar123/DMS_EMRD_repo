package com.scorg.dms.adapters.dms_adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.ViewRights;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.PatientEpisodeFileData;
import com.scorg.dms.singleton.DMSApplication;
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

    private static final String TAG = "PatientListActivity";
    private Context _context;
    private OnEpisodeClickListener onPatientListener;

    private List<PatientEpisodeFileData> _originalListDataHeader = new ArrayList<>(); // header titles

    // @BindString(R.string.opd)
    private String opd;
    private ViewRights viewRights;
    boolean isMainPrimary;

    public PatientEpisodeRecycleViewListAdapter(Context context, List<PatientEpisodeFileData> searchResult, ViewRights viewRights) {
        this._context = context;
        addNewItems(searchResult, isMainPrimary);
        opd = _context.getString(R.string.opd);
        // @BindString(R.string.ipd)
        String ipd = _context.getString(R.string.ipd);
        this.viewRights = viewRights;

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
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setShape(GradientDrawable.RECTANGLE);
        buttonBackground.setColor(Color.parseColor(DMSApplication.COLOR_ACCENT));
        buttonBackground.setCornerRadius(_context.getResources().getDimension(R.dimen.dp5));
        final PatientEpisodeFileData childElement = _originalListDataHeader.get(position);
        childViewHolder.ipdValue.setTextColor(Color.parseColor(DMSApplication.COLOR_PRIMARY));
        childViewHolder.ipdDischargeDateValue.setVisibility(View.GONE);
        childViewHolder.ipdDischargeDate.setVisibility(View.GONE);
        childViewHolder.ipd.setBackground(buttonBackground);
        childViewHolder.labelDoctorName.setText(DMSApplication.LABEL_DOCTOR_NAME + ":");
        //---
        if (opd.equalsIgnoreCase(childElement.getFileType())) {
            // Label

            childViewHolder.ipdAdmissionDate.setText(_context.getString(R.string.visit_date));

            // Label End

            childViewHolder.ipd.setText(opd);
            childViewHolder.ipdValue.setText(childElement.getFileTypeRefId());

            String s = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE);
            childViewHolder.ipdAdmissionDateValue.setText(s);

        } else {

            // Label

            childViewHolder.ipdAdmissionDate.setText(_context.getString(R.string.admission_date));
            childViewHolder.ipdDischargeDate.setText(_context.getString(R.string.discharge_date));

            // Label End

            childViewHolder.ipd.setText(childElement.getFileType().toUpperCase());
            childViewHolder.ipdValue.setText(String.valueOf(childElement.getFileTypeRefId()));

            String date = CommonMethods.formatDateTime(childElement.getAdmissionDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE);
            childViewHolder.ipdAdmissionDateValue.setText(date);

            if (childElement.getDischargeDate() != null) {
                date = CommonMethods.formatDateTime(childElement.getDischargeDate(), DMSConstants.DATE_PATTERN.DD_MMM_YYYY, DMSConstants.DATE_PATTERN.DD_MM_YYYY, DMSConstants.DATE);
                childViewHolder.ipdDischargeDateValue.setText(date);
                childViewHolder.ipdDischargeDateValue.setVisibility(View.VISIBLE);
                childViewHolder.ipdDischargeDate.setVisibility(View.VISIBLE);
            }

        }


        childViewHolder.doctorName.setText(childElement.getDoctorName());

        //--------------------


      //  boolean isShowEye = viewHideEyeIconEpisode(viewRights, childElement.IsView(), childElement.IsPrimary(), childElement.getFileType(), isMainPrimary);

        if (childElement.isFileAcessible()) {
            childViewHolder.imageViewRights.setVisibility(View.GONE);
            childViewHolder.rowLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPatientListener.onEpisodeListItemClick(childElement);
                    // onPatientListener.showRaiseRequestBtn(false);
                }
            });
        } else {

            childViewHolder.imageViewRights.setVisibility(View.VISIBLE);
            onPatientListener.showRaiseRequestBtn(true);
        }

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
        @BindView(R.id.doctorName)
        TextView doctorName;
        @BindView(R.id.labelDoctorName)
        TextView labelDoctorName;
        @BindView(R.id.imageViewRights)
        ImageView imageViewRights;

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

        void showRaiseRequestBtn(boolean isShow);
    }

    public void removeAll() {
        this._originalListDataHeader.clear();
    }

    public void addNewItems(List<PatientEpisodeFileData> searchResult, boolean isMainPrimary) {
        this._originalListDataHeader.addAll(searchResult);
        this.isMainPrimary = isMainPrimary;
    }


    public boolean viewHideEyeIconEpisode(ViewRights viewRights, boolean isView, boolean isPrimary, String fileType, boolean isMainPrimary) {
        if (viewRights.getIsAllFileAccessible()) {
            return false;  //Icon do not show
        }

        if (viewRights.isViewAppointmentPatient()) {
            return false; //Icon do not show
        }

        if (viewRights.getIsRequestForAll() && isView) {
            return false; //Icon do not show
        }

        if ((viewRights.getAllowOnlyPrimaryFiles() && isPrimary) || (viewRights.getAllowOnlyPrimaryFiles() && isView)) {
            return false; //Icon do not show
        }

        if (isMainPrimary) {

            if (viewRights.getIsOneFileIsPrimary() && (viewRights.getPrimaryFileTypeSetting().contains(fileType)) && !isPrimary) {
                return true; // Icon will show
            }else {
                return false;//Icon do not show
            }

        }

        return true; /// // Icon will show
    }

}