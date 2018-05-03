
package com.rescribe.doctor.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.login.Year;
import com.rescribe.doctor.model.my_records.new_pojo.NewYearsMonthsData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

public class MyRecordDataModel implements CustomResponse, Serializable{

    @SerializedName("yearsMonthsData")
    @Expose
    private ArrayList<NewYearsMonthsData> receivedYearMap = new ArrayList<>();
    @SerializedName("originalData")
    @Expose
    private MyRecordInfoMonthContainer myRecordInfoMonthContainer;

    public ArrayList<NewYearsMonthsData> getReceivedYearMap() {
        return receivedYearMap;
    }

    public void setReceivedYearMap(ArrayList<NewYearsMonthsData> receivedYearMap) {
        this.receivedYearMap = receivedYearMap;
    }

    public MyRecordInfoMonthContainer getMyRecordInfoMonthContainer() {
        return myRecordInfoMonthContainer;
    }

    public void setMyRecordInfoMonthContainer(MyRecordInfoMonthContainer myRecordInfoMonthContainer) {
        this.myRecordInfoMonthContainer = myRecordInfoMonthContainer;
    }

    @Override
    public String toString() {
        return "DoctorFilterModel{" +
                "receivedYearMap=" + receivedYearMap +
                ", myRecordInfoMonthContainer=" + myRecordInfoMonthContainer +
                '}';
    }


    public ArrayList<Year> getFormattedYearList() {
        ArrayList<NewYearsMonthsData> yearsMonthsDataList = getReceivedYearMap();
        ArrayList<Year> yearList = new ArrayList<>();
        for (NewYearsMonthsData yearObject :
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
        ArrayList<NewYearsMonthsData> yearsMonthsDataList = getReceivedYearMap();
        HashSet<String> strings = new HashSet<>();
        for (NewYearsMonthsData yearObject :
                yearsMonthsDataList) {
            strings.add(String.valueOf(yearObject.getYear()));
        }
        return new ArrayList(strings);
    }
}
