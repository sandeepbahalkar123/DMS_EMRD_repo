package com.rescribe.doctor.model.appointments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.util.CommonMethods;
import com.rescribe.doctor.util.RescribeConstants;

import java.io.Serializable;

/**
 * Created by riteshpandhurkar on 19/7/17.
 */

public class AptList implements CustomResponse, Serializable {

    @SerializedName("hospital_pat_id")
    @Expose
    private String hospital_pat_id;
    @SerializedName("doc_id")
    @Expose
    private int doc_id;
    @SerializedName("locationId")
    @Expose
    private String locationId;
    @SerializedName("doctorDegree")
    @Expose
    private String doctorDegree;
    @SerializedName("docPhone")
    @Expose
    private String docPhone;
    @SerializedName("aptId")
    @Expose
    private String id;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("cityName")
    @Expose
    private String city_name;
    @SerializedName("areaName")
    @Expose
    private String area_name;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("aptDate")
    @Expose
    private String aptDate;
    @SerializedName("aptStatus")
    @Expose
    private String aptStatus; //upcoming n all
    @SerializedName("aptTime")
    @Expose
    private String aptTime;
    @SerializedName("clinic_name")
    @Expose
    private String clinic_name;
    @SerializedName("rating")
    @Expose
    private double rating;
    @SerializedName("type")
    @Expose
    private String confirmationType; // token/appointment to show confirmation screen.
    @SerializedName("tokenNumber")
    @Expose
    private String tokenNumber;
    @SerializedName("waitingPatientCount")
    @Expose
    private String waitingPatientCount;
    @SerializedName("waitingPatientTime")
    @Expose
    private String waitingPatientTime;
    @SerializedName("clinicAddress")
    @Expose
    private String clinicAddress;


    //--------------------------


    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getAptTime() {
        return aptTime;
    }

    public void setAptTime(String aptTime) {
        this.aptTime = aptTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setAptDate(String aptDate) {
        this.aptDate = aptDate;
    }

    public String getAptStatus() {
        return aptStatus;
    }

    public void setAptStatus(String aptStatus) {
        this.aptStatus = aptStatus;
    }

    public String getHospital_pat_id() {
        return hospital_pat_id;
    }

    public void setHospital_pat_id(String hospital_pat_id) {
        this.hospital_pat_id = hospital_pat_id;
    }

    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDoctorDegree() {
        return doctorDegree;
    }

    public void setDoctorDegree(String doctorDegree) {
        this.doctorDegree = doctorDegree;
    }

    public String getDocPhone() {
        return docPhone;
    }

    public void setDocPhone(String docPhone) {
        this.docPhone = docPhone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getConfirmationType() {
        return confirmationType;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public void setConfirmationType(String confirmationType) {
        this.confirmationType = confirmationType;
    }

    public String getWaitingPatientCount() {
        return waitingPatientCount;
    }

    public void setWaitingPatientCount(String waitingPatientCount) {
        this.waitingPatientCount = waitingPatientCount;
    }

    public String getWaitingPatientTime() {
        return waitingPatientTime;
    }

    public void setWaitingPatientTime(String waitingPatientTime) {
        this.waitingPatientTime = waitingPatientTime;
    }

    //the time which is coming in aptTime is time of appointment
    public String getAptDate() {
        if (aptDate.contains("T")) {
            String date[] = aptDate.split("T");
            String dateBeforeTime = date[0];
            aptDate = CommonMethods.formatDateTime(dateBeforeTime + "T" + aptTime + ".000Z", RescribeConstants.DATE_PATTERN.YYYY_MM_DD_hh_mm_a, RescribeConstants.DATE_PATTERN.UTC_PATTERN, RescribeConstants.DATE);
        }
        return aptDate;
    }
}