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

    @SerializedName("favouriteColor")
    @Expose
    private String favouriteColor;

    @SerializedName("pageCount")
    @Expose
    private int pageCount;
    @SerializedName("confidentialState")
    @Expose
    private int confidentialState;
    @SerializedName("fileTypeRefId")
    @Expose
    private String fileTypeRefId;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("typeId")
    @Expose
    private int typeId;
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

    public String getFavouriteColor() {
        return favouriteColor == null ? "#00000000" : favouriteColor;
    }

    public void setFavouriteColor(String favouriteColor) {
        this.favouriteColor = favouriteColor;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
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