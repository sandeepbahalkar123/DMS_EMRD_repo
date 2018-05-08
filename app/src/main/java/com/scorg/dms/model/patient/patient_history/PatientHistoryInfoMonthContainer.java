package com.scorg.dms.model.patient.patient_history;

import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class PatientHistoryInfoMonthContainer implements CustomResponse, Serializable {

    @SerializedName("year")
    private String year;
    @SerializedName("months")
    private Map<String, ArrayList<PatientHistoryInfo>> monthWiseSortedPatientHistory;// = new TreeMap<String, ArrayList<MyRecordInfoAndReports>>(String.CASE_INSENSITIVE_ORDER);

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Map<String, ArrayList<PatientHistoryInfo>> getMonthWiseSortedPatientHistory() {
        return monthWiseSortedPatientHistory;
    }

    public void setMonthWiseSortedPatientHistory(Map<String, ArrayList<PatientHistoryInfo>> monthWiseSortedPatientHistory) {
        this.monthWiseSortedPatientHistory = monthWiseSortedPatientHistory;
    }


}
