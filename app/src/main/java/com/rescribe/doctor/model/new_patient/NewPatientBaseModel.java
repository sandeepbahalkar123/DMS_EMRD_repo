
package com.rescribe.doctor.model.new_patient;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;

public class NewPatientBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private NewPatientDataModel newPatientDataModel;
    public final static Creator<NewPatientBaseModel> CREATOR = new Creator<NewPatientBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NewPatientBaseModel createFromParcel(Parcel in) {
            return new NewPatientBaseModel(in);
        }

        public NewPatientBaseModel[] newArray(int size) {
            return (new NewPatientBaseModel[size]);
        }

    };

    protected NewPatientBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.newPatientDataModel = ((NewPatientDataModel) in.readValue((NewPatientDataModel.class.getClassLoader())));
    }

    public NewPatientBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public NewPatientDataModel getNewPatientDataModel() {
        return newPatientDataModel;
    }

    public void setNewPatientDataModel(NewPatientDataModel newPatientDataModel) {
        this.newPatientDataModel = newPatientDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(newPatientDataModel);
    }

    public int describeContents() {
        return 0;
    }

}
