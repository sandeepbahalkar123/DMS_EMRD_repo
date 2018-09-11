package com.scorg.dms.model.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClinicList implements Parcelable
{

    @SerializedName("clinicId")
    @Expose
    private Integer clinicId;
    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("clinicAddress")
    @Expose
    private String clinicAddress;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("services")
    @Expose
    private ArrayList<String> services = new ArrayList<>();
    public final static Parcelable.Creator<ClinicList> CREATOR = new Creator<ClinicList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClinicList createFromParcel(Parcel in) {
            return new ClinicList(in);
        }

        public ClinicList[] newArray(int size) {
            return (new ClinicList[size]);
        }

    };

    protected ClinicList(Parcel in) {
        this.clinicId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.services, (java.lang.String.class.getClassLoader()));
    }

    public ClinicList() {
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicId);
        dest.writeValue(clinicName);
        dest.writeValue(clinicAddress);
        dest.writeValue(locationId);
        dest.writeList(services);
    }

    public int describeContents() {
        return 0;
    }

}