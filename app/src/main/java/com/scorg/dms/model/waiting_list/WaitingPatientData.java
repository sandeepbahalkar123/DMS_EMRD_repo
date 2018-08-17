
package com.scorg.dms.model.waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaitingPatientData implements Parcelable {

    @SerializedName("PatientId")
    @Expose
    private String patientId;
    @SerializedName("PatientName")
    @Expose
    private String patientName;
    @SerializedName("Status")
    @Expose
    private String waitingStatus;
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

    @SerializedName("PatAddress")
    @Expose
    private String PatAddress;

    public final static Creator<WaitingPatientData> CREATOR = new Creator<WaitingPatientData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WaitingPatientData createFromParcel(Parcel in) {
            return new WaitingPatientData(in);
        }

        public WaitingPatientData[] newArray(int size) {
            return (new WaitingPatientData[size]);
        }

    };

    protected WaitingPatientData(Parcel in) {
        this.patientId = ((String) in.readValue((String.class.getClassLoader())));
        this.patientName = ((String) in.readValue((String.class.getClassLoader())));
        this.waitingStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.salutation = ((String) in.readValue((String.class.getClassLoader())));
        this.patientImageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.appDate = ((String) in.readValue((String.class.getClassLoader())));
        this.contactNo = ((String) in.readValue((String.class.getClassLoader())));
        this.PatAddress = ((String) in.readValue((String.class.getClassLoader())));

    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(patientId);
        dest.writeValue(patientName);
        dest.writeValue(waitingStatus);
        dest.writeValue(salutation);
        dest.writeValue(patientImageUrl);
        dest.writeValue(appDate);
        dest.writeValue(contactNo);
        dest.writeValue(PatAddress);

    }

    public int describeContents() {
        return 0;
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

    public String getWaitingStatus() {
        return waitingStatus;
    }

    public void setWaitingStatus(String status) {
        this.waitingStatus = status;
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

    public String getPatAddress() {
        return PatAddress;
    }

    public void setPatAddress(String patAddress) {
        PatAddress = patAddress;
    }
}
