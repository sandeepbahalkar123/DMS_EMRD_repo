package com.rescribe.doctor.model.dms_models.requestmodel.filetreerequestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

import java.util.List;

public class FileTreeRequestModel implements CustomResponse{

    @SerializedName("lstSearchParam")
    @Expose
    private List<LstSearchParam> lstSearchParam = null;

    public List<LstSearchParam> getLstSearchParam() {
        return lstSearchParam;
    }

    public void setLstSearchParam(List<LstSearchParam> lstSearchParam) {
        this.lstSearchParam = lstSearchParam;
    }

}