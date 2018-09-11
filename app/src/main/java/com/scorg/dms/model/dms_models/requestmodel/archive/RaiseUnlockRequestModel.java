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
}
