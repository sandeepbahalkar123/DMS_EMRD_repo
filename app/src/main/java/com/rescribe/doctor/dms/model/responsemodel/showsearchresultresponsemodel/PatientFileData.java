package com.rescribe.doctor.dms.model.responsemodel.showsearchresultresponsemodel;

/**
 * Created by sandeep on 2/22/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PatientFileData implements Serializable {

    @SerializedName("fileType")
    @Expose
    private String fileType;
    @SerializedName("referenceId")
    @Expose
    private String referenceId;
    @SerializedName("admissionDate")
    @Expose
    private String admissionDate;
    @SerializedName("dischargeDate")
    @Expose
    private String dischargeDate;

    //-- THIS IS ADDED TO MAINTAIN RESPECTIVE PARENT PATIENT ID
    private String respectiveParentPatientID;

    private boolean isShowCompleteList = false;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
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

    public boolean isShowCompleteList() {
        return isShowCompleteList;
    }

    public void setShowCompleteList(boolean showCompleteList) {
        isShowCompleteList = showCompleteList;
    }

    public String getRespectiveParentPatientID() {
        return respectiveParentPatientID;
    }

    public void setRespectiveParentPatientID(String respectiveParentPatientID) {
        this.respectiveParentPatientID = respectiveParentPatientID;
    }
}
