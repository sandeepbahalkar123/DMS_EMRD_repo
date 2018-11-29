package com.scorg.dms.model.dms_models.requestmodel.archive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class GetArchiveRequestModel implements CustomResponse {

    @SerializedName("patId")
    @Expose
    private String patId;
    @SerializedName("recordId")
    @Expose
    private String recordId;
    @SerializedName("pageNumber")
    @Expose
    private int pageNumber;
    @SerializedName("take")
    @Expose
    private int take;
    @SerializedName("preference")
    @Expose
    private String preference;

    @SerializedName("PageType")
    @Expose
    private String PageType;

    public String getPatId() {
        return patId;
    }

    public void setPatId(String patId) {
        this.patId = patId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getPageType() {
        return PageType;
    }

    public void setPageType(String pageType) {
        PageType = pageType;
    }
}