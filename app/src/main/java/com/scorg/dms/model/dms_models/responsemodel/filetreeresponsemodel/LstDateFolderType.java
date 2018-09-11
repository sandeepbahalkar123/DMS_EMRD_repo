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

    @SerializedName("confidentialState")
    @Expose
    private int confidentialState;

    @SerializedName("lstDocCategories")
    @Expose
    private List<LstDocCategory> lstDocCategories = new ArrayList<LstDocCategory>();

    private int totalDocCategoryPageCount = -1;

    public String getDateFolderType() {
        return dateFolderType;
    }

    public void setDateFolderType(String dateFolderType) {
        this.dateFolderType = dateFolderType;
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