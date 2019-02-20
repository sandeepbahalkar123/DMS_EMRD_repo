package com.scorg.dms.adapters.dms_adapters;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.FileTypeList;
import com.scorg.dms.singleton.DMSApplication;

import java.util.ArrayList;


public class RaiseRequestFileNameAdapter extends RecyclerView.Adapter<RaiseRequestFileNameAdapter.MyViewHolder> {

    private ArrayList<FileTypeList> typeListArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textFileName;
        public LinearLayout layoutFileType;
        public AppCompatCheckBox checkBox;

        @SuppressLint("RestrictedApi")
        public MyViewHolder(View view) {
            super(view);
            textFileName = (TextView) view.findViewById(R.id.textFileName);
            layoutFileType = (LinearLayout) view.findViewById(R.id.layoutFileType);
            checkBox = (AppCompatCheckBox) view.findViewById(R.id.checkboxFile);
            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_enabled}, // enabled
                    new int[]{-android.R.attr.state_enabled}, // disabled
                    new int[]{-android.R.attr.state_checked}, // unchecked
                    new int[]{android.R.attr.state_pressed} // pressed
            };

            int[] colors = new int[]{
                    Color.parseColor(DMSApplication.COLOR_PRIMARY),
                    Color.parseColor(DMSApplication.COLOR_PRIMARY),
                    Color.parseColor(DMSApplication.COLOR_PRIMARY),
                    Color.parseColor(DMSApplication.COLOR_PRIMARY)
            };
            ColorStateList myList = new ColorStateList(states, colors);
            checkBox.setSupportButtonTintList(myList);

        }
    }


    public RaiseRequestFileNameAdapter(ArrayList<FileTypeList> typeListArrayList) {
        this.typeListArrayList = typeListArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_filename_with_checkbox, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final FileTypeList typeList = typeListArrayList.get(position);
        holder.textFileName.setText(typeList.getFileType());
        if (typeList.isChecked()) {
           typeList.setChecked(false);
        }
        holder.layoutFileType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeList.isChecked()) {
                    typeList.setChecked(false);
                    holder.checkBox.setChecked(false);
                } else {
                    typeList.setChecked(true);
                    holder.checkBox.setChecked(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return typeListArrayList.size();
    }
}