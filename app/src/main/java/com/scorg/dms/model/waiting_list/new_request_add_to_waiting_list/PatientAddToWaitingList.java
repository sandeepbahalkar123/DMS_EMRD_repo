
package com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientAddToWaitingList implements Parcelable
{

    @SerializedName("patientId")
    @Expose
    private String patientId;
    @SerializedName("hospitalPatId")
    @Expose
    private String hospitalPatId;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("appointmentId")
    @Expose
    private Integer appointmentId;
    @SerializedName("appointmentStatusId")
    @Expose
    private Integer appointmentStatusId;
    public final static Creator<PatientAddToWaitingList> CREATOR = new Creator<PatientAddToWaitingList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public PatientAddToWaitingList createFromParcel(Parcel in) {
            return new PatientAddToWaitingList(in);
        }

        public PatientAddToWaitingList[] newArray(int size) {
            return (new PatientAddToWaitingList[size]);
        }

    }
    ;

    protected PatientAddToWaitingList(Parcel in) {
        this.appointmentStatusId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.appointmentId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientId = ((String) in.readValue((String.class.getClassLoader())));
        this.hospitalPatId = ((String) in.readValue((String.class.getClassLoader())));
        this.patientName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PatientAddToWaitingList() {
    }
    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Integer getAppointmentStatusId() {
        return appointmentStatusId;
    }

    public void setAppointmentStatusId(Integer appointmentStatusId) {
        this.appointmentStatusId = appointmentStatusId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getHospitalPatId() {
        return hospitalPatId;
    }

    public void setHospitalPatId(String hospitalPatId) {
        this.hospitalPatId = hospitalPatId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(appointmentStatusId);
        dest.writeValue(appointmentId);
        dest.writeValue(patientId);
        dest.writeValue(hospitalPatId);
        dest.writeValue(patientName);
    }

    public int describeContents() {
        return  0;
    }

}
