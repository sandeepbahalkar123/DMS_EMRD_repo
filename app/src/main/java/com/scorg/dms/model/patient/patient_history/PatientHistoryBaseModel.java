package com.scorg.dms.model.patient.patient_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

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
