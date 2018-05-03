package com.rescribe.doctor.bottom_menus;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rescribe.doctor.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomMenuAdapter extends RecyclerView.Adapter<BottomMenuAdapter.ListViewHolder> {

    private static final String DRAWABLE = "drawable";
    private final Context mContext;
    private OnBottomMenuClickListener mBottomMenuListClickListener;
    private ArrayList<BottomMenu> bottomMenus;
    static int appIconIndex;

    BottomMenuAdapter(Context mContext, ArrayList<BottomMenu> bottomMenus) {
        this.bottomMenus = bottomMenus;
        this.mContext = mContext;

        try {
            this.mBottomMenuListClickListener = ((OnBottomMenuClickListener) mContext);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement onBottomMenuListClickListener.");
        }
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_menu_item, parent, false);

        int widthPixels = Resources.getSystem().getDisplayMetrics().widthPixels;
        int width = (widthPixels * 20) / 100;
        ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
        layoutParams.width = width;

        itemView.setLayoutParams(layoutParams);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, final int position) {
        final BottomMenu bottomMenu = bottomMenus.get(position);
        holder.bottomMenuName.setText(bottomMenu.getMenuName());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomMenuListClickListener.onBottomMenuClick(bottomMenu);
            }
        });
        if(bottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.home))){
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.home));

        }else if(bottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.profile))){
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.profile_icon));
        }else if(bottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.app_logo))){
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.logo));
        }else if(bottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.support))){
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.support));
        }else if(bottomMenu.getMenuName().equalsIgnoreCase(mContext.getString(R.string.settings))){
            holder.menuBottomIcon.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.settings));
        }



//for app logo
        if (bottomMenu.isAppIcon()) {
            holder.bottomMenuName.setVisibility(View.GONE);
            holder.bottomMenuTab.setVisibility(View.GONE);

            appIconIndex = position;
            if (bottomMenu.getNotificationCount() > 0) {
                holder.showCountTextView.setText(String.valueOf(bottomMenu.getNotificationCount()));
                holder.showCountTextView.setVisibility(View.VISIBLE);
            } else
                holder.showCountTextView.setVisibility(View.GONE);

        } else {

            holder.showCountTextView.setVisibility(View.GONE);
            if (bottomMenu.isSelected()) {
                holder.bottomMenuTab.setVisibility(View.VISIBLE);
                holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.tagColor));
                holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.tagColor), PorterDuff.Mode.MULTIPLY);
            } else {
                holder.bottomMenuTab.setVisibility(View.INVISIBLE);
                holder.bottomMenuName.setTextColor(holder.bottomMenuName.getContext().getResources().getColor(R.color.grey));
                holder.menuBottomIcon.setColorFilter(ContextCompat.getColor(holder.menuBottomIcon.getContext(), R.color.grey), PorterDuff.Mode.MULTIPLY);
            }
        }

    }

    @Override
    public int getItemCount() {
        return bottomMenus.size();
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

    public interface OnBottomMenuClickListener {

        void onBottomMenuClick(BottomMenu bottomMenu);

        void onProfileImageClick();
    }

}
