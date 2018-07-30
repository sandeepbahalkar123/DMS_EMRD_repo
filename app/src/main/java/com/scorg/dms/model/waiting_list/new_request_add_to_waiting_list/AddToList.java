
package com.scorg.dms.model.waiting_list.new_request_add_to_waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddToList implements Parcelable
{

    @SerializedName("locationId")
    @Expose
    private Integer locationId;

    @SerializedName("locationDetails")
    @Expose
    private String locationDetails;

    @SerializedName("patientsList")
    @Expose
    private ArrayList<PatientAddToWaitingList> patientAddToWaitingList = new ArrayList<PatientAddToWaitingList>();
    public final static Creator<AddToList> CREATOR = new Creator<AddToList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public AddToList createFromParcel(Parcel in) {
            return new AddToList(in);
        }

        public AddToList[] newArray(int size) {
            return (new AddToList[size]);
        }

    };

    protected AddToList(Parcel in) {
        this.locationId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.locationDetails = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.patientAddToWaitingList, (PatientAddToWaitingList.class.getClassLoader()));
    }

    public AddToList() {
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public ArrayList<PatientAddToWaitingList> getPatientAddToWaitingList() {
        return patientAddToWaitingList;
    }

    public void setPatientAddToWaitingList(ArrayList<PatientAddToWaitingList> patientAddToWaitingList) {
        this.patientAddToWaitingList = patientAddToWaitingList;
    }
    public String getLocationDetails() {
        return locationDetails;
    }

    public void setLocationDetails(String locationDetails) {
        this.locationDetails = locationDetails;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(locationId);
        dest.writeValue(locationDetails);
        dest.writeList(patientAddToWaitingList);

    }



    public int describeContents() {
        return  0;
    }

}
