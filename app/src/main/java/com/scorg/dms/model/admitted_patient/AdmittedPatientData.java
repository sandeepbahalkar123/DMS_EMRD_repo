package com.scorg.dms.model.admitted_patient;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdmittedPatientData implements Parcelable, Cloneable, Comparable<AdmittedPatientData> {


    @SerializedName("PatientId")
    @Expose
    private String patientId;
    @SerializedName("PatientName")
    @Expose
    private String patientName;
    @SerializedName("Status")
    @Expose
    private String appointmentStatus;
    @SerializedName("salutation")
    @Expose
    private String salutation;
    @SerializedName("patientImageUrl")
    @Expose
    private String patientImageUrl;
    @SerializedName("AppDate")
    @Expose
    private String appDate;
    @SerializedName("ContactNo")
    @Expose
    private String contactNo;
    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("OutstandingAmount")
    @Expose
    private String outstandingAmount = "";

    @SerializedName("PatAddress")
    @Expose
    private String PatAddress;

    @SerializedName("PatId")
    @Expose
    private String PatId;

    @SerializedName("Bed")
    @Expose
    private String BedNo;

    @SerializedName("AdmissionDate")
    @Expose
    private String AdmissionDate;

    @SerializedName("IsArchived")
    @Expose
    private boolean IsArchived;

    private boolean selectedGroupCheckbox;

    private String spannableString;

    public final static Creator<AdmittedPatientData> CREATOR = new Creator<AdmittedPatientData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AdmittedPatientData createFromParcel(Parcel in) {
            return new AdmittedPatientData(in);
        }

        public AdmittedPatientData[] newArray(int size) {
            return (new AdmittedPatientData[size]);
        }

    };


    protected AdmittedPatientData(Parcel in) {
        this.patientId = ((String) in.readValue((String.class.getClassLoader())));
        this.patientName = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.salutation = ((String) in.readValue((String.class.getClassLoader())));
        this.patientImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.appDate = ((String) in.readValue((String.class.getClassLoader())));
        this.contactNo = ((String) in.readValue((String.class.getClassLoader())));
        this.age = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.outstandingAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.PatAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.PatId = ((String) in.readValue((String.class.getClassLoader())));
        this.BedNo = ((String) in.readValue((String.class.getClassLoader())));
        this.AdmissionDate = ((String) in.readValue((String.class.getClassLoader())));
        this.IsArchived = ((boolean) in.readValue((boolean.class.getClassLoader())));

    }

    public String getAdmissionDate() {
        return AdmissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        AdmissionDate = admissionDate;
    }

    public String getBedNo() {
        return BedNo;
    }

    public void setBedNo(String bedNo) {
        BedNo = bedNo;
    }

    public boolean isSelectedGroupCheckbox() {
        return selectedGroupCheckbox;
    }

    public void setSelectedGroupCheckbox(boolean selectedGroupCheckbox) {
        this.selectedGroupCheckbox = selectedGroupCheckbox;
    }

    public String getPatId() {
        return PatId;
    }

    public void setPatId(String patId) {
        PatId = patId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getPatientImageUrl() {
        return patientImageUrl;
    }

    public void setPatientImageUrl(String patientImageUrl) {
        this.patientImageUrl = patientImageUrl;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPatAddress() {
        return PatAddress;
    }

    public void setPatAddress(String patAddress) {
        PatAddress = patAddress;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String getSpannableString() {
        return spannableString;
    }

    public void setSpannableString(String spannableString) {
        this.spannableString = spannableString;
    }

    public boolean isArchived() {
        return IsArchived;
    }

    public void setArchived(boolean archived) {
        IsArchived = archived;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(patientId);
        dest.writeValue(patientName);
        dest.writeValue(appointmentStatus);
        dest.writeValue(salutation);
        dest.writeValue(patientImageUrl);
        dest.writeValue(appDate);
        dest.writeValue(contactNo);
        dest.writeValue(age);
        dest.writeValue(gender);
        dest.writeValue(outstandingAmount);
        dest.writeValue(PatAddress);
        dest.writeValue(PatId);
        dest.writeValue(BedNo);
        dest.writeValue(AdmissionDate);
        dest.writeValue(IsArchived);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull AdmittedPatientData o) {
        return 0;
    }
}
