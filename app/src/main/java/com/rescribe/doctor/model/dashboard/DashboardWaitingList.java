
package com.rescribe.doctor.model.dashboard;

import java.util.ArrayList;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardWaitingList implements Parcelable
{

    @SerializedName("clinicList")
    @Expose
    private ArrayList<WaitingClinicList> waitingClinicList = new ArrayList<WaitingClinicList>();
    @SerializedName("todayFollowUpCount")
    @Expose
    private Integer todayFollowUpCount;
    @SerializedName("todayNewPatientCount")
    @Expose
    private Integer todayNewPatientCount;
    @SerializedName("todayWaitingCount")
    @Expose
    private Integer todayWaitingCount;
    public final static Creator<DashboardWaitingList> CREATOR = new Creator<DashboardWaitingList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardWaitingList createFromParcel(Parcel in) {
            return new DashboardWaitingList(in);
        }

        public DashboardWaitingList[] newArray(int size) {
            return (new DashboardWaitingList[size]);
        }

    }
    ;

    protected DashboardWaitingList(Parcel in) {
        in.readList(this.waitingClinicList, (WaitingClinicList.class.getClassLoader()));
        this.todayFollowUpCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.todayNewPatientCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.todayWaitingCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public DashboardWaitingList() {
    }

    public ArrayList<WaitingClinicList> getWaitingClinicList() {
        return waitingClinicList;
    }

    public void setWaitingClinicList(ArrayList<WaitingClinicList> waitingClinicList) {
        this.waitingClinicList = waitingClinicList;
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

    public Integer getTodayWaitingCount() {
        return todayWaitingCount;
    }

    public void setTodayWaitingCount(Integer todayWaitingCount) {
        this.todayWaitingCount = todayWaitingCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(waitingClinicList);
        dest.writeValue(todayFollowUpCount);
        dest.writeValue(todayNewPatientCount);
        dest.writeValue(todayWaitingCount);
    }

    public int describeContents() {
        return  0;
    }

}
