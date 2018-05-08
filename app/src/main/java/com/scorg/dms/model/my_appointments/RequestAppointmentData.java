package com.scorg.dms.model.my_appointments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

/**
 * Created by jeetal on 19/2/18.
 */

public class RequestAppointmentData implements CustomResponse {
    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("appName")
    @Expose
    private String appName;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
