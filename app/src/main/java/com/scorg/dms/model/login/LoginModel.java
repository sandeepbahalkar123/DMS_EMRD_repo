package com.scorg.dms.model.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class LoginModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorLoginData doctorLoginData;
    public final static Parcelable.Creator<LoginModel> CREATOR = new Parcelable.Creator<LoginModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LoginModel createFromParcel(Parcel in) {
            return new LoginModel(in);
        }

        public LoginModel[] newArray(int size) {
            return (new LoginModel[size]);
        }

    }
            ;

    protected LoginModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.doctorLoginData = ((DoctorLoginData) in.readValue((DoctorLoginData.class.getClassLoader())));
    }

    public LoginModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorLoginData getDoctorLoginData() {
        return doctorLoginData;
    }

    public void setDoctorLoginData(DoctorLoginData doctorLoginData) {
        this.doctorLoginData = doctorLoginData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(doctorLoginData);
    }

    public int describeContents() {
        return 0;
    }

}