
package com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.util.ArrayList;

public class RequestToAddWaitingList implements Parcelable,CustomResponse
{

    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("addToList")
    @Expose
    private ArrayList<AddToList> addToList = new ArrayList<AddToList>();
    public final static Creator<RequestToAddWaitingList> CREATOR = new Creator<RequestToAddWaitingList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public RequestToAddWaitingList createFromParcel(Parcel in) {
            return new RequestToAddWaitingList(in);
        }

        public RequestToAddWaitingList[] newArray(int size) {
            return (new RequestToAddWaitingList[size]);
        }

    }
    ;

    protected RequestToAddWaitingList(Parcel in) {
        this.docId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.date = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.addToList, (AddToList.class.getClassLoader()));
    }

    public RequestToAddWaitingList() {
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<AddToList> getAddToList() {
        return addToList;
    }

    public void setAddToList(ArrayList<AddToList> addToList) {
        this.addToList = addToList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docId);
        dest.writeValue(date);
        dest.writeValue(time);
        dest.writeList(addToList);
    }

    public int describeContents() {
        return  0;
    }

}
