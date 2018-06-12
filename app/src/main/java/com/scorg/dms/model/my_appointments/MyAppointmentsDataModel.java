
package com.scorg.dms.model.my_appointments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyAppointmentsDataModel implements Parcelable {

    @SerializedName("clinicList")
    @Expose
    private ArrayList<ClinicList> clinicList = new ArrayList<ClinicList>();
    @SerializedName("appointmentList")
    @Expose
    private ArrayList<AppointmentPatientData> appointmentPatientData = new ArrayList<AppointmentPatientData>();

    public final static Creator<MyAppointmentsDataModel> CREATOR = new Creator<MyAppointmentsDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MyAppointmentsDataModel createFromParcel(Parcel in) {
            return new MyAppointmentsDataModel(in);
        }

        public MyAppointmentsDataModel[] newArray(int size) {
            return (new MyAppointmentsDataModel[size]);
        }

    };

    protected MyAppointmentsDataModel(Parcel in) {
        in.readList(this.clinicList, (ClinicList.class.getClassLoader()));
        in.readList(this.appointmentPatientData, (AppointmentPatientData.class.getClassLoader()));
    }


    public MyAppointmentsDataModel() {
    }


    public ArrayList<ClinicList> getClinicList() {
        return clinicList;
    }

    public void setClinicList(ArrayList<ClinicList> clinicList) {
        this.clinicList = clinicList;
    }

    public ArrayList<AppointmentPatientData> getAppointmentPatientData() {
        return appointmentPatientData;
    }

    public void setAppointmentPatientData(ArrayList<AppointmentPatientData> appointmentPatientData) {
        this.appointmentPatientData = appointmentPatientData;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(clinicList);
        dest.writeList(appointmentPatientData);
    }

    public int describeContents() {
        return 0;
    }


    public ArrayList<StatusList> getStatusList() {

        ArrayList<StatusList> statusLists = new ArrayList<StatusList>();

        StatusList s1 = new StatusList();
        s1.setStatusName("In Queue");
        s1.setStatusId(1);
        statusLists.add(s1);
        s1 = new StatusList();
        s1.setStatusName("Coming");
        s1.setStatusId(2);
        statusLists.add(s1);
        s1 = new StatusList();
        s1.setStatusName("Complete");
        s1.setStatusId(3);
        statusLists.add(s1);

        return (statusLists);
    }
}
