package com.scorg.dms.model.my_records;

import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class MyRecordInfoMonthContainer implements CustomResponse, Serializable {

    @SerializedName("year")
    private String year;
    @SerializedName("months")
    private Map<String, ArrayList<MyRecordInfoAndReports>> monthWiseSortedMyRecords;// = new TreeMap<String, ArrayList<MyRecordInfoAndReports>>(String.CASE_INSENSITIVE_ORDER);

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Map<String, ArrayList<MyRecordInfoAndReports>> getMonthWiseSortedMyRecords() {
        return monthWiseSortedMyRecords;
    }

    public void setMonthWiseSortedMyRecords(Map<String, ArrayList<MyRecordInfoAndReports>> monthWiseSortedMyRecords) {
        this.monthWiseSortedMyRecords = monthWiseSortedMyRecords;
    }

    @Override
    public String toString() {
        return "DoctorInfoMonthContainer{" +
                "year='" + year + '\'' +
                ", monthWiseSortedMyRecords=" + monthWiseSortedMyRecords +
                '}';
    }
}
