package com.scorg.dms.model.my_patient_filter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class LocationsRequest implements CustomResponse{

    @SerializedName("docId")
    @Expose
    private Integer docId;

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

}