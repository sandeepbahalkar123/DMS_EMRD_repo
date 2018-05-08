package com.scorg.dms.adapters.my_appointments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.bottom_menus.BottomMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 2/2/18.
 */

public class BottomMenuAppointmentAdapter extends RecyclerView.Adapter<BottomMenuAppointmentAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<BottomMenu> mBottomMenuList;
    private OnMenuBottomItemClickListener mOnMenuBottomItemClickListener;
    private boolean isAllMenuRequired;


    public BottomMenuAppointmentAdapter(Context mContext, OnMenuBottomItemClickListener mOnMenuBottomItemClickListener, ArrayList<BottomMenu> mBottomMenuList, boolean isAllMenuRequired, String completeOpd) {
        this.mContext = mContext;
        this.mOnMenuBottomItemClickListener = mOnMenuBottomItemClickListener;
        this.mBottomMenuList = mBottomMenuList;
        this.isAllMenuRequired = isAllMenuRequired;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_menu_appointment_item_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {

        final BottomMenu mBottomMenu = mBottomMenuList.get(position);
        //TODO : NEED TO IMPLEMENT
        holder.bottomMenuName.setText(mBottomMenu.getMenuName());
        if (!isAllMenuRequired) {
            if (mBottomMenu.getMenuName().equals(mContext.getString(R.string.send_sms))) {
                holder.menuBottomLayout.setVisibility(View.GONE);
            } else {
                holder.menuBottomLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.menuBottomLayout.setVisibility(View.VISIBLE);
        }

        if (mBottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.select_all))) {
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.select_all));
        } else if (mBottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.send_sms))) {
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.send_sms));
        } /*else if (mBottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.send_mail))) {
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.send_email));
        }*/ else if (mBottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.waiting_list))) {
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.add_waiting_list));
        }
        holder.menuBottomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBottomMenu.isSelected()) {
                    mBottomMenu.setSelected(true);
                    holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.tagColor));
                    holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.tagColor), android.graphics.PorterDuff.Mode.MULTIPLY);
                    notifyDataSetChanged();
                } else {
                    mBottomMenu.setSelected(false);
                    holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.grey));
                    holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
                    notifyDataSetChanged();
                }
                mOnMenuBottomItemClickListener.setClickOnMenuItem(position, mBottomMenu);

            }
        });

        if (mBottomMenu.isSelected()) {
            holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.tagColor), android.graphics.PorterDuff.Mode.MULTIPLY);
            holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.tagColor));
        } else {
            holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.grey));
            holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.grey), android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        //  holder.timeSlot.setText(doctorObject.ge);

    }

    @Override
    public int getItemCount() {
        return mBottomMenuList.size();
    }

    public ArrayList<BottomMenu> getList() {
        return mBottomMenuList;
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.spaceView)
        View spaceView;
        @BindView(R.id.menuBottomIcon)
        AppCompatImageView menuBottomIcon;
        @BindView(R.id.showCountTextView)
        TextView showCountTextView;
        @BindView(R.id.bottomMenuName)
        TextView bottomMenuName;
        @BindView(R.id.bottomMenuTab)
        TextView bottomMenuTab;
        @BindView(R.id.menuBottomLayout)
        LinearLayout menuBottomLayout;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnMenuBottomItemClickListener {
        void setClickOnMenuItem(int position, BottomMenu bottomMenu);
    }


}
