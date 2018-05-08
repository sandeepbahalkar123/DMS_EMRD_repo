package com.scorg.dms.model.dms_models.requestmodel.filetreerequestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstSearchParam {

    @SerializedName("patientId")
    @Expose
    private String patientId;
    @SerializedName("fileType")
    @Expose
    private String fileType;
    @SerializedName("fileTypeRefId")
    @Expose
    private String fileTypeRefId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

}