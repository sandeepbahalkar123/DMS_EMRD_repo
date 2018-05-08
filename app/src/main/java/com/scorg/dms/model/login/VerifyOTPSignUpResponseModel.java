package com.scorg.dms.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;
import com.scorg.dms.util.CommonMethods;

import java.io.Serializable;
import java.util.ArrayList;

public class VerifyOTPSignUpResponseModel implements CustomResponse, Serializable {


    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private ArrayList<UserProfile> profileList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public ArrayList<UserProfile> getProfileList() {
        return profileList;
    }

    public void setProfileList(ArrayList<UserProfile> profileList) {
        this.profileList = profileList;
    }

    private class UserProfile {
        @SerializedName("patientId")
        @Expose
        private String patientID;
        @SerializedName("name")
        @Expose
        private String patientName;
        @SerializedName("gender")
        @Expose
        private String patientGender;

        public String getPatientID() {
            return patientID;
        }

        public void setPatientID(String patientID) {
            this.patientID = patientID;
        }

        public String getPatientName() {
            return CommonMethods.toCamelCase(patientName);
        }

        public void setPatientName(String patientName) {
            this.patientName = patientName;
        }

        public String getPatientGender() {
            return patientGender;
        }

        public void setPatientGender(String patientGender) {
            this.patientGender = patientGender;
        }
    }
}