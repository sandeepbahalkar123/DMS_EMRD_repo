package com.scorg.dms.model.dms_models.responsemodel.annotationlistresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnnotationListData {

    @SerializedName("lstAnnotations")
    @Expose
    private List<AnnotationList> annotationLists = null;

    public List<AnnotationList> getAnnotationLists() {
        return annotationLists;
    }

    public void setAnnotationLists(List<AnnotationList> annotationLists) {
        this.annotationLists = annotationLists;
    }
}