
package com.rescribe.doctor.model.patient.patient_history;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.model.login.Year;

import java.util.ArrayList;
import java.util.HashSet;

public class PatientHistoryDataModel {
    @SerializedName("yearsMonthsData")
    @Expose
    private ArrayList<YearsMonthsData> yearsMonthsData = new ArrayList<>();
    @SerializedName("opdList")
    @Expose
    private PatientHistoryInfoMonthContainer patientHistoryInfoMonthContainer;

    @SerializedName("patientDetails")
    @Expose
    private PatientDetails patientDetails;

    public ArrayList<YearsMonthsData> getYearsMonthsData() {
        return yearsMonthsData;
    }

    public void setYearsMonthsData(ArrayList<YearsMonthsData> yearsMonthsData) {
        this.yearsMonthsData = yearsMonthsData;
    }

    public PatientHistoryInfoMonthContainer getPatientHistoryInfoMonthContainer() {
        return patientHistoryInfoMonthContainer;
    }

    public void setPatientHistoryInfoMonthContainer(PatientHistoryInfoMonthContainer patientHistoryInfoMonthContainer) {
        this.patientHistoryInfoMonthContainer = patientHistoryInfoMonthContainer;
    }

    public ArrayList<Year> getFormattedYearList() {
        ArrayList<YearsMonthsData> yearsMonthsDataList = getYearsMonthsData();
        ArrayList<Year> yearList = new ArrayList<>();
        for (YearsMonthsData yearObject :
                yearsMonthsDataList) {
            String[] months = yearObject.getMonths().toArray(new String[yearObject.getMonths().size()]);
            if (months.length > 0) {
                for (int i = 0; i < months.length; i++) {
                    Year year = new Year();
                    year.setYear(String.valueOf(yearObject.getYear()));
                    year.setMonthName(months[i]);
                    yearList.add(year);
                }
            }
        }
        return yearList;
    }

    public ArrayList<String> getUniqueYears() {
        ArrayList<YearsMonthsData> yearsMonthsDataList = getYearsMonthsData();
        HashSet<String> strings = new HashSet<>();
        for (YearsMonthsData yearObject :
                yearsMonthsDataList) {
            strings.add(String.valueOf(yearObject.getYear()));
        }
        return new ArrayList(strings);
    }

    public PatientDetails getPatientDetails() {
        return patientDetails;
    }

    public void setPatientDetails(PatientDetails patientDetails) {
        this.patientDetails = patientDetails;
    }
}
