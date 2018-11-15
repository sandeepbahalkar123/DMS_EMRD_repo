package com.scorg.dms.model.dms_models.requestmodel.archive;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import org.json.JSONArray;

public class RaiseUnlockRequestModel implements CustomResponse {

    @SerializedName("RequestTypeId")
    @Expose
    private String RequestTypeId;

    @SerializedName("CheckList")
    @Expose
    private String[] CheckList;

    @SerializedName("fileTypeId")
    @Expose
    private String[] fileTypeId;


    @SerializedName("patId")
    @Expose
    private String patId;


    public String getRequestTypeId() {
        return RequestTypeId;
    }

    public void setRequestTypeId(String requestTypeId) {
        RequestTypeId = requestTypeId;
    }

    public String[] getCheckList() {
        return CheckList;
    }

    public void setCheckList(String[] checkList) {
        CheckList = checkList;
    }

    public String[] getFileTypeId() {
        return fileTypeId;
    }

    public void setFileTypeId(String[] fileTypeId) {
        this.fileTypeId = fileTypeId;
    }

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }
}
