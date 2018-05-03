package com.rescribe.doctor.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SpinnerDoctor {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("docImg")
    @Expose
    private String docImg;
    @SerializedName("visitDates")
    @Expose
    private ArrayList<VisitDate> dates = new ArrayList<VisitDate>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDocImg() {
        return docImg;
    }

    public void setDocImg(String docImg) {
        this.docImg = docImg;
    }

    public ArrayList<VisitDate> getDates() {
        return dates;
    }

    public void setDates(ArrayList<VisitDate> dates) {
        this.dates = dates;
    }

}