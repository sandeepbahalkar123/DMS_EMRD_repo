package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LstDocType {

    @SerializedName("typeId")
    @Expose
    private Integer typeId;
    @SerializedName("typeName")
    @Expose
    private String typeName;

    @SerializedName("nodeColor")
    @Expose
    private String nodeColor;

    @SerializedName("favouriteColor")
    @Expose
    private String favouriteColor;

    @SerializedName("isFavourite")
    @Expose
    private boolean isFavourite;

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

    @SerializedName("recordDetailId")
    @Expose
    private Integer recordDetailId;
    @SerializedName("confidentialState")
    @Expose
    private Integer confidentialState;

    @SerializedName("fileTypeRefId")
    @Expose
    private String fileTypeRefId;

    @SerializedName("lstdateFileType")
    @Expose
    private ArrayList<LstDateFileType> lstDateFileTypeList = new ArrayList<LstDateFileType>();

    @SerializedName("lsthideDocCategory")
    @Expose
    private List<LstHideDocType> lsthideDocCategory = new ArrayList<LstHideDocType>();

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

    public String getNodeColor() {
        return nodeColor == null ? "#000000" : nodeColor;
    }

    public void setNodeColor(String nodeColor) {
        this.nodeColor = nodeColor;
    }

    public String getFavouriteColor() {
        return favouriteColor == null ? "#00000000" : favouriteColor;
    }

    public void setFavouriteColor(String favouriteColor) {
        this.favouriteColor = favouriteColor;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
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

    public ArrayList<LstDateFileType> getLstDateFileTypeList() {
        return lstDateFileTypeList;
    }

    public void setLstDateFileTypeList(ArrayList<LstDateFileType> lstDateFileTypeList) {
        this.lstDateFileTypeList = lstDateFileTypeList;
    }

    public Integer getRecordDetailId() {
        return recordDetailId;
    }

    public void setRecordDetailId(Integer recordDetailId) {
        this.recordDetailId = recordDetailId;
    }

    public Integer getConfidentialState() {
        return confidentialState;
    }

    public void setConfidentialState(Integer confidentialState) {
        this.confidentialState = confidentialState;
    }

    public String getFileTypeRefId() {
        return fileTypeRefId;
    }

    public void setFileTypeRefId(String fileTypeRefId) {
        this.fileTypeRefId = fileTypeRefId;
    }


    public List<LstHideDocType> getLsthideDocCategory() {
        return lsthideDocCategory;
    }

    public void setLsthideDocCategory(List<LstHideDocType> lsthideDocCategory) {
        this.lsthideDocCategory = lsthideDocCategory;
    }

    @Override
    public String toString() {
        return "LstDocType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", pageCount=" + pageCount +
                ", recordId=" + recordId +
                ", permission='" + permission + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", pageNumber=" + pageNumber +
                ", recordDetailId=" + recordDetailId +
                ", confidentialState=" + confidentialState +
                ", fileTypeRefId='" + fileTypeRefId + '\'' +
                ", lstDateFileTypeList=" + lstDateFileTypeList +
                '}';
    }
}