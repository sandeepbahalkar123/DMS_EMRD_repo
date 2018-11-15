package com.scorg.dms.model.my_appointments;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentPatientData implements Parcelable, Cloneable, Comparable<AppointmentPatientData> {


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

    @SerializedName("consultationType")
    @Expose
    private String consultationType;

    @SerializedName("IsArchived")
    @Expose
    private boolean IsArchived;

    @SerializedName("appointmentCode")
    @Expose
    private int AppointmentCode;

    private boolean selectedGroupCheckbox;

    private String spannableString;

    public final static Creator<AppointmentPatientData> CREATOR = new Creator<AppointmentPatientData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public AppointmentPatientData createFromParcel(Parcel in) {
            return new AppointmentPatientData(in);
        }

        public AppointmentPatientData[] newArray(int size) {
            return (new AppointmentPatientData[size]);
        }

    };


    protected AppointmentPatientData(Parcel in) {
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
        this.consultationType = ((String) in.readValue((String.class.getClassLoader())));
        this.IsArchived = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.AppointmentCode = ((int) in.readValue((int.class.getClassLoader())));

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


    public String getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(String consultationType) {
        this.consultationType = consultationType;
    }

    public boolean isArchived() {
        return IsArchived;
    }

    public void setArchived(boolean archived) {
        IsArchived = archived;
    }

    public int getAppointmentCode() {
        return AppointmentCode;
    }

    public void setAppointmentCode(int appointmentCode) {
        AppointmentCode = appointmentCode;
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
        dest.writeValue(consultationType);
        dest.writeValue(IsArchived);
        dest.writeValue(AppointmentCode);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull AppointmentPatientData o) {
        return 0;
    }
}
