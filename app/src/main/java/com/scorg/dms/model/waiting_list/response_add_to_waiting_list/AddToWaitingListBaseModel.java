
package com.scorg.dms.model.waiting_list.response_add_to_waiting_list;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class AddToWaitingListBaseModel implements Parcelable, CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private AddToWaitingModel addToWaitingModel;
    public final static Creator<AddToWaitingListBaseModel> CREATOR = new Creator<AddToWaitingListBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddToWaitingListBaseModel createFromParcel(Parcel in) {
            return new AddToWaitingListBaseModel(in);
        }

        public AddToWaitingListBaseModel[] newArray(int size) {
            return (new AddToWaitingListBaseModel[size]);
        }

    }
    ;

    protected AddToWaitingListBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.addToWaitingModel = ((AddToWaitingModel) in.readValue((AddToWaitingModel.class.getClassLoader())));
    }

    public AddToWaitingListBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public AddToWaitingModel getAddToWaitingModel() {
        return addToWaitingModel;
    }

    public void setAddToWaitingModel(AddToWaitingModel addToWaitingModel) {
        this.addToWaitingModel = addToWaitingModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(addToWaitingModel);
    }

    public int describeContents() {
        return  0;
    }

}
