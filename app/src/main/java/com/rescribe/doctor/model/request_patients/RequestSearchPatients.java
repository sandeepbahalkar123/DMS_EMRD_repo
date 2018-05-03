
package com.rescribe.doctor.model.request_patients;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

public class RequestSearchPatients implements Parcelable, CustomResponse {

    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("pageNo")
    @Expose
    private Integer pageNo;
    @SerializedName("filterParams")
    @Expose
    private FilterParams filterParams;
    @SerializedName("sortOrder")
    @Expose
    private String sortOrder = "";
    @SerializedName("sortField")
    @Expose
    private String sortField;
    @SerializedName("searchText")
    @Expose
    private String searchText;
    @SerializedName("paginationSize")
    @Expose
    private int paginationSize = 18; // default size keep 18 because added records in sqlite while pagination
    @SerializedName("date")
    @Expose
    private String date;

    public final static Creator<RequestSearchPatients> CREATOR = new Creator<RequestSearchPatients>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RequestSearchPatients createFromParcel(Parcel in) {
            return new RequestSearchPatients(in);
        }

        public RequestSearchPatients[] newArray(int size) {
            return (new RequestSearchPatients[size]);
        }

    };

    protected RequestSearchPatients(Parcel in) {
        this.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.pageNo = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.filterParams = ((FilterParams) in.readValue((FilterParams.class.getClassLoader())));
        this.sortOrder = ((String) in.readValue((String.class.getClassLoader())));
        this.sortField = ((String) in.readValue((String.class.getClassLoader())));
        this.searchText = ((String) in.readValue((String.class.getClassLoader())));
        this.paginationSize = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RequestSearchPatients() {
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public FilterParams getFilterParams() {
        return filterParams;
    }

    public void setFilterParams(FilterParams filterParams) {
        this.filterParams = filterParams;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public int getPaginationSize() {
        return paginationSize;
    }

    public void setPaginationSize(int paginationSize) {
        this.paginationSize = paginationSize;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docId);
        dest.writeValue(pageNo);
        dest.writeValue(filterParams);
        dest.writeValue(sortOrder);
        dest.writeValue(sortField);
        dest.writeValue(searchText);
        dest.writeValue(paginationSize);
        dest.writeValue(date);
    }

    public int describeContents() {
        return 0;
    }

}
