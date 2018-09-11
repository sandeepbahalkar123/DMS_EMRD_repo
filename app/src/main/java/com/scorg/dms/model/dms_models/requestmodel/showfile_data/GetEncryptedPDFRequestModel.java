package com.scorg.dms.model.dms_models.requestmodel.showfile_data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.dms_models.responsemodel.filetreeresponsemodel.LstDocType;

import java.util.ArrayList;
import java.util.List;

public class GetEncryptedPDFRequestModel implements CustomResponse {
    @SerializedName("recordId")
    @Expose
    private String recordId;
    @SerializedName("lstDocTypes")
    @Expose
    private List<LstDocType> lstDocTypes = new ArrayList<LstDocType>();

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public List<LstDocType> getLstDocTypes() {
        return lstDocTypes;
    }

    public void setLstDocTypes(List<LstDocType> lstDocTypes) {
        this.lstDocTypes = lstDocTypes;
    }
}
