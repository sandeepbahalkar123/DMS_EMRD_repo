
package com.rescribe.doctor.model.doctor_connect_chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;

import java.util.ArrayList;

public class Data implements Parcelable,CustomResponse {

    @SerializedName("chatList")
    @Expose
    private ArrayList<ChatList> chatList = new ArrayList<>();
    public final static Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Data createFromParcel(Parcel in) {
            Data instance = new Data();
            in.readList(instance.chatList, (ChatList.class.getClassLoader()));
            return instance;
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    };

    public ArrayList<ChatList> getChatList() {
        return chatList;
    }

    public void setChatList(ArrayList<ChatList> chatList) {
        this.chatList = chatList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(chatList);
    }

    public int describeContents() {
        return 0;
    }

}
