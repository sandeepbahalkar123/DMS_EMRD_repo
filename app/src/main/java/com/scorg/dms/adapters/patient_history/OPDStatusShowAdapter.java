package com.scorg.dms.adapters.patient_history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scorg.dms.R;
import com.scorg.dms.ui.customesViews.CustomTextView;
import com.scorg.dms.util.DMSConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by root on 22/6/16.
 */
public class OPDStatusShowAdapter extends RecyclerView.Adapter<OPDStatusShowAdapter.ListViewHolder> {
    private Context mContext;
    private ArrayList<String> mSelectedOption;

    public OPDStatusShowAdapter(Context context, ArrayList<String> spinner_data) {
        this.mContext = context;
        this.mSelectedOption = spinner_data;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mSelectedOption.size();
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_various_opd_status, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {


        String data = mSelectedOption.get(position);

        holder.txt_data.setText(data);

        switch (data.toLowerCase()) {
            case DMSConstants.PATIENT_OPDS_STATUS.OPD_COMPLETED:
                holder.txt_id.setBackgroundResource(R.drawable.opd_completed_circle);
                break;
            case DMSConstants.PATIENT_OPDS_STATUS.OPD_SAVED:
                holder.txt_id.setBackgroundResource(R.drawable.opd_saved_circle);
                break;
            case DMSConstants.PATIENT_OPDS_STATUS.ONLY_ATTACHMENTS:
                holder.txt_id.setBackgroundResource(R.drawable.only_attachments_circle);
                break;
            case DMSConstants.PATIENT_OPDS_STATUS.NO_SHOW:
                holder.txt_id.setBackgroundResource(R.drawable.no_show_circle);
                break;
        }
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.statusImage)
        ImageView txt_id;
        @BindView(R.id.text)
        CustomTextView txt_data;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

}