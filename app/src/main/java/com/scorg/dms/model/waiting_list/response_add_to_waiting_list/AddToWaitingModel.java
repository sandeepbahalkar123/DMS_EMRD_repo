
package com.scorg.dms.model.waiting_list.response_add_to_waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddToWaitingModel implements Parcelable
{

    @SerializedName("addToWaitingResponse")
    @Expose
    private ArrayList<AddToWaitingResponse> addToWaitingResponse = new ArrayList<AddToWaitingResponse>();
    public final static Creator<AddToWaitingModel> CREATOR = new Creator<AddToWaitingModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddToWaitingModel createFromParcel(Parcel in) {
            return new AddToWaitingModel(in);
        }

        public AddToWaitingModel[] newArray(int size) {
            return (new AddToWaitingModel[size]);
        }

    }
    ;

    protected AddToWaitingModel(Parcel in) {
        in.readList(this.addToWaitingResponse, (AddToWaitingResponse.class.getClassLoader()));
    }

    public AddToWaitingModel() {
    }

    public ArrayList<AddToWaitingResponse> getAddToWaitingResponse() {
        return addToWaitingResponse;
    }

    public void setAddToWaitingResponse(ArrayList<AddToWaitingResponse> addToWaitingResponse) {
        this.addToWaitingResponse = addToWaitingResponse;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(addToWaitingResponse);
    }

    public int describeContents() {
        return  0;
    }

}
