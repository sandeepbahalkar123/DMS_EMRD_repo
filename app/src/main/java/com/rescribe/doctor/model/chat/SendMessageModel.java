package com.rescribe.doctor.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;

public class SendMessageModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
   /* @SerializedName("data")
    @Expose
    private String data;*/

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    /*public String getViewAllData() {
        return data;
    }

    public void notifyDataChanged(String data) {
        this.data = data;
    }*/

}