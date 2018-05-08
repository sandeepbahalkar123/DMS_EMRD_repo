package com.scorg.dms.model.chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MQTTData implements Parcelable {

    @SerializedName("mqttMessages")
    @Expose
    private ArrayList<MQTTMessage> mqttMessages = new ArrayList<MQTTMessage>();
    public final static Creator<MQTTData> CREATOR = new Creator<MQTTData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MQTTData createFromParcel(Parcel in) {
            return new MQTTData(in);
        }

        public MQTTData[] newArray(int size) {
            return (new MQTTData[size]);
        }

    };

    protected MQTTData(Parcel in) {
        in.readList(this.mqttMessages, (MQTTMessage.class.getClassLoader()));
    }

    public MQTTData() {
    }

    public ArrayList<MQTTMessage> getMqttMessages() {
        return mqttMessages;
    }

    public void setMqttMessages(ArrayList<MQTTMessage> mqttMessages) {
        this.mqttMessages = mqttMessages;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(mqttMessages);
    }

    public int describeContents() {
        return 0;
    }

}
