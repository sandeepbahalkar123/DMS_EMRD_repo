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
    @SerializedName("abbreviation")
    @Expose
    private String abbreviation;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;
    @SerializedName("pageNumber")
    @Expose
    private Integer pageNumber;
    @SerializedName("filePath")
    @Expose
    private String encryptedPDFFilePath;


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

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
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


    public String getEncryptedPDFFilePath() {
        return encryptedPDFFilePath;
    }

    public void setEncryptedPDFFilePath(String encryptedPDFFilePath) {
        this.encryptedPDFFilePath = encryptedPDFFilePath;
    }

    @Override
    public String toString() {
        return "LstDocType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", pageCount=" + pageCount +
                ", pageNumber=" + pageNumber +
                ", encryptedPDFFilePath='" + encryptedPDFFilePath + '\'' +
                '}';
    }
}