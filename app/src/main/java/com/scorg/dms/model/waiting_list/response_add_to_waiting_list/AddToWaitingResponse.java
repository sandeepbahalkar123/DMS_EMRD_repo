
package com.scorg.dms.model.waiting_list.response_add_to_waiting_list;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddToWaitingResponse implements Parcelable
{

    @SerializedName("location Id")
    @Expose
    private Integer locationId;
    @SerializedName("locationDetails")
    @Expose
    private String locationDetails;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("success")
    @Expose
    private Boolean success;
    public final static Creator<AddToWaitingResponse> CREATOR = new Creator<AddToWaitingResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddToWaitingResponse createFromParcel(Parcel in) {
            return new AddToWaitingResponse(in);
        }

        public AddToWaitingResponse[] newArray(int size) {
            return (new AddToWaitingResponse[size]);
        }

    }
    ;

    protected AddToWaitingResponse(Parcel in) {
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationDetails = ((String) in.readValue((String.class.getClassLoader())));
        this.statusMessage = ((String) in.readValue((String.class.getClassLoader())));
        this.statusCode = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
    }

    public AddToWaitingResponse() {
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(locationId);
        dest.writeValue(locationDetails);
        dest.writeValue(statusMessage);
        dest.writeValue(statusCode);
        dest.writeValue(success);
    }

    public int describeContents() {
        return  0;
    }

}
