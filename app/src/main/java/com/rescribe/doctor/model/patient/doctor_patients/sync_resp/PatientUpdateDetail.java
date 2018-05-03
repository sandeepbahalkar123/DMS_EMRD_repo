package com.rescribe.doctor.model.patient.doctor_patients.sync_resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PatientUpdateDetail implements Serializable {

    @SerializedName("mobilePatientId")
    @Expose
    private String mobilePatientId;
    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("hospitalPatId")
    @Expose
    private Integer hospitalPatId;

    public String getMobilePatientId() {
        return mobilePatientId;
    }

    public void setMobilePatientId(String mobilePatientId) {
        this.mobilePatientId = mobilePatientId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getHospitalPatId() {
        return hospitalPatId;
    }

    public void setHospitalPatId(Integer hospitalPatId) {
        this.hospitalPatId = hospitalPatId;
    }

}