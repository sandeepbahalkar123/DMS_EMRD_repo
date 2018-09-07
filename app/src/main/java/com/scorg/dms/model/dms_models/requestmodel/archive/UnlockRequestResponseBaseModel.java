package com.scorg.dms.model.dms_models.requestmodel.archive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class UnlockRequestResponseBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private RequestResponseResultUnlock requestResponseResultUnlock;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public RequestResponseResultUnlock getRequestResponseResultUnlock() {
        return requestResponseResultUnlock;
    }

    public void setRequestResponseResultUnlock(RequestResponseResultUnlock requestResponseResultUnlock) {
        this.requestResponseResultUnlock = requestResponseResultUnlock;
    }

}

