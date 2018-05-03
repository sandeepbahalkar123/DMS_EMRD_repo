package com.rescribe.doctor.model.request_appointment_confirmation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;
import com.rescribe.doctor.model.appointments.AptList;


public class ResponseAppointmentConfirmationModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private AptList aptList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public AptList getAptList() {
        return aptList;
    }

    public void setAptList(AptList aptList) {
        this.aptList = aptList;
    }

}