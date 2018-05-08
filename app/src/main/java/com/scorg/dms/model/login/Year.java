package com.scorg.dms.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Year {

    @SerializedName("Yr")
    @Expose
    private String yr;
    @SerializedName("Mon")
    @Expose
    private String mon;

    public String getYear() {
        return yr;
    }

    public void setYear(String yr) {
        this.yr = yr;
    }

    public String getMonthName() {
        return mon;
    }

    public void setMonthName(String mon) {
        this.mon = mon;
    }

}