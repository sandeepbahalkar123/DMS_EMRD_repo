package com.rescribe.doctor.dms.model.responsemodel.showsearchresultresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.dms.interfaces.CustomResponse;
import com.rescribe.doctor.dms.model.responsemodel.Common;

import java.io.Serializable;

public class ShowSearchResultResponseModel implements CustomResponse, Serializable {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private SearchResultData searchResultData;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public SearchResultData getSearchResultData() {
        return searchResultData;
    }

    public void setSearchResultData(SearchResultData searchResultData) {
        this.searchResultData = searchResultData;
    }
}