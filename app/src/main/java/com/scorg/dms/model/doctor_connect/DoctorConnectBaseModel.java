
package com.scorg.dms.model.doctor_connect;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class DoctorConnectBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorConnectDataModel doctorConnectDataModel;
    public final static Creator<DoctorConnectBaseModel> CREATOR = new Creator<DoctorConnectBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorConnectBaseModel createFromParcel(Parcel in) {
            DoctorConnectBaseModel instance = new DoctorConnectBaseModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.doctorConnectDataModel = ((DoctorConnectDataModel) in.readValue((DoctorConnectDataModel.class.getClassLoader())));
            return instance;
        }

        public DoctorConnectBaseModel[] newArray(int size) {
            return (new DoctorConnectBaseModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorConnectDataModel getDoctorConnectDataModel() {
        return doctorConnectDataModel;
    }

    public void setDoctorConnectDataModel(DoctorConnectDataModel doctorConnectDataModel) {
        this.doctorConnectDataModel = doctorConnectDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(doctorConnectDataModel);
    }

    public int describeContents() {
        return 0;
    }

}
