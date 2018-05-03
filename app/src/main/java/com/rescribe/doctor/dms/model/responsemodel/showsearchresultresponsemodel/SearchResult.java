package com.rescribe.doctor.dms.model.responsemodel.showsearchresultresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResult {


    @SerializedName("patientAddress")
    @Expose
    private String patientAddress;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;
    @SerializedName("patientId")
    @Expose
    private String patientId;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("patientFileData")
    @Expose
    private List<PatientFileData> patientFileData = null;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public List<PatientFileData> getPatientFileData() {
        return patientFileData;
    }

    public void setPatientFileData(List<PatientFileData> patientFileData) {
        this.patientFileData = patientFileData;
    }
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientAddress() {
        return patientAddress;
    }

    public void setPatientAddress(String patientAddress) {
        this.patientAddress = patientAddress;
    }

}