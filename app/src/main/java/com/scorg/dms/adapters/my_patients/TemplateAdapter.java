package com.scorg.dms.adapters.my_patients;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scorg.dms.R;
import com.scorg.dms.model.patient.template_sms.TemplateList;
import com.scorg.dms.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 21/2/18.
 */

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ListViewHolder> {

    private ArrayList<TemplateList> mTemplateLists;
    private OnCardViewClickListener mOnCardViewClickListener;

    public TemplateAdapter(Context mContext, ArrayList<TemplateList> mTemplateLists, OnCardViewClickListener mOnCardViewClickListener) {
        this.mTemplateLists = mTemplateLists;
        this.mOnCardViewClickListener = mOnCardViewClickListener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.template_item_row_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final TemplateList templateListObject = mTemplateLists.get(position);

        holder.templateContent.setText(templateListObject.getTemplateContent());
        holder.templateName.setText(templateListObject.getTemplateName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnCardViewClickListener.onCardViewClick(templateListObject);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mTemplateLists.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.templateName)
        CustomTextView templateName;
        @BindView(R.id.templateContent)
        CustomTextView templateContent;
        @BindView(R.id.cardView)
        CardView cardView;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnCardViewClickListener {
        void onCardViewClick(TemplateList templateList);
    }

}
