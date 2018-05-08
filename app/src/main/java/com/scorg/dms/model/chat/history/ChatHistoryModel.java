package com.scorg.dms.model.chat.history;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class ChatHistoryModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private HistoryData historyData;
    public final static Creator<ChatHistoryModel> CREATOR = new Creator<ChatHistoryModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChatHistoryModel createFromParcel(Parcel in) {
            ChatHistoryModel instance = new ChatHistoryModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.historyData = ((HistoryData) in.readValue((HistoryData.class.getClassLoader())));
            return instance;
        }

        public ChatHistoryModel[] newArray(int size) {
            return (new ChatHistoryModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public HistoryData getHistoryData() {
        return historyData;
    }

    public void setHistoryData(HistoryData historyData) {
        this.historyData = historyData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(historyData);
    }

    public int describeContents() {
        return 0;
    }

}