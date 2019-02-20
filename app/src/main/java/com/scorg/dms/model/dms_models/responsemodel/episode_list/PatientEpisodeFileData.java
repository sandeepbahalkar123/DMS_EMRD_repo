package com.scorg.dms.model.dms_models.responsemodel.episode_list;

/**
 * Created by sandeep on 2/22/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PatientEpisodeFileData implements Serializable {

    @SerializedName("RecordId")
    @Expose
    private int recordId;
    @SerializedName("FileType")
    @Expose
    private String fileType;
    @SerializedName("FileTypeRefId")
    @Expose
    private String fileTypeRefId;
    @SerializedName("AdmissionDate")
    @Expose
    private String admissionDate;
    @SerializedName("DateTmeAdmissionDate")
    @Expose
    private String dateTmeAdmissionDate;
    @SerializedName("DischargeDate")
    @Expose
    private String dischargeDate;
    @SerializedName("DateTmeDischargeDate")
    @Expose
    private Object dateTmeDischargeDate;
    @SerializedName("ProcessDate")
    @Expose
    private String processDate;
    @SerializedName("DateTmeProcessDate")
    @Expose
    private String dateTmeProcessDate;
    @SerializedName("Patientid")
    @Expose
    private String patientid;
    @SerializedName("PatientName")
    @Expose
    private String patientName;
    @SerializedName("DoctorID")
    @Expose
    private int doctorID;
    @SerializedName("DoctorName")
    @Expose
    private String doctorName;
    @SerializedName("PatId")
    @Expose
    private int patId;
    @SerializedName("USER_DOCTOR_ID")
    @Expose
    private int uSERDOCTORID;
    @SerializedName("IsView")
    @Expose
    private boolean isView;
    @SerializedName("IsPrimary")
    @Expose
    private boolean isPrimary;
    @SerializedName("IsOldArchivedRecord")
    @Expose
    private boolean isOldArchivedRecord;

    @SerializedName("IsFileAcessible")
    @Expose
    private boolean isFileAcessible;


    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

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

    public String getDateTmeAdmissionDate() {
        return dateTmeAdmissionDate;
    }

    public void setDateTmeAdmissionDate(String dateTmeAdmissionDate) {
        this.dateTmeAdmissionDate = dateTmeAdmissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public Object getDateTmeDischargeDate() {
        return dateTmeDischargeDate;
    }

    public void setDateTmeDischargeDate(Object dateTmeDischargeDate) {
        this.dateTmeDischargeDate = dateTmeDischargeDate;
    }

    public String getProcessDate() {
        return processDate;
    }

    public void setProcessDate(String processDate) {
        this.processDate = processDate;
    }

    public String getDateTmeProcessDate() {
        return dateTmeProcessDate;
    }

    public void setDateTmeProcessDate(String dateTmeProcessDate) {
        this.dateTmeProcessDate = dateTmeProcessDate;
    }

    public String getPatientid() {
        return patientid;
    }

    public void setPatientid(String patientid) {
        this.patientid = patientid;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getPatId() {
        return patId;
    }

    public void setPatId(int patId) {
        this.patId = patId;
    }

    public int getUSERDOCTORID() {
        return uSERDOCTORID;
    }

    public void setUSERDOCTORID(int uSERDOCTORID) {
        this.uSERDOCTORID = uSERDOCTORID;
    }

    public boolean IsView() {
        return isView;
    }

    public void setIsView(boolean isView) {
        this.isView = isView;
    }

    public boolean IsPrimary() {
        return isPrimary;
    }

    public void setIsPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public boolean IsOldArchivedRecord() {
        return isOldArchivedRecord;
    }

    public void setIsOldArchivedRecord(boolean isOldArchivedRecord) {
        this.isOldArchivedRecord = isOldArchivedRecord;
    }

    public boolean isFileAcessible() {
        return isFileAcessible;
    }

    public void setFileAcessible(boolean fileAcessible) {
        isFileAcessible = fileAcessible;
    }
}
