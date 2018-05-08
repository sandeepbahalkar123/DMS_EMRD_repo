
package com.scorg.dms.model.doctor_connect;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class DoctorConnectDataModel implements Parcelable, CustomResponse {

    @SerializedName("connectList")
    @Expose
    private ArrayList<ConnectList> connectList = null;
    public final static Creator<DoctorConnectDataModel> CREATOR = new Creator<DoctorConnectDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorConnectDataModel createFromParcel(Parcel in) {
            DoctorConnectDataModel instance = new DoctorConnectDataModel();
            in.readList(instance.connectList, (ConnectList.class.getClassLoader()));
            return instance;
        }

        public DoctorConnectDataModel[] newArray(int size) {
            return (new DoctorConnectDataModel[size]);
        }

    };

    public ArrayList<ConnectList> getConnectList() {
        return connectList;
    }

    public void setConnectList(ArrayList<ConnectList> connectList) {
        this.connectList = connectList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(connectList);
    }

    public int describeContents() {
        return 0;
    }

}
