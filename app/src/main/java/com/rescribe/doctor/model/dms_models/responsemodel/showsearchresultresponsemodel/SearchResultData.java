package com.rescribe.doctor.model.dms_models.responsemodel.showsearchresultresponsemodel;

/**
 * Created by sandeep on 2/22/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchResultData implements CustomResponse {

    @SerializedName("searchResult")
    @Expose
    private List<SearchResult> searchResult = new ArrayList<SearchResult>();

    public List<SearchResult> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<SearchResult> searchResult) {
        this.searchResult = searchResult;
    }

}
