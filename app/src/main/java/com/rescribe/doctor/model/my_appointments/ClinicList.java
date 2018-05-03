
package com.rescribe.doctor.model.my_appointments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClinicList implements Parcelable
{

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
    @SerializedName("cityId")
    @Expose
    private Integer cityId;
    @SerializedName("clinicId")
    @Expose
    private Integer clinicId;
    private boolean selected;

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
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.area = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.cityId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.clinicId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.selected = ((boolean) in.readValue((String.class.getClassLoader())));
    }

    public ClinicList() {
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

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicName);
        dest.writeValue(locationId);
        dest.writeValue(area);
        dest.writeValue(city);
        dest.writeValue(cityId);
        dest.writeValue(clinicId);
        dest.writeValue(selected);
    }

    public int describeContents() {
        return 0;
    }


}