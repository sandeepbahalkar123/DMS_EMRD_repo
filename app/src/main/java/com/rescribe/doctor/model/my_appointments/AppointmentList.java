package com.rescribe.doctor.model.my_appointments;

import java.util.ArrayList;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentList implements Parcelable, Cloneable, Comparable<AppointmentList> {
    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("clinicId")
    @Expose
    private Integer clinicId;
    @SerializedName("cityId")
    @Expose
    private Integer cityId;
    @SerializedName("patientList")
    @Expose
    private ArrayList<PatientList> patientList = new ArrayList<PatientList>();
    private PatientList patientHeader;
    private boolean selectedGroupCheckbox;

    public final static Creator<AppointmentList> CREATOR = new Creator<AppointmentList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AppointmentList createFromParcel(Parcel in) {
            return new AppointmentList(in);
        }

        public AppointmentList[] newArray(int size) {
            return (new AppointmentList[size]);
        }

    };


    protected AppointmentList(Parcel in) {
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.area = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.cityId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.patientList, (PatientList.class.getClassLoader()));
    }

    public AppointmentList() {
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ArrayList<PatientList> getPatientList() {
        return patientList;
    }

    public void setPatientList(ArrayList<PatientList> patientList) {
        this.patientList = patientList;
    }

    public PatientList getPatientHeader() {
        return patientHeader;
    }

    public void setPatientHeader(PatientList patientHeader) {
        this.patientHeader = patientHeader;
    }

    public boolean isSelectedGroupCheckbox() {
        return selectedGroupCheckbox;
    }

    public void setSelectedGroupCheckbox(boolean selectedGroupCheckbox) {
        this.selectedGroupCheckbox = selectedGroupCheckbox;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }


    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicName);
        dest.writeValue(clinicId);
        dest.writeValue(locationId);
        dest.writeValue(area);
        dest.writeValue(city);
        dest.writeValue(address);
        dest.writeValue(cityId);
        dest.writeList(patientList);


    }

    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull AppointmentList o) {
        return 0;
    }
}
