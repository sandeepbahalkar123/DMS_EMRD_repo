package com.rescribe.doctor.dms.model.responsemodel.getpdfdataresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetPdfDataResponseData {

    @SerializedName("fileData")
    @Expose
    private String fileData;

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }

}