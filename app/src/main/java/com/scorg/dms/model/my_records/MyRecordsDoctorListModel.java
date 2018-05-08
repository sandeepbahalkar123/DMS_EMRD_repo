package com.scorg.dms.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class MyRecordsDoctorListModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DoctorListModel doctorListModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DoctorListModel getDoctorListModel() {
        return doctorListModel;
    }

    public void setDoctorListModel(DoctorListModel doctorListModel) {
        this.doctorListModel = doctorListModel;
    }

}