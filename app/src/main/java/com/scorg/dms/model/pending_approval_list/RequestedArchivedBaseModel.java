
package com.scorg.dms.model.pending_approval_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

import java.util.List;

public class RequestedArchivedBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;

    @SerializedName("data")
    @Expose
    private List<RequestedArchivedDetailList> requestedArchivedDetailList;


    public RequestedArchivedBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public List<RequestedArchivedDetailList> getRequestedArchivedDetailList() {
        return requestedArchivedDetailList;
    }

    public void setRequestedArchivedDetailList(List<RequestedArchivedDetailList> requestedArchivedDetailList) {
        this.requestedArchivedDetailList = requestedArchivedDetailList;
    }
}
