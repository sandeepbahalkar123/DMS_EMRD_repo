
package com.rescribe.doctor.model.doctor_connect_search;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

public class SearchDataModel implements Parcelable, CustomResponse {

    @SerializedName("doctorSpecialities")
    @Expose
    private ArrayList<DoctorSpeciality> doctorSpecialities = null;
    public final static Creator<SearchDataModel> CREATOR = new Creator<SearchDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SearchDataModel createFromParcel(Parcel in) {
            SearchDataModel instance = new SearchDataModel();
            in.readList(instance.doctorSpecialities, (DoctorSpeciality.class.getClassLoader()));
            return instance;
        }

        public SearchDataModel[] newArray(int size) {
            return (new SearchDataModel[size]);
        }

    };

    public ArrayList<DoctorSpeciality> getDoctorSpecialities() {
        return doctorSpecialities;
    }

    public void setDoctorSpecialities(ArrayList<DoctorSpeciality> doctorSpecialities) {
        this.doctorSpecialities = doctorSpecialities;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(doctorSpecialities);
    }

    public int describeContents() {
        return 0;
    }

}
