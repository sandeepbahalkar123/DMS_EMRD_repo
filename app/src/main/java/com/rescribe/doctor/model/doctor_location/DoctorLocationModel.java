
package com.rescribe.doctor.model.doctor_location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorLocationModel implements Parcelable
{

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
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("apptScheduleLmtDays")
    @Expose
    private Integer apptScheduleLmtDays;
    public final static Creator<DoctorLocationModel> CREATOR = new Creator<DoctorLocationModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DoctorLocationModel createFromParcel(Parcel in) {
            return new DoctorLocationModel(in);
        }

        public DoctorLocationModel[] newArray(int size) {
            return (new DoctorLocationModel[size]);
        }

    }
    ;

    protected DoctorLocationModel(Parcel in) {
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.area = ((String) in.readValue((String.class.getClassLoader())));
        this.city = ((String) in.readValue((String.class.getClassLoader())));
        this.cityId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.apptScheduleLmtDays = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    public DoctorLocationModel() {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getApptScheduleLmtDays() {
        return apptScheduleLmtDays;
    }

    public void setApptScheduleLmtDays(Integer apptScheduleLmtDays) {
        this.apptScheduleLmtDays = apptScheduleLmtDays;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicName);
        dest.writeValue(clinicId);
        dest.writeValue(locationId);
        dest.writeValue(area);
        dest.writeValue(city);
        dest.writeValue(cityId);
        dest.writeValue(address);
        dest.writeValue(apptScheduleLmtDays);
    }

    public int describeContents() {
        return  0;
    }
//
    @Override
    public String toString() {
        return clinicName +", "+area+", "+city;

    }
}
