package com.rescribe.doctor.model.my_patient_filter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

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