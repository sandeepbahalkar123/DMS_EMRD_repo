package com.scorg.dms.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class Common implements Parcelable, CustomResponse {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    public final static Parcelable.Creator<Common> CREATOR = new Creator<Common>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Common createFromParcel(Parcel in) {
            Common instance = new Common();
            instance.success = ((String) in.readValue((String.class.getClassLoader())));
            instance.statusCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.statusMessage = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public Common[] newArray(int size) {
            return (new Common[size]);
        }

    };

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(statusCode);
        dest.writeValue(statusMessage);
    }

    public int describeContents() {
        return 0;
    }

}