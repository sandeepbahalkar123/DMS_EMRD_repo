package com.scorg.dms.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageList implements Parcelable {

    @SerializedName("msgId")
    @Expose
    private int msgId;
    @SerializedName("topic")
    @Expose
    private String topic;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("msgTime")
    @Expose
    private String msgTime;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("user2id")
    @Expose
    private int patId;
    @SerializedName("user1id")
    @Expose
    private int docId;

    public final static Parcelable.Creator<MessageList> CREATOR = new Creator<MessageList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MessageList createFromParcel(Parcel in) {
            MessageList instance = new MessageList();
            instance.msgId = ((int) in.readValue((int.class.getClassLoader())));
            instance.topic = ((String) in.readValue((String.class.getClassLoader())));
            instance.msg = ((String) in.readValue((String.class.getClassLoader())));
            instance.msgTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.sender = ((String) in.readValue((String.class.getClassLoader())));
            instance.docId = ((int) in.readValue((int.class.getClassLoader())));
            instance.patId = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public MessageList[] newArray(int size) {
            return (new MessageList[size]);
        }

    };

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getPatId() {
        return patId;
    }

    public void setPatId(int patId) {
        this.patId = patId;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(msgId);
        dest.writeValue(topic);
        dest.writeValue(msg);
        dest.writeValue(msgTime);
        dest.writeValue(sender);
        dest.writeValue(docId);
        dest.writeValue(patId);
    }

    public int describeContents() {
        return 0;
    }

}