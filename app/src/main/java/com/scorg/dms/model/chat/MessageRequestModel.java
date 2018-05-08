package com.scorg.dms.model.chat;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class MessageRequestModel implements CustomResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("user1id")
    @Expose
    private int user1id;
    @SerializedName("user2id")
    @Expose
    private int user2id;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("msgTime")
    @Expose
    private String msgTime;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUser1id() {
        return user1id;
    }

    public void setUser1id(int user1id) {
        this.user1id = user1id;
    }

    public int getUser2id() {
        return user2id;
    }

    public void setUser2id(int user2id) {
        this.user2id = user2id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

}