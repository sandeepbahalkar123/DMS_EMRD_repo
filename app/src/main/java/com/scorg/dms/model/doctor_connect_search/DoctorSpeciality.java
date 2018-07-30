
package com.scorg.dms.model.doctor_connect_search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class DoctorSpeciality implements Parcelable, CustomResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("speciality")
    @Expose
    private String speciality;
    public final static Creator<DoctorSpeciality> CREATOR = new Creator<DoctorSpeciality>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorSpeciality createFromParcel(Parcel in) {
            DoctorSpeciality instance = new DoctorSpeciality();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.speciality = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public DoctorSpeciality[] newArray(int size) {
            return (new DoctorSpeciality[size]);
        }

    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(speciality);
    }

    public int describeContents() {
        return 0;
    }

}
