package com.scorg.dms.model.admitted_patient;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.model.dms_models.ViewRights;
import com.scorg.dms.model.my_appointments.ClinicList;

import java.util.ArrayList;

public class AdmittedPatientDataModel implements Parcelable {

    @SerializedName("clinicList")
    @Expose
    private ArrayList<ClinicList> clinicList = new ArrayList<ClinicList>();
    @SerializedName("admittedPatientList")
    @Expose
    private ArrayList<AdmittedPatientData> admittedPatientData = new ArrayList<AdmittedPatientData>();

    @SerializedName("viewRights")
    @Expose
    private ViewRights viewRights;

    public final static Creator<AdmittedPatientDataModel> CREATOR = new Creator<AdmittedPatientDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AdmittedPatientDataModel createFromParcel(Parcel in) {
            return new AdmittedPatientDataModel(in);
        }

        public AdmittedPatientDataModel[] newArray(int size) {
            return (new AdmittedPatientDataModel[size]);
        }

    };

    protected AdmittedPatientDataModel(Parcel in) {
        in.readList(this.clinicList, (ClinicList.class.getClassLoader()));
        in.readList(this.admittedPatientData, (AdmittedPatientData.class.getClassLoader()));
        in.readValue((ViewRights.class.getClassLoader()));
    }


    public AdmittedPatientDataModel() {
    }


    public ArrayList<ClinicList> getClinicList() {
        return clinicList;
    }

    public void setClinicList(ArrayList<ClinicList> clinicList) {
        this.clinicList = clinicList;
    }

    public ArrayList<AdmittedPatientData> getAdmittedPatientData() {
        return admittedPatientData;
    }

    public void setAdmittedPatientData(ArrayList<AdmittedPatientData> admittedPatientData) {
        this.admittedPatientData = admittedPatientData;
    }

    public ViewRights getViewRights() {
        return viewRights;
    }

    public void setViewRights(ViewRights viewRights) {
        this.viewRights = viewRights;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeList(clinicList);
        dest.writeList(admittedPatientData);
        dest.writeSerializable(viewRights);
    }

    public int describeContents() {
        return 0;
    }



}
