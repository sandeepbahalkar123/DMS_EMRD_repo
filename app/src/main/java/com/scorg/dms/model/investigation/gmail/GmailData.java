package com.scorg.dms.model.investigation.gmail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GmailData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("emailId")
    @Expose
    private String emailId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

}