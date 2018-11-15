package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstHideDocType {

    @SerializedName("categoryId")
    @Expose
    private int categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    @SerializedName("nodeColor")
    @Expose
    private String nodeColor;

    @SerializedName("pageCount")
    @Expose
    private int pageCount;
    @SerializedName("confidentialState")
    @Expose
    private int confidentialState;
    @SerializedName("fileTypeRefId")
    @Expose
    private String fileTypeRefId;
    @SerializedName("documentType")
    @Expose
    private String documentType;
    @SerializedName("documentTypeId")
    @Expose
    private int documentTypeId;
    @SerializedName("recordId")
    @Expose
    private int recordId;
    @SerializedName("recordDetailId")
    @Expose
    private int recordDetailId;
    @SerializedName("fileType")
    @Expose
    private String fileType;

    public LstHideDocType() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getNodeColor() {
        return nodeColor == null ? "#000000" : nodeColor;
    }

    public void setNodeColor(String nodeColor) {
        this.nodeColor = nodeColor;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getConfidentialState() {
        return confidentialState;
    }

    public void setConfidentialState(int confidentialState) {
        this.confidentialState = confidentialState;
    }

    public String getFileTypeRefId() {
        return fileTypeRefId;
    }

    public void setFileTypeRefId(String fileTypeRefId) {
        this.fileTypeRefId = fileTypeRefId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public int getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(int documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getRecordDetailId() {
        return recordDetailId;
    }

    public void setRecordDetailId(int recordDetailId) {
        this.recordDetailId = recordDetailId;
    }

    public Object getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

}