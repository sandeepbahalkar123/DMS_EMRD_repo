package com.rescribe.doctor.dms.model.responsemodel.annotationlistresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sandeep on 3/3/17.
 */

public class AnnotationList {

    @SerializedName("categoryId")
    @Expose
    private Integer categoryId;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("docTypeList")
    @Expose
    private List<DocTypeList> docTypeList = null;

    private Boolean isSelected = false;

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<DocTypeList> getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List<DocTypeList> docTypeList) {
        this.docTypeList = docTypeList;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}