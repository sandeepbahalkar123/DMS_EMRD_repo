package com.scorg.dms.model.patient.add_new_patient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class PatientDetail implements CustomResponse {

    @SerializedName("mobilePatientId")
    @Expose
    private int mobilePatientId;
    @SerializedName("patientFname")
    @Expose
    private String patientFname;
    @SerializedName("patientMname")
    @Expose
    private String patientMname;
    @SerializedName("patientLname")
    @Expose
    private String patientLname;
    @SerializedName("clinicId")
    @Expose
    private int clinicId;
    @SerializedName("patientAge")
    @Expose
    private String patientAge;
    @SerializedName("patientDob")
    @Expose
    private String patientDob;
    @SerializedName("patientPhone")
    @Expose
    private String patientPhone;
    @SerializedName("patientGender")
    @Expose
    private String patientGender;
    @SerializedName("referenceId")
    @Expose
    private String offlineReferenceID;


    public int getMobilePatientId() {
        return mobilePatientId;
    }

    public void setMobilePatientId(int mobilePatientId) {
        this.mobilePatientId = mobilePatientId;
    }

    public String getPatientFname() {
        return patientFname;
    }

    public void setPatientFname(String patientFname) {
        this.patientFname = patientFname;
    }

    public String getPatientMname() {
        return patientMname;
    }

    public void setPatientMname(String patientMname) {
        this.patientMname = patientMname;
    }

    public String getPatientLname() {
        return patientLname;
    }

    public void setPatientLname(String patientLname) {
        this.patientLname = patientLname;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientDob() {
        return patientDob;
    }

    public void setPatientDob(String patientDob) {
        this.patientDob = patientDob;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getOfflineReferenceID() {
        return offlineReferenceID;
    }

    public void setOfflineReferenceID(String offlineReferenceID) {
        this.offlineReferenceID = offlineReferenceID;
    }
}