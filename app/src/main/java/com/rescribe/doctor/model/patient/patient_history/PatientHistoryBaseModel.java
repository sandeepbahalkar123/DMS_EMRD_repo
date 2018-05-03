package com.rescribe.doctor.model.patient.patient_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;

public class PatientHistoryBaseModel implements CustomResponse{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private PatientHistoryDataModel patientHistoryDataModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public PatientHistoryBaseModel withCommon(Common common) {
        this.common = common;
        return this;
    }

    public PatientHistoryDataModel getPatientHistoryDataModel() {
        return patientHistoryDataModel;
    }

    public void setPatientHistoryDataModel(PatientHistoryDataModel patientHistoryDataModel) {
        this.patientHistoryDataModel = patientHistoryDataModel;
    }
}
