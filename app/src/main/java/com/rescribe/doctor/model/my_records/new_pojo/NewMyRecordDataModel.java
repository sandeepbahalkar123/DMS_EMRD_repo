package com.rescribe.doctor.model.my_records.new_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewMyRecordDataModel {

    @SerializedName("yearsMonthsData")
    @Expose
    private ArrayList<NewYearsMonthsData> yearsMonthsData = new ArrayList<NewYearsMonthsData>();
    @SerializedName("originalData")
    @Expose
    private NewOriginalData originalData = new NewOriginalData();

    public ArrayList<NewYearsMonthsData> getYearsMonthsData() {
        return yearsMonthsData;
    }

    public void setYearsMonthsData(ArrayList<NewYearsMonthsData> yearsMonthsData) {
        this.yearsMonthsData = yearsMonthsData;
    }

    public NewOriginalData getOriginalData() {
        return originalData;
    }

    public void setOriginalData(NewOriginalData originalData) {
        this.originalData = originalData;
    }
}