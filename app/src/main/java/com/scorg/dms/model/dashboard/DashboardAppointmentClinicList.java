
package com.scorg.dms.model.dashboard;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardAppointmentClinicList implements Parcelable
{

    @SerializedName("clinicList")
    @Expose
    private ArrayList<AppointmentClinicList> appointmentClinicList = new ArrayList<AppointmentClinicList>();
    @SerializedName("todayFollowUpCount")
    @Expose
    private Integer todayFollowUpCount;
    @SerializedName("todayNewPatientCount")
    @Expose
    private Integer todayNewPatientCount;
    @SerializedName("todayAppointmentCount")
    @Expose
    private Integer todayAppointmentCount;
    @SerializedName("waitingListCount")
    @Expose
    private Integer waitingListCount;
    public final static Creator<DashboardAppointmentClinicList> CREATOR = new Creator<DashboardAppointmentClinicList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardAppointmentClinicList createFromParcel(Parcel in) {
            return new DashboardAppointmentClinicList(in);
        }

        public DashboardAppointmentClinicList[] newArray(int size) {
            return (new DashboardAppointmentClinicList[size]);
        }

    }
    ;

    protected DashboardAppointmentClinicList(Parcel in) {
        in.readList(this.appointmentClinicList, (Object.class.getClassLoader()));
        this.todayFollowUpCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.todayNewPatientCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.todayAppointmentCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingListCount = ((Integer) in.readValue((Integer.class.getClassLoader())));

    }

    public DashboardAppointmentClinicList() {
    }

    public ArrayList<AppointmentClinicList> getAppointmentClinicList() {
        return appointmentClinicList;
    }

    public void setAppointmentClinicList(ArrayList<AppointmentClinicList> appointmentClinicList) {
        this.appointmentClinicList = appointmentClinicList;
    }

    public Integer getTodayFollowUpCount() {
        return todayFollowUpCount;
    }

    public void setTodayFollowUpCount(Integer todayFollowUpCount) {
        this.todayFollowUpCount = todayFollowUpCount;
    }

    public Integer getTodayNewPatientCount() {
        return todayNewPatientCount;
    }

    public void setTodayNewPatientCount(Integer todayNewPatientCount) {
        this.todayNewPatientCount = todayNewPatientCount;
    }
    public Integer getTodayAppointmentCount() {
        return todayAppointmentCount;
    }

    public void setTodayAppointmentCount(Integer todayAppointmentCount) {
        this.todayAppointmentCount = todayAppointmentCount;
    }

    public Integer getWaitingListCount() {
        return waitingListCount;
    }

    public void setWaitingListCount(Integer waitingListCount) {
        this.waitingListCount = waitingListCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(appointmentClinicList);
        dest.writeValue(todayFollowUpCount);
        dest.writeValue(todayNewPatientCount);
        dest.writeValue(todayAppointmentCount);
        dest.writeValue(waitingListCount);
    }

    public int describeContents() {
        return  0;
    }

}
