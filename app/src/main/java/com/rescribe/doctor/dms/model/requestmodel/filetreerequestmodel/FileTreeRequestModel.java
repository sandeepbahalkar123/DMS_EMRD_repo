package com.rescribe.doctor.dms.model.requestmodel.filetreerequestmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.dms.interfaces.CustomResponse;

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