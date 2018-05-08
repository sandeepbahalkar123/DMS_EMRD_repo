package com.scorg.dms.adapters.my_patients;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.scorg.dms.R;
import com.scorg.dms.model.patient.template_sms.TemplateList;
import com.scorg.dms.model.my_appointments.PatientList;
import com.scorg.dms.ui.customesViews.CustomTextView;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 22/2/18.
 */

public class SmsRecepientListAdapter extends RecyclerView.Adapter<SmsRecepientListAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<PatientList> mPatientInfoList;
    private OnCardViewClickListener mOnCardViewClickListener;

    public SmsRecepientListAdapter(Context mContext, ArrayList<PatientList> mPatientInfoList, OnCardViewClickListener mOnCardViewClickListener) {
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
        final PatientList mPatientInfoListObject = mPatientInfoList.get(position);

        holder.patientNameTextView.setText(mPatientInfoListObject.getPatientName());

        // set drawable

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
        void onPhoneNumberClick(PatientList patientList);
    }

    public ArrayList<PatientList> getSmsReciptentList() {

        return mPatientInfoList;
    }

}
