
package com.scorg.dms.model.waiting_list.request_delete_waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class RequestDeleteBaseModel implements Parcelable ,CustomResponse
{

    @SerializedName("waitingId")
    @Expose
    private Integer waitingId;
    @SerializedName("waitingSequence")
    @Expose
    private Integer waitingSequence;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("waitingDate")
    @Expose
    private String waitingDate;
    public final static Creator<RequestDeleteBaseModel> CREATOR = new Creator<RequestDeleteBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RequestDeleteBaseModel createFromParcel(Parcel in) {
            return new RequestDeleteBaseModel(in);
        }

        public RequestDeleteBaseModel[] newArray(int size) {
            return (new RequestDeleteBaseModel[size]);
        }

    }
    ;

    protected RequestDeleteBaseModel(Parcel in) {
        this.waitingId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingSequence = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.waitingDate = ((String) in.readValue((String.class.getClassLoader())));
    }

    public RequestDeleteBaseModel() {
    }

    public Integer getWaitingId() {
        return waitingId;
    }

    public void setWaitingId(Integer waitingId) {
        this.waitingId = waitingId;
    }

    public Integer getWaitingSequence() {
        return waitingSequence;
    }

    public void setWaitingSequence(Integer waitingSequence) {
        this.waitingSequence = waitingSequence;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getWaitingDate() {
        return waitingDate;
    }

    public void setWaitingDate(String waitingDate) {
        this.waitingDate = waitingDate;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(waitingId);
        dest.writeValue(waitingSequence);
        dest.writeValue(locationId);
        dest.writeValue(docId);
        dest.writeValue(waitingDate);
    }

    public int describeContents() {
        return  0;
    }

}
