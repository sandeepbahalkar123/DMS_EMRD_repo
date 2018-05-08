
package com.scorg.dms.model.request_patients;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilterParams implements Parcelable {

    @SerializedName("gender")
    @Expose
    private String gender = "";
    @SerializedName("age")
    @Expose
    private String age = "";
    @SerializedName("city")
    @Expose
    private ArrayList<Integer> cityIDs = new ArrayList<>();
    public final static Parcelable.Creator<FilterParams> CREATOR = new Creator<FilterParams>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FilterParams createFromParcel(Parcel in) {
            return new FilterParams(in);
        }

        public FilterParams[] newArray(int size) {
            return (new FilterParams[size]);
        }

    };

    protected FilterParams(Parcel in) {
        this.gender = ((String) in.readValue((String.class.getClassLoader())));
        this.age = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.cityIDs, (java.lang.Integer.class.getClassLoader()));
    }

    public FilterParams() {
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public ArrayList<Integer> getCityIds() {
        return cityIDs;
    }

    public void setCityIds(ArrayList<Integer> city) {
        this.cityIDs = city;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(gender);
        dest.writeValue(age);
        dest.writeList(cityIDs);
    }

    public int describeContents() {
        return 0;
    }

}