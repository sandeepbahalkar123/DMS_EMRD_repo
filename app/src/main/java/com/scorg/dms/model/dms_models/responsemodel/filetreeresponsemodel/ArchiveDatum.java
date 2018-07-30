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

    private int totalLstDateFolderTypePageCount = -1;


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

    @Override
    public String toString() {
        return "ArchiveDatum{" +
                "fileType='" + fileType + '\'' +
                ", lstDateFolderType=" + lstDateFolderTypeList +
                '}';
    }
}