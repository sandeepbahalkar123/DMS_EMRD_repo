package com.rescribe.doctor.model.my_records.new_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewOriginalData {

    @SerializedName("year")
    @Expose
    private int year;
    @SerializedName("months")
    @Expose
    private ArrayList<NewMonth> months = new ArrayList<NewMonth>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<NewMonth> getMonths() {
        return months;
    }

    public void setMonths(ArrayList<NewMonth> months) {
        this.months = months;
    }

}