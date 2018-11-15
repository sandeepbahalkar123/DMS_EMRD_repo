package com.scorg.dms.adapters.dms_adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scorg.dms.R;
import com.scorg.dms.model.dms_models.responsemodel.episode_list.FileTypeList;

import java.util.ArrayList;


public class RaiseRequestFileNameAdapter extends RecyclerView.Adapter<RaiseRequestFileNameAdapter.MyViewHolder> {

    private ArrayList<FileTypeList> typeListArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView textFileName;
        public LinearLayout layoutFileType;
        public CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);
            textFileName = (TextView) view.findViewById(R.id.textFileName);
            layoutFileType = (LinearLayout) view.findViewById(R.id.layoutFileType);
            checkBox = (CheckBox) view.findViewById(R.id.checkboxFile);
            // genre = (TextView) view.findViewById(R.id.genre);

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