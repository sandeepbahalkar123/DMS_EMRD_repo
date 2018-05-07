package com.rescribe.doctor.model.dms_models.requestmodel.getpdfdatarequestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

import java.util.List;

public class GetPdfDataRequestModel implements CustomResponse {

    @SerializedName("patientId")
    @Expose
    private String patientId;
    @SerializedName("fileType")
    @Expose
    private String fileType;
    @SerializedName("fileTypeRefId")
    @Expose
    private String fileTypeRefId;
    @SerializedName("lstDocTypes")
    @Expose
    private List<LstDocTypeRequest> lstDocTypeRequests = null;

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

    public List<LstDocTypeRequest> getLstDocTypeRequests() {
        return lstDocTypeRequests;
    }

    public void setLstDocTypeRequests(List<LstDocTypeRequest> lstDocTypeRequests) {
        this.lstDocTypeRequests = lstDocTypeRequests;
    }


}