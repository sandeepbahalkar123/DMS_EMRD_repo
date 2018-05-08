
package com.scorg.dms.model.patient.doctor_patients;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class PatientList implements Parcelable, Comparable<PatientList>, CustomResponse {

    @SerializedName("salutation")
    @Expose
    private Integer salutation;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("patientDob")
    @Expose
    private String dateOfBirth;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("outstandingAmount")
    @Expose
    private String outStandingAmount;
    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("patientPhone")
    @Expose
    private String patientPhone;
    @SerializedName("patientImageUrl")
    @Expose
    private String patientImageUrl;
    @SerializedName("patientEmail")
    @Expose
    private String patientEmail;
    @SerializedName("clinicId")
    @Expose
    private int clinicId;
    @SerializedName("clinicName")
    @Expose
    private String clinicName;
    @SerializedName("hospitalPatId")
    @Expose
    private Integer hospitalPatId;

    @SerializedName("patientCityId")
    @Expose
    private int patientCityId;

    @SerializedName("patientCity")
    @Expose
    private String patientCity = "";

    @SerializedName("patientArea")
    @Expose
    private String patientArea = "";
    @SerializedName("aptId")
    @Expose
    private Integer aptId;

    @SerializedName("creationDate")
    @Expose
    private String creationDate;
    @SerializedName("referenceId")
    @Expose
    private String referenceID;

    private String spannableString;
    private boolean selected;
    private boolean isAddedMiddleName;
    //--Added for offline adding patient.

    private boolean isOfflinePatientSynced = true; //considered always sync with server.

    public final static Creator<PatientList> CREATOR = new Creator<PatientList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PatientList createFromParcel(Parcel in) {
            return new PatientList(in);
        }

        public PatientList[] newArray(int size) {
            return (new PatientList[size]);
        }

    };

    protected PatientList(Parcel in) {
        this.salutation = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientName = ((String) in.readValue((String.class.getClassLoader())));
        this.age = ((String) in.readValue((String.class.getClassLoader())));
        this.dateOfBirth = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.outStandingAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.patientId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.patientImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.patientEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicId = ((int) in.readValue((int.class.getClassLoader())));
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
        this.hospitalPatId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientCityId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientCity = ((String) in.readValue((String.class.getClassLoader())));
        this.aptId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.creationDate = ((String) in.readValue((String.class.getClassLoader())));
        this.patientArea = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PatientList() {
    }

    public Integer getSalutation() {
        return salutation;
    }

    public void setSalutation(Integer salutation) {
        this.salutation = salutation;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOutStandingAmount() {
        return outStandingAmount;
    }

    public void setOutStandingAmount(String outStandingAmount) {
        this.outStandingAmount = outStandingAmount;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getPatientImageUrl() {
        return patientImageUrl;
    }

    public void setPatientImageUrl(String patientImageUrl) {
        this.patientImageUrl = patientImageUrl;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSpannableString() {
        return spannableString;
    }

    public void setSpannableString(String spannableString) {
        this.spannableString = spannableString;
    }

    public Integer getAptId() {
        return aptId;
    }

    public void setAptId(Integer aptId) {
        this.aptId = aptId;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public Integer getHospitalPatId() {
        return hospitalPatId;
    }

    public void setHospitalPatId(Integer hospitalPatId) {
        this.hospitalPatId = hospitalPatId;
    }

    public int getPatientCityId() {
        return patientCityId;
    }

    public void setPatientCityId(int patientCityId) {
        this.patientCityId = patientCityId;
    }

    public String getPatientCity() {
        return patientCity;
    }

    public void setPatientCity(String patientCity) {
        this.patientCity = patientCity;
    }

    public void setPatientArea(String patientArea) {
        this.patientArea = patientArea;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(salutation);
        dest.writeValue(patientName);
        dest.writeValue(age);
        dest.writeValue(dateOfBirth);
        dest.writeValue(gender);
        dest.writeValue(outStandingAmount);
        dest.writeValue(patientId);
        dest.writeValue(patientPhone);
        dest.writeValue(patientImageUrl);
        dest.writeValue(patientEmail);
        dest.writeValue(clinicId);
        dest.writeValue(clinicName);
        dest.writeValue(hospitalPatId);
        dest.writeValue(patientCityId);
        dest.writeValue(patientCity);
        dest.writeValue(aptId);
        dest.writeValue(creationDate);
        dest.writeValue(patientArea);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull PatientList o) {
        return 0;
    }

    public String getPatientArea() {
        return patientArea;
    }


    public boolean isOfflinePatientSynced() {
        return isOfflinePatientSynced;
    }

    public void setOfflinePatientSynced(boolean offlinePatientSynced) {
        isOfflinePatientSynced = offlinePatientSynced;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }
}
