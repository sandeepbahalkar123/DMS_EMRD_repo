
package com.scorg.dms.model.waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaitingclinicList implements Parcelable {

    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("clinicId")
    @Expose
    private Integer clinicId;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("cityId")
    @Expose
    private Integer cityId;
    @SerializedName("patientList")
    @Expose
    private WaitingPatientList waitingPatientList;
    public final static Creator<WaitingclinicList> CREATOR = new Creator<WaitingclinicList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WaitingclinicList createFromParcel(Parcel in) {
            return new WaitingclinicList(in);
        }

        public WaitingclinicList[] newArray(int size) {
            return (new WaitingclinicList[size]);
        }

    };

    protected WaitingclinicList(Parcel in) {
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.area = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.cityId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingPatientList = ((WaitingPatientList) in.readValue((WaitingPatientList.class.getClassLoader())));
    }

    public WaitingclinicList() {
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public WaitingPatientList getWaitingPatientList() {
        return waitingPatientList;
    }

    public void setWaitingPatientList(WaitingPatientList waitingPatientList) {
        this.waitingPatientList = waitingPatientList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicName);
        dest.writeValue(clinicId);
        dest.writeValue(locationId);
        dest.writeValue(area);
        dest.writeValue(city);
        dest.writeValue(cityId);
        dest.writeValue(waitingPatientList);
    }

    public int describeContents() {
        return 0;
    }

}
