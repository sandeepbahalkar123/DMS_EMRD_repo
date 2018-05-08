package com.scorg.dms.model.patient.patient_connect;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

import java.util.ArrayList;

public class ChatPatientConnectModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private PatientListData patientListData;

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
        private ArrayList<PatientData> patientDataList;

        public ArrayList<PatientData> getPatientDataList() {
            return patientDataList;
        }

        public void setPatientDataList(ArrayList<PatientData> patientDataList) {
            this.patientDataList = patientDataList;
        }
    }
}
