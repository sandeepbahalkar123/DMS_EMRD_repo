
package com.scorg.dms.model.waiting_list;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaitingListDataModel implements Parcelable
{

    @SerializedName("clinicList")
    @Expose
    private ArrayList<WaitingclinicList> waitingclinicList = new ArrayList<WaitingclinicList>();
    public final static Creator<WaitingListDataModel> CREATOR = new Creator<WaitingListDataModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public WaitingListDataModel createFromParcel(Parcel in) {
            return new WaitingListDataModel(in);
        }

        public WaitingListDataModel[] newArray(int size) {
            return (new WaitingListDataModel[size]);
        }

    };

    protected WaitingListDataModel(Parcel in) {
        in.readList(this.waitingclinicList, (WaitingclinicList.class.getClassLoader()));
    }

    public WaitingListDataModel() {
    }

    public ArrayList<WaitingclinicList> getWaitingclinicList() {
        return waitingclinicList;
    }

    public void setWaitingclinicList(ArrayList<WaitingclinicList> waitingclinicList) {
        this.waitingclinicList = waitingclinicList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(waitingclinicList);
    }

    public int describeContents() {
        return  0;
    }

}
