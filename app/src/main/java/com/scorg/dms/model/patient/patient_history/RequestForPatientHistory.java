package com.scorg.dms.model.patient.patient_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

/**
 * Created by jeetal on 27/2/18.
 */

public class RequestForPatientHistory implements CustomResponse {
    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("year")
    @Expose
    private String year;

    @SerializedName("getPatientInfo")
    @Expose
    private boolean getPatientInfo;
    @SerializedName("hospitalPatId")
    @Expose
    private Integer hospitalPatId;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getYear() {
        return year;
    }

    public Integer getHospitalPatId() {
        return hospitalPatId;
    }

    public void setHospitalPatId(Integer hospitalPatId) {
        this.hospitalPatId = hospitalPatId;
    }

    public void setYear(String year) {
        this.year = year;
    }


    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }


    public boolean getGetPatientInfo() {
        return getPatientInfo;
    }

    public void setGetPatientInfo(boolean getPatientInfo) {
        this.getPatientInfo = getPatientInfo;
    }
}
