
package com.rescribe.doctor.model.doctor_connect_chat;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;

public class DoctorConnectChatBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private Data data;
    public final static Creator<DoctorConnectChatBaseModel> CREATOR = new Creator<DoctorConnectChatBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorConnectChatBaseModel createFromParcel(Parcel in) {
            DoctorConnectChatBaseModel instance = new DoctorConnectChatBaseModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.data = ((Data) in.readValue((Data.class.getClassLoader())));
            return instance;
        }

        public DoctorConnectChatBaseModel[] newArray(int size) {
            return (new DoctorConnectChatBaseModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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
