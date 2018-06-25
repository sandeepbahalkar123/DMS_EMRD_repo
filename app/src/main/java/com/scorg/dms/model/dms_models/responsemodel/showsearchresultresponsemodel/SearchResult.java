package com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{

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
    @SerializedName("patientImageURL")
    @Expose
    private String patientImageURL;
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

    public String getPatientImageURL() {
        return patientImageURL;
    }

    public void setPatientImageURL(String patientImageURL) {
        this.patientImageURL = patientImageURL;
    }
}