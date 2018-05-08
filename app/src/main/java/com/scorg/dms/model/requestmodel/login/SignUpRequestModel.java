package com.scorg.dms.model.requestmodel.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.io.Serializable;

public class SignUpRequestModel implements CustomResponse,Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("password")
    @Expose
    private String password;

    private boolean isGmailLogin;
    private boolean isFaceBookLogin;

    public boolean isGmailLogin() {
        return isGmailLogin;
    }

    public void setGmailLogin(boolean gmailLogin) {
        isGmailLogin = gmailLogin;
    }

    public boolean isFaceBookLogin() {
        return isFaceBookLogin;
    }

    public void setFaceBookLogin(boolean faceBookLogin) {
        isFaceBookLogin = faceBookLogin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}