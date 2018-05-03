package com.rescribe.doctor.model.requestmodel.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

import java.io.Serializable;

public class SignUpVerifyOTPRequestModel implements CustomResponse, Serializable {

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("otp")
    @Expose
    private String OTP;
    @SerializedName("password")
    @Expose
    private String password;

    private String name;
    private String eMailID;

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMailID() {
        return eMailID;
    }

    public void seteMailID(String eMailID) {
        this.eMailID = eMailID;
    }
}