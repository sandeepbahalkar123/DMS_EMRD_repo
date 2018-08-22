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
    @SerializedName("lstDateFolderType")
    @Expose
    private List<LstDateFolderType> lstDateFolderTypeList = new ArrayList<LstDateFolderType>();

    @SerializedName("lstDocCategories")
    @Expose
    private List<LstDocCategory> archiveDataLstDocCategories = new ArrayList<LstDocCategory>();


    private int totalLstDateFolderTypePageCount = -1;
    private int totalArchiveDataLstDocCategoriesPageCount = -1;


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
            for (LstDateFolderType temp :
                    lstDateFolderTypeList) {
                count = count + temp.getPageCount();
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

    @Override
    public String toString() {
        return "ArchiveDatum{" +
                "fileType='" + fileType + '\'' +
                ", lstDateFolderTypeList=" + lstDateFolderTypeList +
                ", archiveDataLstDocCategories=" + archiveDataLstDocCategories +
                ", totalLstDateFolderTypePageCount=" + totalLstDateFolderTypePageCount +
                '}';
    }
}