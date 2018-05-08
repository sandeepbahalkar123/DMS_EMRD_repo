
package com.scorg.dms.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class MyRecordBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private MyRecordDataModel recordMainDataModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public MyRecordDataModel getRecordMainDataModel() {
        return recordMainDataModel;
    }

    public void setRecordMainDataModel(MyRecordDataModel recordMainDataModel) {
        this.recordMainDataModel = recordMainDataModel;
    }

    @Override
    public String toString() {
        return "DoctorBaseModel{" +
                "common=" + common +
                ", recordMainDataModel=" + recordMainDataModel +
                '}';
    }
}
