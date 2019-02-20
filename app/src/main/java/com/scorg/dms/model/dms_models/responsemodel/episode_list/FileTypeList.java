package com.scorg.dms.model.dms_models.responsemodel.episode_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileTypeList {

    @SerializedName("FileTypeId")
    @Expose
    private int fileTypeId;
    @SerializedName("FileType")
    @Expose
    private String fileType;

    boolean isChecked = false;

    public int getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(int fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}