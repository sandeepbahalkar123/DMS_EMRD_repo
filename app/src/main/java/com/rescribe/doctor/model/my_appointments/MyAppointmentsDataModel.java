
package com.rescribe.doctor.model.my_appointments;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyAppointmentsDataModel implements Parcelable {

    @SerializedName("clinicList")
    @Expose
    private ArrayList<ClinicList> clinicList = new ArrayList<ClinicList>();
    @SerializedName("appointmentList")
    @Expose
    private ArrayList<AppointmentList> appointmentList = new ArrayList<AppointmentList>();
    @SerializedName("statusList")
    @Expose
    private ArrayList<StatusList> statusList = new ArrayList<StatusList>();
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
        in.readList(this.appointmentList, (AppointmentList.class.getClassLoader()));
        in.readList(this.statusList, (StatusList.class.getClassLoader()));
    }


    public MyAppointmentsDataModel() {
    }


    public ArrayList<ClinicList> getClinicList() {
        return clinicList;
    }

    public void setClinicList(ArrayList<ClinicList> clinicList) {
        this.clinicList = clinicList;
    }

    public ArrayList<AppointmentList> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(ArrayList<AppointmentList> appointmentList) {
        this.appointmentList = appointmentList;
    }
    public ArrayList<StatusList> getStatusList() {
        return statusList;
    }

    public void setStatusList(ArrayList<StatusList> statusList) {
        this.statusList = statusList;
    }
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(clinicList);
        dest.writeList(appointmentList);
        dest.writeList(statusList);
    }

    public int describeContents() {
        return 0;
    }

}
