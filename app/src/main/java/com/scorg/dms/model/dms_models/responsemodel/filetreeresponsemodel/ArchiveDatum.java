package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArchiveDatum {

    @SerializedName("fileType")
    @Expose
    private String fileType;

    @SerializedName("displayKey")
    @Expose
    private String displayKey;

    @SerializedName("confidentialState")
    @Expose
    private int confidentialState;

    @SerializedName("lstDateFolderType")
    @Expose
    private List<LstDateFolderType> lstDateFolderTypeList = new ArrayList<LstDateFolderType>();

    @SerializedName("lstDocCategories")
    @Expose
    private List<LstDocCategory> archiveDataLstDocCategories = new ArrayList<LstDocCategory>();

    @SerializedName("lstOrderedDocTypes")
    @Expose
    private List<LstOrderedDocType> lstOrderedDocTypes = new ArrayList<LstOrderedDocType>();

    @SerializedName("lsthideDocCategory")
    @Expose
    private List<LstHideDocType> lstHideDocTypes = new ArrayList<LstHideDocType>();

    private int totalLstDateFolderTypePageCount = -1;
    private int totalArchiveDataLstDocCategoriesPageCount = -1;
    private int totalArchiveDataLstOrderPageCount = -1;
    private int totalArchiveDataLstHidePageCount = -1;


    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<LstDateFolderType> getLstDateFolderTypeList() {
        return lstDateFolderTypeList;
    }

    public void setLstDateFolderTypeList(List<LstDateFolderType> lstDateFolderTypeList) {
        this.lstDateFolderTypeList = lstDateFolderTypeList;
    }

    public void setTotalLstDateFolderTypePageCount(int totalLstDateFolderTypePageCount) {
        this.totalLstDateFolderTypePageCount = totalLstDateFolderTypePageCount;
    }

    public int getTotalLstDateFolderTypePageCount() {
        if (totalLstDateFolderTypePageCount == -1) {
            int count = 0;
            if (lstDateFolderTypeList != null) {
                for (LstDateFolderType temp :
                        lstDateFolderTypeList) {
                    count = count + temp.getPageCount();
                }
            }
            setTotalLstDateFolderTypePageCount(count);
        }
        return totalLstDateFolderTypePageCount;
    }

    public List<LstDocCategory> getArchiveDataLstDocCategories() {
        return archiveDataLstDocCategories;
    }

    public void setArchiveDataLstDocCategories(List<LstDocCategory> archiveDataLstDocCategories) {
        this.archiveDataLstDocCategories = archiveDataLstDocCategories;
    }

    public void setTotalArchiveDataLstDocCategoriesPageCount(int totalArchiveDataLstDocCategoriesPageCount) {
        this.totalArchiveDataLstDocCategoriesPageCount = totalArchiveDataLstDocCategoriesPageCount;
    }

    public void setTotalArchiveDataLstOrderPageCount(int totalArchiveDataLstOrderPageCount) {
        this.totalArchiveDataLstOrderPageCount = totalArchiveDataLstOrderPageCount;
    }

    public int getTotalArchiveDataLstDocCategoriesPageCount() {
        if (archiveDataLstDocCategories != null) {
            if (totalArchiveDataLstDocCategoriesPageCount == -1) {
                int count = 0;
                for (LstDocCategory temp :
                        archiveDataLstDocCategories) {
                    count = count + temp.getPageCount();
                }
                setTotalArchiveDataLstDocCategoriesPageCount(count);
            }
        }

        return totalArchiveDataLstDocCategoriesPageCount;
    }

    public int getTotalArchiveDataLstOrderPageCount() {
        if (lstOrderedDocTypes != null) {
            if (totalArchiveDataLstOrderPageCount == -1) {
                int count = 0;
                for (LstOrderedDocType temp :
                        lstOrderedDocTypes) {
                    count = count + temp.getPageCount();
                }
                setTotalArchiveDataLstOrderPageCount(count);
            }
        }

        return totalArchiveDataLstOrderPageCount;
    }

    public int getTotalArchiveDataLstHidePageCount() {
        if (lstOrderedDocTypes != null) {
            if (totalArchiveDataLstHidePageCount == -1) {
                int count = 0;
                for (LstHideDocType temp :
                        lstHideDocTypes) {
                    count = count + temp.getPageCount();
                }
                setTotalArchiveDataLstHidePageCount(count);
            }
        }

        return totalArchiveDataLstOrderPageCount;
    }

    public int getConfidentialState() {
        return confidentialState;
    }

    public void setConfidentialState(int confidentialState) {
        this.confidentialState = confidentialState;
    }

    public List<LstOrderedDocType> getLstOrderedDocTypes() {
        return lstOrderedDocTypes;
    }

    public void setLstOrderedDocTypes(List<LstOrderedDocType> lstOrderedDocTypes) {
        this.lstOrderedDocTypes = lstOrderedDocTypes;
    }

    public String getDisplayKey() {
        return displayKey;
    }

    public void setDisplayKey(String displayKey) {
        this.displayKey = displayKey;
    }

    public List<LstHideDocType> getLstHideDocTypes() {
        return lstHideDocTypes;
    }

    public void setLstHideDocTypes(List<LstHideDocType> lstHideDocTypes) {
        this.lstHideDocTypes = lstHideDocTypes;
    }

    public void setTotalArchiveDataLstHidePageCount(int totalArchiveDataLstHidePageCount) {
        this.totalArchiveDataLstHidePageCount = totalArchiveDataLstHidePageCount;
    }

    @Override
    public String toString() {
        return "ArchiveDatum{" +
                "fileType='" + fileType + '\'' +
                ", lstDateFolderTypeList=" + lstDateFolderTypeList +
                ", archiveDataLstDocCategories=" + archiveDataLstDocCategories +
                ", totalLstDateFolderTypePageCount=" + totalLstDateFolderTypePageCount +
                ", confidentialState=" + confidentialState +
                '}';
    }
}