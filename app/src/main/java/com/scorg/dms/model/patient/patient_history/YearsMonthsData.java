
package com.scorg.dms.model.patient.patient_history;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YearsMonthsData {

    @SerializedName("year")
    @Expose
    private int year;
    @SerializedName("months")
    @Expose
    private ArrayList<String> months = new ArrayList<String>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<String> getMonths() {
        return months;
    }

    public void setMonths(ArrayList<String> months) {
        this.months = months;
    }


}
