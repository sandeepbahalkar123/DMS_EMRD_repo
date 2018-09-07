package com.scorg.dms.model.pending_approval_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class CancelUnlockRequestResponseBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private CancelUnlockRequestResponseData cancelUnlockRequestResponseData;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }


    public CancelUnlockRequestResponseData getCancelUnlockRequestResponseData() {
        return cancelUnlockRequestResponseData;
    }

    public void setCancelUnlockRequestResponseData(CancelUnlockRequestResponseData cancelUnlockRequestResponseData) {
        this.cancelUnlockRequestResponseData = cancelUnlockRequestResponseData;
    }

    public class CancelUnlockRequestResponseData {

        @SerializedName("RequestID")
        @Expose
        private Integer requestID;
        @SerializedName("StageId")
        @Expose
        private Integer stageId;
        @SerializedName("StatusId")
        @Expose
        private Integer statusId;
        @SerializedName("ProcessID")
        @Expose
        private Integer processID;
        @SerializedName("ErrorMessage")
        @Expose
        private String errorMessage;

        public Integer getRequestID() {
            return requestID;
        }

        public void setRequestID(Integer requestID) {
            this.requestID = requestID;
        }

        public Integer getStageId() {
            return stageId;
        }

        public void setStageId(Integer stageId) {
            this.stageId = stageId;
        }

        public Integer getStatusId() {
            return statusId;
        }

        public void setStatusId(Integer statusId) {
            this.statusId = statusId;
        }

        public Integer getProcessID() {
            return processID;
        }

        public void setProcessID(Integer processID) {
            this.processID = processID;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

    }


}