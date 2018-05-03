package com.rescribe.doctor.adapters.my_patients;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rescribe.doctor.R;
import com.rescribe.doctor.model.my_appointments.PatientList;
import com.rescribe.doctor.model.patient.template_sms.request_send_sms.PatientInfoList;
import com.rescribe.doctor.ui.customesViews.CustomTextView;
import com.rescribe.doctor.ui.fragments.patient.my_patient.SendSmsPatientActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 22/2/18.
 */

public class SmsRecepientPatientListAdapter extends RecyclerView.Adapter<SmsRecepientPatientListAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<PatientInfoList> mPatientInfoList;
    private OnCardViewClickListener mOnCardViewClickListener;

    public SmsRecepientPatientListAdapter(Context mContext, ArrayList<PatientInfoList> mPatientInfoList, OnCardViewClickListener mOnCardViewClickListener) {
        this.mPatientInfoList = mPatientInfoList;
        this.mContext = mContext;
        this.mOnCardViewClickListener = mOnCardViewClickListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sms_recipent_item_row, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        final PatientInfoList mPatientInfoListObject = mPatientInfoList.get(position);

        holder.patientNameTextView.setText(mPatientInfoListObject.getPatientName());
        Drawable leftDrawable = AppCompatResources.getDrawable(mContext, R.drawable.ic_highlight_off);
        holder.patientNameTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, leftDrawable, null);

        holder.recepientLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPatientInfoList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mPatientInfoList.size());
                mOnCardViewClickListener.onPhoneNumberClick(mPatientInfoListObject);

            }
        });


    }

    @Override
    public int getItemCount() {
        if (mPatientInfoList == null)
            return 0;
        return mPatientInfoList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.patientNameTextView)
        CustomTextView patientNameTextView;
        @BindView(R.id.recepientLayout)
        LinearLayout recepientLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnCardViewClickListener {
        void onPhoneNumberClick(PatientInfoList patientList);
    }

    public ArrayList<PatientInfoList> getSmsReciptentList() {

        return mPatientInfoList;
    }

}
