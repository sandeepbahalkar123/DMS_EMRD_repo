package com.scorg.dms.model.dms_models.responsemodel.patientnamelistresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstPatient {

    @SerializedName("PatientName")
    @Expose
    private String patientName;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

}