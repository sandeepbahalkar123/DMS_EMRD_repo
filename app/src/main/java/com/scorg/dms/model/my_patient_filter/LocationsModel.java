package com.scorg.dms.model.my_patient_filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class LocationsModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private LocationData data;
    public final static Parcelable.Creator<LocationsModel> CREATOR = new Creator<LocationsModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LocationsModel createFromParcel(Parcel in) {
            return new LocationsModel(in);
        }

        public LocationsModel[] newArray(int size) {
            return (new LocationsModel[size]);
        }

    };

    protected LocationsModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.data = ((LocationData) in.readValue((LocationData.class.getClassLoader())));
    }

    public LocationsModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public LocationData getData() {
        return data;
    }

    public void setData(LocationData data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}