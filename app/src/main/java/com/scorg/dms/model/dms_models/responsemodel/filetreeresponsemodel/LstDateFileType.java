package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;

public class LstDateFileType {

    @SerializedName("typeId")
    @Expose
    private Integer typeId;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("date")
    @Expose
    private String date;

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

    @SerializedName("recordDetailId")
    @Expose
    private Integer recordDetailId;
    @SerializedName("confidentialState")
    @Expose
    private Integer confidentialState;

    @SerializedName("fileTypeRefId")
    @Expose
    private String fileTypeRefId;

    @SerializedName("nodeType")
    @Expose
    private String nodeType;
    @SerializedName("pacsUrl")
    @Expose
    private String pacsUrl;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    @Override
    public String toString() {
        return "LstDateFileType{" +
                "typeId=" + typeId +
                ", typeName='" + typeName + '\'' +
                ", date='" + date + '\'' +
                ", pageCount=" + pageCount +
                ", recordId=" + recordId +
                ", permission='" + permission + '\'' +
                ", recordDetailId=" + recordDetailId +
                ", confidentialState=" + confidentialState +
                ", fileTypeRefId='" + fileTypeRefId + '\'' +
                '}';
    }

    public String getFileTypeRefId() {
        return fileTypeRefId;
    }

    public void setFileTypeRefId(String fileTypeRefId) {
        this.fileTypeRefId = fileTypeRefId;
    }

    public String getNodeType() {
        return nodeType == null ? "" : nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getPacsUrl() {
        return pacsUrl == null ? "" : pacsUrl;
    }

    public void setPacsUrl(String pacsUrl) {
        this.pacsUrl = pacsUrl;
    }

}