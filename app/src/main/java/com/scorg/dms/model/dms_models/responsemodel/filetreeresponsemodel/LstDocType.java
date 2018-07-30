package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstDocType {

    @SerializedName("typeId")
    @Expose
    private Integer typeId;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;
    @SerializedName("recordId")
    @Expose
    private Integer recordId;
    @SerializedName("permission")
    @Expose
    private String permission;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;



    private String[] mergedFileCompareCustomID = null;

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String[] getMergedFileCompareCustomID() {
        return mergedFileCompareCustomID;
    }

    public void setMergedFileCompareCustomID(String[] mergedFileCompareCustomID) {
        this.mergedFileCompareCustomID = mergedFileCompareCustomID;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "LstDocType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", recordId='" + recordId + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", pageCount=" + pageCount +
                ", pageNumber=" + pageNumber +
                ", permission='" + permission + '\'' +
                '}';
    }
}