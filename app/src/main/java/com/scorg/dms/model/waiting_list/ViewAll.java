
package com.scorg.dms.model.waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.util.CommonMethods;

public class ViewAll implements Parcelable {

    @SerializedName("waitingId")
    @Expose
    private Integer waitingId;
    @SerializedName("hospitalPatId")
    @Expose
    private Integer hospitalPatId;
    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("patientPhone")
    @Expose
    private String patientPhone;
    @SerializedName("patientImageUrl")
    @Expose
    private String patientImageUrl;
    @SerializedName("waitingSequence")
    @Expose
    private Integer waitingSequence;
    @SerializedName("tokenNumber")
    @Expose
    private String tokenNumber;
    @SerializedName("waitingInTime")
    @Expose
    private String waitingInTime;
    @SerializedName("waitingStatusId")
    @Expose
    private Integer waitingStatusId;
    @SerializedName("waitingStatus")
    @Expose
    private String waitingStatus;
    @SerializedName("appointmentTime")
    @Expose
    private String appointmentTime;
    @SerializedName("appointmentStatusId")
    @Expose
    private String appointmentStatusId;

    @SerializedName("salutation")
    @Expose
    private Integer salutation = 0;

    @SerializedName("referenceId")
    @Expose
    private String referenceID;

    public final static Creator<ViewAll> CREATOR = new Creator<ViewAll>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ViewAll createFromParcel(Parcel in) {
            return new ViewAll(in);
        }

        public ViewAll[] newArray(int size) {
            return (new ViewAll[size]);
        }

    };

    protected ViewAll(Parcel in) {
        this.waitingId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.hospitalPatId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientName = ((String) in.readValue((String.class.getClassLoader())));
        this.patientPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.patientImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.waitingSequence = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.tokenNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.waitingInTime = ((String) in.readValue((String.class.getClassLoader())));
        this.waitingStatusId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentTime = ((String) in.readValue((String.class.getClassLoader())));
        this.appointmentStatusId = ((String) in.readValue((String.class.getClassLoader())));
        this.salutation = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.referenceID = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ViewAll() {
    }

    public Integer getWaitingId() {
        return waitingId;
    }

    public void setWaitingId(Integer waitingId) {
        this.waitingId = waitingId;
    }

    public Integer getHospitalPatId() {
        return hospitalPatId;
    }

    public void setHospitalPatId(Integer hospitalPatId) {
        this.hospitalPatId = hospitalPatId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return CommonMethods.toCamelCase(patientName);
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    public Integer getWaitingSequence() {
        return waitingSequence;
    }

    public void setWaitingSequence(Integer waitingSequence) {
        this.waitingSequence = waitingSequence;
    }

    public String getTokenNumber() {
        return tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    public String getWaitingInTime() {
        return waitingInTime;
    }

    public void setWaitingInTime(String waitingInTime) {
        this.waitingInTime = waitingInTime;
    }

    public Integer getWaitingStatusId() {
        return waitingStatusId;
    }

    public void setWaitingStatusId(Integer waitingStatusId) {
        this.waitingStatusId = waitingStatusId;
    }

    public String getWaitingStatus() {
        return waitingStatus;
    }

    public void setWaitingStatus(String waitingStatus) {
        this.waitingStatus = waitingStatus;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getAppointmentStatusId() {
        return appointmentStatusId;
    }

    public void setAppointmentStatusId(String appointmentStatusId) {
        this.appointmentStatusId = appointmentStatusId;
    }

    public Integer getSalutation() {
        return salutation;
    }

    public void setSalutation(Integer salutation) {
        this.salutation = salutation;
    }

    public String getReferenceID() {
        return referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(waitingId);
        dest.writeValue(hospitalPatId);
        dest.writeValue(patientId);
        dest.writeValue(patientName);
        dest.writeValue(patientPhone);
        dest.writeValue(patientImageUrl);
        dest.writeValue(waitingSequence);
        dest.writeValue(tokenNumber);
        dest.writeValue(waitingInTime);
        dest.writeValue(waitingStatusId);
        dest.writeValue(waitingStatus);
        dest.writeValue(appointmentTime);
        dest.writeValue(appointmentStatusId);
        dest.writeValue(salutation);
        dest.writeValue(referenceID);
    }

    public int describeContents() {
        return 0;
    }

}
