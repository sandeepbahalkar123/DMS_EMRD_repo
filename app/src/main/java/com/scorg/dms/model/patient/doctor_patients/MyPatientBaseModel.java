
package com.scorg.dms.model.patient.doctor_patients;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class MyPatientBaseModel implements Parcelable, CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private PatientDataModel patientDataModel;
    public final static Creator<MyPatientBaseModel> CREATOR = new Creator<MyPatientBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MyPatientBaseModel createFromParcel(Parcel in) {
            return new MyPatientBaseModel(in);
        }

        public MyPatientBaseModel[] newArray(int size) {
            return (new MyPatientBaseModel[size]);
        }

    }
    ;

    protected MyPatientBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.patientDataModel = ((PatientDataModel) in.readValue((PatientDataModel.class.getClassLoader())));
    }

    public MyPatientBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public PatientDataModel getPatientDataModel() {
        return patientDataModel;
    }

    public void setPatientDataModel(PatientDataModel patientDataModel) {
        this.patientDataModel = patientDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(patientDataModel);
    }

    public int describeContents() {
        return  0;
    }

}
