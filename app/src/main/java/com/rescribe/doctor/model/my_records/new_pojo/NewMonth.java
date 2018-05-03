package com.rescribe.doctor.model.my_records.new_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.model.my_records.MyRecordInfoAndReports;

import java.util.ArrayList;

public class NewMonth {

    @SerializedName("month")
    @Expose
    private String month;
    @SerializedName("docVisits")
    @Expose
    private ArrayList<MyRecordInfoAndReports> docVisits = new ArrayList<MyRecordInfoAndReports>();

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public ArrayList<MyRecordInfoAndReports> getDocVisits() {
        return docVisits;
    }

    public void setDocVisits(ArrayList<MyRecordInfoAndReports> docVisits) {
        this.docVisits = docVisits;
    }

}