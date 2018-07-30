
package com.scorg.dms.model.waiting_list.request_drag_drop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.util.ArrayList;

public class RequestForDragAndDropBaseModel implements Parcelable,CustomResponse
{

    @SerializedName("waitingListSequence")
    @Expose
    private ArrayList<WaitingListSequence> waitingListSequence = new ArrayList<WaitingListSequence>();
    public final static Creator<RequestForDragAndDropBaseModel> CREATOR = new Creator<RequestForDragAndDropBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RequestForDragAndDropBaseModel createFromParcel(Parcel in) {
            return new RequestForDragAndDropBaseModel(in);
        }

        public RequestForDragAndDropBaseModel[] newArray(int size) {
            return (new RequestForDragAndDropBaseModel[size]);
        }

    };

    protected RequestForDragAndDropBaseModel(Parcel in) {
        in.readList(this.waitingListSequence, (WaitingListSequence.class.getClassLoader()));
    }

    public RequestForDragAndDropBaseModel() {
    }

    public ArrayList<WaitingListSequence> getWaitingListSequence() {
        return waitingListSequence;
    }

    public void setWaitingListSequence(ArrayList<WaitingListSequence> waitingListSequence) {
        this.waitingListSequence = waitingListSequence;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(waitingListSequence);
    }

    public int describeContents() {
        return  0;
    }

}
