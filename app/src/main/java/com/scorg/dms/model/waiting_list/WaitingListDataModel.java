
package com.scorg.dms.model.waiting_list;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaitingListDataModel implements Parcelable {

    @SerializedName("clinicList")
    @Expose
    private ArrayList<WaitingClinicList> waitingClinicList = new ArrayList<WaitingClinicList>();


    @SerializedName("waitingList")
    @Expose
    private ArrayList<WaitingPatientData> waitingPatientDataList =new ArrayList<>();

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
        in.readList(this.waitingClinicList, (WaitingClinicList.class.getClassLoader()));
        in.readList(this.waitingPatientDataList, (WaitingPatientData.class.getClassLoader()));
    }

    public ArrayList<WaitingClinicList> getWaitingClinicList() {
        return waitingClinicList;
    }

    public void setWaitingClinicList(ArrayList<WaitingClinicList> waitingClinicList) {
        this.waitingClinicList = waitingClinicList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(waitingClinicList);
        dest.writeList(waitingPatientDataList);
    }

    public int describeContents() {
        return 0;
    }


    public ArrayList<WaitingPatientData> getWaitingPatientDataList() {
        return waitingPatientDataList;
    }

    public void setWaitingPatientDataList(ArrayList<WaitingPatientData> waitingPatientData) {
        this.waitingPatientDataList = waitingPatientData;
    }

}
