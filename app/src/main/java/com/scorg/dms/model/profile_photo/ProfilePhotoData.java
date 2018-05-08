package com.scorg.dms.model.profile_photo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePhotoData {

    @SerializedName("docImgUrl")
    @Expose
    private String docImgUrl;

    public String getDocImgUrl() {
        return docImgUrl;
    }

    public void setDocImgUrl(String docImgUrl) {
        this.docImgUrl = docImgUrl;
    }

}