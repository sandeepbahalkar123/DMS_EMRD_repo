
package com.rescribe.doctor.model.doctor_location;

import java.util.ArrayList;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;

public class DoctorLocationBaseModel implements Parcelable,CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<DoctorLocationModel> doctorLocationModel = new ArrayList<DoctorLocationModel>();
    public final static Creator<DoctorLocationBaseModel> CREATOR = new Creator<DoctorLocationBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DoctorLocationBaseModel createFromParcel(Parcel in) {
            return new DoctorLocationBaseModel(in);
        }

        public DoctorLocationBaseModel[] newArray(int size) {
            return (new DoctorLocationBaseModel[size]);
        }

    }
    ;

    protected DoctorLocationBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        in.readList(this.doctorLocationModel, (DoctorLocationModel.class.getClassLoader()));
    }

    public DoctorLocationBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<DoctorLocationModel> getDoctorLocationModel() {
        return doctorLocationModel;
    }

    public void setDoctorLocationModel(ArrayList<DoctorLocationModel> doctorLocationModel) {
        this.doctorLocationModel = doctorLocationModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeList(doctorLocationModel);
    }

    public int describeContents() {
        return  0;
    }

}
