package com.rescribe.doctor.model.patient.patient_connect;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;
import com.rescribe.doctor.model.patient.patient_connect.PatientData;

import java.util.ArrayList;

public class PatientConnectBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private PatientListData patientListData = new PatientListData();

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public PatientListData getPatientListData() {
        return patientListData;
    }

    public void setPatientListData(PatientListData patientListData) {
        this.patientListData = patientListData;
    }

    public class PatientListData {
        @SerializedName("patientList")
        @Expose
        private ArrayList<PatientData> patientDataList = new ArrayList<>();

        public ArrayList<PatientData> getPatientDataList() {
            return patientDataList;
        }

        public void setPatientDataList(ArrayList<PatientData> patientDataList) {
            this.patientDataList = patientDataList;
        }
    }
}
