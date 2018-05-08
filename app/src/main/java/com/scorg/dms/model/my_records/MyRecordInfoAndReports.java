
package com.scorg.dms.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class MyRecordInfoAndReports implements CustomResponse, Serializable {
    @SerializedName("doctorDetail")
    @Expose
    private MyRecordDoctorInfo myRecordDoctorInfo;

    @SerializedName("reports")
    @Expose
    private ArrayList<MyRecordReports> myRecordReportInfo;


    private boolean isCaseDetailHeader = false;

    public boolean isCaseDetailHeader() {
        return isCaseDetailHeader;
    }

    public void setCaseDetailHeader(boolean caseDetailHeader) {
        isCaseDetailHeader = caseDetailHeader;
    }

    public MyRecordDoctorInfo getMyRecordDoctorInfo() {
        return myRecordDoctorInfo;
    }

    public void setMyRecordDoctorInfo(MyRecordDoctorInfo myRecordDoctorInfo) {
        this.myRecordDoctorInfo = myRecordDoctorInfo;
    }

    public ArrayList<MyRecordReports> getMyRecordReportInfo() {
        return myRecordReportInfo;
    }

    public void setMyRecordReportInfo(ArrayList<MyRecordReports> myRecordReportInfo) {
        this.myRecordReportInfo = myRecordReportInfo;
    }
}
