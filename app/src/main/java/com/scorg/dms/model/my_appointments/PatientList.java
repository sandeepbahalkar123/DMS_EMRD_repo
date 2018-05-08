
package com.scorg.dms.model.my_appointments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientList implements Parcelable, Comparable<PatientList> {

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
    @SerializedName("aptId")
    @Expose
    private Integer aptId;
    @SerializedName("opdId")
    @Expose
    private Integer opdId;
    @SerializedName("patientPhone")
    @Expose
    private String patientPhone;
    @SerializedName("patientImageUrl")
    @Expose
    private String patientImageUrl;
    @SerializedName("appointmentDate")
    @Expose
    private String appointmentDate;
    @SerializedName("appointmentTime")
    @Expose
    private String appointmentTime;
    @SerializedName("appointmentStatus")
    @Expose
    private String appointmentStatus;

    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("hospitalPatId")
    @Expose
    private Integer hospitalPatId;
    @SerializedName("referenceId")
    @Expose
    private String referenceId;
    @SerializedName("appointmentEndTime")
    @Expose
    private String appointmentEndTime;
    @SerializedName("appointmentStatusId")
    @Expose
    private Integer appointmentStatusId;
    @SerializedName("addedToWaiting")
    @Expose
    private Integer addedToWaiting;

    @SerializedName("patientCity")
    @Expose
    private String patientCity = "";

    @SerializedName("patientArea")
    @Expose
    private String patientArea = "";

    private String spannableString;
    private boolean selected;
    private String patientEmail;

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
        this.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.hospitalPatId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientName = ((String) in.readValue((String.class.getClassLoader())));
        this.age = ((String) in.readValue((String.class.getClassLoader())));
        this.dateOfBirth = ((String) in.readValue((String.class.getClassLoader())));
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.outStandingAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.patientId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.aptId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.opdId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.patientImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentDate = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentTime = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentEndTime = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentStatusId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.addedToWaiting = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientCity = ((String) in.readValue((String.class.getClassLoader())));
        this.patientArea = ((String) in.readValue((String.class.getClassLoader())));
        this.referenceId = ((String) in.readValue((String.class.getClassLoader())));
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

    public Integer getAptId() {
        return aptId;
    }

    public void setAptId(Integer aptId) {
        this.aptId = aptId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getHospitalPatId() {
        return hospitalPatId;
    }

    public void setHospitalPatId(Integer hospitalPatId) {
        this.hospitalPatId = hospitalPatId;
    }

    public Integer getOpdId() {
        return opdId;
    }

    public void setOpdId(Integer opdId) {
        this.opdId = opdId;
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

    public String getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Integer getAddedToWaiting() {
        return addedToWaiting;
    }

    public void setAddedToWaiting(Integer addedToWaiting) {
        this.addedToWaiting = addedToWaiting;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public void setAppointmentEndTime(String appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public Integer getAppointmentStatusId() {
        return appointmentStatusId;
    }

    public void setAppointmentStatusId(Integer appointmentStatusId) {
        this.appointmentStatusId = appointmentStatusId;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getSpannableString() {
        return spannableString;
    }

    public void setSpannableString(String spannableString) {
        this.spannableString = spannableString;
    }

    public String getPatientCity() {
        return patientCity;
    }

    public void setPatientCity(String patientCity) {
        this.patientCity = patientCity;
    }

    public String getPatientArea() {
        return patientArea;
    }

    public void setPatientArea(String patientArea) {
        this.patientArea = patientArea;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(salutation);
        dest.writeValue(docId);
        dest.writeValue(hospitalPatId);
        dest.writeValue(patientName);
        dest.writeValue(age);
        dest.writeValue(dateOfBirth);
        dest.writeValue(gender);
        dest.writeValue(outStandingAmount);
        dest.writeValue(patientId);
        dest.writeValue(aptId);
        dest.writeValue(opdId);
        dest.writeValue(patientPhone);
        dest.writeValue(patientImageUrl);
        dest.writeValue(appointmentDate);
        dest.writeValue(appointmentTime);
        dest.writeValue(appointmentStatus);
        dest.writeValue(appointmentEndTime);
        dest.writeValue(appointmentStatusId);
        dest.writeValue(addedToWaiting);
        dest.writeValue(patientCity);
        dest.writeValue(patientArea);
        dest.writeValue(referenceId);

    }

    public int describeContents() {
        return 0;
    }

    @Override
    public int compareTo(@NonNull PatientList o) {
        return 0;
    }
}
