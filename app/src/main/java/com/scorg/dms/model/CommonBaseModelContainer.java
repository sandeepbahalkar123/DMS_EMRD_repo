package com.scorg.dms.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class CommonBaseModelContainer implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common commonRespose;

    public Common getCommonRespose() {
        return commonRespose;
    }

    public void setCommonRespose(Common commonRespose) {
        this.commonRespose = commonRespose;
    }
}