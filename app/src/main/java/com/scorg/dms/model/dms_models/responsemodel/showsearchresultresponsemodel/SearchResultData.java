package com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel;

/**
 * Created by sandeep on 2/22/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchResultData implements CustomResponse {

    @SerializedName("searchResultList")
    @Expose
    private List<SearchResult> searchResult = new ArrayList<SearchResult>();

    @SerializedName("ispaggination")
    @Expose
    private boolean ispaggination;

    public List<SearchResult> getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(List<SearchResult> searchResult) {
        this.searchResult = searchResult;
    }

    public boolean isPaggination() {
        return ispaggination;
    }

    public void setIspaggination(boolean ispaggination) {
        this.ispaggination = ispaggination;
    }
}
