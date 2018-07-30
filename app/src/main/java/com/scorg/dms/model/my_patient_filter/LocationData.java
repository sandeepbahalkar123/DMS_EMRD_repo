package com.scorg.dms.model.my_patient_filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LocationData implements Parcelable {

    @SerializedName("cityList")
    @Expose
    private ArrayList<CityList> cityList = null;
    public final static Parcelable.Creator<LocationData> CREATOR = new Creator<LocationData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        public LocationData[] newArray(int size) {
            return (new LocationData[size]);
        }

    };

    protected LocationData(Parcel in) {
        in.readList(this.cityList, (CityList.class.getClassLoader()));
    }

    public LocationData() {
    }

    public ArrayList<CityList> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityList> cityList) {
        this.cityList = cityList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cityList);
    }

    public int describeContents() {
        return 0;
    }

}