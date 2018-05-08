package com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FileTreeResponseData {

    @SerializedName("archiveData")
    @Expose
    private List<ArchiveDatum> archiveData = new ArrayList<ArchiveDatum>();

    public List<ArchiveDatum> getArchiveData() {
        return archiveData;
    }

    public void setArchiveData(List<ArchiveDatum> archiveData) {
        this.archiveData = archiveData;
    }

    @Override
    public String toString() {
        return "FileTreeResponseData{" +
                "archiveData=" + archiveData +
                '}';
    }
}