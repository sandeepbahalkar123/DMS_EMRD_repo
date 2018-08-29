package com.scorg.dms.model.pending_approval_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

/**
 * Created by jeetal on 19/2/18.
 */

public class RequestPendingApprovalData implements CustomResponse {
    @SerializedName("PageNo")
    @Expose
    private Integer pageNo;

    @SerializedName("Ispending")
    @Expose
    private boolean isPending;


    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }
}
