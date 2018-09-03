
package com.scorg.dms.model.pending_approval_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestedArchivedDetailList {

    @SerializedName("ProcessID")
    @Expose
    private Integer processID;
    @SerializedName("RequestID")
    @Expose
    private Integer requestID;
    @SerializedName("ProcessName")
    @Expose
    private String processName;
    @SerializedName("PatientID")
    @Expose
    private String patientID;
    @SerializedName("PatientName")
    @Expose
    private String patientName;
    @SerializedName("RequestInitiator")
    @Expose
    private Integer requestInitiator;
    @SerializedName("RequestInitiatorName")
    @Expose
    private String requestInitiatorName;
    @SerializedName("CurrentStageID")
    @Expose
    private Integer currentStageID;
    @SerializedName("CurrentStatusID")
    @Expose
    private Integer currentStatusID;
    @SerializedName("ProccessingGroupID")
    @Expose
    private Integer proccessingGroupID;
    @SerializedName("ProccessinguserID")
    @Expose
    private Integer proccessinguserID;
    @SerializedName("CurrentStage")
    @Expose
    private String currentStage;
    @SerializedName("Comments")
    @Expose
    private String comments;
    @SerializedName("CurrentStatus")
    @Expose
    private String currentStatus;
    @SerializedName("Validity")
    @Expose
    private Integer validity;
    @SerializedName("myUserID")
    @Expose
    private Integer myUserID;
    @SerializedName("SessionUserID")
    @Expose
    private Integer sessionUserID;
    @SerializedName("GroupID")
    @Expose
    private Integer groupID;
    @SerializedName("GroupName")
    @Expose
    private String groupName;
    @SerializedName("StageChangeDoctor")
    @Expose
    private Object stageChangeDoctor;
    @SerializedName("StageChangeGroup")
    @Expose
    private Object stageChangeGroup;
    @SerializedName("StageChangeBy")
    @Expose
    private String stageChangeBy;
    @SerializedName("StageChangeDate")
    @Expose
    private String stageChangeDate;
    @SerializedName("SubmittedDate")
    @Expose
    private Object submittedDate;
    @SerializedName("LastUpdated")
    @Expose
    private String lastUpdated;
    @SerializedName("MyElapsedTime")
    @Expose
    private String myElapsedTime;
    @SerializedName("ApproveStatus")
    @Expose
    private Object approveStatus;
    @SerializedName("FileRefID")
    @Expose
    private String fileRefID;
    @SerializedName("RequesterName")
    @Expose
    private String requesterName;
    @SerializedName("isfilelvl")
    @Expose
    private Boolean isfilelvl;
    @SerializedName("FileType")
    @Expose
    private String fileType;
    @SerializedName("DoctypeName")
    @Expose
    private String doctypeName;

    @SerializedName("salutation")
    @Expose
    private String salutation;

    public Integer getProcessID() {
        return processID;
    }

    public void setProcessID(Integer processID) {
        this.processID = processID;
    }

    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Integer getRequestInitiator() {
        return requestInitiator;
    }

    public void setRequestInitiator(Integer requestInitiator) {
        this.requestInitiator = requestInitiator;
    }

    public String getRequestInitiatorName() {
        return requestInitiatorName;
    }

    public void setRequestInitiatorName(String requestInitiatorName) {
        this.requestInitiatorName = requestInitiatorName;
    }

    public Integer getCurrentStageID() {
        return currentStageID;
    }

    public void setCurrentStageID(Integer currentStageID) {
        this.currentStageID = currentStageID;
    }

    public Integer getCurrentStatusID() {
        return currentStatusID;
    }

    public void setCurrentStatusID(Integer currentStatusID) {
        this.currentStatusID = currentStatusID;
    }

    public Integer getProccessingGroupID() {
        return proccessingGroupID;
    }

    public void setProccessingGroupID(Integer proccessingGroupID) {
        this.proccessingGroupID = proccessingGroupID;
    }

    public Integer getProccessinguserID() {
        return proccessinguserID;
    }

    public void setProccessinguserID(Integer proccessinguserID) {
        this.proccessinguserID = proccessinguserID;
    }

    public String getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(String currentStage) {
        this.currentStage = currentStage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    public Integer getMyUserID() {
        return myUserID;
    }

    public void setMyUserID(Integer myUserID) {
        this.myUserID = myUserID;
    }

    public Integer getSessionUserID() {
        return sessionUserID;
    }

    public void setSessionUserID(Integer sessionUserID) {
        this.sessionUserID = sessionUserID;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Object getStageChangeDoctor() {
        return stageChangeDoctor;
    }

    public void setStageChangeDoctor(Object stageChangeDoctor) {
        this.stageChangeDoctor = stageChangeDoctor;
    }

    public Object getStageChangeGroup() {
        return stageChangeGroup;
    }

    public void setStageChangeGroup(Object stageChangeGroup) {
        this.stageChangeGroup = stageChangeGroup;
    }

    public String getStageChangeBy() {
        return stageChangeBy;
    }

    public void setStageChangeBy(String stageChangeBy) {
        this.stageChangeBy = stageChangeBy;
    }

    public String getStageChangeDate() {
        return stageChangeDate;
    }

    public void setStageChangeDate(String stageChangeDate) {
        this.stageChangeDate = stageChangeDate;
    }

    public Object getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Object submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }



    public Object getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Object approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getFileRefID() {
        return fileRefID;
    }

    public void setFileRefID(String fileRefID) {
        this.fileRefID = fileRefID;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public Boolean getIsfilelvl() {
        return isfilelvl;
    }

    public void setIsfilelvl(Boolean isfilelvl) {
        this.isfilelvl = isfilelvl;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDoctypeName() {
        return doctypeName;
    }

    public void setDoctypeName(String doctypeName) {
        this.doctypeName = doctypeName;
    }

    public String getMyElapsedTime() {
        return myElapsedTime;
    }

    public void setMyElapsedTime(String myElapsedTime) {
        this.myElapsedTime = myElapsedTime;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }
}
