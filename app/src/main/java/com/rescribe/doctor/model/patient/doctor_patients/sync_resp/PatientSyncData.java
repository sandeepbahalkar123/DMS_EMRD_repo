package com.rescribe.doctor.model.patient.doctor_patients.sync_resp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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