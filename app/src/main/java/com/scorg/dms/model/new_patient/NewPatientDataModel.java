
package com.scorg.dms.model.new_patient;

import java.util.ArrayList;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewPatientDataModel implements Parcelable {

    @SerializedName("newPatientsDetails")
    @Expose
    private ArrayList<NewPatientsDetail> newPatientsDetails = new ArrayList<NewPatientsDetail>();
    public final static Creator<NewPatientDataModel> CREATOR = new Creator<NewPatientDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NewPatientDataModel createFromParcel(Parcel in) {
            return new NewPatientDataModel(in);
        }

        public NewPatientDataModel[] newArray(int size) {
            return (new NewPatientDataModel[size]);
        }

    };

    protected NewPatientDataModel(Parcel in) {
        in.readList(this.newPatientsDetails, (NewPatientsDetail.class.getClassLoader()));
    }

    public NewPatientDataModel() {
    }

    public ArrayList<NewPatientsDetail> getNewPatientsDetails() {
        return newPatientsDetails;
    }

    public void setNewPatientsDetails(ArrayList<NewPatientsDetail> newPatientsDetails) {
        this.newPatientsDetails = newPatientsDetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(newPatientsDetails);
    }

    public int describeContents() {
        return 0;
    }

}
