package com.scorg.dms.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentClinicList implements Parcelable
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
@SerializedName("appointmentOpdCount")
@Expose
private Integer appointmentOpdCount;
@SerializedName("appointmentOTCount")
@Expose
private Integer appointmentOTCount;
@SerializedName("appointmentStartTime")
@Expose
private String appointmentStartTime;
public final static Parcelable.Creator<AppointmentClinicList> CREATOR = new Creator<AppointmentClinicList>() {


@SuppressWarnings({
"unchecked"
})
public AppointmentClinicList createFromParcel(Parcel in) {
return new AppointmentClinicList(in);
}

public AppointmentClinicList[] newArray(int size) {
return (new AppointmentClinicList[size]);
}

}
;

protected AppointmentClinicList(Parcel in) {
this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
this.cityName = ((String) in.readValue((String.class.getClassLoader())));
this.areaName = ((String) in.readValue((String.class.getClassLoader())));
this.appointmentOpdCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.appointmentOTCount = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.appointmentStartTime = ((String) in.readValue((String.class.getClassLoader())));
}

public AppointmentClinicList() {
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

public Integer getAppointmentOpdCount() {
return appointmentOpdCount;
}

public void setAppointmentOpdCount(Integer appointmentOpdCount) {
this.appointmentOpdCount = appointmentOpdCount;
}

public Integer getAppointmentOTCount() {
return appointmentOTCount;
}

public void setAppointmentOTCount(Integer appointmentOTCount) {
this.appointmentOTCount = appointmentOTCount;
}

public String getAppointmentStartTime() {
return appointmentStartTime;
}

public void setAppointmentStartTime(String appointmentStartTime) {
this.appointmentStartTime = appointmentStartTime;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(clinicName);
dest.writeValue(cityName);
dest.writeValue(areaName);
dest.writeValue(appointmentOpdCount);
dest.writeValue(appointmentOTCount);
dest.writeValue(appointmentStartTime);
}

public int describeContents() {
return 0;
}

}