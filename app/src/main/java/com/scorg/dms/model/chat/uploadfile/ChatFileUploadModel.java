package com.scorg.dms.model.chat.uploadfile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.model.Common;

public class ChatFileUploadModel implements Parcelable {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private UploadFileData data;
    public final static Creator<ChatFileUploadModel> CREATOR = new Creator<ChatFileUploadModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ChatFileUploadModel createFromParcel(Parcel in) {
            return new ChatFileUploadModel(in);
        }

        public ChatFileUploadModel[] newArray(int size) {
            return (new ChatFileUploadModel[size]);
        }

    };

    protected ChatFileUploadModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.data = ((UploadFileData) in.readValue((UploadFileData.class.getClassLoader())));
    }

    public ChatFileUploadModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public UploadFileData getData() {
        return data;
    }

    public void setData(UploadFileData data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}