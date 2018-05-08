
package com.scorg.dms.model.case_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.util.List;

public class PatientHistory implements CustomResponse {

    @SerializedName("caseDetailId")
    @Expose
    private Integer caseDetailId;
    @SerializedName("caseDetailName")
    @Expose
    private String caseDetailName;
    @SerializedName("caseDetailData")
    @Expose
    private List<VisitCommonData>  caseDetailData = null;
    @SerializedName("vitals")
    @Expose
    private List<Vital> vitals = null;

    public Integer getCaseDetailId() {
        return caseDetailId;
    }

    public void setCaseDetailId(Integer caseDetailId) {
        this.caseDetailId = caseDetailId;
    }

    public String getCaseDetailName() {
        return caseDetailName;
    }

    public void setCaseDetailName(String caseDetailName) {
        this.caseDetailName = caseDetailName;
    }

    public List<VisitCommonData> getCommonData() {
        return  caseDetailData;
    }

    public void setCommonData(List<VisitCommonData>  caseDetailData) {
        this. caseDetailData =  caseDetailData;
    }

    public List<Vital> getVitals() {
        return vitals;
    }

    public void setVitals(List<Vital> vitals) {
        this.vitals = vitals;
    }

}
