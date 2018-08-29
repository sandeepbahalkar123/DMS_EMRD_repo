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

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
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
                '}';
    }
}