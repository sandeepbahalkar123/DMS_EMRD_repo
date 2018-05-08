package com.scorg.dms.model.my_records.new_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewDoctorDetail {

    @SerializedName("docId")
    @Expose
    private int docId;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;
    @SerializedName("opdId")
    @Expose
    private int opdId;
    @SerializedName("opdDate")
    @Expose
    private String opdDate;
    @SerializedName("doctorSpeciality")
    @Expose
    private String doctorSpeciality;
    @SerializedName("doctorAddress")
    @Expose
    private String doctorAddress;
    @SerializedName("doctorImageUrl")
    @Expose
    private String doctorImageUrl;

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getOpdId() {
        return opdId;
    }

    public void setOpdId(int opdId) {
        this.opdId = opdId;
    }

    public String getOpdDate() {
        return opdDate;
    }

    public void setOpdDate(String opdDate) {
        this.opdDate = opdDate;
    }

    public String getDoctorSpeciality() {
        return doctorSpeciality;
    }

    public void setDoctorSpeciality(String doctorSpeciality) {
        this.doctorSpeciality = doctorSpeciality;
    }

    public String getDoctorAddress() {
        return doctorAddress;
    }

    public void setDoctorAddress(String doctorAddress) {
        this.doctorAddress = doctorAddress;
    }

    public String getDoctorImageUrl() {
        return doctorImageUrl;
    }

    public void setDoctorImageUrl(String doctorImageUrl) {
        this.doctorImageUrl = doctorImageUrl;
    }

}