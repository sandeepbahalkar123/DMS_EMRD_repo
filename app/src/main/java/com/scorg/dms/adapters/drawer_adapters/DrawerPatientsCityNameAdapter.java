package com.scorg.dms.adapters.drawer_adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

import com.scorg.dms.R;
import com.scorg.dms.model.my_patient_filter.CityList;
import com.scorg.dms.ui.fragments.patient.my_patient.DrawerForMyPatients;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeetal on 12/2/18.
 */

public class DrawerPatientsCityNameAdapter extends RecyclerView.Adapter<DrawerPatientsCityNameAdapter.ListViewHolder> {

    private ArrayList<CityList> cityList;
    private CitySelectListener cityCheckedListener;

    public DrawerPatientsCityNameAdapter(DrawerForMyPatients drawerForMyPatients, ArrayList<CityList> cityList) {
        this.cityCheckedListener = drawerForMyPatients;
        this.cityList = cityList;

        try {
            cityCheckedListener = (CitySelectListener) drawerForMyPatients;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement CitySelectListener");
        }
    }

    @Override
    public DrawerPatientsCityNameAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item_chechbox_layout, parent, false);

        return new DrawerPatientsCityNameAdapter.ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DrawerPatientsCityNameAdapter.ListViewHolder holder, int position) {
        holder.menuName.setText(cityList.get(holder.getAdapterPosition()).getCityName());
        holder.menuName.setChecked(cityList.get(holder.getAdapterPosition()).isChecked());

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
                cityList.get(holder.getAdapterPosition()).setChecked(!holder.menuName.isChecked());
                notifyDataSetChanged();

                cityCheckedListener.onChecked(cityList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void reset() {

        for (CityList city : cityList)
            city.setChecked(false);

        notifyDataSetChanged();
        cityCheckedListener.onChecked(cityList);
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

    public interface CitySelectListener {
        void onChecked(ArrayList<CityList> cityList);
    }
}
