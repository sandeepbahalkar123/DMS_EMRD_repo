package com.scorg.dms.model.my_patient_filter;

public class PatientFilter {
    private String searchValue;
    private String searchType;

    public PatientFilter(String text, String searchType) {
        this.searchValue = text;
        this.searchType = searchType;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String text) {
        this.searchValue = searchValue;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}
