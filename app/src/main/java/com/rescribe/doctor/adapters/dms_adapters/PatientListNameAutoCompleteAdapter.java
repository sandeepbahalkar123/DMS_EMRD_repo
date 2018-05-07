package com.rescribe.doctor.adapters.dms_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rescribe.doctor.R;
import com.rescribe.doctor.model.dms_models.responsemodel.patientnamelistresponsemodel.LstPatient;

import java.util.List;

/**
 * Created by root on 7/6/16.
 */
public class PatientListNameAutoCompleteAdapter extends BaseAdapter
{
    List<LstPatient> patientList;
    Context context;

    public PatientListNameAutoCompleteAdapter(List<LstPatient> lstPatients) {
        this.patientList = lstPatients;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return patientList.size();
    }

    @Override
    public Object getItem(int i)
    {
        return 0;
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        View view=convertView;

        if(convertView==null)
        {
            LayoutInflater layoutInflater= LayoutInflater.from(context);
            view=layoutInflater.inflate(R.layout.show_patient_names_activity,null);
        }

            TextView txt_name=(TextView) view.findViewById(R.id.txt_contact_name);


        LstPatient mPatientList=patientList.get(position);
        txt_name.setText(mPatientList.getPatientName());

        return view;
    }
}
