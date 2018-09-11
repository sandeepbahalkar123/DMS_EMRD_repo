
package com.scorg.dms.model.patient.template_sms.request_send_sms;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClinicListForSms implements Parcelable ,Cloneable{

    @SerializedName("templateContent")
    @Expose
    private String templateContent;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("clinicId")
    @Expose
    private Integer clinicId;
    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("patientIds")
    @Expose
    private ArrayList<PatientInfoList> patientInfoList = new ArrayList<PatientInfoList>();
    private String clinicName;

    public final static Parcelable.Creator<ClinicListForSms> CREATOR = new Creator<ClinicListForSms>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ClinicListForSms createFromParcel(Parcel in) {
            return new ClinicListForSms(in);
        }

        public ClinicListForSms[] newArray(int size) {
            return (new ClinicListForSms[size]);
        }

    }
            ;

    protected ClinicListForSms(Parcel in) {
        this.clinicId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.patientInfoList, (PatientInfoList.class.getClassLoader()));
        this.templateContent = ((String) in.readValue((String.class.getClassLoader())));
        this.clinicName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ClinicListForSms() {
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getClinicId() {
        return clinicId;
    }

    public void setClinicId(Integer clinicId) {
        this.clinicId = clinicId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public ArrayList<PatientInfoList> getPatientInfoList() {
        return patientInfoList;
    }

    public void setPatientInfoList(ArrayList<PatientInfoList> patientInfoList) {
        this.patientInfoList = patientInfoList;
    }
    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(clinicId);
        dest.writeValue(docId);
        dest.writeValue(locationId);
        dest.writeList(patientInfoList);
        dest.writeValue(templateContent);
        dest.writeValue(clinicName);
    }

    public int describeContents() {
        return 0;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
