package com.rescribe.doctor.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MessageModel implements Parcelable {

    @SerializedName("messageList")
    @Expose
    private ArrayList<MQTTMessage> MQTTMessage = new ArrayList<MQTTMessage>();
    public final static Creator<MessageModel> CREATOR = new Creator<MessageModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MessageModel createFromParcel(Parcel in) {
            MessageModel instance = new MessageModel();
            in.readList(instance.MQTTMessage, (MQTTMessage.class.getClassLoader()));
            return instance;
        }

        public MessageModel[] newArray(int size) {
            return (new MessageModel[size]);
        }

    };

    public ArrayList<MQTTMessage> getMQTTMessage() {
        return MQTTMessage;
    }

    public void setMQTTMessage(ArrayList<MQTTMessage> MQTTMessage) {
        this.MQTTMessage = MQTTMessage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(MQTTMessage);
    }

    public int describeContents() {
        return 0;
    }

}