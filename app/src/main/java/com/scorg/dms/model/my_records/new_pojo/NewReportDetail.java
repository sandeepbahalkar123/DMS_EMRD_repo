package com.scorg.dms.model.my_records.new_pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewReportDetail {

    @SerializedName("childCaptionName")
    @Expose
    private String childCaptionName;
    @SerializedName("imageList")
    @Expose
    private ArrayList<String> imageList = new ArrayList<String>();

    public String getChildCaptionName() {
        return childCaptionName;
    }

    public void setChildCaptionName(String childCaptionName) {
        this.childCaptionName = childCaptionName;
    }

    public ArrayList<String> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        this.imageList = imageList;
    }

}