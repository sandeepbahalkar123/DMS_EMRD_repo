package com.rescribe.doctor.model.patient.add_new_patient;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

public class SyncPatientsRequest implements CustomResponse {

    @SerializedName("docId")
    @Expose
    private String docId;
    @SerializedName("patientDetails")
    @Expose
    private ArrayList<PatientDetail> patientDetails = null;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public ArrayList<PatientDetail> getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(ArrayList<PatientDetail> patientDetails) {
        this.patientDetails = patientDetails;
    }
}