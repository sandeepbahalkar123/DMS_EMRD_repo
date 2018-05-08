
package com.scorg.dms.model.waiting_list;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class WaitingListBaseModel implements Parcelable,CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private WaitingListDataModel waitingListDataModel;
    public final static Creator<WaitingListBaseModel> CREATOR = new Creator<WaitingListBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public WaitingListBaseModel createFromParcel(Parcel in) {
            return new WaitingListBaseModel(in);
        }

        public WaitingListBaseModel[] newArray(int size) {
            return (new WaitingListBaseModel[size]);
        }

    };

    protected WaitingListBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.waitingListDataModel = ((WaitingListDataModel) in.readValue((WaitingListDataModel.class.getClassLoader())));
    }

    public WaitingListBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public WaitingListDataModel getWaitingListDataModel() {
        return waitingListDataModel;
    }

    public void setWaitingListDataModel(WaitingListDataModel waitingListDataModel) {
        this.waitingListDataModel = waitingListDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(waitingListDataModel);
    }

    public int describeContents() {
        return  0;
    }

}
