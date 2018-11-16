package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LstDateFolderType {

    @SerializedName("dateFolderType")
    @Expose
    private String dateFolderType;

    @SerializedName("pageCount")
    @Expose
    private int pageCount;

    @SerializedName("nodeColor")
    @Expose
    private String nodeColor;

    @SerializedName("favouriteColor")
    @Expose
    private String favouriteColor;

    @SerializedName("confidentialState")
    @Expose
    private int confidentialState;

    @SerializedName("lstDocCategories")
    @Expose
    private List<LstDocCategory> lstDocCategories = new ArrayList<LstDocCategory>();

    @SerializedName("lsthideDocCategory")
    @Expose
    private List<LstHideDocType> lsthideDocCategory = new ArrayList<LstHideDocType>();

    private int totalDocCategoryPageCount = -1;

    public String getDateFolderType() {
        return dateFolderType;
    }

    public void setDateFolderType(String dateFolderType) {
        this.dateFolderType = dateFolderType;
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

    public List<LstDocCategory> getLstDocCategories() {
        return lstDocCategories;
    }

    public void setLstDocCategories(List<LstDocCategory> lstDocCategories) {
        this.lstDocCategories = lstDocCategories;
    }

    public int getTotalDocCategoryPageCount() {
        if (totalDocCategoryPageCount == -1) {
            int count = 0;
            for (LstDocCategory temp :
                    lstDocCategories) {
                count = count + temp.getTotalDocTypePageCount();
            }
            setTotalDocCategoryPageCount(count);
        }
        return totalDocCategoryPageCount;
    }

    public void setTotalDocCategoryPageCount(int totalDocCategoryPageCount) {
        this.totalDocCategoryPageCount = totalDocCategoryPageCount;
    }

    public int getConfidentialState() {
        return confidentialState;
    }

    public void setConfidentialState(int confidentialState) {
        this.confidentialState = confidentialState;
    }

    public List<LstHideDocType> getLsthideDocCategory() {
        return lsthideDocCategory;
    }

    public void setLsthideDocCategory(List<LstHideDocType> lsthideDocCategory) {
        this.lsthideDocCategory = lsthideDocCategory;
    }

    @Override
    public String toString() {
        return "LstDateFolderType{" +
                "dateFolderType='" + dateFolderType + '\'' +
                ", lstDocCategories=" + lstDocCategories +
                ", totalDocCategoryPageCount=" + totalDocCategoryPageCount +
                ", confidentialState=" + confidentialState +
                '}';
    }
}