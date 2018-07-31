package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LstDocCategory {

    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName; @SerializedName("pageCount")
    @Expose
    private int pageCount;
    @SerializedName("lstDocTypes")
    @Expose
    private List<LstDocType> lstDocTypes = new ArrayList<LstDocType>();

    private int totalDocTypePageCount = -1;

    private String[] mergedFileCompareCustomID = null;

    public Integer getCategoryId() {
        return categoryId;
    }


    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<LstDocType> getLstDocTypes() {
        return lstDocTypes;
    }

    public void setLstDocTypes(List<LstDocType> lstDocTypes) {
        this.lstDocTypes = lstDocTypes;
    }


    public int getTotalDocTypePageCount() {

        if (totalDocTypePageCount == -1) {
            int count = 0;
            if (lstDocTypes != null) {
                for (LstDocType temp :
                        lstDocTypes) {
                    count = count + temp.getPageCount();
                }
            }

            setTotalDocTypePageCount(count);
        }
        return totalDocTypePageCount;
    }

    public void setTotalDocTypePageCount(int totalDocTypePageCount) {
        this.totalDocTypePageCount = totalDocTypePageCount;
    }

    public String[] getMergedFileCompareCustomID() {
        return mergedFileCompareCustomID;
    }

    public void setMergedFileCompareCustomID(String[] mergedFileCompareCustomID) {
        this.mergedFileCompareCustomID = mergedFileCompareCustomID;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "LstDocCategory{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", pageCount=" + pageCount +
                ", lstDocTypes=" + lstDocTypes +
                ", totalDocTypePageCount=" + totalDocTypePageCount +
                ", mergedFileCompareCustomID=" + Arrays.toString(mergedFileCompareCustomID) +
                '}';
    }
}
