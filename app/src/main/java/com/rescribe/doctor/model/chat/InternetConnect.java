package com.rescribe.doctor.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InternetConnect {

    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("sender")
    @Expose
    private String sender;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

}