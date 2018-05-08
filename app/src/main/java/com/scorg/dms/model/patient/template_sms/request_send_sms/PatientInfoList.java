
package com.scorg.dms.model.patient.template_sms.request_send_sms;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.util.CommonMethods;

public class PatientInfoList implements Parcelable {

    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("hospitalPatId")
    @Expose
    private Integer hospitalPatId;
    @SerializedName("patientPhone")
    @Expose
    private String patientPhone;
    private String patientName;

    public final static Parcelable.Creator<PatientInfoList> CREATOR = new Creator<PatientInfoList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PatientInfoList createFromParcel(Parcel in) {
            return new PatientInfoList(in);
        }

        public PatientInfoList[] newArray(int size) {
            return (new PatientInfoList[size]);
        }

    };

    protected PatientInfoList(Parcel in) {
        this.hospitalPatId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.patientPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.patientName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public PatientInfoList() {
    }


    public String getPatientName() {
        return CommonMethods.toCamelCase(patientName);
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getHospitalPatId() {
        return hospitalPatId;
    }

    public void setHospitalPatId(Integer hospitalPatId) {
        this.hospitalPatId = hospitalPatId;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(hospitalPatId);
        dest.writeValue(patientId);
        dest.writeValue(patientPhone);
        dest.writeValue(patientName);
    }

    public int describeContents() {
        return 0;
    }
}
