package com.rescribe.doctor.model.dms_models.responsemodel.annotationlistresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.dms_models.responsemodel.Common;

import java.io.Serializable;

public class AnnotationListResponseModel implements CustomResponse, Serializable {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private AnnotationListData annotationListData;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public AnnotationListData getAnnotationListData() {
        return annotationListData;
    }

    public void setAnnotationListData(AnnotationListData annotationListData) {
        this.annotationListData = annotationListData;
    }
}