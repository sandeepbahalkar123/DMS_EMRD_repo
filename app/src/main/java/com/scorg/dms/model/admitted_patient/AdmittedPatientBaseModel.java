package com.scorg.dms.model.admitted_patient;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class AdmittedPatientBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private AdmittedPatientDataModel admittedPatientDataModel;
    public final static Creator<AdmittedPatientBaseModel> CREATOR = new Creator<AdmittedPatientBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AdmittedPatientBaseModel createFromParcel(Parcel in) {
            return new AdmittedPatientBaseModel(in);
        }

        public AdmittedPatientBaseModel[] newArray(int size) {
            return (new AdmittedPatientBaseModel[size]);
        }

    };

    protected AdmittedPatientBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.admittedPatientDataModel = ((AdmittedPatientDataModel) in.readValue((AdmittedPatientDataModel.class.getClassLoader())));
    }

    public AdmittedPatientBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public AdmittedPatientDataModel getAdmittedPatientDataModel() {
        return admittedPatientDataModel;
    }

    public void setAdmittedPatientDataModel(AdmittedPatientDataModel admittedPatientDataModel) {
        this.admittedPatientDataModel = admittedPatientDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(admittedPatientDataModel);
    }

    public int describeContents() {
        return 0;
    }

}
