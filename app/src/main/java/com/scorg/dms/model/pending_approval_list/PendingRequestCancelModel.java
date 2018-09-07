package com.scorg.dms.model.pending_approval_list;

import com.scorg.dms.interfaces.CustomResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingRequestCancelModel implements CustomResponse {

    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("workFlowId")
    @Expose
    private String workFlowId;
    @SerializedName("statusName")
    @Expose
    private String statusName;
    @SerializedName("stageId")
    @Expose
    private String stageId;
    @SerializedName("comments")
    @Expose
    private String comments;
    @SerializedName("validity")
    @Expose
    private String validity;
    @SerializedName("isWorkFlowProcess")
    @Expose
    private String isWorkFlowProcess;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("enddate")
    @Expose
    private String enddate;
    @SerializedName("listOfDates")
    @Expose
    private String listOfDates;
    @SerializedName("fileRefId")
    @Expose
    private String fileRefId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getWorkFlowId() {
        return workFlowId;
    }

    public void setWorkFlowId(String workFlowId) {
        this.workFlowId = workFlowId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStageId() {
        return stageId;
    }

    public void setStageId(String stageId) {
        this.stageId = stageId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getIsWorkFlowProcess() {
        return isWorkFlowProcess;
    }

    public void setIsWorkFlowProcess(String isWorkFlowProcess) {
        this.isWorkFlowProcess = isWorkFlowProcess;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getListOfDates() {
        return listOfDates;
    }

    public void setListOfDates(String listOfDates) {
        this.listOfDates = listOfDates;
    }

    public String getFileRefId() {
        return fileRefId;
    }

    public void setFileRefId(String fileRefId) {
        this.fileRefId = fileRefId;
    }

}


