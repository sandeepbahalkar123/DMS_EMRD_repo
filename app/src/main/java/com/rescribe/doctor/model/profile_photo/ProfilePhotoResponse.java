package com.rescribe.doctor.model.profile_photo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.model.Common;

public class ProfilePhotoResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ProfilePhotoData data;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ProfilePhotoData getData() {
        return data;
    }

    public void setData(ProfilePhotoData data) {
        this.data = data;
    }

}