package com.rescribe.doctor.model.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

public class ActiveRequest implements CustomResponse {

    @SerializedName("docId")
    @Expose
    private int userId;

    public int getId() {
        return userId;
    }

    public void setId(int patientId) {
        this.userId = patientId;
    }

}