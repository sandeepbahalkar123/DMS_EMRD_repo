
package com.scorg.dms.model.waiting_list.request_drag_drop;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaitingListSequence implements Parcelable
{

    @SerializedName("waitingSequence")
    @Expose
    private Integer waitingSequence;
    @SerializedName("waitingId")
    @Expose
    private String waitingId;
    public final static Creator<WaitingListSequence> CREATOR = new Creator<WaitingListSequence>() {


        @SuppressWarnings({
            "unchecked"
        })
        public WaitingListSequence createFromParcel(Parcel in) {
            return new WaitingListSequence(in);
        }

        public WaitingListSequence[] newArray(int size) {
            return (new WaitingListSequence[size]);
        }

    }
    ;

    protected WaitingListSequence(Parcel in) {
        this.waitingSequence = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingId = ((String) in.readValue((String.class.getClassLoader())));
    }

    public WaitingListSequence() {
    }

    public Integer getWaitingSequence() {
        return waitingSequence;
    }

    public void setWaitingSequence(Integer waitingSequence) {
        this.waitingSequence = waitingSequence;
    }

    public String getWaitingId() {
        return waitingId;
    }

    public void setWaitingId(String waitingId) {
        this.waitingId = waitingId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(waitingSequence);
        dest.writeValue(waitingId);
    }

    public int describeContents() {
        return  0;
    }

}
