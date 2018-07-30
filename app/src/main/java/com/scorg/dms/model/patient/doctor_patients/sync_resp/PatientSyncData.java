package com.scorg.dms.model.patient.doctor_patients.sync_resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PatientSyncData {

    @SerializedName("patientDetails")
    @Expose
    private ArrayList<PatientUpdateDetail> patientUpdateDetails = null;

    public ArrayList<PatientUpdateDetail> getPatientUpdateDetails() {
        return patientUpdateDetails;
    }

    public void setPatientUpdateDetails(ArrayList<PatientUpdateDetail> patientUpdateDetails) {
        this.patientUpdateDetails = patientUpdateDetails;
    }
}