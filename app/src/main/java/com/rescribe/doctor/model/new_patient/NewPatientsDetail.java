
package com.rescribe.doctor.model.new_patient;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewPatientsDetail implements Parcelable {

    @SerializedName("salutation")
    @Expose
    private Integer salutation;
    @SerializedName("patientId")
    @Expose
    private Integer patientID;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("gender")
    @Expose
    private String patientGender;
    @SerializedName("patientPhone")
    @Expose
    private String patientPhon;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("patientDob")
    @Expose
    private String patientDob;

    @SerializedName("patientImageUrl")
    @Expose
    private String profilePhoto;
    @SerializedName("patientEmail")
    @Expose
    private String patientEmail;
    @SerializedName("outstandingAmount")
    @Expose
    private String outstandingAmount;
    @SerializedName("patientCity")
    @Expose
    private String cityName;
    @SerializedName("hospitalPatId")
    @Expose
    private Integer hospitalPatId;
    @SerializedName("referenceId")
    @Expose
    private String referenceId;
    @SerializedName("clinicId")
    @Expose
    private Integer clinicId;
    private String spannableString;
    private boolean selected;
    public final static Creator<NewPatientsDetail> CREATOR = new Creator<NewPatientsDetail>() {


        @SuppressWarnings({
                "unchecked"
        })
        public NewPatientsDetail createFromParcel(Parcel in) {
            return new NewPatientsDetail(in);
        }

        public NewPatientsDetail[] newArray(int size) {
            return (new NewPatientsDetail[size]);
        }

    };

    protected NewPatientsDetail(Parcel in) {
        this.clinicId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.salutation = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientID = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientName = ((String) in.readValue((String.class.getClassLoader())));
        this.patientGender = ((String) in.readValue((String.class.getClassLoader())));
        this.patientPhon = ((String) in.readValue((String.class.getClassLoader())));
        this.age = ((String) in.readValue((String.class.getClassLoader())));
        this.patientDob = ((String) in.readValue((String.class.getClassLoader())));
        this.profilePhoto = ((String) in.readValue((String.class.getClassLoader())));
        this.patientEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.outstandingAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.cityName = ((String) in.readValue((String.class.getClassLoader())));
        this.hospitalPatId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.referenceId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public NewPatientsDetail() {
    }

    public String getSpannableString() {
        return spannableString;
    }

    public void setSpannableString(String spannableString) {
        this.spannableString = spannableString;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getSalutation() {
        return salutation;
    }

    public void setSalutation(Integer salutation) {
        this.salutation = salutation;
    }

    public Integer getPatientID() {
        return patientID;
    }


    public void setPatientID(Integer patientID) {
        this.patientID = patientID;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }
    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientPhon() {
        return patientPhon;
    }

    public void setPatientPhon(String patientPhon) {
        this.patientPhon = patientPhon;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPatientDob() {
        return patientDob;
    }

    public void setPatientDob(String patientDob) {
        this.patientDob = patientDob;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getHospitalPatId() {
        return hospitalPatId;
    }

    public void setHospitalPatId(Integer hospitalPatId) {
        this.hospitalPatId = hospitalPatId;
    }


    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicId);
        dest.writeValue(salutation);
        dest.writeValue(patientID);
        dest.writeValue(patientName);
        dest.writeValue(patientGender);
        dest.writeValue(patientPhon);
        dest.writeValue(age);
        dest.writeValue(patientDob);
        dest.writeValue(profilePhoto);
        dest.writeValue(patientEmail);
        dest.writeValue(outstandingAmount);
        dest.writeValue(cityName);
        dest.writeValue(hospitalPatId);
        dest.writeValue(referenceId);
    }

    public int describeContents() {
        return 0;
    }

}
