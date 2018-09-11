package com.scorg.dms.model.dms_models.responsemodel.episode_list;

/**
 * Created by sandeep on 2/22/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PatientEpisodeFileData implements Serializable {

    @SerializedName("FileType")
    @Expose
    private String fileType;
    @SerializedName("RecordId")
    @Expose
    private int recordId;
    @SerializedName("FileTypeRefId")
    @Expose
    private String fileTypeRefId;
    @SerializedName("AdmissionDate")
    @Expose
    private String admissionDate;
    @SerializedName("DischargeDate")
    @Expose
    private String dischargeDate;
    @SerializedName("DoctorName")
    @Expose
    private String doctorName;

    @SerializedName("PatId")
    @Expose
    private String patId;

    //-- THIS IS ADDED TO MAINTAIN RESPECTIVE PARENT PATIENT ID
    private String respectiveParentPatientID;

    private boolean isShowCompleteList = false;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileTypeRefId() {
        return fileTypeRefId;
    }

    public void setFileTypeRefId(String fileTypeRefId) {
        this.fileTypeRefId = fileTypeRefId;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String getRespectiveParentPatientID() {
        return respectiveParentPatientID;
    }

    public void setRespectiveParentPatientID(String respectiveParentPatientID) {
        this.respectiveParentPatientID = respectiveParentPatientID;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }
}
