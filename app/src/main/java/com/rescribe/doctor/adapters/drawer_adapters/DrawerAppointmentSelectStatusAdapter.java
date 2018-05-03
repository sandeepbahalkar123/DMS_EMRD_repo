package com.rescribe.doctor.adapters.drawer_adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;

import com.rescribe.doctor.R;
import com.rescribe.doctor.model.my_appointments.MyAppointmentsDataModel;
import com.rescribe.doctor.model.my_appointments.StatusList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rescribe.doctor.util.RescribeConstants.APPOINTMENT_STATUS.BOOKED;
import static com.rescribe.doctor.util.RescribeConstants.APPOINTMENT_STATUS.CANCEL;
import static com.rescribe.doctor.util.RescribeConstants.APPOINTMENT_STATUS.COMPLETED;
import static com.rescribe.doctor.util.RescribeConstants.APPOINTMENT_STATUS.CONFIRM;
import static com.rescribe.doctor.util.RescribeConstants.APPOINTMENT_STATUS.NO_SHOW;
import static com.rescribe.doctor.util.RescribeConstants.APPOINTMENT_STATUS.OTHER;

/**
 * Created by jeetal on 12/2/18.
 */

public class DrawerAppointmentSelectStatusAdapter extends RecyclerView.Adapter<DrawerAppointmentSelectStatusAdapter.ListViewHolder> {

    private Context mContext;
    private ArrayList<StatusList> mStatusLists;

    private OnClickOfFilterComponents mOnClickOfFilterComponents;

    public DrawerAppointmentSelectStatusAdapter(Context mContext, ArrayList<StatusList> statusLists, OnClickOfFilterComponents mOnClickOfFilterComponents) {
        this.mContext = mContext;
        this.mStatusLists = statusLists;
        this.mOnClickOfFilterComponents = mOnClickOfFilterComponents;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item_chechbox_layout, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, final int position) {
        if (mStatusLists.get(position).getStatusId().equals(BOOKED)) {
            holder.menuName.setText(mContext.getString(R.string.booked));
        } else if (mStatusLists.get(position).getStatusId().equals(CONFIRM)) {
            holder.menuName.setText(mContext.getString(R.string.Confirmed));
        } else if (mStatusLists.get(position).getStatusId().equals(COMPLETED)) {
            holder.menuName.setText(mContext.getString(R.string.capitalcompleted));
        } else if (mStatusLists.get(position).getStatusId().equals(CANCEL)) {
            holder.menuName.setText(mContext.getString(R.string.cancelled));
        } else if (mStatusLists.get(position).getStatusId().equals(NO_SHOW)) {
            holder.menuName.setText(mContext.getString(R.string.no_show));
        } else if (mStatusLists.get(position).getStatusId().equals(OTHER)) {
            holder.menuName.setText(mContext.getString(R.string.other));
        }
        holder.menuName.setText(mStatusLists.get(position).getStatusName());
        holder.menuName.setChecked(mStatusLists.get(position).isSelected());

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
                mStatusLists.get(position).setSelected(!mStatusLists.get(position).isSelected());
                notifyDataSetChanged();

                mOnClickOfFilterComponents.onClickofSelectStatus(mStatusLists);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStatusLists.size();
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

    public interface OnClickOfFilterComponents {
        public void onClickofSelectStatus(ArrayList<StatusList> mStatusLists);

    }

    public ArrayList<StatusList> getAdapterStatusList() {
        return mStatusLists;
    }
}
