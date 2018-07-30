
package com.scorg.dms.model.patient.doctor_patients;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PatientDataModel implements Parcelable
{

    @SerializedName("patientList")
    @Expose
    private ArrayList<PatientList> patientList = new ArrayList<PatientList>();
    public final static Creator<PatientDataModel> CREATOR = new Creator<PatientDataModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PatientDataModel createFromParcel(Parcel in) {
            return new PatientDataModel(in);
        }

        public PatientDataModel[] newArray(int size) {
            return (new PatientDataModel[size]);
        }

    }
    ;

    protected PatientDataModel(Parcel in) {

        in.readList(this.patientList, (PatientList.class.getClassLoader()));
    }

    public PatientDataModel() {
    }


    public ArrayList<PatientList> getPatientList() {
        return patientList;
    }

    public void setPatientList(ArrayList<PatientList> patientList) {
        this.patientList = patientList;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(patientList);
    }

    public int describeContents() {
        return  0;
    }

}
