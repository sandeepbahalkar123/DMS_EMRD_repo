package com.rescribe.doctor.dms.model.responsemodel.patientnamelistresponsemodel;

/**
 * Created by jeetal on 10/5/17.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.dms.interfaces.CustomResponse;
import com.rescribe.doctor.dms.model.responsemodel.Common;

import java.io.Serializable;

public class PatientNameListResponseModel implements CustomResponse, Serializable {
    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private PatientNameListData data;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public PatientNameListData getData() {
        return data;
    }

    public void setData(PatientNameListData data) {
        this.data = data;
    }
}
