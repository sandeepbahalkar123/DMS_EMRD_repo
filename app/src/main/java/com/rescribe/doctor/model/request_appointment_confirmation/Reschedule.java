package com.rescribe.doctor.model.request_appointment_confirmation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ganeshshirole on 8/2/18.
 */

public class Reschedule {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("aptId")
    @Expose
    private String aptId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAptId() {
        return aptId;
    }

    public void setAptId(String aptId) {
        this.aptId = aptId;
    }
}
