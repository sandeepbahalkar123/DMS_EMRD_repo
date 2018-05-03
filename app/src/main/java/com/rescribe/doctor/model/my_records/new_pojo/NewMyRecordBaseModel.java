package com.rescribe.doctor.model.my_records.new_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;

public class NewMyRecordBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private NewMyRecordDataModel data;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public NewMyRecordDataModel getData() {
        return data;
    }

    public void setData(NewMyRecordDataModel data) {
        this.data = data;
    }

}