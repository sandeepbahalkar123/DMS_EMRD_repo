package com.scorg.dms.adapters.drawer_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.scorg.dms.R;
import com.scorg.dms.model.my_appointments.FilterSortByHighLowList;
import com.scorg.dms.ui.customesViews.CustomTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 27/10/17.
 */

public class SortByPriceFilterAdapter extends RecyclerView.Adapter<SortByPriceFilterAdapter.ListViewHolder> {

   /* private String ratings = " Ratings ";
    private String fees = " Fees ";
    private String asc = "asc";
    private String desc = "desc";
    private String selectedSortedOptionLabel = "";*/
    private ArrayList<FilterSortByHighLowList> mFilterSortByHighLowLists;
    private Context mContext;
    private onSortByAmountMenuClicked mOnSortByAmountMenuClicked;

    public SortByPriceFilterAdapter(Context mContext, ArrayList<FilterSortByHighLowList> mFilterSortByHighLowLists, onSortByAmountMenuClicked mOnSortByAmountMenuClicked) {
        this.mContext = mContext;
        this.mFilterSortByHighLowLists = mFilterSortByHighLowLists;
        this.mOnSortByAmountMenuClicked = mOnSortByAmountMenuClicked;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sort_item_row_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        final FilterSortByHighLowList filterSortByHighLowListObj = mFilterSortByHighLowLists.get(position);
        holder.sortName.setText(filterSortByHighLowListObj.getAmountHighOrLow());


        holder.recyclerViewClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String receivedTitle = "" + v.getTag();
                for (FilterSortByHighLowList object : mFilterSortByHighLowLists) {
                    object.setSelected(false);
                }
                if (filterSortByHighLowListObj.isSelected()) {
                    filterSortByHighLowListObj.setSelected(false);

                } else {
                    filterSortByHighLowListObj.setSelected(true);
                }
                notifyDataSetChanged();
                mOnSortByAmountMenuClicked.onClickOfSortMenu(filterSortByHighLowListObj,position);

               /* if (receivedTitle.equalsIgnoreCase(selectedSortedOptionLabel)) {
                    seglectedSortedOptionLabel = "";
                    holder.serviceIcon.setVisibility(View.GONE);
                } else {
                    selectedSortedOptionLabel = receivedTitle;
                    holder.serviceIcon.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                }
*/
            }
        });

        if (filterSortByHighLowListObj.isSelected()) {
            holder.serviceIcon.setVisibility(View.VISIBLE);
        } else {
            holder.serviceIcon.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mFilterSortByHighLowLists.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sortName)
        CustomTextView sortName;
        @BindView(R.id.serviceIcon)
        ImageView serviceIcon;
        @BindView(R.id.recyclerViewClick)
        LinearLayout recyclerViewClick;

        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public ArrayList<FilterSortByHighLowList> getAmountSortList(){
        return mFilterSortByHighLowLists;
    }

    public interface onSortByAmountMenuClicked {

        void onClickOfSortMenu(FilterSortByHighLowList filterSortByHighLowObject, int groupPosition);


    }

}