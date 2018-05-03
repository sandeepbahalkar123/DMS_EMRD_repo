
package com.rescribe.doctor.model.case_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

import java.util.List;

public class VisitData implements CustomResponse {

    @SerializedName("patientHistory")
    @Expose
    private List<PatientHistory> patientHistory = null;

    public List<PatientHistory> getPatientHistory() {
        return patientHistory;
    }

    public void setPatientHistory(List<PatientHistory> patientHistory) {
        this.patientHistory = patientHistory;
    }

}
