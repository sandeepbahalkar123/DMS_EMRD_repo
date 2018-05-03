
package com.rescribe.doctor.model.patient.patient_history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientHistoryInfo {

    @SerializedName("opdId")
    @Expose
    private int opdId;
    @SerializedName("opdDate")
    @Expose
    private String visitDate;

    @SerializedName("opdTime")
    @Expose
    private String opdTime;
    @SerializedName("opdLabel")
    @Expose
    private String opdLabel;
    @SerializedName("opdValue")
    @Expose
    private String opdValue;
    private boolean longpressed;

    public String getOpdTime() {
        return opdTime;
    }

    public void setOpdTime(String opdTime) {
        this.opdTime = opdTime;
    }

    public String getOpdLabel() {
        return opdLabel;
    }

    public void setOpdLabel(String opdLabel) {
        this.opdLabel = opdLabel;
    }

    public String getOpdValue() {
        return opdValue;
    }

    public void setOpdValue(String opdValue) {
        this.opdValue = opdValue;
    }

    public int getOpdId() {
        return opdId;
    }

    public void setOpdId(int opdId) {
        this.opdId = opdId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }
    public boolean isLongpressed() {
        return longpressed;
    }

    public void setLongpressed(boolean longpressed) {
        this.longpressed = longpressed;
    }

}
