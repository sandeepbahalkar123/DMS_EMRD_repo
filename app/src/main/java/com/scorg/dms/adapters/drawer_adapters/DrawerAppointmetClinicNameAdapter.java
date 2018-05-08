package com.scorg.dms.adapters.drawer_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

import com.scorg.dms.R;
import com.scorg.dms.model.my_appointments.ClinicList;
import com.scorg.dms.model.my_appointments.StatusList;
import com.scorg.dms.ui.fragments.my_appointments.DrawerForMyAppointment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 12/2/18.
 */

public class DrawerAppointmetClinicNameAdapter extends RecyclerView.Adapter<DrawerAppointmetClinicNameAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<ClinicList> mClinicList;
    private OnClickOfFilterClinic mOnClickOfFilterClinic;

    public DrawerAppointmetClinicNameAdapter(Context mContext, ArrayList<ClinicList> clinicList, DrawerForMyAppointment drawerForMyAppointment) {
        this.mContext = mContext;
        this.mClinicList = clinicList;
        this.mOnClickOfFilterClinic = drawerForMyAppointment;
    }

    @Override
    public DrawerAppointmetClinicNameAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item_chechbox_layout, parent, false);

        return new DrawerAppointmetClinicNameAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DrawerAppointmetClinicNameAdapter.ListViewHolder holder, final int position) {

        holder.menuName.setText(mClinicList.get(position).getClinicName() + ", " + mClinicList.get(position).getArea() + ", " + mClinicList.get(position).getCity());
        holder.menuName.setChecked(mClinicList.get(position).isSelected());

        if (!holder.menuName.isChecked()) {
            holder.menuName.setChecked(false);
            holder.menuName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unchked, 0);
        } else {
            holder.menuName.setChecked(true);
            holder.menuName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check_box, 0);
        }

        holder.menuName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClinicList.get(position).setSelected(!holder.menuName.isChecked());
                notifyDataSetChanged();
                mOnClickOfFilterClinic.onClickofClinic(mClinicList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mClinicList.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.menuName)
        CheckedTextView menuName;
        View view;

        ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    public interface OnClickOfFilterClinic {
        public void onClickofClinic(ArrayList<ClinicList> mClinicList);

    }

    public ArrayList<ClinicList> getAdapterClinicList() {
        return mClinicList;
    }
}
