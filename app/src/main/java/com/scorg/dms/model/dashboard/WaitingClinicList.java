
package com.scorg.dms.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaitingClinicList implements Parcelable
{

    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("cityName")
    @Expose
    private String cityName;
    @SerializedName("areaName")
    @Expose
    private String areaName;
    @SerializedName("patientWaitingCount")
    @Expose
    private Integer patientWaitingCount;
    public final static Creator<WaitingClinicList> CREATOR = new Creator<WaitingClinicList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public WaitingClinicList createFromParcel(Parcel in) {
            return new WaitingClinicList(in);
        }

        public WaitingClinicList[] newArray(int size) {
            return (new WaitingClinicList[size]);
        }

    }
    ;

    protected WaitingClinicList(Parcel in) {
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.cityName = ((String) in.readValue((String.class.getClassLoader())));
        this.areaName = ((String) in.readValue((String.class.getClassLoader())));
        this.patientWaitingCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public WaitingClinicList() {
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getPatientWaitingCount() {
        return patientWaitingCount;
    }

    public void setPatientWaitingCount(Integer patientWaitingCount) {
        this.patientWaitingCount = patientWaitingCount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicName);
        dest.writeValue(cityName);
        dest.writeValue(areaName);
        dest.writeValue(patientWaitingCount);
    }

    public int describeContents() {
        return  0;
    }

}
