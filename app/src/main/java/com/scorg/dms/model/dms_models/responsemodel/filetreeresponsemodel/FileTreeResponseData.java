package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FileTreeResponseData {

    @SerializedName("archiveData")
    @Expose
    private List<ArchiveDatum> archiveData = new ArrayList<ArchiveDatum>();

    @SerializedName("ispaggination")
    @Expose
    private boolean isPagination;

    public List<ArchiveDatum> getArchiveData() {
        return archiveData;
    }

    public void setArchiveData(List<ArchiveDatum> archiveData) {
        this.archiveData = archiveData;
    }

    public boolean isPagination() {
        return isPagination;
    }

    public void setPagination(boolean pagination) {
        isPagination = pagination;
    }

    @Override
    public String toString() {
        return "FileTreeResponseData{" +
                "archiveData=" + archiveData +
                '}';
    }
}