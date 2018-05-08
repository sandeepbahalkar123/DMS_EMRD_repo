package com.scorg.dms.model.patient.doctor_patients.sync_resp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class SyncPatientsModel implements CustomResponse{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private PatientSyncData data;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public PatientSyncData getData() {
        return data;
    }

    public void setData(PatientSyncData data) {
        this.data = data;
    }

}